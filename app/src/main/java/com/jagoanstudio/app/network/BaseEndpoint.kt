package com.jagoanstudio.app.network

interface BaseEndpoint {

    companion object {
        val baseUrl = "https://jsonplaceholder.typicode.com/"
        val posts = "posts/"
        val users = "users/"
        val comments = "/comments"
        val albums = "albums"
        val photos = "/photos"
    }

}
