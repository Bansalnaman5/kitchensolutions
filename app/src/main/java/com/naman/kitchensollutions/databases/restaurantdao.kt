package com.naman.kitchensollutions.databases

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface restaurantdao {

    @Insert
    fun insertres(restaurantentity: Restaurantentity)

    @Delete
    fun delres(restaurantentity: Restaurantentity)

    @Query("SELECT * FROM restaurants")
    fun getres():List<Restaurantentity>

    @Query("SELECT * FROM restaurants WHERE id=:id")
    fun  getresbyid(id:String):Restaurantentity

}