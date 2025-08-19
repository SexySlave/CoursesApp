package com.sexyslave.coursesapp.features.courses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sexyslave.domain.model.Course
import com.sexyslave.domain.usecase.GetCoursesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect // Добавлен импорт для collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class CoursesViewModel(private val getCoursesUseCase: GetCoursesUseCase) : ViewModel() {

    private val _coursesState = MutableStateFlow<CoursesUiState>(CoursesUiState.Loading)
    val coursesState: StateFlow<CoursesUiState> = _coursesState.asStateFlow()

    init {
        fetchCourses()
    }

    fun fetchCourses() {
        viewModelScope.launch {
            _coursesState.value = CoursesUiState.Loading
            try {
                getCoursesUseCase().collect { courseList -> // Изменено здесь
                    _coursesState.value = CoursesUiState.Success(courseList) // Убрано as List<Course>
                }
            } catch (e: Exception) {
                _coursesState.value = CoursesUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun toggleFavorite(courseId: String) {
        viewModelScope.launch {
            if (_coursesState.value is CoursesUiState.Success) {
                val currentCourses = (_coursesState.value as CoursesUiState.Success).courses
                val updatedCourses = currentCourses.map { course ->
                    if (course.id == courseId) {
                        course.copy(hasLike = !course.hasLike)
                    } else {
                        course
                    }
                }
                _coursesState.value = CoursesUiState.Success(updatedCourses)
            }
        }
    }

    fun sortCoursesByPublishDate() {
        viewModelScope.launch {
            if (_coursesState.value is CoursesUiState.Success) {
                val currentCourses = (_coursesState.value as CoursesUiState.Success).courses
                // Примечание: Эта сортировка предполагает, что publishDate в формате,
                // подходящем для лексикографического сравнения (например, "yyyy-MM-dd").
                // Для более надежной сортировки дат их следует парсить.
                try {
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Пример формата
                    val sortedCourses = currentCourses.sortedByDescending { course ->
                        try {
                            dateFormat.parse(course.publishDate)
                        } catch (e: Exception) {
                            // Если дата не парсится, помещаем ее в конец или обрабатываем иначе
                            null
                        }
                    }
                    _coursesState.value = CoursesUiState.Success(sortedCourses)
                } catch (e: Exception) {
                     // Если проблема с SimpleDateFormat или общая при сортировке
                    _coursesState.value = CoursesUiState.Error("Error sorting dates: ${e.message}")
                    // Или просто оставляем текущий список
                    // _coursesState.value = CoursesUiState.Success(currentCourses)
                }
            }
        }
    }
}
