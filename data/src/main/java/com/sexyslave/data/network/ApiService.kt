package com.sexyslave.data.network

import com.sexyslave.data.model.CourseResponse
import retrofit2.http.GET

interface ApiService {
    @GET("u/0/uc?id=15arTK7XT2b7Yv4BJsmDctA4Hg-BbS8-q&export=download")
    suspend fun getCourses(): CourseResponse
}