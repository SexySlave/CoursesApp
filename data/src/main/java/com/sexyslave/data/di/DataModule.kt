package com.sexyslave.data.di

import androidx.room.Room
import com.sexyslave.data.database.AppDatabase
import com.sexyslave.data.database.dao.CourseDao
import com.sexyslave.data.network.ApiService
import com.sexyslave.data.repository.CourseRepositoryImpl
import com.sexyslave.domain.repository.CourseRepository
import com.sexyslave.domain.usecase.GetCoursesUseCase // Добавил импорт
import com.sexyslave.domain.usecase.UpdateFavoriteStatusUseCase // Добавил импорт
import org.koin.dsl.module
import org.koin.android.ext.koin.androidContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "courses_database"
        ).build()
    }

    single<CourseDao> {
        get<AppDatabase>().courseDao()
    }
}

val networkModule = module {
    single<ApiService> {
        Retrofit.Builder()
            .baseUrl("https://drive.usercontent.google.com/") // TODO: Move to a constants file
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

val repositoryModule = module {
    single<CourseRepository> {
        CourseRepositoryImpl(get(), get(), get()) // Koin сам подставит ApiService и CourseDao
    }
}

val useCaseModule = module {
    factory { GetCoursesUseCase(get()) } // Предполагаем, что GetCoursesUseCase тоже нужен
    factory { UpdateFavoriteStatusUseCase(get()) }
}

// Объединяем модули для удобства
val dataModule = module {
    includes(databaseModule, networkModule, repositoryModule, useCaseModule)
}
