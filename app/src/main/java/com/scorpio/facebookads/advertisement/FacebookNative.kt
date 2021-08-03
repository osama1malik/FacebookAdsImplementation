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
import com.scorpio.facebookads.databinding.FbNativeBinding


class FacebookNative(private val context: Context) {

    private val TAG = "TestingTag"

    private var nativeAd: NativeAd? = null

    fun loadNativeAd(nativeAdLayout: NativeAdLayout, nativeContainer: ConstraintLayout, adId: String) {
        if (nativeAd == null) {
            Log.i(FacebookNativeBanner.TAG, "Fb Native Ad is null")
            nativeAd = NativeAd(context, adId)
        }

        if (nativeAd?.isAdLoaded == false) {
            val nativeAdListener: NativeAdListener = object : NativeAdListener {
                override fun onMediaDownloaded(ad: Ad) {
                    // Native ad finished downloading all assets
                    Log.e(TAG, "Native ad finished downloading all assets.")
                }

                override fun onError(ad: Ad, adError: AdError) {
                    // Native ad failed to load
                    Log.e(TAG, "Native ad failed to load: " + adError.errorMessage)
                    nativeAd = null
                    nativeContainer.visibility = View.GONE
                }

                override fun onAdLoaded(ad: Ad) {
                    // Native ad is loaded and ready to be displayed
                    Log.d(TAG, "Native ad is loaded and ready to be displayed!")
                    if ((context as Activity).window.decorView.rootView.isShown) {
                        Log.i(FacebookNativeBanner.TAG, "Screen Visible")
                        nativeAd = (ad as NativeAd)
                        inflateAd(nativeAd!!, nativeContainer, nativeAdLayout)
                        nativeAd = null
                    }
                }

                override fun onAdClicked(ad: Ad) {
                    // Native ad clicked
                    Log.d(TAG, "Native ad clicked!")
                }

                override fun onLoggingImpression(ad: Ad) {
                    // Native ad impression
                    Log.d(TAG, "Native ad impression logged!")
                }
            }

            // Request an ad
            AdSettings.addTestDevice("52d4197f-1c5b-4235-a7d6-f0bd0e8ce289")
            nativeAd!!.loadAd(
                nativeAd!!.buildLoadAdConfig()
                    .withAdListener(nativeAdListener)
                    .build()
            )
            Log.i(TAG, "Fb Native Ad load request")
        } else {
            Log.i(TAG, "Fb Native Banner Ad already loaded")
            inflateAd(nativeAd!!, nativeContainer, nativeAdLayout)
            nativeAd = null
        }
    }

    private fun inflateAd(
        nativeAd: NativeAd,
        nativeLayout: ConstraintLayout,
        nativeAdLayout: NativeAdLayout
    ) {

        val textView = nativeLayout.findViewById<TextView>(R.id.loading_ad)
        if (textView != null) textView.visibility = View.GONE

        // Unregister last ad
        nativeAd.unregisterView()

        // Add the Ad view into the ad container.
        val inflater = LayoutInflater.from(context)
        // Inflate the Ad view.  The layout referenced is the one you created in the last step.
        val binding: FbNativeBinding = FbNativeBinding.inflate(inflater)
        nativeAdLayout.addView(binding.root)

        // Add the AdChoices icon
        val adOptionsView = AdOptionsView(context, nativeAd, nativeAdLayout)
        binding.adChoicesContainer.removeAllViews()
        binding.adChoicesContainer.addView(adOptionsView, 0)

        // Set the Text.
        binding.nativeAdCallToAction.text = nativeAd.adCallToAction
        binding.nativeAdCallToAction.visibility =
            if (nativeAd.hasCallToAction()) View.VISIBLE else View.INVISIBLE
        binding.nativeAdTitle.text = nativeAd.advertiserName
        binding.nativeAdSocialContext.text = nativeAd.adSocialContext
        binding.nativeAdSponsoredLabel.text = nativeAd.sponsoredTranslation
        binding.nativeAdBody.text = nativeAd.adBodyText


        // Register the Title and CTA button to listen for clicks.
        val clickableViews: MutableList<View> = ArrayList()
        clickableViews.add(binding.nativeAdTitle)
        clickableViews.add(binding.nativeAdCallToAction)
        clickableViews.add(binding.nativeAdBody)
        nativeAd.registerViewForInteraction(
            binding.root, binding.nativeMediaView, binding.nativeIconView, clickableViews
        );
    }

}