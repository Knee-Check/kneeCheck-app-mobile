package com.example.kneecheck.config

import com.example.kneecheck.entity.BasicDMLDRO
import com.example.kneecheck.entity.BasicDRO
import com.example.kneecheck.entity.HistoryDokterDRO
import com.example.kneecheck.entity.HistoryPasienDRO
import com.example.kneecheck.entity.LoginDRO
import com.example.kneecheck.entity.dashboardDokter
import com.example.kneecheck.entity.getAllPasienDRO
import com.example.kneecheck.entity.landingPageDRO
import com.example.kneecheck.entity.loginDTO
import com.example.kneecheck.entity.predictDRO
import com.example.kneecheck.entity.profileDokterDRO
import com.example.kneecheck.entity.profilePasienDRO
import com.example.kneecheck.entity.registerDokterDTO
import com.example.kneecheck.entity.registerPasienDTO
import com.example.kneecheck.entity.saveHistoryPasienBaruDTO
import com.example.kneecheck.entity.saveHistoryPasienDTO
import com.example.kneecheck.entity.saveHistoryPasienLamaDTO
import com.example.kneecheck.entity.updatePasswordDokterDTO
import com.example.kneecheck.entity.updatePasswordPasienDTO
import com.example.kneecheck.entity.updateProfileDokterDTO
import com.example.kneecheck.entity.updateProfilePasienDTO
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Multipart
import retrofit2.http.Part

interface ApiService {
    @GET("users")
    suspend fun getAllUser() : BasicDRO

    @POST("login")
    suspend fun login(
        @Body user : loginDTO
    ): LoginDRO

    @POST("register/pasien")
    suspend fun registerPasien(
        @Body regPasien : registerPasienDTO
    ): Any

    @POST("register/dokter")
    suspend fun registerDokter(
        @Body regDokter : registerDokterDTO
    ): Any

    @GET("dashboard")
    suspend fun getDashboard(
        @Header("Authorization") token: String
    ): dashboardDokter

    @GET("home")
    suspend fun getLandingPage(
        @Header("Authorization") token: String
    ): landingPageDRO

    @GET("/dokter/profile")
    suspend fun getProfileDokter(
        @Header("Authorization") token: String,
    ): profileDokterDRO

    @PUT("/dokter/profile/update-profile")
    suspend fun updateProfileDokter(
        @Header("Authorization") token: String,
        @Body dataDokter : updateProfileDokterDTO
    ): BasicDMLDRO

    @PUT("/dokter/profile/update-user")
    suspend fun updatePasswordDokter(
        @Header("Authorization") token: String,
        @Body dataDokter : updatePasswordDokterDTO
    ): BasicDMLDRO

    @GET("/pasien/profile")
    suspend fun getPasienProfile(
        @Header("Authorization") token: String,
    ): profilePasienDRO

    @PUT("/pasien/profile/update-profile")
    suspend fun updateProfilePasien(
        @Header("Authorization") token: String,
        @Body dataPasien : updateProfilePasienDTO
    ): BasicDMLDRO

    @PUT("/pasien/profile/update-user")
    suspend fun updatePasswordPasien(
        @Header("Authorization") token: String,
        @Body dataPasien : updatePasswordPasienDTO
    ): BasicDMLDRO

    @Multipart
    @POST("predict")
    suspend fun predict(
        @Header("Authorization") token: String,
        @Part img: MultipartBody.Part,
    ): predictDRO
    
    @GET("/pasien/history")
    suspend fun getHistoryPasien(
        @Header("Authorization") token: String,
    ): HistoryPasienDRO

    @GET("/dokter/history")
    suspend fun getHistoryDokter(
        @Header("Authorization") token: String,
    ): HistoryDokterDRO

    @POST("/pasien/history/save")
    suspend fun saveHistoryPasien(
        @Header("Authorization") token: String,
        @Body dataHistory : saveHistoryPasienDTO
    ): BasicDMLDRO

    @POST("/dokter/history/save-new-pasien")
    suspend fun saveNewPasienfromDokter(
        @Header("Authorization") token: String,
        @Body dataHistory : saveHistoryPasienBaruDTO
    ): BasicDMLDRO

    @POST("/dokter/history/save")
    suspend fun saveHistoryPasienLamaDokter(
        @Header("Authorization") token: String,
        @Body dataHistory : saveHistoryPasienLamaDTO
    ): BasicDMLDRO

    @GET("pasien")
    suspend fun getAllPasien(
        @Header("Authorization") token: String
    ) : getAllPasienDRO
}
