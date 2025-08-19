package com.sexyslave.domain.model

data class Course(
    val id: String,
    val title: String,
    val description: String,
    val price: String,
    val rating: Double,
    val startDate: String,
    val hasLike: Boolean,
    val publishDate: String,
    val imageRes: Int
)