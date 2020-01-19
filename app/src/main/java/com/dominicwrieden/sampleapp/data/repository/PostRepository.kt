package com.dominicwrieden.sampleapp.data.repository

import com.dominicwrieden.sampleapp.data.local.room.PostDao
import com.dominicwrieden.sampleapp.data.remote.Api

class PostRepository private constructor(
    private val postDao: PostDao,
    private val api: Api
) {

    companion object {
        // For Singleton instantiation
        private var sInstance: PostRepository? = null

        fun getInstance(postDao: PostDao, api: Api) =
            sInstance ?: synchronized(this) {
                sInstance
                    ?: PostRepository(
                        postDao = postDao,
                        api = api
                    ).also { sInstance = it }
            }
    }

}