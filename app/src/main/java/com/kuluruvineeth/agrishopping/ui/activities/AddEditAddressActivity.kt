package com.kuluruvineeth.agrishopping.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.kuluruvineeth.agrishopping.R
import com.kuluruvineeth.agrishopping.firestore.FirestoreClass
import com.kuluruvineeth.agrishopping.models.Address
import com.kuluruvineeth.agrishopping.utils.Constants
import kotlinx.android.synthetic.main.activity_add_edit_address.*

class AddEditAddressActivity : BaseActivity() {

    private var mAddressDetails: Address? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_address)
        setupActionBar()

        if(intent.hasExtra(Constants.EXTRA_ADDRESS_DETAILS)){
            mAddressDetails = intent.getParcelableExtra(Constants.EXTRA_ADDRESS_DETAILS)
        }

        btn_submit_address.setOnClickListener {
            saveAddressToFirestore()
        }
        rg_type.setOnCheckedChangeListener { _, checkedId ->
            if(checkedId == R.id.rb_other){
                til_other_details.visibility = View.VISIBLE
            }else{
                til_other_details.visibility = View.GONE
            }
        }
    }
    private fun setupActionBar(){
        setSupportActionBar(toolbar_add_edit_address_activity)

        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }
        toolbar_add_edit_address_activity.setNavigationOnClickListener { onBackPressed() }

    }

    private fun saveAddressToFirestore(){
        val fullName: String = et_full_name.text.toString().trim()
        val phoneNumber: String = et_phone_number.text.toString().trim()
        val address: String = et_address.text.toString().trim()
        val zipCode: String = et_zip_code.text.toString().trim()
        val additionalNote: String = et_additional_note.text.toString().trim()
        val otherDetails: String = et_other_details.text.toString().trim()

        if(validateData()){
            //showProgressDialog(resources.getString(R.string.please_wait))

            val addressType: String = when {
                rb_home.isChecked -> {
                    Constants.HOME
                }
                rb_office.isChecked -> {
                    Constants.OFFICE
                }
                else -> {
                    Constants.OTHER
                }
            }
            val addressModel = Address(
                FirestoreClass().getCurrentUserID(),
                fullName,
                phoneNumber,
                address,
                zipCode,
                additionalNote,
                addressType,
                otherDetails
            )
            FirestoreClass().addAddress(this,addressModel)
        }
    }

    fun addUpdateAddressSuccess(){
       //hideProgressDialog()
        Toast.makeText(
            this,
            resources.getString(R.string.err_your_address_added_successfully),
            Toast.LENGTH_SHORT
        ).show()
        finish()
    }

    private fun validateData(): Boolean {
        return when {
            TextUtils.isEmpty(et_full_name.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_please_enter_full_name),
                    true
                )
                false
            }

            TextUtils.isEmpty(et_phone_number.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_please_enter_phone_number),
                    true
                )
                false
            }

            TextUtils.isEmpty(et_address.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_please_enter_address), true)
                false
            }

            TextUtils.isEmpty(et_zip_code.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_please_enter_zip_code), true)
                false
            }

            rb_other.isChecked && TextUtils.isEmpty(
                et_zip_code.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_please_enter_zip_code), true)
                false
            }
            else -> {
                true
            }
        }
    }
}