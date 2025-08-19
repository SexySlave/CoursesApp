package com.sexyslave.domain.usecase

import com.sexyslave.domain.repository.CourseRepository

class UpdateFavoriteStatusUseCase(private val courseRepository: CourseRepository) {
    suspend operator fun invoke(courseId: String, isFavorite: Boolean) {
        courseRepository.updateFavoriteStatus(courseId, isFavorite)
    }
}