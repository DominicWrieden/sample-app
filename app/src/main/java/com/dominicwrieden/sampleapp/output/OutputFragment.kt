package com.dominicwrieden.sampleapp.output

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.dominicwrieden.sampleapp.R

class OutputFragment : Fragment() {

    private lateinit var outputViewModel: OutputViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        outputViewModel =
            ViewModelProviders.of(this).get(OutputViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_output, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        outputViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}