package com.dominicwrieden.sampleapp.data.repository

import com.dominicwrieden.sampleapp.data.entity.Post
import io.reactivex.Observable
import okhttp3.Response

interface PostRepository {

    fun getPosts(): Observable<List<Post>>

    fun refreshPosts(): Observable<Response>

}