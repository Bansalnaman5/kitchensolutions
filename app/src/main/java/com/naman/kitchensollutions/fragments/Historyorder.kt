package com.naman.kitchensollutions.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.naman.kitchensollutions.R
import com.naman.kitchensollutions.adapter.Orderhistoryadapter
import com.naman.kitchensollutions.model.Orderdetails
import com.naman.kitchensollutions.util.Draw


class Historyorder : Fragment() {



    lateinit var llordehis:LinearLayout
    lateinit var historyorderrecycler:RecyclerView
    lateinit var noorderrl:RelativeLayout
    lateinit var loadinghistoryrl:RelativeLayout
    lateinit var historyprogbar:ProgressBar
    lateinit var orderhistoryadapter: Orderhistoryadapter
    lateinit var sharedprefrences:SharedPreferences
    var userid=""
    var listorderhistory=ArrayList<Orderdetails>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_historyorder, container, false)
        (activity as Draw).draweren(true)
        llordehis = view.findViewById(R.id.llordershis)
        noorderrl = view.findViewById(R.id.noorderrl)
        historyorderrecycler = view.findViewById(R.id.historyorderrecycler)
        loadinghistoryrl = view?.findViewById(R.id.loadinghistoryrl) as RelativeLayout
        loadinghistoryrl.visibility = View.VISIBLE
        sharedprefrences =
            (activity as Context).getSharedPreferences("Myapp", Context.MODE_PRIVATE)
        userid = sharedprefrences.getString("user_id", null) as String
        sendServerRequest(userid)
        return view
    }

    private fun sendServerRequest(userId: String) {
        val queue = Volley.newRequestQueue(activity as Context)
        val url="http://13.235.250.119/v2/orders/fetch_result/"
        val jsonObjectRequest = object :
            JsonObjectRequest(Method.GET, url+ userId, null, Response.Listener {
                loadinghistoryrl.visibility = View.GONE
                try {
                    val data = it.getJSONObject("data")
                    val success = data.getBoolean("success")
                    if (success) {
                        val resar = data.getJSONArray("data")
                        if (resar.length() == 0) {
                            llordehis.visibility = View.GONE
                            noorderrl.visibility = View.VISIBLE
                        } else {
                            for (i in 0 until resar.length()) {
                                val orderObject = resar.getJSONObject(i)
                                val foodOptions = orderObject.getJSONArray("food_items")
                                val orderdetails = Orderdetails(
                                    orderObject.getInt("order_id"),
                                    orderObject.getString("restaurant_name"),
                                    orderObject.getString("order_placed_at"),
                                    foodOptions
                                )
                                listorderhistory.add(orderdetails)
                                if (listorderhistory.isEmpty()) {
                                    llordehis.visibility = View.GONE
                                    noorderrl.visibility = View.VISIBLE
                                } else {
                                    llordehis.visibility = View.VISIBLE
                                    noorderrl.visibility = View.GONE
                                    if (activity != null) {
                                        orderhistoryadapter = Orderhistoryadapter(
                                            activity as Context,
                                            listorderhistory
                                        )
                                        val layoutmanager = LinearLayoutManager(activity as Context)
                                        historyorderrecycler.layoutManager = layoutmanager
                                        historyorderrecycler.itemAnimator = DefaultItemAnimator()
                                        historyorderrecycler.adapter = orderhistoryadapter
                                    } else {
                                        queue.cancelAll(this::class.java.simpleName)
                                    }
                                }
                            }
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

                /*The below used token will not work, kindly use the token provided to you in the training*/
                headers["token"] = "ffc38779fb84f5"
                return headers
            }
        }
        queue.add(jsonObjectRequest)
    }

    }

