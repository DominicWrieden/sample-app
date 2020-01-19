package com.dominicwrieden.sampleapp.data.repository

import android.app.Person
import io.reactivex.Observable

interface PersonRepository {

    fun savePeronData(person: Person): Observable<Boolean>

}