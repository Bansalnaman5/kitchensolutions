package com.naman.kitchensollutions.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import com.naman.kitchensollutions.R
import android.R.array
import com.naman.kitchensollutions.adapter.Restaurantsadapter
import com.naman.kitchensollutions.model.Restaurants
import com.naman.kitchensollutions.util.Arranger
import com.naman.kitchensollutions.util.ConnectionManager
import com.naman.kitchensollutions.util.Draw
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import java.util.Locale.filter
import kotlin.collections.HashMap

class Home : Fragment() {
    lateinit var homeload:RelativeLayout
    lateinit var homeprogressbar:ProgressBar
    lateinit var rlhome:RelativeLayout
    lateinit var recyclerest:RecyclerView
    lateinit var restaurantsadapter: Restaurantsadapter
    var reslist= arrayListOf<Restaurants>()
    var optcheck:Int=-1

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
            val view = inflater.inflate(R.layout.fragment_home, container, false)
            (activity as Draw).draweren(true)
            homeprogressbar = view.findViewById(R.id.homeprogressbar)
            homeload = view.findViewById(R.id.homeload) as RelativeLayout
            homeload.visibility = View.VISIBLE

            setUpRecycler(view)

            setHasOptionsMenu(true)

            return view
        }

    private fun setUpRecycler(view: View) {
        recyclerest= view.findViewById(R.id.recyclerest) as RecyclerView
        val queue = Volley.newRequestQueue(activity as Context)
        var url="http://13.235.250.119/v2/restaurants/fetch_result/"
        if (ConnectionManager().NetworkAvailabel(activity as Context)) {
            val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener<JSONObject> { response ->
                    homeload.visibility = View.GONE
                    try {
                        val data = response.getJSONObject("data")
                        val success = data.getBoolean("success")
                        if (success) {
                            val resar = data.getJSONArray("data")
                            for (i in 0 until resar.length()) {
                                val resObject = resar.getJSONObject(i)
                                val res = Restaurants(
                                    resObject.getString("id").toInt(),
                                    resObject.getString("name"),
                                    resObject.getString("rating"),
                                    resObject.getString("cost_for_one").toInt(),
                                    resObject.getString("image_url")
                                )
                                reslist.add(res)
                                if (activity != null) {
                                    restaurantsadapter = Restaurantsadapter(reslist, activity as Context)
                                    val layoutmanager = LinearLayoutManager(activity)
                                    recyclerest.layoutManager = layoutmanager
                                    recyclerest.itemAnimator = DefaultItemAnimator()
                                    recyclerest.adapter = restaurantsadapter
                                    recyclerest.setHasFixedSize(true)
                                }

                            }
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error: VolleyError? ->
                    Toast.makeText(activity as Context, error?.message, Toast.LENGTH_SHORT).show()
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

        } else {
            val bual = AlertDialog.Builder(activity as Context)
            bual.setTitle("Error")
            bual.setMessage("No Internet.Open After Connecting")
            bual.setCancelable(false)
            bual.setPositiveButton("Ok") { text,litener ->
                val stt=Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(stt)
                activity?.finish()

            }
            bual.setNegativeButton("Exit"){text,listener->
                ActivityCompat.finishAffinity(activity as Activity)}
            bual.create()
            bual.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        activity?.menuInflater?.inflate(R.menu.menu_dashboard, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_sort -> showDialog(context as Context)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDialog(context: Context) {

        val builder: AlertDialog.Builder? = AlertDialog.Builder(context)
        builder?.setTitle("Add Filters")
        builder?.setSingleChoiceItems(R.array.sorttext, optcheck) { _, isChecked ->
            optcheck = isChecked
        }
        builder?.setPositiveButton("Ok") { _, _ ->

            when (optcheck) {
                0 -> {
                    Collections.sort(reslist, Arranger.compaircost)
                }
                1 -> {
                    Collections.sort(reslist, Arranger.compaircost)
                    reslist.reverse()
                }
                2 -> {
                    Collections.sort(reslist, Arranger.comprate)
                    reslist.reverse()
                }
            }
            restaurantsadapter.notifyDataSetChanged()
        }
        builder?.setNegativeButton("Cancel") { _, _ ->

        }
        builder?.create()
        builder?.show()
    }

}


