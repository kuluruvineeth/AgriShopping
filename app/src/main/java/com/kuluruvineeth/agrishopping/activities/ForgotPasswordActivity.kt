package com.kuluruvineeth.agrishopping.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.kuluruvineeth.agrishopping.R

class ForgotPasswordActivity : AppCompatActivity() {
    lateinit var toolbar_forgot_password_activity: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        toolbar_forgot_password_activity = findViewById<Toolbar>(R.id.toolbar_forgot_password_activity)
        setupActionBar()
    }
    private fun setupActionBar(){
        setSupportActionBar(toolbar_forgot_password_activity)

        val actionBar = supportActionBar
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }
        toolbar_forgot_password_activity.setNavigationOnClickListener { onBackPressed() }
    }
}