package com.dominicwrieden.sampleapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Person(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val firstname: String,
    val lastName: String,
    val zipCode: Int
    )