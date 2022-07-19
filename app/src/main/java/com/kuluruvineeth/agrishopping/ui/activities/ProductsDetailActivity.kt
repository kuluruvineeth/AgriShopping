package com.kuluruvineeth.agrishopping.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.kuluruvineeth.agrishopping.R
import com.kuluruvineeth.agrishopping.utils.Constants
import kotlinx.android.synthetic.main.activity_products_detail.*

class ProductsDetailActivity : AppCompatActivity() {

    private var mProductId: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products_detail)
        setupActionBar()

        if(intent.hasExtra(Constants.EXTRA_PRODUCT_ID)){
            mProductId = intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
            Log.i("Product Id",mProductId)
        }
    }

    private fun setupActionBar(){
        setSupportActionBar(toolbar_product_details_activity)

        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }
        toolbar_product_details_activity.setNavigationOnClickListener { onBackPressed() }

    }
}