package com.sample.fetch.hiring.ui

import com.sample.fetch.hiring.model.Hiring

fun List<Hiring>.toHiringItemUiState() = map {
    HiringItemUiState(id = it.id, listId = it.listId, name = it.name.orEmpty())
}

