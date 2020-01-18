package com.dominicwrieden.sampleapp.output

import com.dominicwrieden.sampleapp.R
import com.dominicwrieden.sampleapp.data.entity.Person
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_output_person.*

class PersonItem(val person: Person) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.firstName.text = person.firstname
        viewHolder.lastName.text = person.lastName
        viewHolder.zipCode.text = person.zipCode.toString()
    }

    override fun getLayout() = R.layout.item_output_person


    override fun isSameAs(other: com.xwray.groupie.Item<*>) = other is PersonItem &&
            person.id == other.person.id
}