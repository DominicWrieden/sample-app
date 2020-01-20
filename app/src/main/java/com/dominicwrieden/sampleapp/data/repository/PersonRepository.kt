package com.dominicwrieden.sampleapp.data.repository

import com.dominicwrieden.sampleapp.data.entity.Person

interface PersonRepository {

    fun savePeronData(person: Person)

}