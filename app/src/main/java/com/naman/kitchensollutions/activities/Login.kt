package com.naman.kitchensollutions.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Request.*
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.JsonObject
import com.naman.kitchensollutions.R
import com.naman.kitchensollutions.util.ConnectionManager
import com.naman.kitchensollutions.util.Initiator
import kotlinx.android.synthetic.main.dashboard.*
import org.json.JSONException
import org.json.JSONObject


class Login : AppCompatActivity() {
    lateinit var etmob:EditText
    lateinit var etpas:EditText
    lateinit var btlog:Button
    lateinit var textforgotpass:TextView
    lateinit var txtregister:TextView
    lateinit var initiator: Initiator
    lateinit var sharedprefrences:SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        etmob = findViewById(R.id.etmob)
        etpas = findViewById(R.id.etpas)
        btlog = findViewById(R.id.btlog)
        textforgotpass = findViewById(R.id.textforgotpass)
        txtregister = findViewById(R.id.txtregister)
        initiator = Initiator(this)
        sharedprefrences = this.getSharedPreferences(initiator.pn, initiator.pm)
        textforgotpass.setOnClickListener {
            startActivity(Intent(this@Login, Forgotpass1::class.java))

        }
        txtregister.setOnClickListener {
            startActivity(Intent(this@Login, Registeruser::class.java))
        }
        btlog.setOnClickListener {
            btlog.visibility = View.INVISIBLE
            if ((etmob.text.toString().length == 10) && (etpas.text.toString().length >= 4)) {
                if (ConnectionManager().NetworkAvailabel(this@Login)) {
                    val queue = Volley.newRequestQueue(this@Login)
                    val url = "http://13.235.250.119/v2/login/fetch_result"
                    val jsonParams = JSONObject()
                    jsonParams.put("mobile_number", etmob.text.toString())
                    val put = jsonParams.put("password", etpas.text.toString())
                    val jsonObjectRequest = object : JsonObjectRequest(Method.POST, url, jsonParams,
                        Response.Listener {

                            try {
                                val data = it.getJSONObject("data")
                                val success = data.getBoolean("success")
                                if (success) {
                                    val response = data.getJSONObject("data")
                                    sharedprefrences.edit().putString("user_id", response.getString("user_id")).apply()
                                    sharedprefrences.edit().putString("user_name", response.getString("name")).apply()
                                    sharedprefrences.edit().putString("user_mobile_number", response.getString("mobile_number")).apply()
                                    sharedprefrences.edit().putString("user_address", response.getString("address")).apply()
                                    sharedprefrences.edit().putString("user_email", response.getString("email")).apply()
                                    initiator.setlog(true)
                                    startActivity(Intent(this@Login, Dashboard::class.java))
                                    finish()
                                } else {
                                    btlog.visibility = View.VISIBLE
                                    textforgotpass.visibility = View.VISIBLE
                                    txtregister.visibility = View.VISIBLE
                                    val errormsg = data.getString("errorMessage")
                                    Toast.makeText(
                                        this@Login, errormsg, Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: JSONException) {
                                btlog.visibility = View.VISIBLE
                                textforgotpass.visibility = View.VISIBLE
                                txtregister.visibility = View.VISIBLE
                                e.printStackTrace()
                            }
                        },
                        Response.ErrorListener {
                            btlog.visibility = View.VISIBLE
                            textforgotpass.visibility = View.VISIBLE
                            txtregister.visibility = View.VISIBLE
                            Log.e("HEHE", "request failed: ${it.message}")
                        }) {
                        override fun getHeaders(): MutableMap<String, String> {
                            val headers = HashMap<String, String>()
                            headers["Content-type"] = "application/json"
                            headers["token"] = "ffc38779fb84f5"
                            return headers
                        }
                    }
                    queue.add(jsonObjectRequest)

                }else {
                    btlog.visibility = View.VISIBLE
                    textforgotpass.visibility = View.VISIBLE
                    txtregister.visibility = View.VISIBLE
                    Toast.makeText(this@Login, "No Internet", Toast.LENGTH_SHORT).show()
                }
            } else {
                btlog.visibility = View.VISIBLE
                textforgotpass.visibility = View.VISIBLE
                txtregister.visibility = View.VISIBLE
                Toast.makeText(this@Login, "Phone Ya Password Theek Nahi Hai", Toast.LENGTH_SHORT)
                    .show()

            }
        }

    }
    }

