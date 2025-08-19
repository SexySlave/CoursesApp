package com.sexyslave.coursesapp.di

import androidx.room.Room
import com.sexyslave.coursesapp.features.courses.CoursesViewModel
import com.sexyslave.data.database.AppDatabase
import com.sexyslave.data.database.dao.CourseDao
import com.sexyslave.data.network.ApiService
import com.sexyslave.data.repository.CourseRepositoryImpl
import com.sexyslave.domain.repository.CourseRepository
import com.sexyslave.domain.usecase.GetCoursesUseCase
import com.sexyslave.domain.usecase.UpdateFavoriteStatusUseCase
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
    viewModel { CoursesViewModel(get(), get()) }
}

val domainModule = module {
    // UseCases
    factory { GetCoursesUseCase(get()) }
    factory { UpdateFavoriteStatusUseCase(get()) }
}

val dataModule = module {
    // CoroutineScope for Repository background tasks
    single { CoroutineScope(SupervisorJob() + Dispatchers.IO) }

    // Database
    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java, "courses_database"
        ).build()
    }
    single<CourseDao> { get<AppDatabase>().courseDao() }

    // Repositories
    single<CourseRepository> {
        CourseRepositoryImpl(
            apiService = get(),
            courseDao = get(),
            externalScope = get()
        )
    }

    // Network
    single<ApiService> {
        Retrofit.Builder()
            .baseUrl("https://drive.usercontent.google.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
