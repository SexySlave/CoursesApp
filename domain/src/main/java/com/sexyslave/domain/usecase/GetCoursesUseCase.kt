package com.sexyslave.domain.usecase

import com.sexyslave.domain.model.Course
import com.sexyslave.domain.repository.CourseRepository
import kotlinx.coroutines.flow.Flow

class GetCoursesUseCase(private val courseRepository: CourseRepository) {
    operator fun invoke(): Flow<List<Course>> {
        return courseRepository.getCourses()
    }
}