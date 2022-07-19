package com.kuluruvineeth.agrishopping.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.kuluruvineeth.agrishopping.R
import com.kuluruvineeth.agrishopping.utils.Constants
import com.kuluruvineeth.agrishopping.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_add_product.*
import kotlinx.android.synthetic.main.activity_user_profile.*
import java.io.IOException

class AddProductActivity : BaseActivity(), View.OnClickListener {
    private var mSelectedImageFileURI : Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)
        setupActionBar()
        iv_add_update_product.setOnClickListener(this)
        btn_submit_add_product.setOnClickListener(this)
    }
    private fun setupActionBar(){
        setSupportActionBar(toolbar_add_product_activity)

        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }
        toolbar_add_product_activity.setNavigationOnClickListener { onBackPressed() }

    }

    override fun onClick(v: View?) {
        if(v!=null){
            when(v.id){
                R.id.iv_add_update_product -> {
                    if(ContextCompat.checkSelfPermission(
                            this,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                        == PackageManager.PERMISSION_GRANTED
                    ){
                        Constants.showImageChooser(this)
                    }else{
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }
                R.id.btn_submit_add_product -> {
                    if(validateProductDetails()){
                        showErrorSnackBar("Your product details are valid.",false)
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
                //showErrorSnackBar("The storage permission is granted.",false)
                Constants.showImageChooser(this)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK){
            if(requestCode == Constants.PICK_IMAGE_REQUEST_CODE){
                if(data!=null){
                    iv_add_update_product.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_vector_edit))
                    mSelectedImageFileURI = data.data!!
                    try{
                        GlideLoader(this).loadUserPicture(mSelectedImageFileURI!!,iv_product_image)
                    }catch (e: IOException){
                        e.printStackTrace()
                    }
                }
            }
        }else if(resultCode == Activity.RESULT_CANCELED){
            //A log is printed when user close or cancel the image selection.
            Log.e("Request Cancelled","Image selection cancelled")
        }
    }

    private fun validateProductDetails(): Boolean{
        return when{
            mSelectedImageFileURI == null -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_select_product_image),true)
                false
            }
            TextUtils.isEmpty(et_product_title.text.toString().trim()) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_product_title),true)
                false
            }
            TextUtils.isEmpty(et_product_price.text.toString().trim()) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_product_price),true)
                false
            }
            TextUtils.isEmpty(et_product_price.text.toString().trim()) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_product_price),true)
                false
            }
            TextUtils.isEmpty(et_product_description.text.toString().trim()) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_product_description),true)
                false
            }
            TextUtils.isEmpty(et_product_quantity.text.toString().trim()) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_product_quantity),true)
                false
            }
            else -> {
                true
            }
        }
    }
}