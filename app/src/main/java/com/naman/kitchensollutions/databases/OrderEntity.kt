package com.naman.kitchensollutions.databases

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey val Resid: String,
    @ColumnInfo(name="food_items") val fooditems:String


)
