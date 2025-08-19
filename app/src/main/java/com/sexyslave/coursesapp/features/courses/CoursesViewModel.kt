package com.sexyslave.coursesapp.features.courses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sexyslave.domain.model.Course
import com.sexyslave.domain.usecase.GetCoursesUseCase
import com.sexyslave.domain.usecase.UpdateFavoriteStatusUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect // Добавлен импорт для collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import kotlinx.coroutines.CancellationException // Добавлен импорт для CancellationException

class CoursesViewModel(
    private val getCoursesUseCase: GetCoursesUseCase,
    private val updateFavoriteStatusUseCase: UpdateFavoriteStatusUseCase // Добавлено
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
                getCoursesUseCase().collect { courseList -> // Изменено здесь
                    _coursesState.value = CoursesUiState.Success(courseList) // Убрано as List<Course>
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
                    updateFavoriteStatusUseCase(it.id, newFavoriteState) // Обновляем в БД
                    // Обновляем состояние UI после успешного обновления в БД (опционально, зависит от того, как обновляется UI)
                    // Вместо этого можно просто заново загрузить курсы fetchCourses()
                    // или обновить локальный список, как было до этого:
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
                    if (e is CancellationException) { // Добавлена проверка и здесь на всякий случай
                        throw e
                    }
                    _coursesState.value = CoursesUiState.Error("Error sorting dates: ${e.message}")
                    // Или просто оставляем текущий список
                    // _coursesState.value = CoursesUiState.Success(currentCourses)
                }
            }
        }
    }
}
