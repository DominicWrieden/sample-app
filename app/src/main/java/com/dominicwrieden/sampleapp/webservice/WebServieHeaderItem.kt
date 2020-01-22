package com.dominicwrieden.sampleapp.webservice

import com.dominicwrieden.sampleapp.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item

class WebServiceHeaderItem : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
    }

    override fun getLayout() = R.layout.item_web_service_header

    override fun isSameAs(other: com.xwray.groupie.Item<*>) = other is WebServiceHeaderItem

    override fun hasSameContentAs(other: com.xwray.groupie.Item<*>) = true
}