package com.dominicwrieden.sampleapp.webservice

import com.dominicwrieden.sampleapp.data.entity.Post

sealed class WebServiceStates {
    object Idle : WebServiceStates()
    data class UpdateList(val posts: List<Post>) : WebServiceStates()
    object UpdateError : WebServiceStates()
    //TODO Loading State
}