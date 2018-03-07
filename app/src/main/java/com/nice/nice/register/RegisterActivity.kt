package com.nice.nice.register

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.nice.nice.R
import com.nice.nice.login.LoginActivity
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerBtn.setOnClickListener(this)
        loginBtn.setOnClickListener(this)
        uploadBtn.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.registerBtn -> register()
            R.id.loginBtn -> login()
            R.id.uploadBtn -> upload()
        }
    }

    fun register(){

    }

    fun login(){
        startActivity(LoginActivity.newIntent(this))
    }

    fun upload() {

    }

    companion object {
        fun newIntent(context: Context): Intent {
            val intent = Intent(context, RegisterActivity::class.java)
            return intent
        }
    }
}
