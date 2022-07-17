package com.kuluruvineeth.agrishopping.activities

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.kuluruvineeth.agrishopping.R
import com.kuluruvineeth.agrishopping.models.User
import com.kuluruvineeth.agrishopping.utils.Constants

class UserProfileActivity : BaseActivity(), View.OnClickListener {
    private lateinit var iv_user_photo : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        var et_first_name = findViewById<EditText>(R.id.et_first_name)
        var et_last_name = findViewById<EditText>(R.id.et_last_name)
        var et_email = findViewById<EditText>(R.id.et_email)
        iv_user_photo = findViewById<ImageView>(R.id.iv_user_photo)
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

        iv_user_photo.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if(v!=null){
            when(v.id){
                R.id.iv_user_photo -> {
                    // Here we will check if the permission is already allowed or we need to request for it.
                    // First of all we will check the READ_EXTERNAL_STORAGE permission and if it is not allowed we will request for the same.
                    if(ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                        == PackageManager.PERMISSION_GRANTED
                    ){
                        showErrorSnackBar("You already have the storage permission.",false)
                    }else{
                        /*Requests permissions to be granted to this application. These permissions
                        must be requested in manifest file, they should not be granted to your app,
                        and they should have protection level
                         */
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == Constants.READ_STORAGE_PERMISSION_CODE){
            //If permission is granted
            if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                showErrorSnackBar("The storage permission is granted.",false)
            }else{
                //Displaying another toast if permission is not granted
                Toast.makeText(
                    this,
                    resources.getString(R.string.read_storage_permission_denied),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}