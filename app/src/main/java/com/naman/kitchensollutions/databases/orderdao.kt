package com.naman.kitchensollutions.databases

import android.os.FileObserver.DELETE
import androidx.annotation.IdRes
import androidx.room.*



@Dao
interface orderdao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertorder(orderEntity:OrderEntity)

    @Delete
    fun deleteord(orderEntity: OrderEntity)

    @Query("SELECT * FROM orders")
    fun getorders():List<OrderEntity>

    @Query("DELETE FROM orders WHERE Resid=:id")
    fun delorders(id:String)

    @Query("DELETE FROM orders")
    fun delall()
}


