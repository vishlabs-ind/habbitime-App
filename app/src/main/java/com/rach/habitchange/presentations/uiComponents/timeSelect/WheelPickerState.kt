package com.rach.habitchange.presentations.uiComponents.timeSelect

import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.math.absoluteValue

private const val DefaultInitialIndex = 4

@Composable
fun rememberWheelPickerState(
    initialIndex: Int = DefaultInitialIndex
): WheelPickerState {
    return rememberSaveable(saver = WheelPickerState.Saver) {
        WheelPickerState(
            initialIndex = initialIndex,
        )
    }
}

class WheelPickerState(
    initialIndex: Int = 0,
) {
    internal val lazyListState = LazyListState()

    private var _count = 0
    private var _currentIndex by mutableIntStateOf(-1)
    private var _currentIndexSnapshot by mutableIntStateOf(-1)

    private var _pendingIndex: Int? = initialIndex.coerceAtLeast(0)
        set(value) {
            field = value?.also {
                check(it >= 0)
                check(_count == 0)
            }
        }
    private var _pendingIndexContinuation: Continuation<Unit>? = null

    /**
     * Index of picker when it is idle, -1 means that there is no data.
     *
     * Note that this property is observable and if you use it in the composable function
     * it will be recomposed on every change.
     */
    val currentIndex: Int get() = _currentIndex

    /**
     * Index of picker when it is idle or drag but not fling, -1 means that there is no data.
     *
     * Note that this property is observable and if you use it in the composable function
     * it will be recomposed on every change.
     */
    val currentIndexSnapshot: Int get() = _currentIndexSnapshot

    /**
     * [LazyListState.interactionSource]
     */
    val interactionSource: InteractionSource get() = lazyListState.interactionSource

    /**
     * [LazyListState.isScrollInProgress]
     */
    val isScrollInProgress: Boolean get() = lazyListState.isScrollInProgress

    suspend fun animateScrollToIndex(index: Int) {
        @Suppress("NAME_SHADOWING")
        val index = index.coerceAtLeast(0)

        lazyListState.animateScrollToItem(index)
        synchronizeCurrentIndex()
    }

    suspend fun scrollToIndex(index: Int, pending: Boolean = true) {
        @Suppress("NAME_SHADOWING")
        val index = index.coerceAtLeast(0)

        lazyListState.scrollToItem(index)
        synchronizeCurrentIndex()

        if (pending) {
            awaitIndex(index)
        }
    }

    private suspend fun awaitIndex(index: Int) {
        if (index < 0) return
        if (_count > 0) return
        if (_currentIndex == index) return

        // Resume last continuation before suspend.
        resumeAwaitIndex()

        suspendCancellableCoroutine { cont ->
            _pendingIndex = index
            _pendingIndexContinuation = cont
            cont.invokeOnCancellation {
                _pendingIndex = null
                _pendingIndexContinuation = null
            }
        }

    }

    private fun resumeAwaitIndex() {
        _pendingIndexContinuation?.let {
            _pendingIndexContinuation = null
            it.resume(Unit)
        }
    }

    internal suspend fun updateCount(items: List<Int>) {

        _count = items.count()

        val maxIndex = items.size - 1
        if (maxIndex < _currentIndex) {
            scrollToIndex(maxIndex, pending = false)
        }

        if (items.isNotEmpty()) {
            _pendingIndex?.let { pendingIndex ->
                scrollToIndex(pendingIndex, pending = false)
                _pendingIndex = null
                resumeAwaitIndex()
            }
            if (_currentIndex < 0) {
                synchronizeCurrentIndex()
            }
        }
    }

    private fun synchronizeCurrentIndex() {
        val index = synchronizeCurrentIndexSnapshot().coerceAtLeast(-1)
        if (_currentIndex != index) {
            _currentIndex = index
            _currentIndexSnapshot = index
        }
    }

    internal fun synchronizeCurrentIndexSnapshot(): Int {
        return (mostStartItemInfo()?.index ?: -1).also {
            _currentIndexSnapshot = it
        }
    }

    /**
     * The item closest to the viewport start.
     */
    private fun mostStartItemInfo(): LazyListItemInfo? {
        if (_count <= 0) return null

        val layoutInfo = lazyListState.layoutInfo
        val listInfo = layoutInfo.visibleItemsInfo

        if (listInfo.isEmpty()) return null
        if (listInfo.size == 1) return listInfo.first()

        val firstItem = listInfo.first()
        val firstOffsetDelta = (firstItem.offset - layoutInfo.viewportStartOffset).absoluteValue
        return if (firstOffsetDelta < firstItem.size / 2) {
            firstItem
        } else {
            listInfo[1]
        }
    }

    companion object {
        val Saver: Saver<WheelPickerState, *> = listSaver(
            save = {
                listOf<Any>(
                    it.currentIndex,
                )
            },
            restore = {
                WheelPickerState(
                    initialIndex = it[0] as Int,
                )
            }
        )
    }
}
