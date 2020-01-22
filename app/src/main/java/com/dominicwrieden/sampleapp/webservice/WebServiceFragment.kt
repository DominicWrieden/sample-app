package com.dominicwrieden.sampleapp.webservice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
    ) = inflater.inflate(R.layout.fragment_web_service, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postList.adapter = postListAdapter
        postListAdapter.add(postListSection)
        postListSection.setHeader(WebServiceHeaderItem())
        postListSection.setHideWhenEmpty(true)

        // Observe live data from the viewmodel
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

        postListAdapter.setOnItemClickListener { item, _ ->
            if (item is PostItem)
                Toast.makeText(
                    activity, "Show details screen not implemented yet :(",
                    Toast.LENGTH_SHORT
                ).show()
        }

        //Observe the pull to refresh action and notify the viewmodel
        refresh.setOnRefreshListener {
            viewModel.refreshTriggered()
        }
    }

    /**
     * Show empty list of posts
     */
    private fun showEmptyList() {
        postListSection.update(emptyList())
        emptyListText.visibility = View.VISIBLE
    }

    /**
     * Show list of posts
     */
    private fun showPostList(posts: List<Post>) {
        emptyListText.visibility = View.GONE
        postListSection.update(posts.map { PostItem(it) })
    }

    /**
     * Show loading state
     */
    private fun showLoading() {
        refresh.isRefreshing = true
    }

    /**
     * Hide loading state
     */
    private fun hideLoading() {
        refresh.isRefreshing = false
    }

    /**
     * Show errors, which can occur while updating the post lists
     */
    private fun showUpdateError(updateErrorState: UpdateErrorState) {
        when (updateErrorState) {
            UpdateErrorState.NoInternetUpdateError ->
                showSnackbar(getString(R.string.web_service_loading_error_snackbar_no_internet))
            UpdateErrorState.OtherUpdateError ->
                showSnackbar(getString(R.string.web_service_loading_error_snackbar_other))
            UpdateErrorState.SSLv3UpdateError ->
                showSnackbar(getString(R.string.web_service_loading_error_snackbar_sslv3))
        }
    }

    /**
     * Helper function to show the SnackBar
     */
    private fun showSnackbar(message: String) {
        activity?.let {
            Snackbar.make(it.findViewById(R.id.snackiContainer), message, Snackbar.LENGTH_LONG)
                .show()
        }
    }
}