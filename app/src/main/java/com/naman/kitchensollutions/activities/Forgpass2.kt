package com.naman.kitchensollutions.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.naman.kitchensollutions.R
import com.naman.kitchensollutions.util.ConnectionManager

import kotlinx.android.synthetic.main.activity_forgpass2.*
import kotlinx.android.synthetic.main.dashboard.*
import org.json.JSONObject

class Forgpass2 : AppCompatActivity() {
    lateinit var rl2:RelativeLayout
    lateinit var forotp:EditText
    lateinit var forpass1:EditText
    lateinit var forpass2:EditText
    lateinit var fornext:Button
    lateinit var for2prog:ProgressBar
    lateinit var mobno:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgpass2)
        rl2=findViewById(R.id.rl2)
        forotp=findViewById(R.id.forotp)
        forpass1=findViewById(R.id.forpass1)
        forpass2=findViewById(R.id.forpass2)
        fornext=findViewById(R.id.fornext)
        for2prog=findViewById(R.id.for2prog)
        rl2.visibility=View.VISIBLE
        for2prog.visibility=View.GONE
        if(intent!=null){
            mobno=intent.getStringExtra("user_mobile") as String
        }
        fornext.setOnClickListener {
            rl2.visibility= View.GONE
            for2prog.visibility=View.VISIBLE
            if(ConnectionManager().NetworkAvailabel(this@Forgpass2)){
                if(forotp.text.toString().length==4){
                    if(forpass1.text.toString().length>=4){
                        if(forpass1.text.toString()==forpass2.text.toString()){
                            reset(mobno,forotp.text.toString(),forpass2.text.toString())

                        }else{
                            rl2.visibility=View.VISIBLE
                            for2prog.visibility=View.GONE
                            Toast.makeText(this@Forgpass2,"Password do not match",Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        rl2.visibility=View.VISIBLE
                        for2prog.visibility=View.GONE
                        Toast.makeText(this@Forgpass2,"Invalid password",Toast.LENGTH_SHORT).show()

                    }

                }else{
                    rl2.visibility=View.VISIBLE
                    for2prog.visibility=View.GONE
                    Toast.makeText(this@Forgpass2,"Invalid OTP",Toast.LENGTH_SHORT).show()
                }
            }else{
                rl2.visibility=View.VISIBLE
                for2prog.visibility=View.GONE
                Toast.makeText(this@Forgpass2,"No Internet",Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun reset(mobile:String,otp:String,pass:String){
        val queue= Volley.newRequestQueue(this)
        var url="http://13.235.250.119/v2/reset_password/fetch_result"
        val jsonparams=JSONObject()
        jsonparams.put("mobile_number",mobile)
        jsonparams.put("password",pass)
        jsonparams.put("otp",otp)
        val jsonObjectRequest = object : JsonObjectRequest(Method.POST, url, jsonparams, Response.Listener {
                try {
                    val data = it.getJSONObject("data")
                    val success = data.getBoolean("success")
                    if (success) {
                        for2prog.visibility = View.INVISIBLE
                        val builder = AlertDialog.Builder(this@Forgpass2)
                        builder.setTitle("Information")
                        builder.setMessage("Password Changed Successfully")
                        builder.setCancelable(false)
                        builder.setPositiveButton("Ok") { _, _ ->
                            startActivity(
                                Intent(
                                    this@Forgpass2,
                                    Login::class.java
                                )
                            )
                            ActivityCompat.finishAffinity(this@Forgpass2)
                        }
                        builder.create().show()
                    } else {
                        rl2.visibility = View.VISIBLE
                        for2prog.visibility = View.GONE
                        val error = data.getString("errorMessage")
                        Toast.makeText(
                            this@Forgpass2,
                            error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    rl2.visibility = View.VISIBLE
                    for2prog.visibility = View.GONE
                    Toast.makeText(
                        this@Forgpass2,
                        "Some Error",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }, Response.ErrorListener {
                rl2.visibility = View.VISIBLE
                for2prog.visibility = View.GONE
                VolleyLog.e("HEHE::", "request failed Error: ${it.message}")
                Toast.makeText(this@Forgpass2, it.message, Toast.LENGTH_SHORT).show()
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


