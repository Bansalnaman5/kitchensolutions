package com.naman.kitchensollutions.activities

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.naman.kitchensollutions.R
import com.naman.kitchensollutions.util.ConnectionManager

import kotlinx.android.synthetic.main.dashboard.*
import org.json.JSONObject

class Forgotpass1 : AppCompatActivity() {
    lateinit var forgmob:EditText
    lateinit var formail:EditText
    lateinit var forgpass1rl:RelativeLayout
    lateinit var forpass1progressbar:ProgressBar
    lateinit var fornext:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgotpass1)
        forgmob=findViewById(R.id.forgmob)
        formail=findViewById(R.id.formail)
        forgpass1rl=findViewById(R.id.forgpass1rl)
        forpass1progressbar=findViewById(R.id.forpass1progressbar)
        fornext=findViewById(R.id.fornext)
        forpass1progressbar.visibility=View.GONE
        forgpass1rl.visibility=View.VISIBLE
        fornext.setOnClickListener {
            if(forgmob.text.toString().length==10){
                forgmob.error=null
                val mn=formail.text.toString()
                if((!mn.isEmpty())&&(Patterns.EMAIL_ADDRESS.matcher(mn).matches())){
                    if(ConnectionManager().NetworkAvailabel(this@Forgotpass1)){
                        forgpass1rl.visibility=View.GONE
                        forpass1progressbar.visibility=View.VISIBLE
                        otp(forgmob.text.toString(),mn)
                    }
                    else{
                        forgpass1rl.visibility=View.VISIBLE
                        forpass1progressbar.visibility=View.GONE
                        Toast.makeText(this@Forgotpass1,"No Internet Connection",Toast.LENGTH_SHORT).show()
                    }

                    }else{
                    forgpass1rl.visibility=View.GONE
                    forpass1progressbar.visibility=View.VISIBLE
                    formail.error="Invalid Email" }
            }else{
                forgpass1rl.visibility=View.GONE
                forpass1progressbar.visibility=View.VISIBLE
                forgmob.error="Invalid Mobile Number"
            }
        }
    }
    fun otp(mobile:String,email:String){
        val queue = Volley.newRequestQueue(this)

        val jsonParams = JSONObject()
        jsonParams.put("mobile_number", mobile)
        jsonParams.put("email", email)
        var url="http://13.235.250.119/v2/forgot_password/fetch_result"

        val jsonObjectRequest =
            object : JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener {
                try {
                    val data = it.getJSONObject("data")
                    val success = data.getBoolean("success")
                    if (success) {
                        val firstTry = data.getBoolean("first_try")
                        if (firstTry) {
                            val builder = AlertDialog.Builder(this@Forgotpass1)
                            builder.setTitle("Information")
                            builder.setMessage("Refer to Registered Mail Address for OTP.")
                            builder.setCancelable(false)
                            builder.setPositiveButton("Ok") { _, _ ->
                                val intent = Intent(
                                    this@Forgotpass1,
                                    Forgpass2::class.java
                                )
                                intent.putExtra("user_mobile", mobile)
                                startActivity(intent)
                            }
                            builder.create().show()
                        } else {
                            val builder = AlertDialog.Builder(this@Forgotpass1)
                            builder.setTitle("Information")
                            builder.setMessage("Please refer to the previous email for the OTP.")
                            builder.setCancelable(false)
                            builder.setPositiveButton("Ok") { _, _ ->
                                val intent = Intent(
                                    this@Forgotpass1,
                                    Forgpass2::class.java
                                )
                                intent.putExtra("user_mobile", mobile)
                                startActivity(intent)
                            }
                            builder.create().show()
                        }
                    } else {
                        forgpass1rl.visibility = View.VISIBLE
                        forpass1progressbar.visibility = View.GONE
                        Toast.makeText(
                            this@Forgotpass1,
                            "Unregistered Mobile Number Entered",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    forgpass1rl.visibility = View.VISIBLE
                    forpass1progressbar.visibility = View.GONE
                    Toast.makeText(
                        this@Forgotpass1,
                        "Incorrect response error!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }, Response.ErrorListener {
                forgpass1rl.visibility = View.VISIBLE
                forpass1progressbar.visibility = View.GONE
                VolleyLog.e("HEHE::"," request fail! Error: ${it.message}")
                Toast.makeText(this@Forgotpass1, it.message, Toast.LENGTH_SHORT).show()
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


