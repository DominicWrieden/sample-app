package com.dominicwrieden.sampleapp.data.repository

import com.dominicwrieden.sampleapp.data.local.room.PostDao

class PostRepository private constructor(
    private val postDao: PostDao
) {

    companion object {
        // For Singleton instantiation
        private var sInstance: PostRepository? = null

        fun getInstance(postDao: PostDao) =
            sInstance ?: synchronized(this) {
                sInstance
                    ?: PostRepository(
                        postDao = postDao
                    ).also { sInstance = it }
            }
    }
}