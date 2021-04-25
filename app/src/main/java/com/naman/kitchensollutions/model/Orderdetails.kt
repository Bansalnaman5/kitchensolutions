package com.naman.kitchensollutions.model

import org.json.JSONArray

data class Orderdetails (
    val orderId: Int,
    val resName: String,
    val orderDate: String,
    val foodItem: JSONArray
)