package com.kuluruvineeth.agrishopping.utils

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.kuluruvineeth.agrishopping.R
import java.io.IOException

class GlideLoader(val context: Context) {
    fun loadUserPicture(image: Any, imageView: ImageView){
        try{
            //Load the user image in the ImageView.
            Glide
                .with(context)
                .load(Uri.parse(image.toString())) // URI of the image
                .centerCrop() // Scale type of the image
                .placeholder(R.drawable.ic_user_placeholder) //A default place holder if image is failed to load
                .into(imageView) //the view in which the image will be loaded.
        }catch (e: IOException){
            e.printStackTrace()
        }
    }
}