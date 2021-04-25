package com.naman.kitchensollutions.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson

import com.naman.kitchensollutions.R
import com.naman.kitchensollutions.activities.Checkoutcart
import com.naman.kitchensollutions.adapter.Resmenuadapter
import com.naman.kitchensollutions.databases.OrderEntity
import com.naman.kitchensollutions.databases.Resdatabase
import com.naman.kitchensollutions.model.FoodOptions
import com.naman.kitchensollutions.util.ConnectionManager
import com.naman.kitchensollutions.util.Draw


class Restaurantsfrag : Fragment() {
    lateinit var recyclemenu:RecyclerView
    lateinit var rlres:RelativeLayout
    lateinit var resprogbar:ProgressBar
    lateinit var sharedprefreces:SharedPreferences
    lateinit var resmenuadapter: Resmenuadapter
    var listmenu= arrayListOf<FoodOptions>()
    var listoforder= arrayListOf<FoodOptions>()
    companion object{
        lateinit var cartbtnres:Button
        var resId:Int?=0
        var resName:String?=""
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_restaurants, container, false)
        sharedprefreces = activity?.getSharedPreferences("Myapp", Context.MODE_PRIVATE) as SharedPreferences
        rlres = view?.findViewById(R.id.rlres) as RelativeLayout
        rlres.visibility = View.VISIBLE
        resId = arguments?.getInt("id", 0)
        resName = arguments?.getString("name", "")
        (activity as Draw).draweren(false)
        setHasOptionsMenu(true)
        cartbtnres = view.findViewById(R.id.cartbtnres) as Button
        cartbtnres.visibility = View.GONE
        cartbtnres.setOnClickListener {
            cartproceed()
        }
        menures(view)
        return view
    }


    fun menures(view: View) {

        recyclemenu = view.findViewById(R.id.recyclemenu)
        if (ConnectionManager().NetworkAvailabel(activity as Context)) {

            val queue = Volley.newRequestQueue(activity as Context)
            val url="http://13.235.250.119/v2/restaurants/fetch_result/"

            val jsonObjectRequest = object :
                JsonObjectRequest(Method.GET, url + resId, null, Response.Listener {
                    rlres.visibility = View.GONE
                    try {
                        val data = it.getJSONObject("data")
                        val success = data.getBoolean("success")
                        if (success) {
                            val resar = data.getJSONArray("data")
                            for (i in 0 until resar.length()) {
                                val menuObject = resar.getJSONObject(i)
                                val foodoption = FoodOptions(
                                    menuObject.getString("id"),
                                    menuObject.getString("name"),
                                    menuObject.getString("cost_for_one").toInt()
                                )
                                listmenu.add(foodoption)
                                resmenuadapter = Resmenuadapter(activity as Context,listmenu,
                                    object :Resmenuadapter.OnItemClickListener{
                                        override fun Addclickitem(option: FoodOptions) {
                                            listoforder.add(option)
                                            if(listoforder.size>0){
                                                cartbtnres.visibility=View.VISIBLE
                                                Resmenuadapter.isemptycart=false
                                            }
                                        }

                                        override fun Removeclickitem(option: FoodOptions) {
                                            listoforder.remove(option)
                                        if (listoforder.isEmpty()){
                                        cartbtnres.visibility=View.GONE
                                        Resmenuadapter.isemptycart=true
                                        }
                                        }
                                    })
                                val layoutManager = LinearLayoutManager(activity)
                                recyclemenu.layoutManager = layoutManager
                                recyclemenu.itemAnimator = DefaultItemAnimator()
                                recyclemenu.adapter = resmenuadapter
                            }
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }, Response.ErrorListener {
                    Toast.makeText(activity as Context, it.message, Toast.LENGTH_SHORT).show()
                }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "ffc38779fb84f5"
                    return headers
                }
            }
            queue.add(jsonObjectRequest)
        } else {
            Toast.makeText(activity as Context, "No Internet", Toast.LENGTH_SHORT).show()
        }
    }

    fun cartproceed() {
        val gson = Gson()
        val foodOptions = gson.toJson(listoforder)
        val asyn = Cartitems(activity as Context, resId.toString(), foodOptions, 1).execute()
        val result = asyn.get()
        if (result) {
            val data = Bundle()
            data.putInt("resId", resId as Int)
            data.putString("resName", resName)
            val intent = Intent(activity, Checkoutcart::class.java)
            intent.putExtra("data", data)
            startActivity(intent)
        } else {
            Toast.makeText((activity as Context), "HEHE ERROR AGAYA", Toast.LENGTH_SHORT)
                .show()
        }

    }


    class Cartitems(context: Context, val restid: String, val fooditem: String, val mode: Int) : AsyncTask<Void, Void, Boolean>() {
        val db = Room.databaseBuilder(context, Resdatabase::class.java, "res-db").build()


        override fun doInBackground(vararg params: Void?): Boolean {
            when (mode) {
                1 -> {
                    db.orderdao().insertorder(OrderEntity(restid,fooditem))
                    db.close()
                    return true
                }

                2 -> {
                    db.orderdao().deleteord(OrderEntity(restid,fooditem))
                    db.close()
                    return true
                }
            }

            return false
        }

    }

}



