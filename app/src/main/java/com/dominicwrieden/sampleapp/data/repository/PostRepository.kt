package com.dominicwrieden.sampleapp.data.repository

import com.dominicwrieden.sampleapp.data.entity.Post
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.Response

interface PostRepository {

    /**
     * Get all posts saved in the local database
     */
    fun getPosts(): Observable<List<Post>>

    /**
     * Requests new posts from the internet and saves them in the local database
     */
    fun refreshPosts(): Single<Response>

}