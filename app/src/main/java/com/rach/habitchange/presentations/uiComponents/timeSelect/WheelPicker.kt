package com.rach.habitchange.presentations.uiComponents.timeSelect

import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.animateDecay
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import kotlin.math.abs

interface WheelPickerContentScope {
    val state: WheelPickerState
}

interface WheelPickerDisplayScope : WheelPickerContentScope {
    @Composable
    fun Content(index: Int)
}

@Composable
fun BaseVerticalWheelPicker(
    modifier: Modifier = Modifier,
    items: List<Int>,
    state: WheelPickerState = rememberWheelPickerState(),
    key: ((index: Int) -> Any)? = null,
    itemHeight: Dp = 30.dp,
    unfocusedCount: Int = 1,
    userScrollEnabled: Boolean = true,
    reverseLayout: Boolean = false,
    focus: @Composable () -> Unit = { WheelPickerFocusVertical() },
    display: @Composable WheelPickerDisplayScope.(index: Int) -> Unit = { DefaultWheelPickerDisplay( it) },
    content: @Composable WheelPickerContentScope.(index: Int) -> Unit,
) {
    WheelPicker(
        modifier = modifier,
        isVertical = true,
        items = items,
        state = state,
        key = key,
        itemSize = itemHeight,
        unfocusedCount = unfocusedCount,
        userScrollEnabled = userScrollEnabled,
        reverseLayout = reverseLayout,
        focus = focus,
        display = display,
        content = content,
    )
}

@Composable
fun BaseHorizontalWheelPicker(
    modifier: Modifier = Modifier,
    items: List<Int>,
    state: WheelPickerState = rememberWheelPickerState(),
    key: ((index: Int) -> Any)? = null,
    itemWidth: Dp = 35.dp,
    unfocusedCount: Int = 1,
    userScrollEnabled: Boolean = true,
    reverseLayout: Boolean = false,
    focus: @Composable () -> Unit = { WheelPickerFocusHorizontal() },
    display: @Composable WheelPickerDisplayScope.(index: Int) -> Unit = {
        DefaultWheelPickerDisplay(
            it)
    },
    content: @Composable WheelPickerContentScope.(index: Int) -> Unit,
) {
    WheelPicker(
        modifier = modifier,
        isVertical = false,
        items = items,
        state = state,
        key = key,
        itemSize = itemWidth,
        unfocusedCount = unfocusedCount,
        userScrollEnabled = userScrollEnabled,
        reverseLayout = reverseLayout,
        focus = focus,
        display = display,
        content = content,
    )
}

@Composable
private fun WheelPicker(
    modifier: Modifier,
    isVertical: Boolean,
    items: List<Int>,
    state: WheelPickerState,
    key: ((index: Int) -> Any)?,
    itemSize: Dp,
    unfocusedCount: Int,
    userScrollEnabled: Boolean,
    reverseLayout: Boolean,
    focus: @Composable () -> Unit,
    display: @Composable WheelPickerDisplayScope.(item: Int) -> Unit,
    content: @Composable WheelPickerContentScope.(item: Int) -> Unit,
) {
    require(items.size >= 0) { "require count >= 0" }
    require(unfocusedCount >= 0) { "require unfocusedCount >= 0" } //you can change this number and pass whatever number you want as to show the grayed out digits

    LaunchedEffect(state, items) {
        state.updateCount(items)
    }

    val nestedScrollConnection = remember(state) {
        WheelPickerNestedScrollConnection(state)
    }.apply {
        this.isVertical = isVertical
        this.itemSizePx = with(LocalDensity.current) { itemSize.roundToPx() }
        this.reverseLayout = reverseLayout
    }

    val totalSize = remember(itemSize, unfocusedCount) {
        itemSize * (unfocusedCount * 2 + 1)
    }

    val displayScope = remember(state) {
        WheelPickerDisplayScopeImpl(state)
    }.apply {
        this.content = content
    }

    Box(
        modifier = modifier
            .nestedScroll(nestedScrollConnection)
            .run {
                if (totalSize > 0.dp) {
                    if (isVertical) {
                        height(totalSize).widthIn(40.dp)
                    } else {
                        width(totalSize).heightIn(40.dp)
                    }
                } else {
                    this
                }
            },
        contentAlignment = Alignment.Center,
    ) {

        val lazyListScope: LazyListScope.() -> Unit = {

            repeat(unfocusedCount) {
                item(contentType = "placeholder") {
                    ItemSizeBox(
                        isVertical = isVertical,
                        itemSize = itemSize,
                    )
                }
            }

            items(
                count = items.count(),
                key = key,
            ) { index ->
                ItemSizeBox(
                    isVertical = isVertical,
                    itemSize = itemSize,
                ) {
                    displayScope.display(index)
                }
            }

            repeat(unfocusedCount) {
                item(contentType = "placeholder") {
                    ItemSizeBox(
                        isVertical = isVertical,
                        itemSize = itemSize,
                    )
                }
            }
        }

        if (isVertical) {
            LazyColumn(
                state = state.lazyListState,
                horizontalAlignment = Alignment.CenterHorizontally,
                reverseLayout = reverseLayout,
                userScrollEnabled = userScrollEnabled,
                modifier = Modifier
                    .matchParentSize(),
                flingBehavior = verySlowScrollFlingBehavior(),
                content = lazyListScope,
            )
        } else {
            LazyRow(
                state = state.lazyListState,
                verticalAlignment = Alignment.CenterVertically,
                reverseLayout = reverseLayout,
                userScrollEnabled = userScrollEnabled,
                modifier = Modifier.matchParentSize(),
                content = lazyListScope,
            )
        }

        ItemSizeBox(
            modifier = Modifier.align(Alignment.Center),
            isVertical = isVertical,
            itemSize = itemSize,
        ) {
            focus()
        }
    }
}

@Composable
private fun ItemSizeBox(
    modifier: Modifier = Modifier,
    isVertical: Boolean,
    itemSize: Dp,
    content: @Composable () -> Unit = { },
) {
    Box(
        modifier
            .run {
                if (isVertical) {
                    height(itemSize)
                } else {
                    width(itemSize)
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

class WheelPickerNestedScrollConnection(
    private val state: WheelPickerState,
) : NestedScrollConnection {
    var isVertical: Boolean = true
    var itemSizePx: Int = 0
    var reverseLayout: Boolean = false

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource
    ): Offset {
        state.synchronizeCurrentIndexSnapshot()
        return super.onPostScroll(consumed, available, source)
    }

    override suspend fun onPreFling(available: Velocity): Velocity {
        val currentIndex = state.synchronizeCurrentIndexSnapshot()
        return if (currentIndex >= 0) {
            available.flingItemCount(
                isVertical = isVertical,
                itemSize = itemSizePx,
                decay = exponentialDecay(2f),
                reverseLayout = reverseLayout,
            ).let { flingItemCount ->
                if (flingItemCount == 0) {
                    state.animateScrollToIndex(currentIndex)
                } else {
                    state.animateScrollToIndex(currentIndex - flingItemCount)
                }
            }
            available
        } else {
            super.onPreFling(available)
        }
    }
}

fun Velocity.flingItemCount(
    isVertical: Boolean,
    itemSize: Int,
    decay: DecayAnimationSpec<Float>,
    reverseLayout: Boolean,
): Int {
    if (itemSize <= 0) return 0
    val velocity = if (isVertical) y else x
    val targetValue = decay.calculateTargetValue(0f, velocity)
    val flingItemCount = (targetValue / itemSize).toInt()
    return if (reverseLayout) -flingItemCount else flingItemCount
}

class WheelPickerDisplayScopeImpl(
    override val state: WheelPickerState,
) : WheelPickerDisplayScope {

    var content: @Composable WheelPickerContentScope.(index: Int) -> Unit by mutableStateOf({})

    @Composable
    override fun Content(index: Int) {
        content(index)
    }
}
class SlowScrollFlingBehavior(
    private val flingDecay: DecayAnimationSpec<Float>,
    private val maxInitialVelocity: Float = 3000f, // Adjust for desired starting speed
    private val minConsumedThreshold: Float = 0.05f, // Adjust for stopping sensitivity
    private val minFlingVelocity: Float = 10f // Adjust for minimum fling to trigger animation
) : FlingBehavior {

    override suspend fun ScrollScope.performFling(initialVelocity: Float): Float {
        val clampedVelocity = minOf(maxOf(initialVelocity, -maxInitialVelocity), maxInitialVelocity)
        if (abs(clampedVelocity) < minFlingVelocity) return clampedVelocity // Ignore very slow flings

        var velocityLeft = clampedVelocity
        var lastValue = 0f
        AnimationState(
            initialValue = 0f,
            initialVelocity = clampedVelocity,
        ).animateDecay(flingDecay) {
            val delta = value - lastValue
            val consumed = scrollBy(delta)
            lastValue = value
            velocityLeft = this.velocity
            // Stop if velocity is too low or there's unconsumed scroll
            if (abs(velocityLeft) < 5f || abs(delta - consumed) > minConsumedThreshold) {
                this.cancelAnimation()
            }
        }
        return velocityLeft
    }
}

@Composable
fun verySlowScrollFlingBehavior(): FlingBehavior {
    val flingSpec = rememberSplineBasedDecay<Float>() // Implement your desired decay behavior
    return remember(flingSpec) {
        SlowScrollFlingBehavior(flingSpec)
    }
}