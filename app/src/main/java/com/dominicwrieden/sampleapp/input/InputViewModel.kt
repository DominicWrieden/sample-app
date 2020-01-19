package com.dominicwrieden.sampleapp.input

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dominicwrieden.sampleapp.data.repository.PersonRepository

class InputViewModel(private val personRepository: PersonRepository) : ViewModel() {

    val firstNameState: LiveData<FirstNameState> = MutableLiveData()
    val lastNameState: LiveData<LastNameState> = MutableLiveData()
    val zipCodeState: LiveData<ZipCodeState> = MutableLiveData()
    val notificationState: LiveData<NotificationState> = MutableLiveData()


    fun firstNameChanged(firstName: String) {

    }

    fun lastNameChanged(lastName: String) {
    }

    fun zipCodeChanged(zipCode: String) {

    }

    fun saveButtonClicked() {
    }


}