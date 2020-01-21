package com.dominicwrieden.sampleapp.webservice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dominicwrieden.sampleapp.data.repository.PostRepository
import com.dominicwrieden.sampleapp.util.toLiveData
import com.jakewharton.rxrelay2.BehaviorRelay

class WebServiceViewModel(private val postRepository: PostRepository) : ViewModel() {


    private val loadingStateRelay = BehaviorRelay.create<LoadingState>()
    private val updateErrorStateRelay = BehaviorRelay.create<UpdateErrorState>()

    val postListeState: LiveData<PostListState> = MutableLiveData()
    val loadingState: LiveData<LoadingState> = MutableLiveData()
    val updateErrorState: LiveData<UpdateErrorState> = MutableLiveData()

    fun refreshTriggered() {

    }

    /**
     *
     * For test purposes. Because SingleLiveEvent isn't properly observable
     */
    fun getLoadingStateTest() = loadingStateRelay.toLiveData()

    /**
     *
     * For test purposes. Because SingleLiveEvent isn't properly observable
     */
    fun getUpdateErrorStateTest() = updateErrorStateRelay.toLiveData()
}