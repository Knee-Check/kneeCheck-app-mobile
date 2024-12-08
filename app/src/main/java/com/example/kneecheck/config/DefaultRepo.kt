package com.example.kneecheck.config

import android.util.Log
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

class DefaultRepo (
    private val dataSourceRemote : ApiService,
) {
    suspend fun getAllUser() : BasicDRO {
        Log.e("DefaultRepo", dataSourceRemote.getAllUser().toString())
        return dataSourceRemote.getAllUser()
    }

    suspend fun login(user : loginDTO): LoginDRO {
        Log.e("DefaultRepo result", dataSourceRemote.login(user).toString())
        return dataSourceRemote.login(user)
    }

    suspend fun registerPasien(user : registerPasienDTO): Any{
        return dataSourceRemote.registerPasien(user)
    }

    suspend fun registerDokter(user : registerDokterDTO): Any{
        return dataSourceRemote.registerDokter(user)
    }

    suspend fun getDashboard(token: String): dashboardDokter{
        return dataSourceRemote.getDashboard(token)
    }

    suspend fun getLandingPage(token: String): landingPageDRO{
        return dataSourceRemote.getLandingPage(token)
    }

    suspend fun getProfilePasien(token: String): profilePasienDRO {
        return dataSourceRemote.getPasienProfile(token)
    }

    suspend fun updateProfilePasien(token: String, data: updateProfilePasienDTO): BasicDMLDRO{
        return dataSourceRemote.updateProfilePasien(token, data)
    }

    suspend fun updatePasswordPasien(token: String, data: updatePasswordPasienDTO): BasicDMLDRO {
        return dataSourceRemote.updatePasswordPasien(token, data)
    }

    suspend fun getProfileDokter(token: String): profileDokterDRO {
        return dataSourceRemote.getProfileDokter(token)
    }

    suspend fun updateProfileDokter(token: String, data: updateProfileDokterDTO): BasicDMLDRO{
        return dataSourceRemote.updateProfileDokter(token, data)
    }

    suspend fun updatePasswordDokter(token: String, data: updatePasswordDokterDTO): BasicDMLDRO {
        return dataSourceRemote.updatePasswordDokter(token, data)
    }

    suspend fun predict(token: String, img: MultipartBody.Part): predictDRO {
        return dataSourceRemote.predict(token, img)
    }
    
    suspend fun getHistoryPasien(token: String): HistoryPasienDRO{
        return dataSourceRemote.getHistoryPasien(token)
    }

    suspend fun getHistoryDokter(token: String): HistoryDokterDRO{
        return dataSourceRemote.getHistoryDokter(token)
    }

    suspend fun saveHistoryPasien(token: String, data: saveHistoryPasienDTO): BasicDMLDRO {
        return dataSourceRemote.saveHistoryPasien(token, data)
    }

    suspend fun saveHistoryPasienBaru(token: String, data: saveHistoryPasienBaruDTO): BasicDMLDRO {
        return dataSourceRemote.saveNewPasienfromDokter(token, data)
    }

    suspend fun saveHistoryPasienLama(token: String, data: saveHistoryPasienLamaDTO): BasicDMLDRO {
        return dataSourceRemote.saveHistoryPasienLamaDokter(token, data)
    }

    suspend fun getAllPasien(token: String): getAllPasienDRO {
        return dataSourceRemote.getAllPasien(token)
    }
}