package com.jagoanstudio.app.network

interface CallBackClient {

    fun loading()
    fun success(message: String?, code: Int?)
    fun failed(message: String?)
    fun errorConnection(t: Throwable?)
    fun error(t: Throwable?)

}