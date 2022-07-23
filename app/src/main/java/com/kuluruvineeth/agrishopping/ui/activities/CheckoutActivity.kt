package com.kuluruvineeth.agrishopping.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuluruvineeth.agrishopping.R
import com.kuluruvineeth.agrishopping.firestore.FirestoreClass
import com.kuluruvineeth.agrishopping.models.Address
import com.kuluruvineeth.agrishopping.models.CartItem
import com.kuluruvineeth.agrishopping.models.Order
import com.kuluruvineeth.agrishopping.models.Product
import com.kuluruvineeth.agrishopping.ui.adapters.CartItemsListAdapter
import com.kuluruvineeth.agrishopping.utils.Constants
import kotlinx.android.synthetic.main.activity_cart_list.*
import kotlinx.android.synthetic.main.activity_checkout.*

class CheckoutActivity : BaseActivity() {

    private var mAddressDetails: Address? = null
    private lateinit var mProductsList: ArrayList<Product>
    private lateinit var mCartItemsList: ArrayList<CartItem>
    private var mSubTotal: Double = 0.0
    private var mTotalAmount: Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        setupActionBar()

        if(intent.hasExtra(Constants.EXTRA_SELECTED_ADDRESS)){
            mAddressDetails = intent.getParcelableExtra(Constants.EXTRA_SELECTED_ADDRESS)
        }

        if(mAddressDetails != null){
            tv_checkout_address_type.text = mAddressDetails?.type
            tv_checkout_full_name.text = mAddressDetails?.name
            tv_checkout_address.text = "${mAddressDetails!!.address}, ${mAddressDetails!!.zipCode}"
            tv_checkout_additional_note.text = mAddressDetails?.additionalNote

            if(mAddressDetails?.otherDetails!!.isNotEmpty()){
                tv_checkout_other_details.text = mAddressDetails?.otherDetails
            }
            tv_mobile_number.text = mAddressDetails?.mobileNumber
        }
        getProductsList()

        btn_place_order.setOnClickListener {
            placeAndOrder()
        }
    }

    fun orderPlacedSuccess(){
        //hideProgressDialog()
        Toast.makeText(this,"Your order was place successfully",Toast.LENGTH_SHORT)
            .show()
        val intent = Intent(this,DashboardActivity2::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    fun successProductListFromFireStore(productsList: ArrayList<Product>){
        mProductsList = productsList
        getCartItemsList()
    }

    private fun getCartItemsList(){
        FirestoreClass().getCartList(this@CheckoutActivity)
    }

    private fun placeAndOrder(){
        //showProgressDialog(resources.getString(R.string.please_wait))
        if(mAddressDetails != null){
            val order = Order(
                FirestoreClass().getCurrentUserID(),
                mCartItemsList,
                mAddressDetails!!,
                "My order ${System.currentTimeMillis()}",
                mCartItemsList[0].image,
                mSubTotal.toString(),
                "30.0",
                mTotalAmount.toString()
            )
            FirestoreClass().placeOrder(this,order)
        }
    }

    fun successCartItemsList(cartList: ArrayList<CartItem>){
        //hideProgressDialog()
        for(product in mProductsList){
            for(cart in cartList){
                if(product.product_id == cart.product_id){
                    cart.stock_quantity = product.stock_quantity
                }
            }
        }
        mCartItemsList = cartList

        rv_cart_list_items.layoutManager = LinearLayoutManager(this@CheckoutActivity)
        rv_cart_list_items.setHasFixedSize(true)

        val cartListAdapter = CartItemsListAdapter(this@CheckoutActivity,mCartItemsList,false)
        rv_cart_list_items.adapter = cartListAdapter

        for(item in mCartItemsList){
            val availableQuantity = item.stock_quantity.toInt()
            if(availableQuantity > 0){
                val price = item.price.toDouble()
                val quantity = item.cart_quantity.toInt()
                mSubTotal += (price * quantity)
            }
        }
        tv_checkout_sub_total.text = "Rs$mSubTotal"
        tv_checkout_shipping_charge.text = "Rs30.0"

        if(mSubTotal > 0){
            ll_checkout_place_order.visibility = View.VISIBLE

            mTotalAmount = mSubTotal + 30.0
            tv_checkout_total_amount.text = "Rs$mTotalAmount"
        }else{
            ll_checkout_place_order.visibility = View.GONE
        }
    }

    private fun getProductsList(){
        //showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getAllProductsList(this@CheckoutActivity)
    }

    private fun setupActionBar(){
        setSupportActionBar(toolbar_checkout_activity)

        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }
        toolbar_checkout_activity.setNavigationOnClickListener { onBackPressed() }

    }
}