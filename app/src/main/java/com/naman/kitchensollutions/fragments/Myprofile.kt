package com.naman.kitchensollutions.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity

import com.naman.kitchensollutions.R
import com.naman.kitchensollutions.util.Draw

class Myprofile : Fragment() {
    lateinit var profll:LinearLayout
    lateinit var usernametxt:TextView
    lateinit var mobilenumbertxt:TextView
    lateinit var emailtxt:TextView
    lateinit var addresstxt:TextView
    lateinit var sharedprefrences:SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_myprofile, container, false)
        (activity as Draw).draweren(true)
        sharedprefrences = (activity as FragmentActivity).getSharedPreferences("Myapp", Context.MODE_PRIVATE)
        usernametxt = view.findViewById(R.id.usernametxt)
        mobilenumbertxt = view.findViewById(R.id.mobilenumbertxt)
        emailtxt = view.findViewById(R.id.emailtxt)
        addresstxt = view.findViewById(R.id.addresstxt)
        usernametxt.text = sharedprefrences.getString("user_name", null)
        val pt = sharedprefrences.getString("user_mobile_number", null)
        mobilenumbertxt.text = pt
        emailtxt.text = sharedprefrences.getString("user_email", null)
        val ad = sharedprefrences.getString("user_address", null)
        addresstxt.text = ad
        return view
    }
}
