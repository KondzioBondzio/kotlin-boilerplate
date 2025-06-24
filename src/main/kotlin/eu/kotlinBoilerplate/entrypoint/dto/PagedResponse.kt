package eu.kotlinBoilerplate.entrypoint.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class PagedResponse<T>(
    val currentPage: Int,
    val lastPage: Int,
    val total: Long,
    val data: List<T>
)