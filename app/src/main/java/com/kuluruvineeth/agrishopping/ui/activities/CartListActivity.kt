package com.kuluruvineeth.agrishopping.ui.activities

import android.os.Bundle
import android.util.Log
import com.kuluruvineeth.agrishopping.R
import com.kuluruvineeth.agrishopping.firestore.FirestoreClass
import com.kuluruvineeth.agrishopping.models.CartItem
import kotlinx.android.synthetic.main.activity_cart_list.*

class CartListActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_list)
        setupActionBar()
    }

    fun successCartItemsList(cartList: ArrayList<CartItem>){
        //hideProgressDialog()
        for(i in cartList){
            Log.i("Cart Item Title",i.title)
        }
    }

    private fun getCartItemsList(){
        //showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getCartList(this)
    }

    override fun onResume() {
        super.onResume()
        getCartItemsList()
    }

    private fun setupActionBar(){
        setSupportActionBar(toolbar_cart_list_activity)

        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }
        toolbar_cart_list_activity.setNavigationOnClickListener { onBackPressed() }

    }
}