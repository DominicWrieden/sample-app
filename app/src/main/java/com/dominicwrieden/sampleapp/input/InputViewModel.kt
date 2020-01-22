package com.dominicwrieden.sampleapp.input

import androidx.lifecycle.ViewModel
import com.dominicwrieden.sampleapp.data.entity.Person
import com.dominicwrieden.sampleapp.data.repository.PersonRepository
import com.dominicwrieden.sampleapp.util.SingleLiveEvent
import com.dominicwrieden.sampleapp.util.toLiveData
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.BehaviorSubject

class InputViewModel(private val personRepository: PersonRepository) : ViewModel() {


    companion object {
        private val ZIP_CODE_LENGTH = 5
    }

    private val disposable = CompositeDisposable()

    private val firstNameStateRelay =
        BehaviorRelay.createDefault<FirstNameState>(FirstNameState.Idle)
    private val lastNameStateRelay = BehaviorRelay.createDefault<LastNameState>(LastNameState.Idle)
    private val zipCodeStateRelay = BehaviorRelay.createDefault<ZipCodeState>(ZipCodeState.Idle)
    private val notificationStateRelay =
        BehaviorRelay.createDefault<NotificationState>(NotificationState.NotVisible)

    private val firstNameChangedSubject = BehaviorSubject.createDefault("")
    private val lastNameChangedSubject = BehaviorSubject.createDefault("")
    private val zipCodeChangedSubject = BehaviorSubject.createDefault("")

    val firstNameState = firstNameStateRelay.toLiveData()
    val lastNameState = lastNameStateRelay.toLiveData()
    val zipCodeState = zipCodeStateRelay.toLiveData()
    val notificationState = SingleLiveEvent<NotificationState>()

    init {
        firstNameChangedSubject.distinctUntilChanged()
            .map { inputFirstName ->
                if (inputFirstName.isEmpty()) {
                    FirstNameState.ClearInput
                } else {
                    FirstNameState.Idle
                }
            }.subscribe(firstNameStateRelay).addTo(disposable)

        lastNameChangedSubject.distinctUntilChanged()
            .map { inputLastName ->
                if (inputLastName.isEmpty()) {
                    LastNameState.ClearInput
                } else {
                    LastNameState.Idle
                }
            }.subscribe(lastNameStateRelay).addTo(disposable)


        zipCodeChangedSubject.distinctUntilChanged()
            .map { inputZipCode ->
                if (inputZipCode.isEmpty()) {
                    ZipCodeState.ClearInput
                } else {
                    ZipCodeState.Idle
                }
            }.subscribe(zipCodeStateRelay).addTo(disposable)

        notificationStateRelay.subscribe { notificationState.postValue(it) }.addTo(disposable)
    }

    /**
     * Function for the attached view notify the viewmodel about changes
     * in the first name text field
     */
    fun firstNameChanged(firstName: String) {
        firstNameChangedSubject.onNext(firstName)
    }

    /**
     * Function for the attached view notify the viewmodel about changes
     * in the last name text field
     */
    fun lastNameChanged(lastName: String) {
        lastNameChangedSubject.onNext(lastName)
    }

    /**
     * Function for the attached view notify the viewmodel about changes
     * in the zip code text field
     */
    fun zipCodeChanged(zipCode: String) {
        zipCodeChangedSubject.onNext(zipCode)
    }

    /**
     * Function for the attached view notify the viewmodel, that the save button was clicked
     */
    fun saveButtonClicked() {
        //Check if the input data is valid
        var personDataValid = true

        if (firstNameChangedSubject.value.isNullOrBlank()) {
            firstNameStateRelay.accept(FirstNameState.Missing)
            personDataValid = false
        }

        if (lastNameChangedSubject.value.isNullOrBlank()) {
            lastNameStateRelay.accept(LastNameState.Missing)
            personDataValid = false
        }

        if (zipCodeChangedSubject.value.isNullOrBlank()) {
            zipCodeStateRelay.accept(ZipCodeState.Error(ZipCodeErrors.ZIP_CODE_MISSING))
            personDataValid = false
        } else if (zipCodeChangedSubject.value?.trim()?.length ?: 0 < ZIP_CODE_LENGTH) {
            zipCodeStateRelay.accept(ZipCodeState.Error(ZipCodeErrors.ZIP_CODE_TOO_SHORT))
            personDataValid = false
        } else if (zipCodeChangedSubject.value?.trim()?.length ?: 0 > ZIP_CODE_LENGTH) {
            zipCodeStateRelay.accept(ZipCodeState.Error(ZipCodeErrors.ZIP_CODE_TOO_LONG))
            personDataValid = false
        }
        // TODO zip code not existing

        if (personDataValid) {
            val person = Person(
                id = 0,
                firstname = firstNameChangedSubject.value!!.trim(),
                lastName = lastNameChangedSubject.value!!.trim(),
                zipCode = Integer.valueOf(zipCodeChangedSubject.value!!.trim())
            )

            //save data to local database
            personRepository.savePersonData(person)

            // clear person data input
            firstNameChangedSubject.onNext("")
            lastNameChangedSubject.onNext("")
            zipCodeChangedSubject.onNext("")
            notificationStateRelay.accept(NotificationState.SavingSuccessful)
        }
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }

    /**
     *
     * For test purposes. Because SingleLiveEvent isn't properly observable
     */
    fun getNotificationStateTest() = notificationStateRelay.toLiveData()
}