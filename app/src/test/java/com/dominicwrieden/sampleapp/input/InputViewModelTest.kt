package com.dominicwrieden.sampleapp.input

import com.InstantTaskExecutorRule
import com.RxSchedulerRule
import com.dominicwrieden.sampleapp.data.repository.PersonRepository
import com.dominicwrieden.testObserver
import com.google.common.truth.Truth
import org.junit.*
import org.mockito.Mock
import org.mockito.MockitoAnnotations


class InputViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rxSchedulerRule = RxSchedulerRule()


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

        val firstNameState = inputViewModel.firstNameState.testObserver()
        val lastNameState = inputViewModel.lastNameState.testObserver()
        val zipCodeState = inputViewModel.zipCodeState.testObserver()
        val notificationState = inputViewModel.getNotificationStateTest().testObserver()

        Truth.assert_()
            .that(firstNameState.observedValues.last())
            .isEqualTo(FirstNameState.Idle)

        Truth.assert_()
            .that(lastNameState.observedValues.last())
            .isEqualTo(LastNameState.Idle)

        Truth.assert_()
            .that(zipCodeState.observedValues.last())
            .isEqualTo(ZipCodeState.Idle)

        Truth.assert_()
            .that(notificationState.observedValues.last())
            .isEqualTo(NotificationState.NotVisible)
    }


    @Test
    fun saveClicked_personDataValid() {
        val inputViewModel = InputViewModel(personRepository)

        val firstNameState = inputViewModel.firstNameState.testObserver()
        val lastNameState = inputViewModel.lastNameState.testObserver()
        val zipCodeState = inputViewModel.zipCodeState.testObserver()
        val notificationState = inputViewModel.getNotificationStateTest().testObserver()

        inputViewModel.firstNameChanged("Ingeborg")
        inputViewModel.lastNameChanged("Schnabel")
        inputViewModel.zipCodeChanged("28201")

        inputViewModel.saveButtonClicked()

        Truth.assert_()
            .that(firstNameState.observedValues.last())
            .isEqualTo(FirstNameState.ClearInput)

        Truth.assert_()
            .that(lastNameState.observedValues.last())
            .isEqualTo(LastNameState.ClearInput)

        Truth.assert_()
            .that(zipCodeState.observedValues.last())
            .isEqualTo(ZipCodeState.ClearInput)

        Truth.assert_()
            .that(notificationState.observedValues.last())
            .isEqualTo(NotificationState.SavingSuccessful)
    }

    @Test
    fun saveClicked_personDataUnvalid_firstName() {
        val inputViewModel = InputViewModel(personRepository)

        val firstNameState = inputViewModel.firstNameState.testObserver()
        val lastNameState = inputViewModel.lastNameState.testObserver()
        val zipCodeState = inputViewModel.zipCodeState.testObserver()
        val notificationState = inputViewModel.getNotificationStateTest().testObserver()

        inputViewModel.firstNameChanged("")
        inputViewModel.lastNameChanged("Schnabel")
        inputViewModel.zipCodeChanged("28201")

        inputViewModel.saveButtonClicked()

        Truth.assert_()
            .that(firstNameState.observedValues.last())
            .isEqualTo(FirstNameState.Missing)

        Truth.assert_()
            .that(lastNameState.observedValues.last())
            .isEqualTo(LastNameState.Idle)

        Truth.assert_()
            .that(zipCodeState.observedValues.last())
            .isEqualTo(ZipCodeState.Idle)

        Truth.assert_()
            .that(notificationState.observedValues.last())
            .isEqualTo(NotificationState.NotVisible)
    }

    @Test
    fun saveClicked_personDataUnvalid_lastName() {
        val inputViewModel = InputViewModel(personRepository)

        val firstNameState = inputViewModel.firstNameState.testObserver()
        val lastNameState = inputViewModel.lastNameState.testObserver()
        val zipCodeState = inputViewModel.zipCodeState.testObserver()
        val notificationState = inputViewModel.getNotificationStateTest().testObserver()

        inputViewModel.firstNameChanged("Ingeborg")
        inputViewModel.lastNameChanged("")
        inputViewModel.zipCodeChanged("28201")

        inputViewModel.saveButtonClicked()


        Truth.assert_()
            .that(firstNameState.observedValues.last())
            .isEqualTo(FirstNameState.Idle)

        Truth.assert_()
            .that(lastNameState.observedValues.last())
            .isEqualTo(LastNameState.Missing)

        Truth.assert_()
            .that(zipCodeState.observedValues.last())
            .isEqualTo(ZipCodeState.Idle)

        Truth.assert_()
            .that(notificationState.observedValues.last())
            .isEqualTo(NotificationState.NotVisible)
    }

    @Test
    fun saveClicked_personDataUnvalid_zipCode_missing() {
        val inputViewModel = InputViewModel(personRepository)

        val firstNameState = inputViewModel.firstNameState.testObserver()
        val lastNameState = inputViewModel.lastNameState.testObserver()
        val zipCodeState = inputViewModel.zipCodeState.testObserver()
        val notificationState = inputViewModel.getNotificationStateTest().testObserver()

        inputViewModel.firstNameChanged("Ingeborg")
        inputViewModel.lastNameChanged("Schnabel")
        inputViewModel.zipCodeChanged("")

        inputViewModel.saveButtonClicked()

        Truth.assert_()
            .that(firstNameState.observedValues.last())
            .isEqualTo(FirstNameState.Idle)

        Truth.assert_()
            .that(lastNameState.observedValues.last())
            .isEqualTo(LastNameState.Idle)

        Truth.assert_()
            .that(zipCodeState.observedValues.last())
            .isEqualTo(ZipCodeState.Error(ZipCodeErrors.ZIP_CODE_MISSING))

        Truth.assert_()
            .that(notificationState.observedValues.last())
            .isEqualTo(NotificationState.NotVisible)
    }

    @Test
    fun saveClicked_personDataUnvalid_zipCode_tooShort() {
        val inputViewModel = InputViewModel(personRepository)

        val firstNameState = inputViewModel.firstNameState.testObserver()
        val lastNameState = inputViewModel.lastNameState.testObserver()
        val zipCodeState = inputViewModel.zipCodeState.testObserver()
        val notificationState = inputViewModel.getNotificationStateTest().testObserver()

        inputViewModel.firstNameChanged("Ingeborg")
        inputViewModel.lastNameChanged("Schnabel")
        inputViewModel.zipCodeChanged("123")

        inputViewModel.saveButtonClicked()

        Truth.assert_()
            .that(firstNameState.observedValues.last())
            .isEqualTo(FirstNameState.Idle)

        Truth.assert_()
            .that(lastNameState.observedValues.last())
            .isEqualTo(LastNameState.Idle)

        Truth.assert_()
            .that(zipCodeState.observedValues.last())
            .isEqualTo(ZipCodeState.Error(ZipCodeErrors.ZIP_CODE_TOO_SHORT))

        Truth.assert_()
            .that(notificationState.observedValues.last())
            .isEqualTo(NotificationState.NotVisible)
    }

    @Test
    fun saveClicked_personDataUnvalid_zipCode_tooLong() {
        val inputViewModel = InputViewModel(personRepository)

        val firstNameState = inputViewModel.firstNameState.testObserver()
        val lastNameState = inputViewModel.lastNameState.testObserver()
        val zipCodeState = inputViewModel.zipCodeState.testObserver()
        val notificationState = inputViewModel.getNotificationStateTest().testObserver()

        inputViewModel.firstNameChanged("Ingeborg")
        inputViewModel.lastNameChanged("Schnabel")
        inputViewModel.zipCodeChanged("123123123123")

        inputViewModel.saveButtonClicked()

        Truth.assert_()
            .that(firstNameState.observedValues.last())
            .isEqualTo(FirstNameState.Idle)

        Truth.assert_()
            .that(lastNameState.observedValues.last())
            .isEqualTo(LastNameState.Idle)

        Truth.assert_()
            .that(zipCodeState.observedValues.last())
            .isEqualTo(ZipCodeState.Error(ZipCodeErrors.ZIP_CODE_TOO_LONG))

        Truth.assert_()
            .that(notificationState.observedValues.last())
            .isEqualTo(NotificationState.NotVisible)
    }

    @Test
    @Ignore("Goging to be implemented, when the zipCode to GeoPosition API is implemented")
    fun saveClicked_personDataUnvalid_zipCode_notExisting() {
        val inputViewModel = InputViewModel(personRepository)

        val firstNameState = inputViewModel.firstNameState.testObserver()
        val lastNameState = inputViewModel.lastNameState.testObserver()
        val zipCodeState = inputViewModel.zipCodeState.testObserver()
        val notificationState = inputViewModel.getNotificationStateTest().testObserver()

        inputViewModel.firstNameChanged("Ingeborg")
        inputViewModel.lastNameChanged("Schnabel")
        inputViewModel.zipCodeChanged("28999")

        inputViewModel.saveButtonClicked()

        Truth.assert_()
            .that(firstNameState.observedValues.last())
            .isEqualTo(FirstNameState.Idle)

        Truth.assert_()
            .that(lastNameState.observedValues.last())
            .isEqualTo(LastNameState.Idle)

        Truth.assert_()
            .that(zipCodeState.observedValues.last())
            .isEqualTo(ZipCodeState.Error(ZipCodeErrors.ZIP_DOES_NOT_EXIST))

        Truth.assert_()
            .that(notificationState.observedValues.last())
            .isEqualTo(NotificationState.NotVisible)
    }

    @Test
    fun saveClicked_personDataUnvalid_firstName_lastName() {
        val inputViewModel = InputViewModel(personRepository)

        val firstNameState = inputViewModel.firstNameState.testObserver()
        val lastNameState = inputViewModel.lastNameState.testObserver()
        val zipCodeState = inputViewModel.zipCodeState.testObserver()
        val notificationState = inputViewModel.getNotificationStateTest().testObserver()

        inputViewModel.firstNameChanged("")
        inputViewModel.lastNameChanged("")
        inputViewModel.zipCodeChanged("28999")

        inputViewModel.saveButtonClicked()

        Truth.assert_()
            .that(firstNameState.observedValues.last())
            .isEqualTo(FirstNameState.Missing)

        Truth.assert_()
            .that(lastNameState.observedValues.last())
            .isEqualTo(LastNameState.Missing)

        Truth.assert_()
            .that(zipCodeState.observedValues.last())
            .isEqualTo(ZipCodeState.Idle)

        Truth.assert_()
            .that(notificationState.observedValues.last())
            .isEqualTo(NotificationState.NotVisible)
    }


    @Test
    @Ignore("Goging to be implemented, when the zipCode to GeoPosition API is implemented")
    fun saveClicked_personDataUnvalid_firstName_zipCodeNotExisting() {
        val inputViewModel = InputViewModel(personRepository)

        val firstNameState = inputViewModel.firstNameState.testObserver()
        val lastNameState = inputViewModel.lastNameState.testObserver()
        val zipCodeState = inputViewModel.zipCodeState.testObserver()
        val notificationState = inputViewModel.getNotificationStateTest().testObserver()

        inputViewModel.firstNameChanged("")
        inputViewModel.lastNameChanged("Schnabel")
        inputViewModel.zipCodeChanged("28999")

        inputViewModel.saveButtonClicked()

        Truth.assert_()
            .that(firstNameState.observedValues.last())
            .isEqualTo(FirstNameState.Missing)

        Truth.assert_()
            .that(lastNameState.observedValues.last())
            .isEqualTo(LastNameState.Idle)

        Truth.assert_()
            .that(zipCodeState.observedValues.last())
            .isEqualTo(ZipCodeState.Error(ZipCodeErrors.ZIP_DOES_NOT_EXIST))

        Truth.assert_()
            .that(notificationState.observedValues.last())
            .isEqualTo(NotificationState.NotVisible)
    }

    @Test
    fun saveClicked_personDataUnvalid_lastName_zipCodeTooShort() {
        val inputViewModel = InputViewModel(personRepository)

        val firstNameState = inputViewModel.firstNameState.testObserver()
        val lastNameState = inputViewModel.lastNameState.testObserver()
        val zipCodeState = inputViewModel.zipCodeState.testObserver()
        val notificationState = inputViewModel.getNotificationStateTest().testObserver()

        inputViewModel.firstNameChanged("Dieter")
        inputViewModel.lastNameChanged("")
        inputViewModel.zipCodeChanged("289")

        inputViewModel.saveButtonClicked()

        Truth.assert_()
            .that(firstNameState.observedValues.last())
            .isEqualTo(FirstNameState.Idle)

        Truth.assert_()
            .that(lastNameState.observedValues.last())
            .isEqualTo(LastNameState.Missing)

        Truth.assert_()
            .that(zipCodeState.observedValues.last())
            .isEqualTo(ZipCodeState.Error(ZipCodeErrors.ZIP_CODE_TOO_SHORT))

        Truth.assert_()
            .that(notificationState.observedValues.last())
            .isEqualTo(NotificationState.NotVisible)
    }

    @Test
    fun saveClicked_personDataUnvalid_firstName_lastName_zipCodeTooLong() {
        val inputViewModel = InputViewModel(personRepository)

        val firstNameState = inputViewModel.firstNameState.testObserver()
        val lastNameState = inputViewModel.lastNameState.testObserver()
        val zipCodeState = inputViewModel.zipCodeState.testObserver()
        val notificationState = inputViewModel.getNotificationStateTest().testObserver()

        inputViewModel.firstNameChanged("")
        inputViewModel.lastNameChanged("")
        inputViewModel.zipCodeChanged("28999999")

        inputViewModel.saveButtonClicked()

        Truth.assert_()
            .that(firstNameState.observedValues.last())
            .isEqualTo(FirstNameState.Missing)

        Truth.assert_()
            .that(lastNameState.observedValues.last())
            .isEqualTo(LastNameState.Missing)

        Truth.assert_()
            .that(zipCodeState.observedValues.last())
            .isEqualTo(ZipCodeState.Error(ZipCodeErrors.ZIP_CODE_TOO_LONG))

        Truth.assert_()
            .that(notificationState.observedValues.last())
            .isEqualTo(NotificationState.NotVisible)
    }

    @Test
    fun saveClicked_firstName_clearError() {
        val inputViewModel = InputViewModel(personRepository)

        val firstNameState = inputViewModel.firstNameState.testObserver()
        val lastNameState = inputViewModel.lastNameState.testObserver()
        val zipCodeState = inputViewModel.zipCodeState.testObserver()
        val notificationState = inputViewModel.getNotificationStateTest().testObserver()

        inputViewModel.firstNameChanged("")
        inputViewModel.lastNameChanged("Wowi")
        inputViewModel.zipCodeChanged("28213")

        inputViewModel.saveButtonClicked()

        Truth.assert_()
            .that(firstNameState.observedValues.last())
            .isEqualTo(FirstNameState.Missing)

        Truth.assert_()
            .that(lastNameState.observedValues.last())
            .isEqualTo(LastNameState.Idle)

        Truth.assert_()
            .that(zipCodeState.observedValues.last())
            .isEqualTo(ZipCodeState.Idle)

        Truth.assert_()
            .that(notificationState.observedValues.last())
            .isEqualTo(NotificationState.NotVisible)

        inputViewModel.firstNameChanged("asd")

        Truth.assert_()
            .that(firstNameState.observedValues.last())
            .isEqualTo(FirstNameState.Idle)

        Truth.assert_()
            .that(lastNameState.observedValues.last())
            .isEqualTo(LastNameState.Idle)

        Truth.assert_()
            .that(zipCodeState.observedValues.last())
            .isEqualTo(ZipCodeState.Idle)

        Truth.assert_()
            .that(notificationState.observedValues.last())
            .isEqualTo(NotificationState.NotVisible)
    }

    @Test
    fun saveClicked_lastName_clearError() {
        val inputViewModel = InputViewModel(personRepository)

        val firstNameState = inputViewModel.firstNameState.testObserver()
        val lastNameState = inputViewModel.lastNameState.testObserver()
        val zipCodeState = inputViewModel.zipCodeState.testObserver()
        val notificationState = inputViewModel.getNotificationStateTest().testObserver()

        inputViewModel.firstNameChanged("Wolf")
        inputViewModel.lastNameChanged("")
        inputViewModel.zipCodeChanged("28213")

        inputViewModel.saveButtonClicked()

        Truth.assert_()
            .that(firstNameState.observedValues.last())
            .isEqualTo(FirstNameState.Idle)

        Truth.assert_()
            .that(lastNameState.observedValues.last())
            .isEqualTo(LastNameState.Missing)

        Truth.assert_()
            .that(zipCodeState.observedValues.last())
            .isEqualTo(ZipCodeState.Idle)

        Truth.assert_()
            .that(notificationState.observedValues.last())
            .isEqualTo(NotificationState.NotVisible)

        inputViewModel.lastNameChanged("asdd")

        Truth.assert_()
            .that(firstNameState.observedValues.last())
            .isEqualTo(FirstNameState.Idle)

        Truth.assert_()
            .that(lastNameState.observedValues.last())
            .isEqualTo(LastNameState.Idle)

        Truth.assert_()
            .that(zipCodeState.observedValues.last())
            .isEqualTo(ZipCodeState.Idle)

        Truth.assert_()
            .that(notificationState.observedValues.last())
            .isEqualTo(NotificationState.NotVisible)
    }

    @Test
    fun saveClicked_zipCode_clearError() {
        val inputViewModel = InputViewModel(personRepository)

        val firstNameState = inputViewModel.firstNameState.testObserver()
        val lastNameState = inputViewModel.lastNameState.testObserver()
        val zipCodeState = inputViewModel.zipCodeState.testObserver()
        val notificationState = inputViewModel.getNotificationStateTest().testObserver()

        inputViewModel.firstNameChanged("Wolf")
        inputViewModel.lastNameChanged("Wolfensen")
        inputViewModel.zipCodeChanged("")

        inputViewModel.saveButtonClicked()

        Truth.assert_()
            .that(firstNameState.observedValues.last())
            .isEqualTo(FirstNameState.Idle)

        Truth.assert_()
            .that(lastNameState.observedValues.last())
            .isEqualTo(LastNameState.Idle)

        Truth.assert_()
            .that(zipCodeState.observedValues.last())
            .isEqualTo(ZipCodeState.Error(ZipCodeErrors.ZIP_CODE_MISSING))

        Truth.assert_()
            .that(notificationState.observedValues.last())
            .isEqualTo(NotificationState.NotVisible)

        inputViewModel.zipCodeChanged("213")

        Truth.assert_()
            .that(firstNameState.observedValues.last())
            .isEqualTo(FirstNameState.Idle)

        Truth.assert_()
            .that(lastNameState.observedValues.last())
            .isEqualTo(LastNameState.Idle)

        Truth.assert_()
            .that(zipCodeState.observedValues.last())
            .isEqualTo(ZipCodeState.Idle)

        Truth.assert_()
            .that(notificationState.observedValues.last())
            .isEqualTo(NotificationState.NotVisible)
    }
}