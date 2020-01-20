package com.dominicwrieden.sampleapp.data.repository

import com.dominicwrieden.sampleapp.data.entity.Person
import io.reactivex.Observable

interface PersonRepository {

    fun savePersonData(person: Person)

    fun getPersonList(): Observable<List<Person>>

}