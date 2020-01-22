package com.dominicwrieden.sampleapp.output

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.dominicwrieden.sampleapp.Dependencies
import com.dominicwrieden.sampleapp.R
import com.dominicwrieden.sampleapp.data.repository.PersonRepository
import com.dominicwrieden.sampleapp.util.observeWith
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import kotlinx.android.synthetic.main.fragment_output.*

class OutputFragment : Fragment() {


    private val personRepository: PersonRepository by Dependencies

    private val viewModel: OutputViewModel by lazy {
        ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return OutputViewModel(personRepository) as T
            }
        }).get(OutputViewModel::class.java)
    }

    private val personListAdapter = GroupAdapter<GroupieViewHolder>()
    private val personListSection = Section()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_output, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        personList.adapter = personListAdapter
        personListAdapter.add(personListSection)
        personListSection.setHeader(OutputHeaderItem())
        personListSection.setHideWhenEmpty(true)

        viewModel.personListState.observeWith(this) { personListState ->
            when (personListState) {
                PersonListState.Empty -> showEmptyState()
                is PersonListState.PersonList -> showPersonList(personListState)
            }
        }
    }


    /**
     * Updates the existing list of persons with the given ones
     */
    private fun showPersonList(personListState: PersonListState.PersonList) {
        emptyListText.visibility = View.GONE
        personListSection.update(personListState.personList.map { PersonItem(it) })
    }

    private fun showEmptyState() {
        personListSection.update(emptyList())
        emptyListText.visibility = View.VISIBLE
    }
}