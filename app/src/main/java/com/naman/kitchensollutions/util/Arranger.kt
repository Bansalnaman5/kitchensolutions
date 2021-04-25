package com.naman.kitchensollutions.util

import com.naman.kitchensollutions.model.Restaurants

class Arranger {
    companion object {
        var compaircost = Comparator<Restaurants> { res1, res2 ->
            val fcost = res1.costfortwo as Int
            val scost = res2.costfortwo as Int
            if (fcost.compareTo(scost) == 0) {
                comprate.compare(res1, res2)
            } else {
                fcost.compareTo(scost)
            }
        }

        var comprate = Comparator<Restaurants> { res1, res2 ->
            val frat = res1.rating as String
            val srat = res2.rating as String
            if (frat.compareTo(srat) == 0) {
                val costOne = res1.costfortwo as Int
                val costTwo = res2.costfortwo as Int
                costOne.compareTo(costTwo)
            } else {
                frat.compareTo(srat)
            }
        }
    }
}