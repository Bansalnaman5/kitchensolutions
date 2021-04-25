package com.naman.kitchensollutions.util

import android.content.Context

class Initiator(context: Context) {
    var pm=0
    val pn="Myapp"
    val key1="isloggedin"
    val p=context.getSharedPreferences(pn,pm)
    val ed=p.edit()
    fun setlog(isloggedin:Boolean){
        ed.putBoolean(key1,isloggedin)
        ed.apply()
    }

    fun cheklog():Boolean{
        return p.getBoolean(key1,false)
    }

}