package com.dominicwrieden.sampleapp.output

import com.dominicwrieden.sampleapp.data.entity.Person

sealed class PersonListState {
    object Empty : PersonListState()
    data class PersonList(val personList: List<Person>) : PersonListState()
}