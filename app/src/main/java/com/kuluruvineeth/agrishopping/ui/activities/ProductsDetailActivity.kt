package com.kuluruvineeth.agrishopping.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.kuluruvineeth.agrishopping.R
import com.kuluruvineeth.agrishopping.firestore.FirestoreClass
import com.kuluruvineeth.agrishopping.models.CartItem
import com.kuluruvineeth.agrishopping.models.Product
import com.kuluruvineeth.agrishopping.utils.Constants
import com.kuluruvineeth.agrishopping.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_products_detail.*

class ProductsDetailActivity : BaseActivity(),View.OnClickListener {

    private var mProductId: String = ""
    private lateinit var mProductDetails: Product
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products_detail)
        setupActionBar()

        if(intent.hasExtra(Constants.EXTRA_PRODUCT_ID)){
            mProductId = intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
            Log.i("Product Id",mProductId)
            getProductDetails()
        }
        var productOwnerId : String = ""
        if(intent.hasExtra(Constants.EXTRA_PRODUCT_OWNER_ID)){
            productOwnerId = intent.getStringExtra(Constants.EXTRA_PRODUCT_OWNER_ID)!!
        }
        if(FirestoreClass().getCurrentUserID() == productOwnerId){
            btn_add_to_cart.visibility = View.GONE
        }else{
            btn_add_to_cart.visibility = View.VISIBLE
        }
        btn_add_to_cart.setOnClickListener(this)
    }

    private fun getProductDetails(){
        //showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getProductDetails(this,mProductId)
    }
    fun productDetailsSuccess(product: Product){
        mProductDetails = product
        //hideProgressDialog()
        GlideLoader(this).loadProductPicture(
            product.image,
            iv_product_detail_image
        )
        tv_product_details_title.text = product.title
        tv_product_details_price.text = "${product.price}"
        tv_product_details_description.text = product.description
        tv_product_details_available_quantity.text = product.stock_quantity
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

    private fun addToCart(){
        val addToCart = CartItem(
            FirestoreClass().getCurrentUserID(),
            mProductId,
            mProductDetails.title,
            mProductDetails.price,
            mProductDetails.image,
            Constants.DEFAULT_CART_QUANTITY
        )
    }

    override fun onClick(v: View?) {
        if(v!=null){
            when(v.id){
                R.id.btn_add_to_cart -> {
                    addToCart()
                }
            }
        }
    }
}