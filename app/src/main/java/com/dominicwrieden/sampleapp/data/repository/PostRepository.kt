package com.dominicwrieden.sampleapp.data.repository

import com.dominicwrieden.sampleapp.data.entity.Post
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.Response

interface PostRepository {

    fun getPosts(): Observable<List<Post>>

    fun refreshPosts(): Single<Response>

}