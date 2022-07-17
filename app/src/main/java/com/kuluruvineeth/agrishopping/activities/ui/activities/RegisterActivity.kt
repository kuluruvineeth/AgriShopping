package com.kuluruvineeth.agrishopping.activities.ui.activities

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.kuluruvineeth.agrishopping.R
import com.kuluruvineeth.agrishopping.firestore.FirestoreClass
import com.kuluruvineeth.agrishopping.models.User

class RegisterActivity : BaseActivity() {
    lateinit var toolbar_register_activity: Toolbar
    lateinit var et_first_name: TextView
    lateinit var et_last_name: TextView
    lateinit var et_email: TextView
    lateinit var et_password: TextView
    lateinit var et_confirm_password: TextView
    lateinit var cb_terms_and_condition: CheckBox
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val tv_login = findViewById<TextView>(R.id.tv_login)
        toolbar_register_activity = findViewById<Toolbar>(R.id.toolbar_register_activity)
        et_first_name = findViewById<TextView>(R.id.et_first_name)
        et_last_name = findViewById<TextView>(R.id.et_last_name)
        et_email = findViewById<TextView>(R.id.et_email)
        et_password = findViewById<TextView>(R.id.et_password)
        et_confirm_password = findViewById<TextView>(R.id.et_confirm_password)
        cb_terms_and_condition = findViewById<CheckBox>(R.id.cb_terms_and_condition)
        val btn_register = findViewById<Button>(R.id.btn_register)
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
            /*val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)*/
            onBackPressed()
        }
        btn_register.setOnClickListener{
            registerUser()
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

    /**
     * A function to validate the entries of a new user.
     */
    private fun validateRegisterDetails(): Boolean {
        return when {
            TextUtils.isEmpty(et_first_name.text.toString().trim()) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_first_name), true)
                false
            }

            TextUtils.isEmpty(et_last_name.text.toString().trim()) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_last_name), true)
                false
            }

            TextUtils.isEmpty(et_email.text.toString().trim()) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(et_password.text.toString().trim()) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }

            TextUtils.isEmpty(et_confirm_password.text.toString().trim()) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_enter_confirm_password),
                    true
                )
                false
            }

            et_password.text.toString().trim() != et_confirm_password.text.toString()
                .trim() -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_password_and_confirm_password_mismatch),
                    true
                )
                false
            }
            !cb_terms_and_condition.isChecked -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_agree_terms_and_condition),
                    true
                )
                false
            }
            else -> {
                //showErrorSnackBar("Your details are valid.", false)
                true
            }
        }
    }

    private fun registerUser(){
        //Check with validate function if the entries are valid or not.
        if(validateRegisterDetails()){
            //showProgressDialog(resources.getString(R.string.please_wait))
            val email: String = et_email.text.toString().trim()
            val password: String = et_password.text.toString().trim()

            //create an instance and create a register a user with email and password.
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->
                        //hideProgressDialog()
                        //If the registration is successfully done
                        if(task.isSuccessful){
                            //Firebase registered user
                            val firebaseUser: FirebaseUser = task.result!!.user!!

                            val user = User(
                                firebaseUser.uid,
                                et_first_name.text.toString().trim(),
                                et_last_name.text.toString().trim(),
                                et_email.text.toString().trim()
                            )
                            /*showErrorSnackBar(
                                "You are registered successfully. Your user id is ${firebaseUser.uid}",
                                false
                            )*/
                            FirestoreClass().registerUser(this,user)
                            //FirebaseAuth.getInstance().signOut()
                            //finish()
                        }else{
                            //If the registering is not successful then show error message.
                            showErrorSnackBar(task.exception!!.message.toString(),true)
                        }
                    }
                )
        }
    }

    fun userRegistrationSuccess(){
        //Hide the progress dialog
        //hideProgressDialog()

        Toast.makeText(
            this,
            resources.getString(R.string.register_success),
            Toast.LENGTH_SHORT
        )
    }
}