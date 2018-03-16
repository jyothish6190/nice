package com.nice.nice.splash

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.nice.nice.HomeActivity
import com.nice.nice.R
import com.nice.nice.chat.ChatListActivity
import com.nice.nice.login.LoginActivity
import com.nice.nice.register.RegisterActivity


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class SplashActivity : AppCompatActivity(), View.OnClickListener {
    private var auth: FirebaseAuth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        setContentView(R.layout.activity_splash)

        ChatListActivity.open(this)

//        isloggedIn()
//
//        registerBtn.setOnClickListener(this)
//        loginBtn.setOnClickListener(this)


    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.registerBtn -> register()
            R.id.loginBtn -> login()
        }


    }


    private fun isloggedIn(){
        auth = FirebaseAuth.getInstance()
        if (auth?.currentUser != null) {
            startActivity(HomeActivity.newIntent(this, auth?.currentUser))
            finish()
        }

    }

    private fun register() {
        startActivity(RegisterActivity.newIntent(this))

    }

    private fun login() {
        startActivity(LoginActivity.newIntent(this))
    }


}
