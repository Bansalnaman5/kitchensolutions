package com.naman.kitchensollutions.activities

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.naman.kitchensollutions.R
import com.naman.kitchensollutions.adapter.Cartadapter
import com.naman.kitchensollutions.adapter.Resmenuadapter
import com.naman.kitchensollutions.databases.OrderEntity
import com.naman.kitchensollutions.databases.Resdatabase
import com.naman.kitchensollutions.fragments.Restaurantsfrag
import com.naman.kitchensollutions.model.FoodOptions

import kotlinx.android.synthetic.main.activity_checkoutcart.*
import kotlinx.android.synthetic.main.dashboard.*
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class Checkoutcart : AppCompatActivity() {
    lateinit var cartrecycleradapter:Cartadapter
    lateinit var bar:Toolbar
    lateinit var cartrl:RelativeLayout
    lateinit var cartll:LinearLayout
    lateinit var restnamecart:TextView
    lateinit var cartrecycler:RecyclerView
    lateinit var rlprogress:RelativeLayout
    lateinit var cartprogressbar:ProgressBar
    lateinit var orderconbtn:Button
    var listoforder=ArrayList<FoodOptions>()
    var resname:String=""
    var resid:Int=0
    class EXtractfromdbasync(context: Context):AsyncTask<Void,Void,List<OrderEntity>>(){
        val db= Room.databaseBuilder(context,Resdatabase::class.java,"res-db").build()
        override fun doInBackground(vararg params: Void?): List<OrderEntity> {
            return db.orderdao().getorders()

        }

    }
    class Removefromdbasync(context: Context,val resid:String):AsyncTask<Void,Void,Boolean>(){
        val db=Room.databaseBuilder(context,Resdatabase::class.java,"res-db").build()
        override fun doInBackground(vararg params: Void?): Boolean {
            db.orderdao().delorders(resid)
            db.orderdao().delall()
            db.close()
            return true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val initialcart=Removefromdbasync(applicationContext,resid.toString()).execute().get()
        Resmenuadapter.isemptycart=true
        onBackPressed()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkoutcart)
        toolb()
        init()
        cartlist()
        placingorder()
    }
    fun cartlist(){
        cartrecycler=findViewById(R.id.cartrecycler)
        val listdatabase=EXtractfromdbasync(applicationContext).execute().get()
        for(e in listdatabase){
            listoforder.addAll(Gson().fromJson(e.fooditems,Array<FoodOptions>::class.java).asList())
        }
        if(listoforder.isEmpty()){
            cartrl.visibility=View.GONE
            rlprogress.visibility=View.VISIBLE
        }else{
            cartrl.visibility=View.VISIBLE
            rlprogress.visibility=View.GONE
        }
        cartrecycleradapter= Cartadapter(listoforder,this@Checkoutcart)
        val layoutmanager=LinearLayoutManager(this@Checkoutcart)
        cartrecycler.layoutManager=layoutmanager
        cartrecycler.itemAnimator=DefaultItemAnimator()
        cartrecycler.adapter=cartrecycleradapter
    }
    fun toolb(){}
    fun init(){
        rlprogress=findViewById(R.id.rlprogress)
        cartrl=findViewById(R.id.cartrl)
        restnamecart=findViewById(R.id.restnamecart)
        restnamecart.text=Restaurantsfrag.resName
        val bundle=intent.getBundleExtra("data")
        resid=bundle?.getInt("resId",0) as Int
        resname=bundle?.getString("resName","") as String
    }
    fun placingorder(){
        orderconbtn=findViewById(R.id.orderconbtn)
        var s=0
        for(j in 0 until listoforder.size){
            s=s+listoforder[j].cost as Int
        }
        val tot="Order Total:RS ${s}"
        orderconbtn.text=tot
        orderconbtn.setOnClickListener {
            rlprogress.visibility=View.VISIBLE
            cartrl.visibility=View.INVISIBLE
            var url="http://13.235.250.119/v2/place_order/fetch_result"
            val queue=Volley.newRequestQueue(this)
            val jsonparanms=JSONObject()
            jsonparanms.put("user_id",this@Checkoutcart.getSharedPreferences("Myapp",Context.MODE_PRIVATE).getString("user_id",null) as String)
            jsonparanms.put("restaurant_id",Restaurantsfrag.resId?.toString() as String)
            var s=0
            for(j in 0 until listoforder.size){
                s=s+listoforder[j].cost as Int
            }
            jsonparanms.put("total_cost",s.toString())
            val itemar=JSONArray()
            for(j in 0 until listoforder.size){
                val itemid=JSONObject()
                itemid.put("food_item_id",listoforder[j].id)
                itemar.put(j,itemid)
            }
            jsonparanms.put("food",itemar)
            val jsonObjectRequest=object:JsonObjectRequest(Method.POST,url,jsonparanms,Response.Listener {
                try{
                    val data=it.getJSONObject("data")
                    val success=data.getBoolean("success")
                    if(success){
                        var initialcart=Removefromdbasync(applicationContext,resid.toString()).execute().get()
                        Resmenuadapter.isemptycart=true
                        val dialog=Dialog(this@Checkoutcart,android.R.style.Theme_Light_NoTitleBar_Fullscreen)
                        dialog.setContentView(R.layout.ordercomplete)
                        dialog.show()
                        dialog.setCancelable(false)
                        val btnfinal=dialog.findViewById<Button>(R.id.btnfinal)
                        btnfinal.setOnClickListener {
                            dialog.dismiss()
                            startActivity(Intent(this@Checkoutcart,Dashboard::class.java))
                            ActivityCompat.finishAffinity(this@Checkoutcart)
                        }
                    }else{
                        cartrl.visibility=View.VISIBLE
                        Toast.makeText(this@Checkoutcart,"HEHE !!!ERROR AGAYAA",Toast.LENGTH_SHORT).show()
                    }
                }catch (e:Exception){
                    cartrl.visibility=View.VISIBLE
                    e.printStackTrace()
                }
            },Response.ErrorListener {
                cartrl.visibility=View.VISIBLE
                Toast.makeText(this@Checkoutcart,it.message,Toast.LENGTH_SHORT).show()
            }){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers=HashMap<String,String>()
                    headers["Conten-type"]="application/json"
                    headers["token"]="ffc38779fb84f5"
                    return headers
                }
            }
            queue.add(jsonObjectRequest)

        }
    }


}
