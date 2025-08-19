package com.sexyslave.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "courses")
data class CourseEntity(
    @PrimaryKey val id: String,
    val title: String?,
    val text: String?,
    val price: String?,
    val rate: Double?,
    val startDate: String?,
    var hasLike: Boolean = false,
    val publishDate: String?
)
