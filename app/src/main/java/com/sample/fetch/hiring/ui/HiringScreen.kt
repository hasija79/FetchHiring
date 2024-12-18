package com.sample.fetch.hiring.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sample.fetch.hiring.R
import com.sample.fetch.hiring.app.theme.FetchHiringTheme

@Composable
fun HiringScreen(
    modifier: Modifier = Modifier,
    viewModel: HiringViewModel = hiltViewModel(),
) {
    HiringPage(
        modifier = modifier,
        state = viewModel.uiState,
        onLoad = viewModel::getHiringList
    )
}

@Composable
private fun HiringPage(
    modifier: Modifier = Modifier,
    state: HiringUiState,
    onLoad: () -> Unit = {},
) {
    when (state) {
        is HiringUiState.Error -> HiringError()
        is HiringUiState.Loading -> HiringLoading()
        is HiringUiState.Loaded -> HiringList(
            modifier = modifier,
            state = state.items,
            onLoad = onLoad,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HiringList(
    modifier: Modifier = Modifier,
    state: List<HiringItemUiState>,
    onLoad: () -> Unit = {}
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(all = 16.dp)
            .wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
        content = {
            item { HiringLoad(onClick = onLoad) }
            items(
                items = state,
                contentType = { it::class.simpleName },
                itemContent = { Hiring(state = it) }
            )
        }
    )
}

@Composable
private fun HiringLoading() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
        content = {
            Text(
                fontSize = 24.sp,
                color = colorScheme.primary,
                style = typography.bodyLarge,
                text = stringResource(R.string.loading_msg),
            )
            CircularProgressIndicator()
        }
    )
}

@Composable
private fun HiringError(
    onRetry: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
        content = {
            Text(
                fontSize = 20.sp,
                color = colorScheme.error,
                style = typography.bodyLarge,
                text = stringResource(R.string.error_msg),
            )
            HiringLoad(onClick = onRetry)
        }
    )
}

@Composable
private fun HiringLoad(
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        content = {
            Text(
                modifier = Modifier
                    .padding(all = 8.dp),
                fontSize = 20.sp,
                color = colorScheme.background,
                style = typography.bodyLarge,
                text = stringResource(R.string.get_hiring_list)
            )
        }
    )
}

@Composable
private fun Hiring(
    state: HiringItemUiState
) {
    ListItem(
        modifier = Modifier.clip(RoundedCornerShape(8.dp)),
        colors = ListItemDefaults.colors(containerColor = colorScheme.surfaceContainer),
        headlineContent = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                fontSize = 16.sp,
                color = colorScheme.primary,
                style = typography.bodyMedium,
                text = "Name: ${state.name}",
            )
        },
        leadingContent = {
            Text(
                fontSize = 16.sp,
                color = colorScheme.primary,
                style = typography.bodyMedium,
                text = "Id: ${state.id}",
            )
        },
        trailingContent = {
            Text(
                fontSize = 16.sp,
                color = colorScheme.primary,
                style = typography.bodyMedium,
                text = "List Id: ${state.listId}",
            )
        }
    )
}

@Stable
sealed interface HiringUiState {
    data object Error : HiringUiState
    data object Loading : HiringUiState
    data class Loaded(val items: List<HiringItemUiState>) : HiringUiState
}

@Stable
data class HiringItemUiState(
    var id: Int,
    var listId: Int,
    var name: String,
)

@Preview
@Composable
private fun MediaCarouselComponentPreview(
    @PreviewParameter(HiringPreviewProvider::class) state: HiringUiState,
) = FetchHiringTheme { HiringPage(state = state) }

private const val ITEMS_COUNT = 200

class HiringPreviewProvider(
    private val itemsState: List<HiringItemUiState> = List(ITEMS_COUNT) {
        HiringItemUiState(id = it, listId = it, name = "Item $it")
    },
    override val values: Sequence<HiringUiState> = sequenceOf(
        HiringUiState.Loaded(items = itemsState),
        HiringUiState.Error,
        HiringUiState.Loading,
    ),
) : PreviewParameterProvider<HiringUiState>
