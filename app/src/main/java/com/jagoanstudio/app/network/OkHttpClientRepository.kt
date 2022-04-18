package com.jagoanstudio.app.network

import android.content.Context
import okhttp3.*
import java.util.concurrent.TimeUnit

object OkHttpClientRepository {

    fun general(context: Context?): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HeaderRepository(context))
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

}