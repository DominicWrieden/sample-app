package com.dominicwrieden.sampleapp.webservice

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dominicwrieden.sampleapp.data.repository.PostRepository
import com.dominicwrieden.sampleapp.util.SingleLiveEvent
import com.dominicwrieden.sampleapp.util.toLiveData
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.ReplayRelay
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import okhttp3.Response
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

class WebServiceViewModel(private val postRepository: PostRepository) : ViewModel() {


    private val disposable = CompositeDisposable()

    private val loadingStateRelay = BehaviorRelay.createDefault<LoadingState>(LoadingState.Loading)
    private val updateErrorStateRelay = BehaviorRelay.create<UpdateErrorState>()

    private val loadingReplayRelay = ReplayRelay.create<LoadingState>()


    val postListeState: LiveData<PostListState> = postRepository.getPosts()
        .map { postList ->
            if (postList.isNullOrEmpty()) {
                PostListState.EmptyList
            } else {
                PostListState.PostList(postList)
            }
        }.toLiveData()
    val loadingState = SingleLiveEvent<LoadingState>()
    val updateErrorState = SingleLiveEvent<UpdateErrorState>()


    init {
        updateErrorStateRelay.subscribe {
            updateErrorState.postValue(it)
        }.addTo(disposable)

        loadingStateRelay.distinctUntilChanged().doOnNext {
            loadingState.postValue(it)
        }.subscribe(loadingReplayRelay).addTo(disposable)

        loadPosts()
    }

    fun refreshTriggered() {
        loadPosts()
    }

    private fun loadPosts() {
        postRepository.refreshPosts()
            .doOnSubscribe {
                loadingStateRelay.accept(LoadingState.Loading)
            }
            .subscribe(
                { response: Response ->
                    loadingStateRelay.accept(LoadingState.NotLoading)
                },
                { error: Throwable ->
                    loadingStateRelay.accept(LoadingState.NotLoading)
                    if (error is UnknownHostException) {
                        updateErrorStateRelay.accept(UpdateErrorState.NoInternetUpdateError)
                    } else if (error is SSLHandshakeException) {
                        updateErrorStateRelay.accept(UpdateErrorState.SSLv3UpdateError)
                    } else {
                        updateErrorStateRelay.accept(UpdateErrorState.OtherUpdateError)
                    }
                }).addTo(disposable)
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }

    /**
     *
     * For test purposes. Because SingleLiveEvent isn't properly observable
     */
    fun getLoadingStateTest() = loadingReplayRelay.toLiveData()

    /**
     *
     * For test purposes. Because SingleLiveEvent isn't properly observable
     */
    fun getUpdateErrorStateTest() = updateErrorStateRelay.toLiveData()
}