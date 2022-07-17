package com.kuluruvineeth.agrishopping.activities.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.kuluruvineeth.agrishopping.R
import com.kuluruvineeth.agrishopping.firestore.FirestoreClass
import com.kuluruvineeth.agrishopping.models.User
import com.kuluruvineeth.agrishopping.utils.Constants

class LoginActivity : BaseActivity(),View.OnClickListener {
    private lateinit var et_email: EditText
    private lateinit var et_password: EditText
    private lateinit var tv_forgot_password: TextView
    private lateinit var btn_login: Button
    private lateinit var tv_register: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        tv_register = findViewById<TextView>(R.id.tv_register)
        et_email = findViewById<EditText>(R.id.et_email)
        et_password = findViewById<EditText>(R.id.et_password)
        tv_forgot_password = findViewById<TextView>(R.id.tv_forgot_password)
        btn_login = findViewById<Button>(R.id.btn_login)
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

        //Assign the click listener to the views
        tv_forgot_password.setOnClickListener(this)
        btn_login.setOnClickListener(this)
        tv_register.setOnClickListener(this)
    }

    fun userLoggedInSuccess(user: User){
        //Hide the progress dialog.
        //hideProgressDialog()

        if(user.profileCompleted == 0){
            //If the user profile is incomplete then launch the UserProfileActivity
            val intent = Intent(this, UserProfileActivity::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS,user)
            startActivity(intent)
        }else{
            //Redirect the user to Main Screen after log in.
            startActivity(Intent(this, DashboardActivity2::class.java))
        }

        //Redirect the user to Main Screen after log in.
        //startActivity(Intent(this,MainActivity::class.java))
        //finish()
    }

    //In Login screen the clickable components are Login Button, ForgotPassword text and Register Text.
    override fun onClick(v: View?){
        if(v!=null){
            when(v.id){
                R.id.tv_forgot_password -> {
                    val intent = Intent(this, ForgotPasswordActivity::class.java)
                    startActivity(intent)
                }
                R.id.btn_login -> {
                    //validateLoginDetails()
                    logInRegisteredUser()
                }
                R.id.tv_register -> {
                    val intent = Intent(this, RegisterActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    /*
    A function to validate the login entries of a user.
     */
    private fun validateLoginDetails(): Boolean{
        return when{
            TextUtils.isEmpty(et_email.text.toString().trim()) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email),true)
                false
            }
            TextUtils.isEmpty(et_password.text.toString().trim()) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password),true)
                false
            }
            else -> {
                //showErrorSnackBar("Your details are valid",false)
                true
            }
        }
    }

    private fun logInRegisteredUser(){
        if(validateLoginDetails()){
            //show the progress dialog
            //showProgressDialog(resources.getString(R.string.please_wait))

            //Get the text from edittext and trim the space
            val email = et_email.text.toString().trim()
            val password = et_password.text.toString().trim()

            //Log-in using FirebaseAuth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                .addOnCompleteListener{ task ->
                    //Hode the progress dialog
                    //hideProgressDialog()

                    if(task.isSuccessful){
                        FirestoreClass().getUserDetails(this)
                        //showErrorSnackBar("You are logged in successfully",false)
                    }else{
                        //hideProgressDialog()
                        showErrorSnackBar(task.exception!!.message.toString(),true)
                    }
                }
        }
    }
}