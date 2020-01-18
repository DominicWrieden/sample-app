package com.dominicwrieden.sampleapp.output

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.dominicwrieden.sampleapp.R
import com.dominicwrieden.sampleapp.data.entity.Person
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import kotlinx.android.synthetic.main.fragment_output.*

class OutputFragment : Fragment() {

    private lateinit var outputViewModel: OutputViewModel

    private val peronListAdapter = GroupAdapter<GroupieViewHolder>()
    private val personListSection = Section()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        outputViewModel =
            ViewModelProviders.of(this).get(OutputViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_output, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        personList.adapter = peronListAdapter
        peronListAdapter.add(personListSection)

        // TODO: Show state from VM
        // TODO: If time, implement DataBinding

        val testData = listOf(
            Person(1, "Lars", "Schnabel", 28195),
            Person(2, "Viktor", "Otten", 28195),
            Person(3, "Jonas", "Corgi", 28195),
            Person(4, "Kim", "Janine", 28195),
            Person(5, "Pieter", "Parker", 28195),
            Person(6, "Barbara", "Unschönberger", 28195),
            Person(7, "Alfi", "Hardcore", 28195)
        )
        showPersons(testData)
    }


    /**
     * Updates the existing list of persons with the given ones
     */
    private fun showPersons(persons: List<Person>) {
        personListSection.update(persons.map { PersonItem(it) })
    }
}