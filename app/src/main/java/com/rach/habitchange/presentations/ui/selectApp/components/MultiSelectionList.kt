package com.rach.habitchange.presentations.ui.selectApp.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun <T : Any> MultiSelectionList(
    modifier: Modifier = Modifier,
    items: List<T>,
    key: ((T) -> Any)? = null,
    isMultiSelectionModeEnabled: Boolean,
    selectedItem: List<T>,
    onClick: (T) -> Unit,
    onLongClick: (T) -> Unit,
    itemContent: @Composable (T) -> Unit
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(
            count = items.size,
            key = { index -> key?.invoke(items[index]) ?: index }
        ) { index ->
            val item = items[index]
            MultiSelectionContainer(
                isMultiSelectionModeEnabled = isMultiSelectionModeEnabled,
                isSelected = selectedItem.contains(item),
                onClick = { onClick(item) },
                onLongClick = { onLongClick(item) },
                content = { itemContent(item) }
            )

        }
    }


}

@Composable
fun MultiSelectionContainer(
    modifier: Modifier = Modifier,
    isMultiSelectionModeEnabled: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 50.dp)
            .combinedClickable(
                onClick = onClick,
                onLongClick = {
                    onLongClick()
                    onClick()
                }
            ),
        contentAlignment = Alignment.CenterEnd
    ) {
        content()
        AnimatedVisibility(
            modifier = Modifier,
            visible = isMultiSelectionModeEnabled,
            enter = slideInHorizontally(initialOffsetX = { it / 2 }) + fadeIn(),
            exit = slideOutHorizontally(targetOffsetX = { it / 2 }) + fadeOut()
        ) {
            RadioButton(
                selected = isSelected,
                onClick = onClick
            )
        }
    }

}