package com.dominicwrieden.sampleapp.webservice

import com.dominicwrieden.sampleapp.R
import com.dominicwrieden.sampleapp.data.entity.Post
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_web_service_post.*

class PostItem(val post: Post) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.title.text = post.title
        viewHolder.body.text = post.body
    }

    override fun getLayout() = R.layout.item_web_service_post


    override fun isSameAs(other: com.xwray.groupie.Item<*>) = other is PostItem &&
            post.id == other.post.id

    override fun hasSameContentAs(other: com.xwray.groupie.Item<*>) = other is PostItem
            && post.title == other.post.title
            && post.body == other.post.body
}