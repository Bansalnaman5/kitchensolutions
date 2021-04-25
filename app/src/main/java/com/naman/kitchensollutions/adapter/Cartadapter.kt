package com.naman.kitchensollutions.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.naman.kitchensollutions.R
import com.naman.kitchensollutions.model.FoodOptions
import kotlinx.android.synthetic.main.recycler_cart_single_row.view.*

class Cartadapter(val listcart:ArrayList<FoodOptions>,val context:Context):RecyclerView.Adapter<Cartadapter.Cartviewholder>(){

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Cartviewholder {
        val  itemView=LayoutInflater.from(p0.context).inflate(R.layout.recycler_cart_single_row,p0,false)
        return Cartviewholder((itemView))
    }

    override fun getItemCount(): Int {
        return listcart.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(p0: Cartviewholder, position: Int) {
        val objcart=listcart[position]
        p0.nameitem.text=objcart.name
        val price="RS ${objcart.cost?.toString()}"
        p0.priceitem.text=price


    }
    class Cartviewholder(view:View):RecyclerView.ViewHolder(view){

        val nameitem: TextView =view.findViewById(R.id.cartitemnametxt)
        val priceitem:TextView=view.findViewById(R.id.txtitemprice)

    }

}
