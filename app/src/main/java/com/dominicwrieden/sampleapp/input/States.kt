package com.dominicwrieden.sampleapp.input

enum class ZipCodeErrors {
    ZIP_CODE_MISSING,
    ZIP_CODE_TOO_SHORT,
    ZIP_CODE_TOO_LONG,
    ZIP_DOES_NOT_EXIST // but matches length
}

sealed class FirstNameState {
    object Idle : FirstNameState()
    object ClearInput : FirstNameState()
    object Missing : FirstNameState()
}

sealed class LastNameState {
    object Idle : LastNameState()
    object ClearInput : LastNameState()
    object Missing : LastNameState()
}

sealed class ZipCodeState {
    object Idle : ZipCodeState()
    object ClearInput : ZipCodeState()
    data class Error(val zipCodeErrors: ZipCodeErrors) : ZipCodeState()
}

sealed class NotificationState {
    object NotVisible : NotificationState()
    object SavingSuccessful : NotificationState()
    object SavingFailed : NotificationState()
}