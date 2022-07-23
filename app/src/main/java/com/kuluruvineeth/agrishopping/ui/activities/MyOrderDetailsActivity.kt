package com.kuluruvineeth.agrishopping.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kuluruvineeth.agrishopping.R
import com.kuluruvineeth.agrishopping.models.Order
import com.kuluruvineeth.agrishopping.utils.Constants
import kotlinx.android.synthetic.main.activity_my_order_details.*

class MyOrderDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_order_details)
        setupActionBar()

        var myOrderDetails: Order
        if(intent.hasExtra(Constants.EXTRA_MY_ORDER_DETAILS)){
            myOrderDetails = intent.getParcelableExtra(Constants.EXTRA_MY_ORDER_DETAILS)!!
        }
    }
    private fun setupActionBar(){
        setSupportActionBar(toolbar_my_order_details_activity)

        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }
        toolbar_my_order_details_activity.setNavigationOnClickListener { onBackPressed() }

    }
}