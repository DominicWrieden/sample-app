package com.dominicwrieden.sampleapp.output

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dominicwrieden.sampleapp.data.repository.PersonRepository
import com.dominicwrieden.sampleapp.util.toLiveData

class OutputViewModel(private val personRepository: PersonRepository) : ViewModel() {

    val personListState: LiveData<PersonListState> = personRepository.getPersonList()
        .map { personList ->
            if (personList.isNullOrEmpty()) {
                PersonListState.Empty
            } else {
                PersonListState.PersonList(personList)
            }
        }.toLiveData()

}