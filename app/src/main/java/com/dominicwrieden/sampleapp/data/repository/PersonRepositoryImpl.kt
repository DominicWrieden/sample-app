package com.dominicwrieden.sampleapp.data.repository

import android.app.Person
import com.dominicwrieden.sampleapp.data.local.room.PersonDao
import io.reactivex.Observable

class PersonRepositoryImpl private constructor(private val personDao: PersonDao) :
    PersonRepository {

    companion object {
        // For Singleton instantiation
        private var sInstance: PersonRepositoryImpl? = null

        fun getInstance(personDao: PersonDao) =
            sInstance ?: synchronized(this) {
                sInstance
                    ?: PersonRepositoryImpl(
                        personDao = personDao
                    ).also { sInstance = it }
            }
    }

    override fun savePeronData(person: Person): Observable<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}