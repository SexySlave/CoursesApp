package com.sexyslave.coursesapp.features.courses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sexyslave.domain.model.Course
import com.sexyslave.domain.usecase.GetCoursesUseCase
import com.sexyslave.domain.usecase.UpdateFavoriteStatusUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import kotlinx.coroutines.CancellationException

class CoursesViewModel(
    private val getCoursesUseCase: GetCoursesUseCase,
    private val updateFavoriteStatusUseCase: UpdateFavoriteStatusUseCase
) : ViewModel() {

    private val _coursesState = MutableStateFlow<CoursesUiState>(CoursesUiState.Loading)
    val coursesState: StateFlow<CoursesUiState> = _coursesState.asStateFlow()

    init {
        fetchCourses()
    }

    fun fetchCourses() {
        viewModelScope.launch {
            _coursesState.value = CoursesUiState.Loading
            try {
                getCoursesUseCase().collect { courseList ->
                    _coursesState.value = CoursesUiState.Success(courseList)
                }
            } catch (e: Exception) {
                if (e is CancellationException) {
                    throw e
                }
                _coursesState.value = CoursesUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun toggleFavorite(courseId: String) {
        viewModelScope.launch {
            if (_coursesState.value is CoursesUiState.Success) {
                val currentCourses = (_coursesState.value as CoursesUiState.Success).courses
                val courseToUpdate = currentCourses.find { it.id == courseId }
                courseToUpdate?.let {
                    val newFavoriteState = !it.hasLike
                    updateFavoriteStatusUseCase(it.id, newFavoriteState)
                    val updatedCourses = currentCourses.map { course ->
                        if (course.id == courseId) {
                            course.copy(hasLike = newFavoriteState)
                        } else {
                            course
                        }
                    }
                    _coursesState.value = CoursesUiState.Success(updatedCourses)
                }
            }
        }
    }

    fun sortCoursesByPublishDate() {
        viewModelScope.launch {
            if (_coursesState.value is CoursesUiState.Success) {
                val currentCourses = (_coursesState.value as CoursesUiState.Success).courses
                try {
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val sortedCourses = currentCourses.sortedByDescending { course ->
                        try {
                            dateFormat.parse(course.publishDate)
                        } catch (e: Exception) {
                            null
                        }
                    }
                    _coursesState.value = CoursesUiState.Success(sortedCourses)
                } catch (e: Exception) {
                    if (e is CancellationException) {
                        throw e
                    }
                    _coursesState.value = CoursesUiState.Error("Error sorting dates: ${e.message}")
                }
            }
        }
    }
}