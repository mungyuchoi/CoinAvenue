package com.moon.coinavenue.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AvenueService {
    //creating a Network Interceptor to add api_key in all the request as authInterceptor
    private val interceptor = Interceptor { chain ->
        val url = chain.request().url.newBuilder().addQueryParameter("apiKey", "API_KEY").build()
        val request = chain.request().newBuilder().url(url).build()
        chain.proceed(request)
    }

    private val apiClient = OkHttpClient().newBuilder().addInterceptor(interceptor).build()

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder().client(apiClient)
            .baseUrl("https://api.upbit.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    val avenueApi: AvenueApiInterface = getRetrofit().create(AvenueApiInterface::class.java)
}