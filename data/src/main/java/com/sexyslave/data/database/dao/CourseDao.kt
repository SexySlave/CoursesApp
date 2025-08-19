package com.sexyslave.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sexyslave.data.database.entity.CourseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourses(courses: List<CourseEntity>): List<Long>

    @Query("SELECT * FROM courses")
    fun getAllCourses(): Flow<List<CourseEntity>>

    @Query("UPDATE courses SET hasLike = :isFavorite WHERE id = :courseId")
    suspend fun updateFavoriteStatus(courseId: String, isFavorite: Boolean): Int

    @Query("SELECT * FROM courses WHERE id = :courseId")
    suspend fun getCourseById(courseId: String): CourseEntity?

    @Query("DELETE FROM courses")
    suspend fun deleteAllCourses(): Int
}
