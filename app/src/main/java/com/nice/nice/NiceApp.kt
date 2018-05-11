package com.nice.nice

import android.support.multidex.MultiDexApplication
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.nice.nice.config.ForceUpdateChecker


/**
 * Created by jyothish on 4/28/18.
 */
class NiceApp : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        setUpRemoteConfig()



    }


    private fun setUpRemoteConfig(){
        val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

        // set in-app defaults
        val remoteConfigDefaults = HashMap<String, Any>()
        remoteConfigDefaults[ForceUpdateChecker.KEY_UPDATE_REQUIRED] = false
        remoteConfigDefaults[ForceUpdateChecker.KEY_CURRENT_VERSION] = "1.0.0"
        remoteConfigDefaults[ForceUpdateChecker.KEY_UPDATE_URL] = "https://play.google.com/store/apps/details?id=com.nice.nice"

        firebaseRemoteConfig.setDefaults(remoteConfigDefaults)

        firebaseRemoteConfig.fetch(0)
                .addOnCompleteListener({ task ->
                    if (task.isSuccessful) {
                        firebaseRemoteConfig.activateFetched()
                    }
                })


    }



}