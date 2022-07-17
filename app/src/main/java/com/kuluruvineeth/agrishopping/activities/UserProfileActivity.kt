package com.kuluruvineeth.agrishopping.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.kuluruvineeth.agrishopping.R
import com.kuluruvineeth.agrishopping.models.User
import com.kuluruvineeth.agrishopping.utils.Constants

class UserProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        var et_first_name = findViewById<EditText>(R.id.et_first_name)
        var et_last_name = findViewById<EditText>(R.id.et_last_name)
        var et_email = findViewById<EditText>(R.id.et_email)
        var userDetails: User = User()
        if(intent.hasExtra(Constants.EXTRA_USER_DETAILS)){
            //Get the user details from intent as a ParcelableExtra.
            userDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }
        et_first_name.isEnabled = false
        et_first_name.setText(userDetails.firstName)
        et_last_name.isEnabled = false
        et_last_name.setText(userDetails.lastName)
        et_email.isEnabled = false
        et_email.setText(userDetails.email)
    }
}