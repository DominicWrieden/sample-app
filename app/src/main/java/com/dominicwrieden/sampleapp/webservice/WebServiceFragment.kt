package com.dominicwrieden.sampleapp.webservice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.dominicwrieden.sampleapp.R
import com.dominicwrieden.sampleapp.data.entity.Post
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import kotlinx.android.synthetic.main.fragment_web_service.*

class WebServiceFragment : Fragment() {

    private lateinit var webServiceViewModel: WebServiceViewModel


    private val postListAdapter = GroupAdapter<GroupieViewHolder>()
    private val postListSection = Section()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        webServiceViewModel =
            ViewModelProviders.of(this).get(WebServiceViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_web_service, container, false)
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postList.adapter = postListAdapter
        postListAdapter.add(postListSection)


        val testData = listOf(
            Post(
                1,
                1,
                "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
                "quia et suscipit\nnsuscipit recusandae consequuntur expedita et cum\nnreprehenderit molestiae ut ut quas totam\nnnostrum rerum est autem sunt rem eveniet architecto"
            ),
            Post(
                2,
                1,
                "qui est esse",
                "est rerum tempore vitae\nnsequi sint nihil reprehenderit dolor beatae ea dolores neque\nnfugiat blanditiis voluptate porro vel nihil molestiae ut reiciendis\nnqui aperiam non debitis possimus qui neque nisi nulla"
            ),
            Post(
                3,
                1,
                "ea molestias quasi exercitationem repellat qui ipsa sit aut",
                "et iusto sed quo iure\nnvoluptatem occaecati omnis eligendi aut ad\nnvoluptatem doloribus vel accusantium quis pariatur\nnmolestiae porro eius odio et labore et velit aut"
            )
        )

        updatePosts(testData)

    }

    /**
     * Show idle screen
     */
    private fun showIdle() {

    }


    /**
     * Update the list of posts
     */
    private fun updatePosts(posts: List<Post>) {
        postListSection.update(posts.map { PostItem(it) })
    }

    /**
     * Show error, if update failed
     */
    private fun showUpdateError() {

    }
}