package com.kuluruvineeth.agrishopping.ui.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.kuluruvineeth.agrishopping.R
import com.kuluruvineeth.agrishopping.utils.Constants

class MainActivity : AppCompatActivity() {
    private lateinit var tv_main: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv_main = findViewById<TextView>(R.id.tv_main)
        val shredPreferences =
            getSharedPreferences(Constants.AGRISHOP_PREFERENCES,Context.MODE_PRIVATE)
        val username = shredPreferences.getString(Constants.LOGGED_IN_USERNAME,"")!!
        tv_main.text = "The logged in user is $username"
    }
}