package com.naman.kitchensollutions.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.naman.kitchensollutions.R
import com.naman.kitchensollutions.model.FoodOptions
import kotlinx.android.synthetic.main.recycler_res_menu_single_row.view.*
import java.nio.file.CopyOption

class Resmenuadapter(val context: Context,val listmenu:ArrayList<FoodOptions>,val clickitem:OnItemClickListener):
    RecyclerView.Adapter<Resmenuadapter.resmenuholder>() {
    companion object{
        var isemptycart=true
    }
    class resmenuholder(view:View):RecyclerView.ViewHolder(view){
        val itemname:TextView=view.findViewById(R.id.itemnametxt)
        val itemcost:TextView=view.findViewById(R.id.itemcosttxt)
        val sno:TextView=view.findViewById(R.id.snotxt)
        val btnadd:Button=view.findViewById(R.id.addedcartbtn)
        val btnremove:Button=view.findViewById(R.id.removecartbtn)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): resmenuholder {

        val itemView=LayoutInflater.from(p0.context).inflate(R.layout.recycler_res_menu_single_row,p0,false)
        return resmenuholder(itemView)
    }
    interface OnItemClickListener{
        fun Addclickitem(option:FoodOptions)
        fun Removeclickitem(option:FoodOptions)
    }

    override fun getItemCount(): Int {
        return listmenu.size
    }

    override fun onBindViewHolder(p0: resmenuholder, p1: Int) {

        val objmenu=listmenu[p1]
        p0.itemname.text=objmenu.name
        val co="RS ${objmenu.cost?.toString()}"
        p0.itemcost.text=co
        p0.sno.text=(p1+1).toString()
        p0.btnadd.setOnClickListener {
            p0.btnadd.visibility=View.GONE
            p0.btnremove.visibility=View.VISIBLE
            clickitem.Addclickitem(objmenu)
        }
        p0.btnremove.setOnClickListener {
            p0.btnremove.visibility=View.GONE
            p0.btnadd.visibility=View.VISIBLE
            clickitem.Removeclickitem(objmenu)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}