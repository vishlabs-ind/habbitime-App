package com.rach.habitchange.presentations.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rach.core.data.model.SelectedAppInfoCore
import com.rach.core.useCases.SaveAppUseCases
import com.rach.core.useCases.SelectAppUseCases
import com.rach.habitchange.presentations.model.SelectedAppInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val KEY_MULTI_SELECTION_MODE = "multi_selection_mode"
private const val KEY_SELECTED_ITEMS = "selected_items"

@HiltViewModel
class SelectAppViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val selectAppUseCases: SelectAppUseCases,
    private val saveToRoomUseCases: SaveAppUseCases
) : ViewModel() {

    private val _installAllApps = MutableStateFlow<List<SelectedAppInfo>>(emptyList())
    val installApps: StateFlow<List<SelectedAppInfo>> = _installAllApps.asStateFlow()

    data class SavedAppInfo(
        val id: Int,
        val name: String,
        val packageName: String
    )

    private val _uiState = MutableStateFlow(
        SelectedAppUiState(
            selectedItems = savedStateHandle.get<Array<SavedAppInfo>>(KEY_SELECTED_ITEMS)
                ?.map { savedApps ->
                    SelectedAppInfo(
                        id = savedApps.id,
                        name = savedApps.name,
                        packageName = savedApps.packageName
                    )
                } ?: emptyList(),
            isMultiSelectionModeEnabled = savedStateHandle.get<Boolean>(KEY_MULTI_SELECTION_MODE)
                ?: false
        )
    )

    val uiState: StateFlow<SelectedAppUiState> = _uiState.asStateFlow()

    /**
     * ViewModelScope.launch is run on Default.Main Thread so use only others Dispatchers
     * Explicitly switching to Dispatchers.Main using something like withContext(Dispatchers.Main) in the collect block
     * would be redundant and could add unnecessary overhead.
     */
    fun getInstallApps() {
        _uiState.value = _uiState.value.copy(
            loading = true,
            errorMessage = null
        )
        viewModelScope.launch {
            flow {
                val result = selectAppUseCases.getAllInstallApps()
                if (result.isSuccess) {
                    emit(result.getOrThrow().map {
                        SelectedAppInfo(
                            id = it.id,
                            name = it.name,
                            packageName = it.packageName
                        )
                    })
                } else {
                    throw result.exceptionOrNull() ?: Exception("Something Went Wrong")
                }
            }.flowOn(Dispatchers.IO)
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        errorMessage = e.localizedMessage ?: "Something Went Wrong"
                    )
                }.collect { data ->
                    _installAllApps.value = data
                    _uiState.value = _uiState.value.copy(loading = false)
                }

        }
    }

    fun onItemClick(item: SelectedAppInfo) {
        if (_uiState.value.isMultiSelectionModeEnabled) {
            val updatedSelectedItems = _uiState.value.selectedItems.toMutableList()
            if (updatedSelectedItems.contains(item)) {
                updatedSelectedItems.remove(item)
            } else {
                updatedSelectedItems.add(item)
            }

            _uiState.value = _uiState.value.copy(
                selectedItems = updatedSelectedItems
            )
            savedStateHandle[KEY_SELECTED_ITEMS] = updatedSelectedItems.map { it.id }
        }
    }

    fun onLongItemClick(item: SelectedAppInfo) {
        if (!uiState.value.isMultiSelectionModeEnabled) {
            _uiState.value = _uiState.value.copy(
                isMultiSelectionModeEnabled = true,
                selectedItems = listOf(item)
            )
        }
    }

    fun disableMultiSelectionMode() {
        _uiState.value = _uiState.value.copy(
            isMultiSelectionModeEnabled = false,
            selectedItems = emptyList()
        )
    }

    fun onConfirmButtonClick() {
        val selectedApps = _uiState.value.selectedItems
        val convertToSelectedAppInfoCore = selectedApps.map {
            SelectedAppInfoCore(
                id = it.id,
                name = it.name,
                packageName = it.packageName
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(loading = true)
            try {
                saveToRoomUseCases.invoke(convertToSelectedAppInfoCore)
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    successMessage = "App Added SuccessFully",
                    selectedItems = emptyList()
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    errorMessage = e.message
                )
            }

        }

    }

    fun clearErrorMessage() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null
        )
    }

    fun clearSuccessMessage() {
        _uiState.value = _uiState.value.copy(
            successMessage = null
        )
    }

}

data class SelectedAppUiState(
    val selectedItems: List<SelectedAppInfo> = emptyList(),
    val isMultiSelectionModeEnabled: Boolean = false,
    val errorMessage: String? = null,
    val loading: Boolean = false,
    val successMessage: String? = null
)