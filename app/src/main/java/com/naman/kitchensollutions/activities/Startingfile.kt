package com.naman.kitchensollutions.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.naman.kitchensollutions.R
import com.naman.kitchensollutions.util.Initiator

class Startingfile : AppCompatActivity() {
    lateinit var initiator: Initiator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starting)
        initiator = Initiator(this)

            Handler().postDelayed({
                openact()
            }, 2000)
        }

        fun openact() {
            if (initiator.cheklog()) {
                val intent = Intent(this, Dashboard::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
                finish()
            }
        }


        override fun onPause() {
            super.onPause()
            finish()
        }
    }

