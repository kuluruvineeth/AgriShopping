package com.kuluruvineeth.agrishopping.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuluruvineeth.agrishopping.R
import com.kuluruvineeth.agrishopping.firestore.FirestoreClass
import com.kuluruvineeth.agrishopping.models.CartItem
import com.kuluruvineeth.agrishopping.models.Product
import com.kuluruvineeth.agrishopping.ui.adapters.CartItemsListAdapter
import com.kuluruvineeth.agrishopping.utils.Constants
import kotlinx.android.synthetic.main.activity_cart_list.*

class CartListActivity : BaseActivity() {

    private lateinit var mProductsList: ArrayList<Product>
    private lateinit var mCartListItems: ArrayList<CartItem>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_list)
        setupActionBar()

        btn_checkout.setOnClickListener {
            val intent = Intent(this,AddressListActivity::class.java)
            intent.putExtra(Constants.EXTRA_SELECT_ADDRESS,true)
            startActivity(intent)
        }
    }

    fun successCartItemsList(cartList: ArrayList<CartItem>){
        //hideProgressDialog()
        for(product in mProductsList){
            for(cart in cartList){
                if(product.product_id == cart.product_id){
                    cart.stock_quantity = product.stock_quantity
                    if(product.stock_quantity.toInt() == 0){
                        cart.cart_quantity = product.stock_quantity
                    }
                }
            }
        }
        mCartListItems = cartList
        if(mCartListItems.size > 0){
            rv_cart_items_list.visibility = View.VISIBLE
            ll_checkout.visibility = View.VISIBLE
            tv_no_cart_item_found.visibility = View.GONE

            rv_cart_items_list.layoutManager = LinearLayoutManager(this)
            rv_cart_items_list.setHasFixedSize(true)
            val cartListAdapter = CartItemsListAdapter(this,cartList)
            rv_cart_items_list.adapter = cartListAdapter
            var subTotal: Double = 0.0
            for(item in mCartListItems){
                val availableQuantity = item.stock_quantity.toInt()
                if(availableQuantity > 0){
                    val price = item.price.toDouble()
                    val quantity = item.cart_quantity.toInt()
                    subTotal += (price * quantity)
                }
            }
            tv_sub_total.text = "$subTotal"
            tv_shipping_charge.text = "Rs.30.0" //TODO change shipping charge logic

            if(subTotal > 0){
                ll_checkout.visibility = View.VISIBLE

                val total = subTotal + 10 //TODO - Change Logic here
                tv_total_amount.text = "$total"
            }else{
                ll_checkout.visibility = View.GONE
            }
        }
        else{
            rv_cart_items_list.visibility = View.GONE
            ll_checkout.visibility = View.GONE
            tv_no_cart_item_found.visibility = View.VISIBLE
        }
    }

    fun successProductsListFromFireStore(productsList: ArrayList<Product>){
        //hideProgressDialog()
        mProductsList = productsList
        getCartItemsList()
    }

    private fun getProductsList(){
        //showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getAllProductsList(this)
    }

    private fun getCartItemsList(){
        //showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getCartList(this)
    }

    fun itemUpdateSuccess(){
        //hideProgressDialog()
        getCartItemsList()
    }

    override fun onResume() {
        super.onResume()
        //getCartItemsList()
        getProductsList()
    }

    fun itemRemovedSuccess(){
        //hideProgressDialog()
        Toast.makeText(
            this,
            resources.getString(R.string.msg_item_removed_successfully),
            Toast.LENGTH_SHORT
        ).show()
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