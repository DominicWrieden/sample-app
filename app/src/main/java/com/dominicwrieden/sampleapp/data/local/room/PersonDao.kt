package com.dominicwrieden.sampleapp.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dominicwrieden.sampleapp.data.entity.Person
import io.reactivex.Observable

@Dao
interface PersonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPerson(person: Person)

    @Query("SELECT * FROM Person")
    fun getAllPersons(): Observable<List<Person>>
}