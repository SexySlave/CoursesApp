package com.sexyslave.domain.repository

import com.sexyslave.domain.model.Course
import kotlinx.coroutines.flow.Flow

interface CourseRepository {
    fun getCourses(): Flow<List<Course>>
    suspend fun updateFavoriteStatus(courseId: String, isFavorite: Boolean)
}