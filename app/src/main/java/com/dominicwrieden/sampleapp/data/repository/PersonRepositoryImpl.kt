package com.dominicwrieden.sampleapp.data.repository

import com.dominicwrieden.sampleapp.data.entity.Person
import com.dominicwrieden.sampleapp.data.local.room.PersonDao
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

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

    override fun savePersonData(person: Person) {
        Observable.just(Unit)
            .subscribeOn(Schedulers.io()).subscribe {
                personDao.insertPerson(person)
            }
    }

    override fun getPersonList(): Observable<List<Person>> {
        return Observable.just(emptyList())
    }
}