package com.dominicwrieden.sampleapp.output

import com.dominicwrieden.sampleapp.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item

class OutputHeaderItem : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
    }

    override fun getLayout() = R.layout.item_output_header

    override fun isSameAs(other: com.xwray.groupie.Item<*>) = other is OutputHeaderItem

    override fun hasSameContentAs(other: com.xwray.groupie.Item<*>) = true
}