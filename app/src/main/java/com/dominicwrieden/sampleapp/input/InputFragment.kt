package com.dominicwrieden.sampleapp.input

import android.os.Bundle
import android.text.Editable
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
import com.dominicwrieden.sampleapp.input.ZipCodeErrors.*
import com.dominicwrieden.sampleapp.util.observeWith
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.textChanges
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_input.*


class InputFragment : Fragment() {

    private val personRepository: PersonRepository by Dependencies

    private val disposable = CompositeDisposable()

    private val viewModel: InputViewModel by lazy {
        ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return InputViewModel(personRepository) as T
            }
        }).get(InputViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_input, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.firstNameState.observeWith(this) {
            showFirstNameState(it)
        }
        viewModel.lastNameState.observeWith(this) {
            showLastNameState(it)
        }
        viewModel.zipCodeState.observeWith(this) {
            showZipCodeState(it)
        }
        viewModel.notificationState.observeWith(this) { showNotificationState(it) }


        firstNameEditText
            .textChanges()
            .subscribe {
                viewModel.firstNameChanged(it.toString())
            }
            .addTo(disposable)

        lastNameEditText
            .textChanges()
            .subscribe {
                viewModel.lastNameChanged(it.toString())
            }
            .addTo(disposable)

        zipCodeEditText
            .textChanges()
            .subscribe {
                viewModel.zipCodeChanged(it.toString())
            }
            .addTo(disposable)

        save.clicks().subscribe { viewModel.saveButtonClicked() }.addTo(disposable)
    }

    private fun showFirstNameState(firstNameState: FirstNameState) {
        when (firstNameState) {
            FirstNameState.Idle -> firstNameInputLayout.error = null
            FirstNameState.ClearInput ->
                firstNameEditText.text = Editable.Factory.getInstance().newEditable("")
            FirstNameState.Missing ->
                firstNameInputLayout.error = getString(R.string.input_text_error_first_name)
        }
    }

    private fun showLastNameState(lastNameState: LastNameState) {
        when (lastNameState) {
            LastNameState.Idle -> lastNameInputLayout.error = null
            LastNameState.ClearInput ->
                lastNameEditText.text = Editable.Factory.getInstance().newEditable("")
            LastNameState.Missing ->
                lastNameInputLayout.error = getString(R.string.input_text_error_last_name)
        }
    }

    private fun showZipCodeState(zipCodeState: ZipCodeState) {
        when (zipCodeState) {
            ZipCodeState.Idle -> zipCode.error = null
            ZipCodeState.ClearInput ->
                zipCodeEditText.text = Editable.Factory.getInstance().newEditable("")
            is ZipCodeState.Error -> showZipCodeError(zipCodeState.zipCodeErrors)
        }
    }

    private fun showZipCodeError(zipCodeErrors: ZipCodeErrors) {
        when (zipCodeErrors) {
            ZIP_CODE_MISSING ->
                zipCode.error = getString(R.string.input_text_error_zip_code_missing)
            ZIP_CODE_TOO_SHORT ->
                zipCode.error = getString(R.string.input_text_error_zip_code_too_short)
            ZIP_CODE_TOO_LONG ->
                zipCode.error = getString(R.string.input_text_error_zip_code_too_long)
            ZIP_DOES_NOT_EXIST ->
                zipCode.error = getString(R.string.input_text_error_zip_code_not_existing)
        }
    }

    private fun showNotificationState(notificationState: NotificationState) {
        when (notificationState) {
            NotificationState.SavingSuccessful ->
                showSnackbar(getString(R.string.input_snackbar_save_successful))
            NotificationState.SavingFailed ->
                showSnackbar(getString(R.string.input_snackbar_save_failed))
        }
    }

    private fun showSnackbar(message: String) {
        activity?.let {
            Snackbar.make(it.findViewById(R.id.snackiContainer), message, Snackbar.LENGTH_SHORT)
                .show()
        }
    }


    override fun onDetach() {
        super.onDetach()
        disposable.dispose()
    }
}