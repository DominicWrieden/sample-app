package com.dominicwrieden.sampleapp.webservice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.dominicwrieden.sampleapp.Dependencies
import com.dominicwrieden.sampleapp.R
import com.dominicwrieden.sampleapp.data.entity.Post
import com.dominicwrieden.sampleapp.data.repository.PostRepositoryImpl
import com.dominicwrieden.sampleapp.util.observeWith
import com.google.android.material.snackbar.Snackbar
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import kotlinx.android.synthetic.main.fragment_web_service.*

class WebServiceFragment : Fragment() {

    private val postRepository: PostRepositoryImpl by Dependencies

    private val postListAdapter = GroupAdapter<GroupieViewHolder>()
    private val postListSection = Section()


    private val viewModel: WebServiceViewModel by lazy {
        ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return WebServiceViewModel(postRepository) as T
            }
        }).get(WebServiceViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_web_service, container, false)
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postList.adapter = postListAdapter
        postListAdapter.add(postListSection)

        viewModel.postListeState.observeWith(this) { webServiceStates ->
            when (webServiceStates) {
                is PostListState.PostList -> showPostList(webServiceStates.posts)
                PostListState.EmptyList -> showEmptyList()
            }
        }

        viewModel.updateErrorState.observeWith(this) { showUpdateError(it) }

        viewModel.loadingState.observeWith(this) { loadingState ->
            when (loadingState) {
                LoadingState.Loading -> showLoading()
                LoadingState.NotLoading -> hideLoading()
            }
        }

        refresh.setOnRefreshListener {
            viewModel.refreshTriggered()
        }
    }

    private fun showEmptyList() {
        postListSection.clear()
        emptyListText.visibility = View.VISIBLE
    }

    private fun showPostList(posts: List<Post>) {
        emptyListText.visibility = View.GONE
        postListSection.update(posts.map { PostItem(it) })
    }

    private fun showLoading() {
        refresh.isRefreshing = true
    }

    private fun hideLoading() {
        refresh.isRefreshing = false
    }

    private fun showUpdateError(updateErrorState: UpdateErrorState) {
        when (updateErrorState) {
            UpdateErrorState.NoInternetUpdateError ->
                showSnackbar(getString(R.string.web_service_loading_error_snackbar_no_internet))
            UpdateErrorState.OtherUpdateError ->
                showSnackbar(getString(R.string.web_service_loading_error_snackbar_other))
        }
    }

    private fun showSnackbar(message: String) {
        activity?.let {
            Snackbar.make(it.findViewById(R.id.snackiContainer), message, Snackbar.LENGTH_SHORT)
                .show()
        }
    }


}