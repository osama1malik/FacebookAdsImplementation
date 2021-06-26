package com.scorpio.facebookads

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.scorpio.facebookads.advertisement.FacebookInterstitial
import com.scorpio.facebookads.advertisement.FacebookNativeBanner
import com.scorpio.facebookads.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    var binding: ActivityMainBinding ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        findViewById<Button>(R.id.show_ad).setOnClickListener {
            FacebookInterstitial().loadAndShowInterstitial(
                this,
                "CAROUSEL_IMG_SQUARE_LINK#YOUR_PLACEMENT_ID"
            ) {}
        }

        FacebookNativeBanner(this).loadNativeBannerAd(binding?.layoutNativeAds?.fbNativeContainer!!, binding!!.layoutNativeAds.parentNativeContainer, "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID")
    }
}