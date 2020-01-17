package com.dominicwrieden.sampleapp.data.repository

import com.dominicwrieden.sampleapp.data.local.room.PersonDao

class PersonRepository private constructor(
    private val personDao: PersonDao
) {

    companion object {
        // For Singleton instantiation
        private var sInstance: PersonRepository? = null

        fun getInstance(personDao: PersonDao) =
            sInstance ?: synchronized(this) {
                sInstance
                    ?: PersonRepository(
                        personDao = personDao
                    ).also { sInstance = it }
            }
    }
}