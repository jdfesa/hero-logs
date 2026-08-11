package com.herologs.domain.localdata

class GetStoredDataCategoriesUseCase(
    private val repository: LocalDataRepository,
) {
    operator fun invoke(): List<LocalDataCategory> = repository.getStoredCategories()
}

class DeleteAllLocalDataUseCase(
    private val repository: LocalDataRepository,
) {
    suspend operator fun invoke(): LocalDataDeletionResult = repository.deleteAll()
}
