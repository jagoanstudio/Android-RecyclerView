package com.jagoanstudio.app.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.OkHttpResponseAndStringRequestListener
import com.google.gson.Gson
import com.jagoanstudio.app.R
import com.jagoanstudio.app.model.ModelAlbums
import com.jagoanstudio.app.network.*
import okhttp3.Response

class AlbumsViewModel : ViewModel(), BaseEndpoint {

    private var context: Context? = null
    private var callBackClient: CallBackClient? = null
    private var callBackAlbums: CallBackAlbums? = null

    fun init(context: Context, callBackClient: CallBackClient, callBackAlbums: CallBackAlbums) {
        this.context = context
        this.callBackClient = callBackClient
        this.callBackAlbums = callBackAlbums
    }

    fun fetchListAlbums(userId: String?): LiveData<ResponseApi> {
        val listData: MutableLiveData<ResponseApi> = MutableLiveData()
        listData.postValue(ResponseApi.loading())
        AndroidNetworking.get(BaseEndpoint.baseUrl.plus(BaseEndpoint.users).plus(userId).plus("/").plus(BaseEndpoint.albums))
            .setOkHttpClient(OkHttpClientRepository.general(context))
            .build()
            .getAsOkHttpResponseAndString(object : OkHttpResponseAndStringRequestListener {
                override fun onResponse(okHttpResponse: Response, response: String) {
                    listData.postValue(ResponseApi.success(okHttpResponse, response, null))
                }

                override fun onError(anError: ANError?) {
                    listData.postValue(ResponseApi.error(anError))
                }
            })
        return listData
    }

    fun processListAlbums(responseApi: ResponseApi) {
        when (responseApi.status) {
            Status.ERROR ->
                try {
                    val api = Gson().fromJson(responseApi.error?.errorBody, ModelAlbums::class.java)
                } catch (e: Exception) {
                    if (responseApi.error?.errorDetail == "responseFromServerError") {
                        callBackClient?.error(e)
                    } else if (responseApi.error?.errorDetail == "connectionError") {
                        if (responseApi.error.toString().contains("timeout")) {
                            callBackClient?.failed(context?.getString(R.string.connection_timeout))
                        } else {
                            callBackClient?.errorConnection(e)
                        }
                    } else {
                        callBackClient?.error(e)
                    }
                }
            Status.LOADING -> callBackClient?.loading()
            Status.SUCCESS ->
                try {
                    val api = Gson().fromJson(responseApi.responseString, Array<ModelAlbums>::class.java)
                    callBackClient?.success("Success", 200)
                    callBackAlbums?.resultListAlbums("Success", api.toList())
                } catch (e: Exception) {
                    if (responseApi.error?.errorDetail == "responseFromServerError") {
                        callBackClient?.error(e)
                    } else if (responseApi.error?.errorDetail == "connectionError") {
                        if (responseApi.error.toString().contains("timeout")) {
                            callBackClient?.failed(context?.getString(R.string.connection_timeout))
                        } else {
                            callBackClient?.errorConnection(e)
                        }
                    } else {
                        callBackClient?.error(e)
                    }
                }
        }
    }

    interface CallBackAlbums {
        fun resultListAlbums(message: String?, data: List<ModelAlbums>?)
    }

}