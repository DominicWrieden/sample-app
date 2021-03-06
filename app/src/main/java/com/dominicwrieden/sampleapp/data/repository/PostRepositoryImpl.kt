package com.dominicwrieden.sampleapp.data.repository

import com.dominicwrieden.sampleapp.data.local.room.PostDao
import com.dominicwrieden.sampleapp.data.remote.Api
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.Response

class PostRepositoryImpl private constructor(
    private val postDao: PostDao,
    private val api: Api
) : PostRepository {

    companion object {
        // For Singleton instantiation
        private var sInstance: PostRepositoryImpl? = null

        fun getInstance(postDao: PostDao, api: Api) =
            sInstance ?: synchronized(this) {
                sInstance
                    ?: PostRepositoryImpl(
                        postDao = postDao,
                        api = api
                    ).also { sInstance = it }
            }
    }

    override fun getPosts() = postDao.getAllPosts()


    override fun refreshPosts(): Single<Response> {
        return api.postWebService.getPosts()
            .subscribeOn(Schedulers.io())
            .doOnSuccess { successfulResponse ->
                postDao.insertPosts(successfulResponse.body() ?: emptyList())
            }
            .map { respose ->
                respose.raw()
            }
    }
}