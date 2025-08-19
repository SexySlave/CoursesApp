package com.sexyslave.data.model

data class CourseResponse(
    val courses: List<CourseDto>
)

data class CourseDto(
    val id: String,
    val title: String,
    val text: String,
    val price: String,
    val rate: Double,
    val startDate: String,
    val hasLike: Boolean,
    val publishDate: String
)