package com.dominicwrieden.sampleapp.data.remote

import com.dominicwrieden.sampleapp.data.entity.Post
import io.reactivex.Single
import retrofit2.http.GET

interface PostService {
    @GET("posts")
    fun getPosts(): Single<List<Post>>
}

