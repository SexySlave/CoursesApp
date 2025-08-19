package com.sexyslave.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "courses")
data class CourseEntity(
    @PrimaryKey val id: String,
    val title: String?,
    val text: String?, // В API это `text`, но в Course.kt это `description`
    val price: String?,
    val rate: Double?, // В API это `rate`, в Course.kt это `rating`
    val startDate: String?,
    var hasLike: Boolean = false,
    val publishDate: String?
)
