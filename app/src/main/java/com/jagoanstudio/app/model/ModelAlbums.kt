package com.jagoanstudio.app.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ModelAlbums {

    @SerializedName("userId")
    @Expose
    var userId: Int? = null

    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("photos")
    @Expose
    var photos: List<ModelPhotos>? = null

}