package com.sexyslave.data.repository

import com.sexyslave.data.database.dao.CourseDao
import com.sexyslave.data.database.entity.CourseEntity
import com.sexyslave.data.model.CourseDto
import com.sexyslave.data.network.ApiService
import com.sexyslave.domain.model.Course
import com.sexyslave.domain.repository.CourseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class CourseRepositoryImpl(
    private val apiService: ApiService,
    private val courseDao: CourseDao,
    private val externalScope: CoroutineScope
) : CourseRepository {

    private val fetchMutex = Mutex()
    private var initialDataFetched = false // Флаг, чтобы не загружать данные каждый раз


    override fun getCourses(): Flow<List<Course>> {
        if (!initialDataFetched) {
            externalScope.launch {
                fetchAndSyncCourses()
            }
        }

        return courseDao.getAllCourses().map { entities ->
            entities.map { it.toDomain() }
        }
    }


    private suspend fun fetchAndSyncCourses() {
        fetchMutex.withLock {
            try {
                val coursesDto = apiService.getCourses().courses

                val entitiesToSave = coursesDto.map { dto ->
                    val existingEntity = courseDao.getCourseById(dto.id)
                    val localHasLike = existingEntity?.hasLike ?: false
                    val finalHasLike = if (localHasLike) {
                        true
                    } else {
                        dto.hasLike ?: false
                    }
                    
                    dto.toEntity(finalHasLike)
                }

                courseDao.insertCourses(entitiesToSave)
                initialDataFetched = true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override suspend fun updateFavoriteStatus(courseId: String, isFavorite: Boolean) {
        courseDao.updateFavoriteStatus(courseId, isFavorite)

    }
}

// --- Mappers ---
fun CourseDto.toEntity(currentHasLike: Boolean): CourseEntity {
    return CourseEntity(
        id = id,
        title = title,
        text = text,
        price = price,
        rate = rate,
        startDate = startDate,
        hasLike = currentHasLike,
        publishDate = publishDate
    )
}


fun CourseEntity.toDomain(): Course {
    return Course(
        id = id,
        title = title ?: "",
        description = text ?: "",
        price = price ?: "",
        rating = rate ?: 0.0,
        startDate = startDate ?: "",
        hasLike = hasLike,
        publishDate = publishDate ?: "",
        imageRes = android.R.drawable.ic_menu_gallery
    )
}
