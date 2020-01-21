package com.dominicwrieden.sampleapp.webservice

import com.dominicwrieden.sampleapp.data.entity.Post

sealed class LoadingState {
    object Loading : LoadingState()
    object NotLoading : LoadingState()
}

sealed class UpdateErrorState {
    object NoInternetUpdateError : UpdateErrorState()
    object SSLv3UpdateError : UpdateErrorState()
    object OtherUpdateError : UpdateErrorState()
}

sealed class PostListState {
    data class PostList(val posts: List<Post>) : PostListState()
    object EmptyList : PostListState()
}