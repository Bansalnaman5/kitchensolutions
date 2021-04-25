package com.naman.kitchensollutions.adapter

import android.content.Context
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.naman.kitchensollutions.R
import com.naman.kitchensollutions.databases.Resdatabase
import com.naman.kitchensollutions.databases.Restaurantentity
import com.naman.kitchensollutions.fragments.Restaurantsfrag
import com.naman.kitchensollutions.model.Restaurants
import com.squareup.picasso.Picasso

class Restaurantsadapter(val restrau:ArrayList<Restaurants>,val context: Context):
    RecyclerView.Adapter<Restaurantsadapter.Restrauntviewholder>() {

    class Restrauntviewholder(view: View):RecyclerView.ViewHolder(view){
        val resimage:ImageView=view.findViewById(R.id.rescardimg)
        val resname:TextView=view.findViewById(R.id.txtresname)
        val rating:TextView=view.findViewById(R.id.txtresrating)
        val cost:TextView=view.findViewById(R.id.txtcostres)
        val favimage:ImageView=view.findViewById(R.id.favimg)
        val restaurantrow:CardView=view.findViewById(R.id.restaurantrow)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Restrauntviewholder {
        val itemView=LayoutInflater.from(p0.context).inflate(R.layout.recycler_restaurant_single_row,p0,false)
        return Restrauntviewholder(itemView)

    }

    override fun getItemCount(): Int {
        return restrau.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(p0: Restrauntviewholder, p1: Int) {
        val objres=restrau.get(p1)
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            p0.resimage.clipToOutline=true
        }
        p0.resname.text=objres.name
        p0.rating.text=objres.rating
        val costtwo="${objres.costfortwo.toString()}/person"
        p0.cost.text=costtwo
        Picasso.get().load(objres.iMgurl).error(R.drawable.applogo).into(p0.resimage)
        val favlist=getfav(context).execute().get()
        if(favlist.isNotEmpty() && favlist.contains((objres.id.toString()))){
            p0.favimage.setImageResource(R.drawable.filledfav)
        }else{
            p0.favimage.setImageResource(R.drawable.emptyfav)
        }
        p0.favimage.setOnClickListener {
            val resentity=Restaurantentity(objres.id,objres.name,objres.rating,objres.costfortwo.toString(),objres.iMgurl)
            if(!Dbasynktask(context,resentity,1).execute().get()){
                val asy=Dbasynktask(context,resentity,2).execute()
                val result=asy.get()
                if(result){
                    p0.favimage.setImageResource(R.drawable.filledfav)
                }
            }else{
                val asy=Dbasynktask(context,resentity,3).execute()
                val result=asy.get()
                if(result){
                    p0.favimage.setImageResource(R.drawable.emptyfav)
                }
            }

        }
        p0.restaurantrow.setOnClickListener {
            val frag=com.naman.kitchensollutions.fragments.Restaurantsfrag()
            val argumen=Bundle()
            argumen.putInt("id",objres.id as Int)
            argumen.putString("name",objres.name)
            frag.arguments=argumen
            val tran=(context as FragmentActivity).supportFragmentManager.beginTransaction()
            tran.replace(R.id.frmdash,frag)
            tran.commit()
            (context as AppCompatActivity).supportActionBar?.title=p0.resname.text.toString()
        }
    }
    class Dbasynktask(context: Context,val resentity:Restaurantentity,val mo:Int):AsyncTask<Void,Void,Boolean>(){
        val db=Room.databaseBuilder(context,Resdatabase::class.java,"res-db").build()
        override fun doInBackground(vararg params: Void?): Boolean {
            when(mo){
                1->{
                    val res:Restaurantentity?=db.restaurantdao().getresbyid(resentity.id.toString())
                    db.close()
                    return res!=null
                }
                2->{
                    db.restaurantdao().insertres(resentity)
                    db.close()
                    return true
                }
                3->{
                    db.restaurantdao().delres(resentity)
                    db.close()
                    return true
                }
            }
            return false
        }
    }
    class getfav(context: Context):AsyncTask<Void,Void,List<String>>(){
        val db=Room.databaseBuilder(context,Resdatabase::class.java,"res-db").build()
        override fun doInBackground(vararg params: Void?): List<String> {
            val list1=db.restaurantdao().getres()
            val idlist= arrayListOf<String>()
            for(j in list1){
                idlist.add(j.id.toString())
            }
            return idlist
        }
    }

}