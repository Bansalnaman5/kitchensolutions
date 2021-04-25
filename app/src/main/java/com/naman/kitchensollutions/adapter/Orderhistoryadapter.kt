package com.naman.kitchensollutions.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.naman.kitchensollutions.R
import com.naman.kitchensollutions.model.FoodOptions
import com.naman.kitchensollutions.model.Orderdetails
import kotlinx.android.synthetic.main.recycler_orderhistory.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Orderhistoryadapter(val context: Context,val listorderhis:ArrayList<Orderdetails>):
RecyclerView.Adapter<Orderhistoryadapter.Orderhisviewholder>(){
    class Orderhisviewholder(view:View):RecyclerView.ViewHolder(view){

        val resname:TextView=view.findViewById(R.id.orderhisresname)
        val orderdate:TextView=view.findViewById(R.id.orderhisdate)
        val orderhisrecycler:RecyclerView=view.findViewById(R.id.orderhisrecycler)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Orderhisviewholder {
        val view=LayoutInflater.from(context).inflate(R.layout.recycler_orderhistory,p0,false)
        return Orderhisviewholder(view)
    }
    override fun getItemCount(): Int {
        return listorderhis.size
    }

    override fun onBindViewHolder(p0: Orderhisviewholder, p1: Int) {
        val objorderhis=listorderhis[p1]
        p0.resname.text=objorderhis.resName
        p0.orderdate.text=date(objorderhis.orderDate)
        recycleset(p0.orderhisrecycler,objorderhis)


    }


    fun date(stringd:String):String?{

        val inpform = SimpleDateFormat("dd-MM-yy HH:mm:ss", Locale.ENGLISH)
        val date:Date=inpform.parse(stringd) as Date
        val outform=SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        return outform.format(date)

    }

    fun recycleset(recreshis:RecyclerView,orderhislis:Orderdetails){
        val itemlist=ArrayList<FoodOptions>()
        for(j in 0 until orderhislis.foodItem.length()) {
            val Jsonf=orderhislis.foodItem.getJSONObject(j)
            itemlist.add(
                FoodOptions(
                Jsonf.getString("food_item_id"),Jsonf.getString("name"),Jsonf.getString("cost").toInt()
            )
            )
        }
        val cartadapter=Cartadapter(itemlist,context)
        val layoutmanager=LinearLayoutManager(context)
        recreshis.layoutManager=layoutmanager
        recreshis.itemAnimator=DefaultItemAnimator()
        recreshis.adapter=cartadapter
    }
}