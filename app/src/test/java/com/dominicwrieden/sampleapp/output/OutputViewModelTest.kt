package com.dominicwrieden.sampleapp.output

import com.InstantTaskExecutorRule
import com.RxSchedulerRule
import com.dominicwrieden.sampleapp.data.entity.Person
import com.dominicwrieden.sampleapp.data.repository.PersonRepository
import com.dominicwrieden.testObserver
import com.google.common.truth.Truth
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class OutputViewModelTest {

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

    @Test
    fun empty_list() {
        whenever(personRepository.getPersonList()).doReturn(Observable.just(emptyList()))

        val outputViewModel = OutputViewModel(personRepository)

        val personListState = outputViewModel.personListState.testObserver()

        Truth.assert_()
            .that(personListState.observedValues.last())
            .isEqualTo(PersonListState.Empty)
    }

    @Test
    fun list_1_element() {
        val person = Person(1, "Kim", "Janine", 27711)
        whenever(personRepository.getPersonList()).doReturn(Observable.just(listOf(person)))

        val outputViewModel = OutputViewModel(personRepository)

        val personListState = outputViewModel.personListState.testObserver()

        Truth.assert_()
            .that(personListState.observedValues.last())
            .isEqualTo(PersonListState.PersonList(listOf(person)))
    }

    @Test
    fun list_3_elements() {
        val person1 = Person(1, "Kim", "Janine", 27711)
        val person2 = Person(2, "Pieter", "Parker", 28203)
        val person3 = Person(3, "Niklas", "Ummo", 28193)
        whenever(personRepository.getPersonList()).doReturn(
            Observable.just(listOf(person1, person2, person3))
        )

        val outputViewModel = OutputViewModel(personRepository)

        val personListState = outputViewModel.personListState.testObserver()

        Truth.assert_()
            .that(personListState.observedValues.last())
            .isEqualTo(PersonListState.PersonList(listOf(person1, person2, person3)))
    }

    @Test
    @Ignore("Fix test stubbing for changing live data ") //TODO
    fun list_2_to_3_elements() {
        val person1 = Person(1, "Kim", "Janine", 27711)
        val person2 = Person(2, "Pieter", "Parker", 28203)
        val person3 = Person(3, "Niklas", "Ummo", 28193)
        whenever(personRepository.getPersonList()).doReturn(
            Observable.just(listOf(person1, person2))
        )

        val outputViewModel = OutputViewModel(personRepository)

        val personListState = outputViewModel.personListState.testObserver()

        Truth.assert_()
            .that(personListState.observedValues.last())
            .isEqualTo(PersonListState.PersonList(listOf(person1, person2)))

        whenever(personRepository.getPersonList()).doReturn(
            Observable.just(listOf(person1, person2, person3))
        )

        Truth.assert_()
            .that(personListState.observedValues.last())
            .isEqualTo(PersonListState.PersonList(listOf(person1, person2, person3)))
    }


    @Test
    @Ignore("Fix test stubbing for changing live data ") //TODO
    fun list_3_to_2_elements() {
        val person1 = Person(1, "Kim", "Janine", 27711)
        val person2 = Person(2, "Pieter", "Parker", 28203)
        val person3 = Person(3, "Niklas", "Ummo", 28193)
        whenever(personRepository.getPersonList()).doReturn(
            Observable.just(listOf(person1, person2, person3)),
            Observable.just(listOf(person1, person2))
        )

        val outputViewModel = OutputViewModel(personRepository)

        val personListState = outputViewModel.personListState.testObserver()

        Truth.assert_()
            .that(personListState.observedValues.last())
            .isEqualTo(PersonListState.PersonList(listOf(person1, person2, person3)))

        whenever(personRepository.getPersonList()).doReturn(
            Observable.just(listOf(person1, person2))
        )

        Truth.assert_()
            .that(personListState.observedValues.last())
            .isEqualTo(PersonListState.PersonList(listOf(person1, person2)))
    }


    @Test
    fun list_1_element_missing_firstName() {
        val person1 = Person(1, "", "Janine", 27711)
        whenever(personRepository.getPersonList()).doReturn(
            Observable.just(listOf(person1))
        )

        val outputViewModel = OutputViewModel(personRepository)

        val personListState = outputViewModel.personListState.testObserver()

        Truth.assert_()
            .that(personListState.observedValues.last())
            .isEqualTo(PersonListState.PersonList(listOf(person1)))
    }

    @Test
    fun list_1_element_missing_lastName() {
        val person1 = Person(1, "Kim", "", 27711)
        whenever(personRepository.getPersonList()).doReturn(
            Observable.just(listOf(person1))
        )

        val outputViewModel = OutputViewModel(personRepository)

        val personListState = outputViewModel.personListState.testObserver()

        Truth.assert_()
            .that(personListState.observedValues.last())
            .isEqualTo(PersonListState.PersonList(listOf(person1)))
    }

    @Test
    fun list_1_element_incorrect_zipCode() {
        val person1 = Person(1, "Kim", "", -1)
        whenever(personRepository.getPersonList()).doReturn(
            Observable.just(listOf(person1))
        )

        val outputViewModel = OutputViewModel(personRepository)

        val personListState = outputViewModel.personListState.testObserver()

        Truth.assert_()
            .that(personListState.observedValues.last())
            .isEqualTo(PersonListState.PersonList(listOf(person1)))
    }

    @Test
    fun list_1_element_missing_firstName_lastName() {
        val person1 = Person(1, "", "", 27711)
        whenever(personRepository.getPersonList()).doReturn(
            Observable.just(listOf(person1))
        )

        val outputViewModel = OutputViewModel(personRepository)

        val personListState = outputViewModel.personListState.testObserver()

        Truth.assert_()
            .that(personListState.observedValues.last())
            .isEqualTo(PersonListState.PersonList(listOf(person1)))
    }

    @Test
    fun list_1_element_missing_firstName_incorrect_ZipCode() {
        val person1 = Person(1, "", "Burns", 0)
        whenever(personRepository.getPersonList()).doReturn(
            Observable.just(listOf(person1))
        )

        val outputViewModel = OutputViewModel(personRepository)

        val personListState = outputViewModel.personListState.testObserver()

        Truth.assert_()
            .that(personListState.observedValues.last())
            .isEqualTo(PersonListState.PersonList(listOf(person1)))
    }

    @Test
    fun list_1_element_missing_lastName_incorrect_ZipCode() {
        val person1 = Person(1, "Smithers", "", 123)
        whenever(personRepository.getPersonList()).doReturn(
            Observable.just(listOf(person1))
        )

        val outputViewModel = OutputViewModel(personRepository)

        val personListState = outputViewModel.personListState.testObserver()

        Truth.assert_()
            .that(personListState.observedValues.last())
            .isEqualTo(PersonListState.PersonList(listOf(person1)))
    }

    @Test
    fun list_1_element_missing_firstName_lastName_incorrect_ZipCode() {
        val person1 = Person(1, "", "", 1231231231)
        whenever(personRepository.getPersonList()).doReturn(
            Observable.just(listOf(person1))
        )

        val outputViewModel = OutputViewModel(personRepository)

        val personListState = outputViewModel.personListState.testObserver()

        Truth.assert_()
            .that(personListState.observedValues.last())
            .isEqualTo(PersonListState.PersonList(listOf(person1)))
    }
}