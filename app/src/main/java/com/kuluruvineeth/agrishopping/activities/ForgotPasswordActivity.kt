package com.kuluruvineeth.agrishopping.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.kuluruvineeth.agrishopping.R

class ForgotPasswordActivity : BaseActivity() {
    lateinit var toolbar_forgot_password_activity: Toolbar
    lateinit var btn_submit: Button
    lateinit var et_email_forgot_pw: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        toolbar_forgot_password_activity = findViewById<Toolbar>(R.id.toolbar_forgot_password_activity)
        btn_submit = findViewById<Button>(R.id.btn_submit)
        et_email_forgot_pw = findViewById<EditText>(R.id.et_email_forgot_pw)
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

        btn_submit.setOnClickListener{
            val email: String = et_email_forgot_pw.text.toString().trim()
            if(email.isEmpty()){
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email),true)
            }else{
                //showProgressDialog(resources.getString(R.string.please_wait))
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener{task ->
                        //hideProgressDialog()
                        if(task.isSuccessful){
                            Toast.makeText(
                                this,
                                resources.getString(R.string.email_sent_success),
                                Toast.LENGTH_LONG
                            ).show()
                            finish()
                        }else{
                            showErrorSnackBar(task.exception!!.message.toString(),true)
                        }

                    }
            }
        }
    }
}