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
    private val personDataSavedSubject = BehaviorSubject.create<Boolean>()

    val firstNameState = firstNameStateRelay.toLiveData()
    val lastNameState = lastNameStateRelay.toLiveData()
    val zipCodeState = zipCodeStateRelay.toLiveData()
    val notificationState = SingleLiveEvent<NotificationState>()

    init {
        firstNameChangedSubject.distinctUntilChanged()
            .skip(1)
            .map { inputFirstName ->
                if (inputFirstName.isEmpty()) {
                    FirstNameState.ClearInput
                } else {
                    FirstNameState.Idle
                }
            }.subscribe(firstNameStateRelay).addTo(disposable)

        lastNameChangedSubject.distinctUntilChanged()
            .skip(1)
            .map { inputLastName ->
                if (inputLastName.isEmpty()) {
                    LastNameState.ClearInput
                } else {
                    LastNameState.Idle
                }
            }.subscribe(lastNameStateRelay).addTo(disposable)


        zipCodeChangedSubject.distinctUntilChanged()
            .skip(1)
            .map { inputZipCode ->
                if (inputZipCode.isEmpty()) {
                    ZipCodeState.ClearInput
                } else {
                    ZipCodeState.Idle
                }
            }.subscribe(zipCodeStateRelay).addTo(disposable)

        personDataSavedSubject.filter { savingSuccessful -> savingSuccessful }
            .doOnNext {
                firstNameChangedSubject.onNext("")
                lastNameChangedSubject.onNext("")
                zipCodeChangedSubject.onNext("")
            }.map { NotificationState.SavingSuccessful }
            .subscribe(notificationStateRelay).addTo(disposable)

        notificationStateRelay.subscribe { notificationState.postValue(it) }.addTo(disposable)
    }

    fun firstNameChanged(firstName: String) {
        firstNameChangedSubject.onNext(firstName)
    }

    fun lastNameChanged(lastName: String) {
        lastNameChangedSubject.onNext(lastName)
    }

    fun zipCodeChanged(zipCode: String) {
        zipCodeChangedSubject.onNext(zipCode)
    }

    fun saveButtonClicked() {
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

            personRepository.savePersonData(person)

            personDataSavedSubject.onNext(true)
        }
    }

    /**
     *
     * For test purposes. Because SingleLiveEvent isn't properly observable
     */
    fun getNotificationStateTest() = notificationStateRelay.toLiveData()
}