package com.kuluruvineeth.agrishopping.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.kuluruvineeth.agrishopping.R
import com.kuluruvineeth.agrishopping.firestore.FirestoreClass
import com.kuluruvineeth.agrishopping.models.Address
import kotlinx.android.synthetic.main.activity_address_list.*

class AddressListActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_list)
        setupActionBar()
        tv_add_address.setOnClickListener{
            val intent = Intent(this,AddEditAddressActivity::class.java)
            startActivity(intent)
        }
        getAddressList()
    }

    private fun getAddressList(){
        //showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getAddressesList(this)
    }

    fun successAddressListFromFirestore(addressList: ArrayList<Address>){
        //hideProgressDialog()
        for(i in addressList){
            Log.i("Name and Address","${i.name} :: ${i.address}")
        }
    }
    private fun setupActionBar(){
        setSupportActionBar(toolbar_address_list_activity)

        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }
        toolbar_address_list_activity.setNavigationOnClickListener { onBackPressed() }
    }
}