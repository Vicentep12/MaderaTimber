package com.duoc.madertimber.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Crea una sola instancia de Retrofit para toda la app
object ClienteApi {
    val api: XanoApi by lazy {
        Retrofit.Builder()
            .baseUrl(XanoApi.URL_BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(XanoApi::class.java)
    }
}
