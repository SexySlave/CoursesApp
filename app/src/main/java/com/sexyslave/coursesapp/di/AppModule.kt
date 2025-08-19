package com.sexyslave.coursesapp.di


import androidx.room.Room
import com.sexyslave.coursesapp.features.courses.CoursesViewModel
import com.sexyslave.data.database.AppDatabase // Предполагаемый импорт
import com.sexyslave.data.database.dao.CourseDao // Предполагаемый импорт
import com.sexyslave.data.network.ApiService
import com.sexyslave.data.repository.CourseRepositoryImpl
import com.sexyslave.domain.repository.CourseRepository
import com.sexyslave.domain.usecase.GetCoursesUseCase
import com.sexyslave.domain.usecase.UpdateFavoriteStatusUseCase // Добавленный импорт
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    // ViewModel
    viewModel { CoursesViewModel(get()) } // Добавили GetCoursesUseCase и UpdateFavoriteStatusUseCase
}

val domainModule = module {
    // UseCases
    factory { GetCoursesUseCase(get()) }
    factory { UpdateFavoriteStatusUseCase(get()) }
}

val dataModule = module {
    // CoroutineScope for Repository background tasks
    single { CoroutineScope(SupervisorJob() + Dispatchers.IO) } // Предоставляем CoroutineScope

    // Database
    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java, "courses_database" // Имя БД, если другое - поправьте
        ).build()
    }
    single<CourseDao> { get<AppDatabase>().courseDao() }

    // Repositories
    single<CourseRepository> {
        CourseRepositoryImpl(
            apiService = get(),
            courseDao = get(),
            externalScope = get() // Внедряем CoroutineScope
        )
    }

    // Network
    single<ApiService> {
        Retrofit.Builder()
            .baseUrl("https://drive.usercontent.google.com/") // Base URL from your link
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
