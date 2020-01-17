package com.dominicwrieden.sampleapp.personinput

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.dominicwrieden.sampleapp.R

class PersonInputFragment : Fragment() {

    private lateinit var personInputViewModel: PersonInputViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        personInputViewModel =
            ViewModelProviders.of(this).get(PersonInputViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_person_input, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        personInputViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}