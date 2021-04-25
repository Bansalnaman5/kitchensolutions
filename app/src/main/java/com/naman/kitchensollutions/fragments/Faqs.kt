package com.naman.kitchensollutions.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.naman.kitchensollutions.R
import com.naman.kitchensollutions.util.Draw


class Faqs : Fragment() {

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            // Inflate the layout for this fragment
            val t=inflater.inflate(R.layout.fragment_faqs, container, false)
            (activity as Draw).draweren(true)
            return t
        }

    }