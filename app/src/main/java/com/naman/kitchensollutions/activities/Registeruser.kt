package com.naman.kitchensollutions.activities

import android.app.DownloadManager
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.naman.kitchensollutions.R
import com.naman.kitchensollutions.util.ConnectionManager
import com.naman.kitchensollutions.util.Initiator

import kotlinx.android.synthetic.main.dashboard.*
import org.json.JSONObject
import java.lang.Exception

class Registeruser : AppCompatActivity() {

    lateinit var regname:EditText
    lateinit var regmail:EditText
    lateinit var regmobile:EditText
    lateinit var regaddress:EditText
    lateinit var regpass:EditText
    lateinit var regconpass:EditText
    lateinit var initiator: Initiator
    lateinit var regbtn:Button
    lateinit var sharedPreferences: SharedPreferences
    lateinit var regrl:RelativeLayout
    lateinit var regprogressbar:ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_registeruser)
        regname=findViewById(R.id.regname)
        regmail=findViewById(R.id.regmail)
        regmobile=findViewById(R.id.regmobile)
        regaddress=findViewById(R.id.regaddress)
        regpass=findViewById(R.id.regpass)
        regconpass=findViewById(R.id.regconpass)
        initiator= Initiator(this)
        sharedPreferences=getSharedPreferences(initiator.pn,initiator.pm)
        regbtn=findViewById(R.id.regbtn)
        regrl=findViewById(R.id.regrl)
        regprogressbar=findViewById(R.id.regprogressbar)
        regrl.visibility= View.VISIBLE
        regprogressbar.visibility=View.INVISIBLE
        regbtn.setOnClickListener {
            regrl.visibility=View.INVISIBLE
            regprogressbar.visibility=View.VISIBLE
            if(regname.text.toString().length>=3){
                regname.error=null
                if(regmobile.text.toString().length==10){
                    regmobile.error=null
                    if(regpass.text.toString().length>=4){
                        regpass.error=null
                        if(regpass.text.toString()==regconpass.text.toString()){
                            regpass.error=null
                            regconpass.error=null
                            if(ConnectionManager().NetworkAvailabel(this@Registeruser)) {
                                val queue = Volley.newRequestQueue(this)
                                val jsonparams = JSONObject()
                                val url: String = "http://13.235.250.119/v2/register/fetch_result"
                                jsonparams.put("name", regname.text.toString())
                                jsonparams.put("mobile_number", regmobile.text.toString())
                                jsonparams.put("password", regpass.text.toString())
                                jsonparams.put("address", regaddress.text.toString())
                                jsonparams.put("email", regmail.text.toString())
                                val jsonObjectRequest = object:JsonObjectRequest(Request.Method.POST,url,jsonparams,Response.Listener {
                                    try {
                                        val data = it.getJSONObject("data")
                                        val success = data.getBoolean("success")
                                        if (success) {
                                            val response = data.getJSONObject("data")
                                            sharedPreferences.edit()
                                                .putString("user_id", response.getString("user_id"))
                                                .apply()
                                            sharedPreferences.edit().putString(
                                                "user_name",
                                                response.getString("user_name")
                                            ).apply()
                                            sharedPreferences.edit().putString(
                                                "user_mobile_number",
                                                response.getString("user_mobile_number")
                                            ).apply()
                                            sharedPreferences.edit().putString(
                                                "user_address",
                                                response.getString("user_address")
                                            ).apply()
                                            sharedPreferences.edit().putString(
                                                "user_email",
                                                response.getString("user_email")
                                            ).apply()
                                            initiator.setlog(true)
                                            startActivity(
                                                Intent(
                                                    this@Registeruser,
                                                    Dashboard::class.java
                                                )
                                            )
                                            finish()
                                        } else {
                                            regrl.visibility = View.VISIBLE
                                            regprogressbar.visibility = View.INVISIBLE
                                            val errmsg = data.getString("errorMessage")
                                            Toast.makeText(
                                                this@Registeruser,
                                                errmsg,
                                                Toast.LENGTH_SHORT
                                            ).show()

                                        }
                                    }catch (e:Exception){
                                        regrl.visibility=View.VISIBLE
                                        regprogressbar.visibility=View.INVISIBLE
                                        e.printStackTrace()
                                    }
                                },
                                    Response.ErrorListener {
                                        Toast.makeText(this@Registeruser,it.message,Toast.LENGTH_SHORT).show()
                                        regrl.visibility=View.VISIBLE
                                        regprogressbar.visibility=View.INVISIBLE
                                    })
                                {
                                    override fun getHeaders(): MutableMap<String, String> {
                                        val headers = HashMap<String, String>()
                                        headers["Content-type"] = "application/json"

                                        /*The below used token will not work, kindly use the token provided to you in the training*/
                                        headers["token"] = "ffc38779fb84f5"
                                        return headers
                                    }
                                }
                                queue.add(jsonObjectRequest)


                            }else{
                                regrl.visibility=View.VISIBLE
                                regprogressbar.visibility=View.INVISIBLE
                                Toast.makeText(this@Registeruser,"No Internet",Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            regrl.visibility=View.VISIBLE
                            regprogressbar.visibility=View.INVISIBLE
                            regpass.error="Different password entered"
                            regconpass.error="Different password entered"
                            Toast.makeText(this@Registeruser,"Different passwords entered",Toast.LENGTH_SHORT).show()

                        }
                    }else{
                        regrl.visibility=View.VISIBLE
                        regprogressbar.visibility=View.INVISIBLE
                        regpass.error="Password too short"
                        Toast.makeText(this@Registeruser,"Password Should Be Greater Than 4 Digit",Toast.LENGTH_SHORT).show()
                    }
                }else{
                    regrl.visibility=View.VISIBLE
                    regprogressbar.visibility=View.INVISIBLE
                    regmobile.error="Invalid Number"
                    Toast.makeText(this@Registeruser,"Invalid mobile Number",Toast.LENGTH_SHORT).show()
                }
            }else{
                regrl.visibility=View.VISIBLE
                regprogressbar.visibility=View.INVISIBLE
                regname.error="INvalid name"
                Toast.makeText(this@Registeruser,"Invalid user Name",Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        Volley.newRequestQueue(this).cancelAll(this::class.java.simpleName)
        onBackPressed()
        return true
    }}









