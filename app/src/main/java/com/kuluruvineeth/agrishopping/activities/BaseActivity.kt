package com.kuluruvineeth.agrishopping.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.kuluruvineeth.agrishopping.R

open class BaseActivity : AppCompatActivity() {
    fun showErrorSnackBar(message: String,errorMessage: Boolean){
        val snackbar =
            Snackbar.make(findViewById(android.R.id.content),message,Snackbar.LENGTH_LONG)
        val snackBarView = snackbar.view

        if(errorMessage){
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.colorSnackBarError
                )
            )
        }else{
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.colorSnackBarSuccess
                )
            )
        }
        snackbar.show()
    }
}