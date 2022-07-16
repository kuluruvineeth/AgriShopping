package com.kuluruvineeth.agrishopping.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.kuluruvineeth.agrishopping.R

class RegisterActivity : AppCompatActivity() {
    lateinit var toolbar_register_activity: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val tv_login = findViewById<TextView>(R.id.tv_login)
        toolbar_register_activity = findViewById<Toolbar>(R.id.toolbar_register_activity)
        //Hide status bar
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        setupActionBar()
        tv_login.setOnClickListener {
            //Launch Logic screen on click
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupActionBar(){
        setSupportActionBar(toolbar_register_activity)

        val actionBar = supportActionBar
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }
        toolbar_register_activity.setNavigationOnClickListener { onBackPressed() }
    }

}