package com.naman.kitchensollutions.databases

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "restaurants")
data class Restaurantentity (
    @PrimaryKey val id:Int,
    @ColumnInfo(name="name") val name:String,
    @ColumnInfo(name="rating") val rating:String,
    @ColumnInfo(name="cost_for_two") val Costfortwo:String,
    @ColumnInfo(name="image_url") val imgurl:String

)
