package com.dominicwrieden.sampleapp.output

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dominicwrieden.sampleapp.data.repository.PersonRepository

class OutputViewModel(private val personRepository: PersonRepository) : ViewModel() {

    val personListState: LiveData<PersonListState> = MutableLiveData<PersonListState>()
}