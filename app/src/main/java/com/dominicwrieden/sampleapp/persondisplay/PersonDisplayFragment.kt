package com.dominicwrieden.sampleapp.persondisplay

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.dominicwrieden.sampleapp.R

class PersonDisplayFragment : Fragment() {

    private lateinit var personDisplayViewModel: PersonDisplayViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        personDisplayViewModel =
            ViewModelProviders.of(this).get(PersonDisplayViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_person_display, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        personDisplayViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}