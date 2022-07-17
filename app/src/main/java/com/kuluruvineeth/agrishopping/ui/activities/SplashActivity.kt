package com.kuluruvineeth.agrishopping.ui.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowInsets
import android.view.WindowManager
import com.kuluruvineeth.agrishopping.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        //val tv_app_name = findViewById<TextView>(R.id.tv_app_name)
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        @Suppress("DEPRECATION")
        Handler().postDelayed(
            {
                //Launch the Main Activity
                startActivity(Intent(this, DashboardActivity2::class.java))
                finish() // call this when your activity is done and should be closed
            },
            2500
        )

        //val typeface: Typeface = Typeface.createFromAsset(assets,"Montserrat-Bold.ttf")
        //tv_app_name.typeface = typeface
    }
}