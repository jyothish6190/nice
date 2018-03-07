package com.nice.nice

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseUser

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }

    companion object {

        private val INTENT_USER_ID = "user"

        fun newIntent(context: Context, user: FirebaseUser?): Intent {
            val intent = Intent(context, HomeActivity::class.java)
            intent.putExtra(INTENT_USER_ID, user?.uid)
            return intent
        }
    }
}
