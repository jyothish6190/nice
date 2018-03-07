package com.nice.nice.login

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.nice.nice.R
import com.nice.nice.forgot.ForgotActivity
import com.nice.nice.register.RegisterActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        registerBtn.setOnClickListener(this)
        loginBtn.setOnClickListener(this)
        forgotBtn.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.loginBtn ->  login()
            R.id.registerBtn -> register()
            R.id.forgotBtn -> forgot()
        }
    }

    fun forgot(){
        startActivity(ForgotActivity.newIntent(this))
    }

    fun register(){
        startActivity(RegisterActivity.newIntent(this))
    }

    fun login(){

    }

    companion object {
        fun newIntent(context: Context): Intent {
            val intent = Intent(context, LoginActivity::class.java)
            return intent
        }
    }

}
