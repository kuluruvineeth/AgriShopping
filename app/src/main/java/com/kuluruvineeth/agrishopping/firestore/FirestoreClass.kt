package com.kuluruvineeth.agrishopping.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.kuluruvineeth.agrishopping.models.Address
import com.kuluruvineeth.agrishopping.models.CartItem
import com.kuluruvineeth.agrishopping.models.Product
import com.kuluruvineeth.agrishopping.models.User
import com.kuluruvineeth.agrishopping.ui.activities.*
import com.kuluruvineeth.agrishopping.ui.fragments.DashboardFragment
import com.kuluruvineeth.agrishopping.ui.fragments.ProductsFragment
import com.kuluruvineeth.agrishopping.utils.Constants


class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User){
        // The "users" is collection name. If the collection is already created then it will not create the same
        mFireStore.collection(Constants.USERS)
            //Document ID for users fields. Here the document it is the user ID.
            .document(userInfo.id)
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge later
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                //Here call a function of base activity for transferring the result to it
                activity.userRegistrationSuccess()
            }
            .addOnFailureListener{e ->
                //activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering the user.",
                    e
                )
            }
    }

    fun getCurrentUserID(): String{
        //An Instance of currentUser using FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        //A variable to assign the currentUserId if it is not null or else it will be blank.
        var currentUserID = ""
        if(currentUser!=null){
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    fun getUserDetails(activity: Activity){
        //Here we pass the collection name from which we wants the data.
        mFireStore.collection(Constants.USERS)
            //The document id to get the Fields of user
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName,document.toString())

                //Here we have received the document snapshot which is converted into thse User Data model object
                val user = document.toObject(User::class.java)!!
                val sharedPreferences = activity.getSharedPreferences(
                    Constants.AGRISHOP_PREFERENCES,
                    Context.MODE_PRIVATE
                )
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                //Key:Value logged_in_username: Agririze yours
                editor.putString(
                    Constants.LOGGED_IN_USERNAME,
                    "${user.firstName} ${user.lastName}"
                )
                editor.apply()
                when(activity){
                    is LoginActivity -> {
                        //call a function of base activity for transferring the result to it.
                        activity.userLoggedInSuccess(user)
                    }
                    is SettingsActivity -> {
                        activity.userDetailsSuccess(user)
                    }
                }
            }
            .addOnFailureListener{e ->
                //Hide the progress dialog if there is any error. And print the error in log.
                when(activity){
                    is LoginActivity -> {
                        //activity.hideProgressDialog()
                    }
                    is SettingsActivity -> {
                        //activity.hideProgressDialog()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while getting user details.",
                    e
                )
            }
    }

    fun updateUserProfileData(activity: Activity,userHashMap: HashMap<String,Any>){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .update(userHashMap)
            .addOnSuccessListener {
                when(activity){
                    is UserProfileActivity -> {
                        activity.userProfileUpdateSuccess()
                    }
                }
            }
            .addOnFailureListener{e ->
                when(activity){
                    is UserProfileActivity -> {
                        //Hide the progress dialog if there is any error. And print the error in log.
                        //activity.hideProgressDialog()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating the user details",
                    e
                )
            }
    }

    fun uploadImageToCloudStorage(activity: Activity, imageFileURI: Uri?,imageType: String){
        val sRef: StorageReference = FirebaseStorage.getInstance().reference
            .child(
                imageType + System.currentTimeMillis() + "."
                    + Constants.getFileExtension(
                    activity,
                    imageFileURI
                    )
            )
        sRef.putFile(imageFileURI!!)
            .addOnSuccessListener { taskSnapshot ->
                //The image upload is success
                Log.e(
                    "Firebase Image URL",
                    taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                )
                //Get the downloadable url from the task snapshot
                taskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { uri ->
                        Log.e("Downloadable Image URL",uri.toString())
                        //pass the success result to base class
                        when(activity){
                            is UserProfileActivity -> {
                                activity.imageUploadSuccess(uri.toString())
                            }
                            is AddProductActivity -> {
                                activity.imageUploadSuccess(uri.toString())
                            }
                        }
                    }
            }
            .addOnFailureListener{exception ->
                when(activity){
                    is UserProfileActivity -> {
                        //activity.hideProgressDialog()
                    }
                    is AddProductActivity -> {
                        //activity.hideProgressDialog()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    exception.message,
                    exception
                )
            }
    }

    fun uploadProductDetails(activity: AddProductActivity, productInfo: Product){
        mFireStore.collection(Constants.PRODUCTS)
            .document()
            .set(productInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.productUploadSuccess()
            }
            .addOnFailureListener{ e ->
                //activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while uploading the product details",
                    e
                )
            }
    }

    fun getProductsList(fragment: Fragment){
        mFireStore.collection(Constants.PRODUCTS)
            .whereEqualTo(Constants.USER_ID,getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.e("Products List",document.documents.toString())
                val productsList: ArrayList<Product> = ArrayList()
                for(i in document.documents){
                    val product = i.toObject(Product::class.java)
                    product!!.product_id = i.id
                    productsList.add(product)
                }
                when(fragment){
                    is ProductsFragment -> {
                        fragment.successProductsListFromFireStore(productsList)
                    }
                }
            }
    }

    fun getCartList(activity: Activity){
        mFireStore.collection(Constants.CART_ITEMS)
            .whereEqualTo(Constants.USER_ID,getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.e(activity.javaClass.simpleName,document.documents.toString())
                val list: ArrayList<CartItem> = ArrayList()
                for(i in document.documents){
                    val cartItem = i.toObject(CartItem::class.java)
                    cartItem?.id = i.id
                    list.add(cartItem!!)
                }
                when(activity){
                    is CartListActivity -> {
                        activity.successCartItemsList(list)
                    }
                }
            }.addOnFailureListener { e ->
                when(activity){
                    is CartListActivity -> {
                        //activity.hideProgressDialog()
                    }
                }
                Log.e(activity.javaClass.simpleName,"Error while getting the cart list items",e)
            }
    }

    fun addCartItems(activity: ProductsDetailActivity, addToCart: CartItem){
        mFireStore.collection(Constants.CART_ITEMS)
            .document()
            .set(addToCart, SetOptions.merge())
            .addOnSuccessListener {
                activity.addToCartSuccess()
            }.addOnFailureListener {
                e ->
                //activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while creating the document for cart item",
                    e
                )
            }
    }

    fun checkIfItemsExistInCart(activity: ProductsDetailActivity,productId: String){
        mFireStore.collection(Constants.CART_ITEMS)
            .whereEqualTo(Constants.USER_ID,getCurrentUserID())
            .whereEqualTo(Constants.PRODUCT_ID,productId)
            .get()
            .addOnSuccessListener { document ->
                Log.e(activity.javaClass.simpleName,document.documents.toString())
                if(document.documents.size > 0){
                    activity.productExistsInCart()
                }else{
                   // activity.hideProgressDialog()
                }
            }.addOnFailureListener { e ->
                //activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while checking the existing cart list",
                    e
                )
            }
    }

    fun getProductDetails(activity: ProductsDetailActivity,productId: String){
        mFireStore.collection(Constants.PRODUCTS)
            .document(productId)
            .get()
            .addOnSuccessListener { document ->
                Log.e(activity.javaClass.simpleName,document.toString())
                val product = document.toObject(Product::class.java)
                activity.productDetailsSuccess(product!!)
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName,"Error while getting the product details.",e)
            }
    }
    fun deleteProduct(fragment: ProductsFragment,productId: String){
        mFireStore.collection(Constants.PRODUCTS)
            .document(productId)
            .delete()
            .addOnSuccessListener {
                fragment.productDeleteSuccess()
            }
            .addOnFailureListener { e ->
                fragment.hideProgressDialog()
                Log.e(
                    fragment.requireActivity().javaClass.simpleName,
                    "Error while deleting the product",
                    e
                )
            }
    }

    fun getDashboardItemsList(fragment: DashboardFragment){
        mFireStore.collection(Constants.PRODUCTS)
            .get()
            .addOnSuccessListener { document ->
                Log.e(fragment.javaClass.simpleName,document.documents.toString())

                val productsList: ArrayList<Product> = ArrayList()
                for(i in document.documents){
                    val product = i.toObject(Product::class.java)!!
                    product.product_id = i.id
                    productsList.add(product)
                }
                fragment.successDashboardItemsList(productsList)
            }
            .addOnFailureListener{
                e ->
                // Hide the progress dialog if there is any error which getting the dashboard items list.
                fragment.hideProgressDialog()
                Log.e(fragment.javaClass.simpleName, "Error while getting dashboard items list.",e)
            }
    }

    fun getAllProductsList(activity: CartListActivity){
        mFireStore.collection(Constants.PRODUCTS)
            .get()
            .addOnSuccessListener { document ->
                Log.e("Products List",document.documents.toString())
                val productsList: ArrayList<Product> = ArrayList()
                for(i in document.documents){
                    val product = i.toObject(Product::class.java)
                    product!!.product_id = i.id
                    productsList.add(product)
                }
                activity.successProductsListFromFireStore(productsList)
            }.addOnFailureListener { e ->
                //activity.hideProgressDialog()
                Log.e("Get Product List","Error while getting all product list.",e)
            }
    }

    fun removeItemFromCart(context: Context,cart_id: String){
        mFireStore.collection(Constants.CART_ITEMS)
            .document(cart_id)
            .delete()
            .addOnSuccessListener {
                when(context){
                    is CartListActivity -> {
                        context.itemRemovedSuccess()
                    }
                }
            }.addOnFailureListener { e ->
                when(context){
                    is CartListActivity -> {
                        //context.hideProgressDialog()
                    }
                }
                Log.e(
                    context.javaClass.simpleName,
                    "Error while removing the item from the cart list.",
                    e
                )
            }
    }

    fun updateAddress(activity: AddEditAddressActivity,addressInfo: Address,addressId: String){
        mFireStore.collection(Constants.ADDRESSES)
            .document(addressId)
            .set(addressInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.addUpdateAddressSuccess()
            }.addOnFailureListener { e ->
                //activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating address details",
                    e
                )
            }
    }

    fun getAddressesList(activity: AddressListActivity){
        mFireStore.collection(Constants.ADDRESSES)
            .whereEqualTo(Constants.USER_ID,getCurrentUserID())
            .get()
            .addOnSuccessListener {
                document ->
                Log.e(activity.javaClass.simpleName,document.documents.toString())
                val addressList: ArrayList<Address> = ArrayList()
                for(i in document.documents){
                    val address = i.toObject(Address::class.java)!!
                    address.id = i.id
                    addressList.add(address)
                }
                activity.successAddressListFromFirestore(addressList)
            }.addOnFailureListener { e ->
                //activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName,"Error while gettong the addresses",e)
            }
    }

    fun addAddress(activity: AddEditAddressActivity,addressInfo: Address){
        mFireStore.collection(Constants.ADDRESSES)
            .document()
            .set(addressInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.addUpdateAddressSuccess()
            }.addOnFailureListener {
                e ->
                //activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while adding the address.",
                    e
                )
            }
    }

    fun updateMyCart(context: Context, cart_id: String, itemHashMap: HashMap<String,Any>){
        mFireStore.collection(Constants.CART_ITEMS)
            .document(cart_id)
            .update(itemHashMap)
            .addOnSuccessListener {
                when(context){
                    is CartListActivity -> {
                        context.itemUpdateSuccess()
                    }
                }
            }.addOnFailureListener {
                e ->
                when(context){
                    is CartListActivity -> {
                        //context.hideProgressDialog()
                    }
                }
                Log.e(
                    context.javaClass.simpleName,
                    "Error while updating the cart item.",
                    e
                )
            }
    }
}