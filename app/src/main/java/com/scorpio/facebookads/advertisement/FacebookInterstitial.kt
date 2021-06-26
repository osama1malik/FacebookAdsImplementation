package com.scorpio.facebookads.advertisement

import android.content.Context
import android.util.Log
import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.facebook.ads.InterstitialAd
import com.facebook.ads.InterstitialAdListener

class FacebookInterstitial {
    val TAG = "FacebookInterstitial"

    var interstitialAd: InterstitialAd? = null
    public fun loadAndShowInterstitial(
        context: Context,
        adId: String,
        dismissCallback: () -> Unit
    ) {
        LoadingDialog.showLoadingDialog(context)

        if (interstitialAd == null) {
            interstitialAd = InterstitialAd(context, adId)
        }
        val interstitialAdListener: InterstitialAdListener = object : InterstitialAdListener {
            override fun onInterstitialDisplayed(ad: Ad) {
                Log.e(TAG, "Interstitial ad displayed.")
            }

            override fun onInterstitialDismissed(ad: Ad) {
                Log.e(TAG, "Interstitial ad dismissed.")
                dismissCallback()
                interstitialAd?.destroy()
                interstitialAd = null

            }

            override fun onError(ad: Ad, adError: AdError) {
                Log.e(TAG, "Interstitial ad failed to load: " + adError.errorMessage)
                dismissCallback()
            }

            override fun onAdLoaded(ad: Ad) {
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!")
                LoadingDialog.hideLoadingDialog()
                (ad as InterstitialAd).show()
            }

            override fun onAdClicked(ad: Ad) {
                Log.d(TAG, "Interstitial ad clicked!")
            }

            override fun onLoggingImpression(ad: Ad) {
                Log.d(TAG, "Interstitial ad impression logged!")
            }
        }

        if (!interstitialAd?.isAdLoaded!!) {
            interstitialAd?.loadAd(
                interstitialAd?.buildLoadAdConfig()!!
                    .withAdListener(interstitialAdListener)
                    .build()
            )
        }
    }

}