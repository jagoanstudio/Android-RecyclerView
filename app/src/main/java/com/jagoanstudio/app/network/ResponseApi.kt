package com.jagoanstudio.app.network

import com.androidnetworking.error.ANError
import okhttp3.Response

class ResponseApi private constructor(val status: Status, val data: Response?, val error: ANError?, val responseString: String?, val type: String?) {

    companion object {
        fun loading(): ResponseApi {
            return ResponseApi(Status.LOADING, null, null, null, null)
        }

        fun success(data: Response, responseString: String, type: String?): ResponseApi {
            return ResponseApi(Status.SUCCESS, data, null, responseString, type)
        }

        fun error(error: ANError?): ResponseApi {
            return ResponseApi(Status.ERROR, null, error, null, null)
        }
    }

}