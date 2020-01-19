package com.dominicwrieden.sampleapp.input

import com.InstantTaskExecutorRule
import com.RxImmediateSchedulerRule
import com.dominicwrieden.sampleapp.data.repository.PersonRepository
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import org.junit.*
import org.mockito.Mock
import org.mockito.MockitoAnnotations


class InputViewModelTest {
    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Rule
    @JvmField
    val ruleForLivaData = InstantTaskExecutorRule()


    @Mock
    lateinit var personRepository: PersonRepository


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun initialCondition() {
        val inputViewModel = InputViewModel(personRepository)

        Assert.assertEquals(FirstNameState.Idle, inputViewModel.firstNameState)
        Assert.assertEquals(LastNameState.Idle, inputViewModel.lastNameState)
        Assert.assertEquals(ZipCodeState.Idle, inputViewModel.zipCodeState)
        Assert.assertEquals(NotificationState.NotVisible, inputViewModel.notificationState)
    }


    @Test
    fun saveClicked_personDataValid() {
        whenever(personRepository.savePeronData(any())).doReturn(Observable.just(true))
        val inputViewModel = InputViewModel(personRepository)

        inputViewModel.firstNameChanged("Ingeborg")
        inputViewModel.lastNameChanged("Schnabel")
        inputViewModel.zipCodeChanged("28201")

        inputViewModel.saveButtonClicked()


        Assert.assertEquals(FirstNameState.ClearInput, inputViewModel.firstNameState)
        Assert.assertEquals(LastNameState.ClearInput, inputViewModel.lastNameState)
        Assert.assertEquals(ZipCodeState.ClearInput, inputViewModel.zipCodeState)
        Assert.assertEquals(NotificationState.SavingSuccessful, inputViewModel.notificationState)
    }

    @Test
    fun saveClicked_personDataUnvalid_firstName() {
        val inputViewModel = InputViewModel(personRepository)

        inputViewModel.firstNameChanged("")
        inputViewModel.lastNameChanged("Schnabel")
        inputViewModel.zipCodeChanged("28201")

        inputViewModel.saveButtonClicked()

        Assert.assertEquals(FirstNameState.Missing, inputViewModel.firstNameState)
        Assert.assertEquals(LastNameState.Idle, inputViewModel.lastNameState)
        Assert.assertEquals(ZipCodeState.Idle, inputViewModel.zipCodeState)
        Assert.assertEquals(NotificationState.NotVisible, inputViewModel.notificationState)
    }

    @Test
    fun saveClicked_personDataUnvalid_lastName() {
        val inputViewModel = InputViewModel(personRepository)

        inputViewModel.firstNameChanged("Ingeborg")
        inputViewModel.lastNameChanged("")
        inputViewModel.zipCodeChanged("28201")

        inputViewModel.saveButtonClicked()


        Assert.assertEquals(FirstNameState.Idle, inputViewModel.firstNameState)
        Assert.assertEquals(LastNameState.Missing, inputViewModel.lastNameState)
        Assert.assertEquals(ZipCodeState.Idle, inputViewModel.zipCodeState)
        Assert.assertEquals(NotificationState.NotVisible, inputViewModel.notificationState)
    }

    @Test
    fun saveClicked_personDataUnvalid_zipCode_missing() {
        val inputViewModel = InputViewModel(personRepository)

        inputViewModel.firstNameChanged("Ingeborg")
        inputViewModel.lastNameChanged("Schnabel")
        inputViewModel.zipCodeChanged("")

        inputViewModel.saveButtonClicked()


        Assert.assertEquals(FirstNameState.Idle, inputViewModel.firstNameState)
        Assert.assertEquals(LastNameState.Idle, inputViewModel.lastNameState)
        Assert.assertEquals(
            ZipCodeState.Error(ZipCodeErrors.ZIP_CODE_MISSING),
            inputViewModel.zipCodeState
        )
        Assert.assertEquals(NotificationState.NotVisible, inputViewModel.notificationState)
    }

    @Test
    fun saveClicked_personDataUnvalid_zipCode_tooShort() {
        val inputViewModel = InputViewModel(personRepository)

        inputViewModel.firstNameChanged("Ingeborg")
        inputViewModel.lastNameChanged("Schnabel")
        inputViewModel.zipCodeChanged("123")

        inputViewModel.saveButtonClicked()


        Assert.assertEquals(FirstNameState.Idle, inputViewModel.firstNameState)
        Assert.assertEquals(LastNameState.Idle, inputViewModel.lastNameState)
        Assert.assertEquals(
            ZipCodeState.Error(ZipCodeErrors.ZIP_CODE_TOO_SHORT),
            inputViewModel.zipCodeState
        )
        Assert.assertEquals(NotificationState.NotVisible, inputViewModel.notificationState)
    }

    @Test
    fun saveClicked_personDataUnvalid_zipCode_tooLong() {
        val inputViewModel = InputViewModel(personRepository)

        inputViewModel.firstNameChanged("Ingeborg")
        inputViewModel.lastNameChanged("Schnabel")
        inputViewModel.zipCodeChanged("123123123123")

        inputViewModel.saveButtonClicked()


        Assert.assertEquals(FirstNameState.Idle, inputViewModel.firstNameState)
        Assert.assertEquals(LastNameState.Idle, inputViewModel.lastNameState)
        Assert.assertEquals(
            ZipCodeState.Error(ZipCodeErrors.ZIP_CODE_TOO_LONG),
            inputViewModel.zipCodeState
        )
        Assert.assertEquals(NotificationState.NotVisible, inputViewModel.notificationState)
    }

    @Test
    fun saveClicked_personDataUnvalid_zipCode_notExisting() {
        val inputViewModel = InputViewModel(personRepository)

        inputViewModel.firstNameChanged("Ingeborg")
        inputViewModel.lastNameChanged("Schnabel")
        inputViewModel.zipCodeChanged("28999")

        inputViewModel.saveButtonClicked()

        Assert.assertEquals(FirstNameState.Idle, inputViewModel.firstNameState)
        Assert.assertEquals(LastNameState.Idle, inputViewModel.lastNameState)
        Assert.assertEquals(
            ZipCodeState.Error(ZipCodeErrors.ZIP_DOES_NOT_EXIST),
            inputViewModel.zipCodeState
        )
        Assert.assertEquals(NotificationState.NotVisible, inputViewModel.notificationState)
    }

    @Test
    fun saveClicked_personDataUnvalid_firstName_lastName() {
        val inputViewModel = InputViewModel(personRepository)

        inputViewModel.firstNameChanged("")
        inputViewModel.lastNameChanged("")
        inputViewModel.zipCodeChanged("28999")

        inputViewModel.saveButtonClicked()


        Assert.assertEquals(FirstNameState.Missing, inputViewModel.firstNameState)
        Assert.assertEquals(LastNameState.Missing, inputViewModel.lastNameState)
        Assert.assertEquals(
            ZipCodeState.Idle,
            inputViewModel.zipCodeState
        )
        Assert.assertEquals(NotificationState.NotVisible, inputViewModel.notificationState)
    }

    @Test
    fun saveClicked_personDataUnvalid_firstName_zipCodeNotExisting() {
        val inputViewModel = InputViewModel(personRepository)

        inputViewModel.firstNameChanged("")
        inputViewModel.lastNameChanged("Schnabel")
        inputViewModel.zipCodeChanged("28999")

        inputViewModel.saveButtonClicked()


        Assert.assertEquals(FirstNameState.Missing, inputViewModel.firstNameState)
        Assert.assertEquals(LastNameState.Idle, inputViewModel.lastNameState)
        Assert.assertEquals(
            ZipCodeState.Error(ZipCodeErrors.ZIP_DOES_NOT_EXIST),
            inputViewModel.zipCodeState
        )
        Assert.assertEquals(NotificationState.NotVisible, inputViewModel.notificationState)
    }

    @Test
    fun saveClicked_personDataUnvalid_lastName_zipCodeTooShort() {
        val inputViewModel = InputViewModel(personRepository)

        inputViewModel.firstNameChanged("Dieter")
        inputViewModel.lastNameChanged("")
        inputViewModel.zipCodeChanged("289")

        inputViewModel.saveButtonClicked()


        Assert.assertEquals(FirstNameState.Idle, inputViewModel.firstNameState)
        Assert.assertEquals(LastNameState.Missing, inputViewModel.lastNameState)
        Assert.assertEquals(
            ZipCodeState.Error(ZipCodeErrors.ZIP_CODE_TOO_SHORT),
            inputViewModel.zipCodeState
        )
        Assert.assertEquals(NotificationState.NotVisible, inputViewModel.notificationState)
    }

    @Test
    fun saveClicked_personDataUnvalid_firstName_lastName_zipCodeTooLong() {
        val inputViewModel = InputViewModel(personRepository)

        inputViewModel.firstNameChanged("")
        inputViewModel.lastNameChanged("")
        inputViewModel.zipCodeChanged("28999999")

        inputViewModel.saveButtonClicked()


        Assert.assertEquals(FirstNameState.Missing, inputViewModel.firstNameState)
        Assert.assertEquals(LastNameState.Missing, inputViewModel.lastNameState)
        Assert.assertEquals(
            ZipCodeState.Error(ZipCodeErrors.ZIP_CODE_TOO_LONG),
            inputViewModel.zipCodeState
        )
        Assert.assertEquals(NotificationState.NotVisible, inputViewModel.notificationState)
    }

    @Test
    fun saveClicked_savingFailed() {
        whenever(personRepository.savePeronData(any())).doReturn(Observable.just(false))

        val inputViewModel = InputViewModel(personRepository)

        inputViewModel.firstNameChanged("Klaus")
        inputViewModel.lastNameChanged("Wowi")
        inputViewModel.zipCodeChanged("28213")

        inputViewModel.saveButtonClicked()

        Assert.assertEquals(FirstNameState.Idle, inputViewModel.firstNameState)
        Assert.assertEquals(LastNameState.Idle, inputViewModel.lastNameState)
        Assert.assertEquals(ZipCodeState.Idle, inputViewModel.zipCodeState)
        Assert.assertEquals(NotificationState.SavingFailed, inputViewModel.notificationState)
    }

    @Test
    fun saveClicked_firstName_clearError() {
        val inputViewModel = InputViewModel(personRepository)

        inputViewModel.firstNameChanged("")
        inputViewModel.lastNameChanged("Wowi")
        inputViewModel.zipCodeChanged("28213")

        inputViewModel.saveButtonClicked()

        Assert.assertEquals(FirstNameState.Missing, inputViewModel.firstNameState)
        Assert.assertEquals(LastNameState.Idle, inputViewModel.lastNameState)
        Assert.assertEquals(ZipCodeState.Idle, inputViewModel.zipCodeState)
        Assert.assertEquals(NotificationState.NotVisible, inputViewModel.notificationState)

        inputViewModel.firstNameChanged("asd")

        Assert.assertEquals(FirstNameState.Idle, inputViewModel.firstNameState)
        Assert.assertEquals(LastNameState.Idle, inputViewModel.lastNameState)
        Assert.assertEquals(ZipCodeState.Idle, inputViewModel.zipCodeState)
        Assert.assertEquals(NotificationState.NotVisible, inputViewModel.notificationState)
    }

    @Test
    fun saveClicked_lastName_clearError() {
        val inputViewModel = InputViewModel(personRepository)

        inputViewModel.firstNameChanged("Wolf")
        inputViewModel.lastNameChanged("")
        inputViewModel.zipCodeChanged("28213")

        inputViewModel.saveButtonClicked()

        Assert.assertEquals(FirstNameState.Idle, inputViewModel.firstNameState)
        Assert.assertEquals(LastNameState.Missing, inputViewModel.lastNameState)
        Assert.assertEquals(ZipCodeState.Idle, inputViewModel.zipCodeState)
        Assert.assertEquals(NotificationState.NotVisible, inputViewModel.notificationState)

        inputViewModel.lastNameChanged("asdd")

        Assert.assertEquals(FirstNameState.Idle, inputViewModel.firstNameState)
        Assert.assertEquals(LastNameState.Idle, inputViewModel.lastNameState)
        Assert.assertEquals(ZipCodeState.Idle, inputViewModel.zipCodeState)
        Assert.assertEquals(NotificationState.NotVisible, inputViewModel.notificationState)
    }

    @Test
    fun saveClicked_zipCode_clearError() {
        val inputViewModel = InputViewModel(personRepository)

        inputViewModel.firstNameChanged("Wolf")
        inputViewModel.lastNameChanged("Wolfensen")
        inputViewModel.zipCodeChanged("")

        inputViewModel.saveButtonClicked()

        Assert.assertEquals(FirstNameState.Idle, inputViewModel.firstNameState)
        Assert.assertEquals(LastNameState.Idle, inputViewModel.lastNameState)
        Assert.assertEquals(
            ZipCodeState.Error(ZipCodeErrors.ZIP_CODE_MISSING),
            inputViewModel.zipCodeState
        )
        Assert.assertEquals(NotificationState.NotVisible, inputViewModel.notificationState)

        inputViewModel.zipCodeChanged("213")

        Assert.assertEquals(FirstNameState.Idle, inputViewModel.firstNameState)
        Assert.assertEquals(LastNameState.Idle, inputViewModel.lastNameState)
        Assert.assertEquals(ZipCodeState.Idle, inputViewModel.zipCodeState)
        Assert.assertEquals(NotificationState.NotVisible, inputViewModel.notificationState)
    }
}