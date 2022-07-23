package com.kuluruvineeth.agrishopping.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kuluruvineeth.agrishopping.R
import com.kuluruvineeth.agrishopping.models.SoldProduct
import com.kuluruvineeth.agrishopping.utils.Constants

class SoldProductsDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sold_products_details)

        var productDetails: SoldProduct = SoldProduct()

        if(intent.hasExtra(Constants.EXTRA_SOLD_PRODUCT_DETAILS)){
            productDetails =
                intent.getParcelableExtra<SoldProduct>(Constants.EXTRA_SOLD_PRODUCT_DETAILS)!!

        }
    }
}