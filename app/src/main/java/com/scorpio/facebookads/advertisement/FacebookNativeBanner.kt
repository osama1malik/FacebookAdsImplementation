package com.scorpio.facebookads.advertisement

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.facebook.ads.*
import com.scorpio.facebookads.R
import com.scorpio.facebookads.databinding.FbNativeBannerBinding


class FacebookNativeBanner(private val context: Context) {

    companion object {
        const val TAG = "NativeBannerAd"
        var fbNativeBannerAd: NativeBannerAd? = null
    }

    //------------------------- FACEBOOK ADS --------------------------
    fun loadNativeBannerAd(
        nativeAdLayout: NativeAdLayout,
        nativeContainer: ConstraintLayout,
        adId: String
    ) {
        if (fbNativeBannerAd == null) {
            Log.i(TAG, "Fb Native Banner Ad is null")
            fbNativeBannerAd =
                NativeBannerAd(context, adId) //"YOUR_PLACEMENT_ID" use for using test ads.

        }

        if (!fbNativeBannerAd?.isAdLoaded!!) {
            val nativeAdListener: NativeAdListener = object : NativeAdListener {
                override fun onMediaDownloaded(ad: Ad) {}
                override fun onError(ad: Ad, adError: AdError) {
                    Log.i(TAG, "Fb Native Banner Ad Load error: " + adError.errorMessage)
                    fbNativeBannerAd = null
                    nativeContainer.visibility = View.GONE
                }

                override fun onAdLoaded(ad: Ad) {
                    Log.i(TAG, "Fb Native Banner Ad loaded")
                    if ((context as Activity).window.decorView.rootView.isShown) {
                        Log.i(TAG, "Screen Visible")
                        fbNativeBannerAd = (ad as NativeBannerAd)
                        inflateAd(fbNativeBannerAd!!, nativeContainer, nativeAdLayout)
                        fbNativeBannerAd = null
                    }
                }

                override fun onAdClicked(ad: Ad) {}
                override fun onLoggingImpression(ad: Ad) {}
            }

            //Load native ad
            fbNativeBannerAd?.loadAd(
                fbNativeBannerAd?.buildLoadAdConfig()!!
                    .withAdListener(nativeAdListener)
                    .build()
            )
            Log.i(TAG, "Fb Native Banner Ad load request")
        } else {
            Log.i(TAG, "Fb Native Banner Ad already loaded")
            inflateAd(fbNativeBannerAd!!, nativeContainer, nativeAdLayout)
            fbNativeBannerAd = null
        }
    }

    private fun inflateAd(
        nativeBannerAd: NativeBannerAd,
        nativeLayout: ConstraintLayout,
        nativeAdLayout: NativeAdLayout
    ) {

        val textView = nativeLayout.findViewById<TextView>(R.id.loading_ad)
        if (textView != null) textView.visibility = View.GONE

        // Unregister last ad
        nativeBannerAd.unregisterView()

        // Add the Ad view into the ad container.
        val inflater = LayoutInflater.from(context)
        // Inflate the Ad view.  The layout referenced is the one you created in the last step.
        val binding: FbNativeBannerBinding = FbNativeBannerBinding.inflate(inflater)
        nativeAdLayout.addView(binding.root)

        // Add the AdChoices icon
        val adOptionsView = AdOptionsView(context, nativeBannerAd, nativeAdLayout)
        binding.adChoicesContainer.removeAllViews()
        binding.adChoicesContainer.addView(adOptionsView, 0)

        // Set the Text.
        binding.nativeAdCallToAction.text = nativeBannerAd.adCallToAction
        binding.nativeAdCallToAction.visibility =
            if (nativeBannerAd.hasCallToAction()) View.VISIBLE else View.INVISIBLE
        binding.nativeAdTitle.text = nativeBannerAd.advertiserName
        binding.nativeAdSocialContext.text = nativeBannerAd.adSocialContext
        binding.nativeAdSponsoredLabel.text = nativeBannerAd.sponsoredTranslation

        // Register the Title and CTA button to listen for clicks.
        val clickableViews: MutableList<View> = ArrayList()
        clickableViews.add(binding.nativeAdTitle)
        clickableViews.add(binding.nativeAdCallToAction)
        nativeBannerAd.registerViewForInteraction(
            binding.root,
            binding.nativeIconView,
            clickableViews
        )
    }

}