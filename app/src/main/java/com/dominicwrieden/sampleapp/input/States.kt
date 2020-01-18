package com.dominicwrieden.sampleapp.input

enum class MissingInput {
    MISSING_FIRST_NAME,
    MISSING_LAST_NAME,
    MISSING_ZIP_CODE
}


sealed class InputStates {
    object Idle : InputStates()
    object SaveSuccessful : InputStates()
    data class SaveError(val missingInputs: List<MissingInput>) : InputStates()
}