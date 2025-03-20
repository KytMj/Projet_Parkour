package com.example.projet_parkour.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val baseUrl = "http://92.222.217.100";

    private fun getInstance() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okhttpClient(token = Token).build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun okhttpClient(token : Token) = OkHttpClient().newBuilder()
        .addInterceptor(
            Interceptor { chain ->
                val request : Request = chain.request().newBuilder()
                    .header("Authorization", token.value)
                    .build()

                chain.proceed(request)
            }
        )

    val api : API = getInstance().create(API::class.java)
}