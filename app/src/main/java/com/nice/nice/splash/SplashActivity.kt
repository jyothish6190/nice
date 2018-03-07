package com.nice.nice.splash

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.nice.nice.R
import com.nice.nice.login.LoginActivity
import com.nice.nice.register.RegisterActivity
import kotlinx.android.synthetic.main.activity_splash.*

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class SplashActivity : AppCompatActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        setContentView(R.layout.activity_splash)

        registerBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);


    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.registerBtn -> register()
            R.id.loginBtn -> login()
        }


    }

    fun register() {
        startActivity(RegisterActivity.newIntent(this))

    }

    fun login() {
        startActivity(LoginActivity.newIntent(this))
    }


}
