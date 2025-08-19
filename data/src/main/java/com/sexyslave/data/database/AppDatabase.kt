package com.sexyslave.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sexyslave.data.database.dao.CourseDao
import com.sexyslave.data.database.entity.CourseEntity

@Database(entities = [CourseEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun courseDao(): CourseDao
}
