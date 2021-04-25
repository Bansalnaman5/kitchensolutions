package com.naman.kitchensollutions.fragments

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room

import com.naman.kitchensollutions.R
import com.naman.kitchensollutions.adapter.Restaurantsadapter
import com.naman.kitchensollutions.databases.Resdatabase
import com.naman.kitchensollutions.databases.Restaurantentity
import com.naman.kitchensollutions.model.Restaurants
import com.naman.kitchensollutions.util.Draw
import kotlinx.android.synthetic.main.fragment_favourites.view.*


class Favourites : Fragment() {
    lateinit var favrl:RelativeLayout
    lateinit var recrestaurants:RecyclerView
    lateinit var resadapter:Restaurantsadapter
    lateinit var nofavrl:RelativeLayout
    lateinit var rlfavlloading:RelativeLayout
    lateinit var favprogressbar:ProgressBar
    var reslist= arrayListOf<Restaurants>()

       override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
           val view = inflater.inflate(R.layout.fragment_favourites, container, false)
           (activity as Draw).draweren(true)
           favrl = view.findViewById(R.id.favrl)
           nofavrl = view.findViewById(R.id.nofavrl)
           rlfavlloading = view.findViewById(R.id.rlfavlloading)
           rlfavlloading.visibility = View.VISIBLE
           recset(view)
           return view
       }

    fun recset(view: View) {
        recrestaurants = view.findViewById(R.id.recrestaurants)
        val backgroundList = Favasync(activity as Context).execute().get()
        if (backgroundList.isEmpty()) {
            rlfavlloading.visibility = View.GONE
            favrl.visibility = View.GONE
            nofavrl.visibility = View.VISIBLE
        } else {
            favrl.visibility = View.VISIBLE
            rlfavlloading.visibility = View.GONE
            nofavrl.visibility = View.GONE
            for (i in backgroundList) {
                reslist.add(Restaurants(i.id, i.name, i.rating, i.Costfortwo.toInt(), i.imgurl))
            }

            resadapter = Restaurantsadapter(reslist, activity as Context)
            val layoutmanager = LinearLayoutManager(activity)
            recrestaurants.layoutManager = layoutmanager
            recrestaurants.itemAnimator = DefaultItemAnimator()
            recrestaurants.adapter = resadapter
            recrestaurants.setHasFixedSize(true)
        }

    }
    class Favasync(context: Context) : AsyncTask<Void, Void, List<Restaurantentity>>() {

        val db = Room.databaseBuilder(context, Resdatabase::class.java, "res-db").build()

        override fun doInBackground(vararg params: Void?): List<Restaurantentity> {

            return db.restaurantdao().getres()
        }

    }



    }



