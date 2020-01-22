package com.dominicwrieden.sampleapp.data.repository

import com.dominicwrieden.sampleapp.data.entity.Person
import io.reactivex.Observable

interface PersonRepository {

    /**
     * Saves person data to the local database
     */
    fun savePersonData(person: Person)

    /**
     * Retrieves the list of all saved person data in the local database
     */
    fun getPersonList(): Observable<List<Person>>

}