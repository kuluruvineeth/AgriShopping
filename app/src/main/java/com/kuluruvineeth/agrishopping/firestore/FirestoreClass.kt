package com.kuluruvineeth.agrishopping.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.kuluruvineeth.agrishopping.models.User
import com.kuluruvineeth.agrishopping.ui.activities.*
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
}