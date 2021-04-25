package com.naman.kitchensollutions.databases

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Restaurantentity::class,OrderEntity::class],version = 1)
abstract class Resdatabase:RoomDatabase() {
    abstract fun orderdao():orderdao


    abstract fun restaurantdao():restaurantdao

}