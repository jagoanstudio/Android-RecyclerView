package com.jagoanstudio.app.network

import android.content.Context
import android.os.Build
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class HeaderRepository internal constructor(context: Context?) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        val deviceName = manufacturer.toUpperCase() + " " + model + " " + Build.VERSION.SDK_INT

        val builder: Request.Builder = chain.request().newBuilder()
        builder.addHeader("User-Agent", deviceName)

        return chain.proceed(builder.build())
    }

}