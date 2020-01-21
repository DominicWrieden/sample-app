package com.dominicwrieden.sampleapp.webservice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dominicwrieden.sampleapp.data.repository.PostRepository

class WebServiceViewModel(private val postRepository: PostRepository) : ViewModel() {

    val postListeState: LiveData<PostListState> = MutableLiveData()
    val loadingState: LiveData<LoadingState> = MutableLiveData()
    val updateErrorState: LiveData<UpdateErrorState> = MutableLiveData()

    fun refreshTriggered() {

    }
}