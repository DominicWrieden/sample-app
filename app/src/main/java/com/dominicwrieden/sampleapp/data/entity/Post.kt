package com.dominicwrieden.sampleapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Post(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val userId: Int,
    val title: String,
    val body: String
)