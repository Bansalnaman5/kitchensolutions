package com.naman.kitchensollutions.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.android.volley.toolbox.Volley
import com.google.android.material.navigation.NavigationView
import com.naman.kitchensollutions.R
import com.naman.kitchensollutions.adapter.Resmenuadapter
import com.naman.kitchensollutions.fragments.*
import com.naman.kitchensollutions.fragments.Restaurantsfrag.Companion.resId
import com.naman.kitchensollutions.util.Draw
import com.naman.kitchensollutions.util.Initiator

class Dashboard : AppCompatActivity() ,Draw{
    override fun draweren(enable: Boolean) {
        val lm=if(enable)
            DrawerLayout.LOCK_MODE_UNLOCKED
        else
            DrawerLayout.LOCK_MODE_LOCKED_CLOSED
        drawer.setDrawerLockMode(lm)
        actionbartog.isDrawerIndicatorEnabled=enable
    }
    lateinit var tbar:Toolbar
    lateinit var sharedprefrences:SharedPreferences
    lateinit var initiator: Initiator
    lateinit var navdash:NavigationView
    lateinit var drawer:DrawerLayout
    lateinit var actionbartog:ActionBarDrawerToggle
    var menuitemprev:MenuItem?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard)
        initiator = Initiator(this@Dashboard)
        sharedprefrences=this@Dashboard.getSharedPreferences(initiator.pn, initiator.pm)
        init()
        toolb()
        togaction()
        homedisplay()
        navdash.setNavigationItemSelectedListener { opt: MenuItem ->

            if (menuitemprev!= null) {
                menuitemprev?.isChecked = false
            }

            opt.isCheckable = true
            opt.isChecked = true

            menuitemprev= opt

            val mPendingRunnable = Runnable { drawer.closeDrawer(GravityCompat.START) }
            Handler().postDelayed(mPendingRunnable, 200)
            val tranfrag = supportFragmentManager.beginTransaction()
            when (opt.itemId) {

                R.id.home -> {
                    val homeFragment = Home()
                    tranfrag.replace(R.id.frmdash, homeFragment)
                    tranfrag.commit()
                    supportActionBar?.title = "All Restaurants"
                }

                /*Opening the profile fragment*/
                R.id.profile -> {
                    val profileFragment = Myprofile()
                    tranfrag.replace(R.id.frmdash, profileFragment)
                    tranfrag.commit()
                    supportActionBar?.title = "My profile"
                }

                /*Opening the Order history fragment*/
                R.id.historyorder -> {
                    val orderhis = Historyorder()
                    tranfrag.replace(R.id.frmdash, orderhis)
                    tranfrag.commit()
                    supportActionBar?.title = "My Previous Orders"
                }

                /*Opening the favorites fragment*/
                R.id.favouriteres -> {
                    val favfrag = Favourites()
                    tranfrag.replace(R.id.frmdash, favfrag)
                    tranfrag.commit()
                    supportActionBar?.title = "Favorite Restaurants"
                }

                /*Opening the frequently asked questions i.e. FAQ fragment*/
                R.id.faq -> {
                    val faqfrag = Faqs()
                    tranfrag.replace(R.id.frmdash, faqfrag)
                    tranfrag.commit()
                    supportActionBar?.title = "Frequently Asked Questions"
                }

                /*Exiting the application*/
                R.id.logout -> {

                    /*Creating a confirmation dialog*/
                    val builder = AlertDialog.Builder(this@Dashboard)
                    builder.setTitle("Confirmation")
                        .setMessage("Leaving So Soon??")
                        .setPositiveButton("ok") { _, _ ->
                            initiator.setlog(false)
                            sharedprefrences.edit().clear().apply()
                            startActivity(Intent(this@Dashboard, Login::class.java))
                            Volley.newRequestQueue(this).cancelAll(this::class.java.simpleName)
                            ActivityCompat.finishAffinity(this)
                        }
                        .setNegativeButton("No") { _, _ ->
                            homedisplay()
                        }
                        .create()
                        .show()

                }

            }
            return@setNavigationItemSelectedListener true
        }

        /*This is how we can add header to the Navigation drawer using Kotlin.
        * Using the XML file to add the drawer header is also correct
        * However, we wanted to show you another and lengthy way of doing it.
        * The method of using XML to add drawer layout is a better way*/
        val convertView = LayoutInflater.from(this@Dashboard).inflate(R.layout.header_drawer, null)
        val userName: TextView = convertView.findViewById(R.id.txtdrawertext)
        val userPhone: TextView = convertView.findViewById(R.id.usercontact)
        val appIcon: ImageView = convertView.findViewById(R.id.drawerimg)
        userName.text = sharedprefrences.getString("user_name", null)
        val phoneText = sharedprefrences.getString("user_mobile_number", null)
        userPhone.text = phoneText
        navdash.addHeaderView(convertView)


        /*Here we have also added clicks to the views present inside the navigation drawer*/
        userName.setOnClickListener {
            val profileFragment = Myprofile()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frmdash, profileFragment)
            transaction.commit()
            supportActionBar?.title = "My profile"
            val mPendingRunnable = Runnable { drawer.closeDrawer(GravityCompat.START) }
            Handler().postDelayed(mPendingRunnable, 100)
        }

        appIcon.setOnClickListener {
            val profileFragment = Myprofile()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frmdash, profileFragment)
            transaction.commit()
            supportActionBar?.title = "My profile"
            val mPendingRunnable = Runnable { drawer.closeDrawer(GravityCompat.START) }
            Handler().postDelayed(mPendingRunnable, 50)
        }

    }

    /*Since, there are a lot of ways from which Home fragment will open therefore it is better to make a
    * separate method for it.*/
    fun homedisplay() {
        val fragment = Home()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frmdash, fragment)
        transaction.commit()
        supportActionBar?.title = "All Restaurants"
        navdash.setCheckedItem(R.id.home)
    }

    fun togaction() {
        actionbartog = object :
            ActionBarDrawerToggle(this, drawer,R.string.open,R.string.Close) {
            override fun onDrawerStateChanged(newState: Int) {
                super.onDrawerStateChanged(newState)
                val pendingRunnable = Runnable {
                    val inputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                }

                /*delaying the closing of the navigation drawer for that the motion looks smooth*/
                Handler().postDelayed(pendingRunnable, 50)
            }
        }
        drawer.addDrawerListener(actionbartog)
        actionbartog.syncState()

    }

    private fun toolb() {
        setSupportActionBar(tbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "All Restaurants"
            }

    /*Initialising the views*/
    private fun init() {
        tbar = findViewById(R.id.tbar)
        drawer = findViewById(R.id.dashdrawlayout)
        navdash = findViewById(R.id.navdash)
    }


    /*Setting up the opening of navigation drawer*/
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        val f = supportFragmentManager.findFragmentById(R.id.frmdash)
        when (id) {
            android.R.id.home -> {
                if (f is Restaurantsfrag) {
                    onBackPressed()
                } else {
                    drawer.openDrawer(GravityCompat.START)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }


    /*Adding custom routes from different fragments when we press the back button*/
    override fun onBackPressed() {
        val f = supportFragmentManager.findFragmentById(R.id.frmdash)
        when (f) {
            is Home -> {
                Volley.newRequestQueue(this).cancelAll(this::class.java.simpleName)
                super.onBackPressed()
            }
            is Restaurantsfrag -> {
                if (!Resmenuadapter.isemptycart) {
                    val builder = AlertDialog.Builder(this@Dashboard)
                    builder.setTitle("Confirmation")
                        .setMessage("Leaving Here Will Reset Cart.Still Want To Leave???")
                        .setPositiveButton("Yes") { _, _ ->
                            val clearCart =
                                Checkoutcart.Removefromdbasync(applicationContext, resId.toString()).execute().get()
                            homedisplay()
                            Resmenuadapter.isemptycart = true
                        }
                        .setNegativeButton("No") { _, _ ->

                        }
                        .create()
                        .show()
                } else {
                    homedisplay()
                }
            }
            else -> homedisplay()
        }
    }
    }

