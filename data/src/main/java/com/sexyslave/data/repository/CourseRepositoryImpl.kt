package com.sexyslave.data.repository

import com.sexyslave.data.database.dao.CourseDao
import com.sexyslave.data.database.entity.CourseEntity
import com.sexyslave.data.model.CourseDto // Предполагается, что CourseDto - это модель ответа API
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
    private val externalScope: CoroutineScope // Для запуска фоновых задач
) : CourseRepository {

    private val fetchMutex = Mutex()
    private var initialDataFetched = false // Флаг, чтобы не загружать данные каждый раз

    // Метод для получения курсов, возвращает Flow из БД
    // и инициирует загрузку/синхронизацию из сети при необходимости.
    override fun getCourses(): Flow<List<Course>> {
        // Запускаем синхронизацию в отдельной корутине, чтобы не блокировать Flow
        // и не делать это при каждой подписке, а только один раз или по условию.
        // Используем externalScope, предоставленный через DI, чтобы задача не отменялась
        // вместе с ViewModelScope подписчика, если это нежелательно.
        // Здесь можно добавить более сложную логику: проверять время последней синхронизации,
        // наличие сети и т.д.
        // Для простого случая: загружаем данные, если еще не были загружены.
        if (!initialDataFetched) {
            externalScope.launch {
                fetchAndSyncCourses()
            }
        }

        return courseDao.getAllCourses().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    // Основная логика загрузки из сети и синхронизации с БД
    private suspend fun fetchAndSyncCourses() {
        fetchMutex.withLock { // Используем Mutex, чтобы избежать параллельных загрузок
            try {
                val coursesDto = apiService.getCourses().courses // Загружаем из API

                val entitiesToSave = coursesDto.map { dto ->
                    val existingEntity = courseDao.getCourseById(dto.id)
                    val localHasLike = existingEntity?.hasLike ?: false // Берем локальный статус, если есть
                    
                    // Если локально было true, а API говорит false (или не говорит), оставляем true.
                    // Если API говорит true, то ставим true.
                    // Иначе используем значение из API (или false, если API его не вернул).
                    val finalHasLike = if (localHasLike) {
                        true
                    } else {
                        dto.hasLike ?: false // Предполагаем, что в DTO hasLike может быть nullable
                    }
                    
                    dto.toEntity(finalHasLike)
                }

                courseDao.insertCourses(entitiesToSave)
                initialDataFetched = true // Устанавливаем флаг после успешной загрузки
            } catch (e: Exception) {
                // TODO: Обработать ошибку (логирование, сообщение пользователю и т.д.)
                // initialDataFetched можно не менять, чтобы попытаться снова позже
                e.printStackTrace()
            }
        }
    }

    override suspend fun updateFavoriteStatus(courseId: String, isFavorite: Boolean) {
        courseDao.updateFavoriteStatus(courseId, isFavorite)
        // После обновления статуса в БД, Flow из getAllCourses() автоматически
        // эмитит новое состояние, и UI обновится.
    }
}

// --- Mappers ---
// (Рассмотрите возможность вынести их в отдельные файлы/объекты)

// CourseDto (из сети) -> CourseEntity (для БД)
fun CourseDto.toEntity(currentHasLike: Boolean): CourseEntity {
    return CourseEntity(
        id = id,
        title = title,
        text = text,
        price = price,
        rate = rate,
        startDate = startDate,
        hasLike = currentHasLike, // Используем актуальный hasLike
        publishDate = publishDate
    )
}

// CourseEntity (из БД) -> Course (для Domain/UI)
fun CourseEntity.toDomain(): Course {
    return Course(
        id = id,
        title = title ?: "", // Предоставляем значения по умолчанию, если поля nullable
        description = text ?: "",
        price = price ?: "",
        rating = rate ?: 0.0,
        startDate = startDate ?: "",
        hasLike = hasLike,
        publishDate = publishDate ?: "",
        imageRes = android.R.drawable.ic_menu_gallery // TODO: Заменить плейсхолдер
    )
}
