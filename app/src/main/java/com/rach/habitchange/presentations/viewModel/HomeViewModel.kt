package com.rach.habitchange.presentations.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rach.core.domain.repo.UsageStatsRepository
import com.rach.core.permissions.UsageStatsPermissionChecker
import com.rach.core.useCases.LoadAppUseCases
import com.rach.habitchange.presentations.model.LoadAppDataWithUsage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val loadAppDataUseCases: LoadAppUseCases,
    private val usageStatsRepository: UsageStatsRepository
) : ViewModel() {

    private val _showPermissionDialog = MutableStateFlow(false)
    val showPermissionDialog: StateFlow<Boolean> = _showPermissionDialog.asStateFlow()

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        checkStatsPermissionAllow()
    }

    private fun checkStatsPermissionAllow() {
        viewModelScope.launch {
            val result = UsageStatsPermissionChecker.hasUsageStatsPermission(context)
            _showPermissionDialog.value = !result
        }
    }

    fun dismissDialog() {
        _showPermissionDialog.value = false
    }

    fun goToSettingsForRequestUsageStatsPermission() {
        UsageStatsPermissionChecker.requestUsageStatsPermission(context)
        _showPermissionDialog.value = false
    }

    fun loadTodayUsageData() {
        loadAppUsageToday()
    }

    fun fiveDayDataUsage(
        packageName: String,
        days: Int = 7
    ) {
        loadAppDataForRange(
            packageName = packageName,
            days = days
        )
    }

    private fun loadAppUsageToday() {
        _uiState.value = _uiState.value.copy(
            loading = true,
            errorMessage = null
        )
        viewModelScope.launch {
            try {
                loadAppDataUseCases.invoke().collect { apps ->
                    val selectedPackageNames = apps.map { it.packageName }

                    val (startTime, endTime) = usageStatsRepository.getTodayUsage()


                    val usageMap = usageStatsRepository.getUsageStatsForRange(
                        startTime,
                        endTime,
                        selectedPackageName = selectedPackageNames
                    )

                    val appDataConversion = apps.map {
                        LoadAppDataWithUsage(
                            id = it.id,
                            name = it.name,
                            packageName = it.packageName,
                            todayUsageInMinutes = usageMap[it.packageName] ?: 0L
                        )
                    }

                    Log.d("himi", "$appDataConversion")

                    _uiState.value = _uiState.value.copy(
                        appsData = appDataConversion,
                        loading = false
                    )
                }

            } catch (e: Exception) {

                _uiState.value = _uiState.value.copy(
                    loading = false,
                    errorMessage = e.localizedMessage
                )

            } finally {
                _uiState.value = _uiState.value.copy(
                    loading = false
                )
            }

        }
    }


    private fun loadAppDataForRange(packageName: String, days: Int) {
        _uiState.value = _uiState.value.copy(
            loading = true,
            errorMessage = null
        )

        viewModelScope.launch {
            try {
                val usageData = mutableListOf<LoadAppDataWithUsage>()

                val dateFormat = SimpleDateFormat("dd/MM/ yyyy", Locale.getDefault())

                for (day in 0 until days) {
                    val (startTime, endTime) = usageStatsRepository.getFiveDaysUsage(day)
                    val usageStatsMap = usageStatsRepository.getUsageStatsForRange(
                        startTimeInMillis = startTime,
                        endTimeInMills = endTime,
                        selectedPackageName = listOf(packageName)
                    )

                    val usageMinutes = usageStatsMap[packageName] ?: 0L

                    val date = Calendar.getInstance().apply {
                        add(Calendar.DAY_OF_YEAR, -(day + 1))
                    }

                    val formattedDate = dateFormat.format(date.time)

                    usageData.add(
                        LoadAppDataWithUsage(
                            id = day + 1,
                            name = "Day ${day + 1}",
                            packageName = packageName,
                            todayUsageInMinutes = usageMinutes,
                            date = formattedDate,
                        )
                    )

                    _uiState.value = _uiState.value.copy(
                        appsData = usageData,
                        loading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.localizedMessage,
                    loading = false
                )
            } finally {
                _uiState.value = _uiState.value.copy(
                    loading = false
                )
            }
        }
    }

    fun clearErrorMessage() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null
        )
    }

}

data class HomeUiState(
    val loading: Boolean = false,
    val errorMessage: String? = null,
    val appsData: List<LoadAppDataWithUsage> = emptyList()
)