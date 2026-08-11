package com.herologs.feature.privacy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.herologs.domain.localdata.DeleteAllLocalDataUseCase
import com.herologs.domain.localdata.GetStoredDataCategoriesUseCase
import com.herologs.domain.localdata.LocalDataCategory
import com.herologs.domain.localdata.LocalDataDeletionResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PrivacyDataUiState(
    val storedCategories: List<LocalDataCategory>,
    val isDeleteConfirmationVisible: Boolean = false,
    val isDeleting: Boolean = false,
    val deletionComplete: Boolean = false,
    val deletionFailure: LocalDataDeletionResult.Failure? = null,
)

class PrivacyDataViewModel(
    getStoredDataCategories: GetStoredDataCategoriesUseCase,
    private val deleteAllLocalData: DeleteAllLocalDataUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        PrivacyDataUiState(storedCategories = getStoredDataCategories()),
    )
    val uiState: StateFlow<PrivacyDataUiState> = _uiState.asStateFlow()

    fun requestDeleteAll() {
        if (_uiState.value.isDeleting) return
        _uiState.update {
            it.copy(
                isDeleteConfirmationVisible = true,
                deletionComplete = false,
                deletionFailure = null,
            )
        }
    }

    fun dismissDeleteConfirmation() {
        if (_uiState.value.isDeleting) return
        _uiState.update { it.copy(isDeleteConfirmationVisible = false) }
    }

    fun confirmDeleteAll() {
        val current = _uiState.value
        if (!current.isDeleteConfirmationVisible || current.isDeleting) return

        _uiState.update {
            it.copy(
                isDeleteConfirmationVisible = false,
                isDeleting = true,
                deletionComplete = false,
                deletionFailure = null,
            )
        }
        viewModelScope.launch {
            when (val result = deleteAllLocalData()) {
                LocalDataDeletionResult.Success -> _uiState.update {
                    it.copy(isDeleting = false, deletionComplete = true)
                }

                is LocalDataDeletionResult.Failure -> _uiState.update {
                    it.copy(isDeleting = false, deletionFailure = result)
                }
            }
        }
    }

    companion object {
        fun factory(
            getStoredDataCategories: GetStoredDataCategoriesUseCase,
            deleteAllLocalData: DeleteAllLocalDataUseCase,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PrivacyDataViewModel(
                    getStoredDataCategories = getStoredDataCategories,
                    deleteAllLocalData = deleteAllLocalData,
                )
            }
        }
    }
}
