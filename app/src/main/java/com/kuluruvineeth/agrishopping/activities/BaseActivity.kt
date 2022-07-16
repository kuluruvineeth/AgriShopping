package com.kuluruvineeth.agrishopping.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.kuluruvineeth.agrishopping.R

open class BaseActivity : AppCompatActivity() {

    private lateinit var mProgressDialog: Dialog
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
    /*
    This function is used to show the progress dialog with the title and message to user
     */
    fun showProgressDialog(text: String){
        mProgressDialog = Dialog(this)

        /*
        Set the screen content from a layout resource.
        The resource will be inflated, adding all top-level views to the screen.
         */
        mProgressDialog.setContentView(R.layout.dialog_progress)
        val tv_progress_text = findViewById<TextView>(R.id.tv_progress_text)
        tv_progress_text.text = text
        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)

        //start the dialog and display it on screen.
        mProgressDialog.show()
    }

    /*
    This function is used to dismiss the progress dialog if it is visible to user.
     */
    fun hideProgressDialog(){
        mProgressDialog.dismiss()
    }
}