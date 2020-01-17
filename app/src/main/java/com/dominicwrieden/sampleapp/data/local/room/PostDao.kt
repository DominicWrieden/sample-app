package com.dominicwrieden.sampleapp.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dominicwrieden.sampleapp.data.entity.Post
import io.reactivex.Observable

@Dao
interface PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPosts(posts: List<Post>)


    @Query("SELECT * FROM Post")
    fun getAllPosts(): Observable<List<Post>>

    @Query("SELECT * FROM Post WHERE id = :id")
    fun getPost(id: Int): Observable<Post>

}