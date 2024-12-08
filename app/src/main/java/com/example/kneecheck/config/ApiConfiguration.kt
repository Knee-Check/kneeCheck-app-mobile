package com.example.kneecheck.config

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ApiConfiguration{
    companion object{
        lateinit var defaultRepo: DefaultRepo

        fun getApiService() {
            // API
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://kneecheck-app-expressjs-91798386303.asia-southeast2.run.app/")
                .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
                .build()

            defaultRepo = DefaultRepo(retrofit.create(ApiService::class.java))
        }
    }
}