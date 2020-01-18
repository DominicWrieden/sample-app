package com.dominicwrieden.sampleapp.input

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.dominicwrieden.sampleapp.R
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.widget.textChanges
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_input.*

class InputFragment : Fragment() {

    private lateinit var inputViewModel: InputViewModel
    private val disposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inputViewModel =
            ViewModelProviders.of(this).get(InputViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_input, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpTextInputs()


        // TODO: Show state from VM
        // TODO: If time, implement DataBinding
    }

    /**
     * Setting up text input fields
     */
    private fun setUpTextInputs() {
        firstName.editText?.textChanges()?.subscribe { firstName.error = null }?.addTo(disposable)
        lastName.editText?.textChanges()?.subscribe { lastName.error = null }?.addTo(disposable)
        zipCode.editText?.textChanges()?.subscribe { zipCode.error = null }?.addTo(disposable)
    }

    /**
     * Show idle state
     */
    private fun showIdle() {
        firstName.error = null
        firstName.editText?.text = null
        lastName.error = null
        lastName.editText?.text = null
        zipCode.error = null
        zipCode.editText?.text = null
    }

    /**
     * Show state, when saving the inputs failed
     */
    private fun showSaveError(missingInputs: List<MissingInput>) {
        missingInputs.iterator().forEach {
            when (it) {
                MissingInput.MISSING_FIRST_NAME ->
                    firstName.error = getString(R.string.input_text_error_first_name)
                MissingInput.MISSING_LAST_NAME ->
                    lastName.error = getString(R.string.input_text_error_last_name)
                MissingInput.MISSING_ZIP_CODE
                -> zipCode.error = getString(R.string.input_text_error_zip_code)
            }
        }
    }

    /**
     * Show state, when saving the inputs was successful
     */
    private fun showSaveSuccessful() {
        Snackbar.make(
            fragment_input, R.string.input_snackbar_save_successful,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun onDetach() {
        super.onDetach()
        disposable.dispose()
    }
}