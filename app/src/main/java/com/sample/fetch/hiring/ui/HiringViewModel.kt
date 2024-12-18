package com.sample.fetch.hiring.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buildertrend.core.services.dailylogs.HiringRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HiringViewModel @Inject constructor(
    private val hiringRepository: HiringRepository,
) : ViewModel() {

    var uiState: HiringUiState by mutableStateOf(HiringUiState.Loading)
        private set

    init {
        getHiringList()
    }

    fun getHiringList() {
        uiState = HiringUiState.Loading
        viewModelScope.launch {
            uiState = hiringRepository.getHiringList().fold(
                onFailure = { HiringUiState.Error },
                onSuccess = { HiringUiState.Loaded(items = it.toHiringItemUiState()) },
            )
        }
    }
}
