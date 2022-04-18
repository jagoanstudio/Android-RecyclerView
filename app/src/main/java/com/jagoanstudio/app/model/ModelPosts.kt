package com.jagoanstudio.app.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ModelPosts {

    @SerializedName("userId")
    @Expose
    var userId: Int? = null

    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("body")
    @Expose
    var body: String? = null

    @SerializedName("company")
    @Expose
    var company: ModelUsers.Company? = null

}