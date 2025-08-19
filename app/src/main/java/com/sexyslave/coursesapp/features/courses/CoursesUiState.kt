package com.sexyslave.coursesapp.features.courses

import com.sexyslave.domain.model.Course

sealed interface CoursesUiState {
    object Loading : CoursesUiState
    data class Success(val courses: List<Course>) : CoursesUiState
    data class Error(val message: String) : CoursesUiState
}