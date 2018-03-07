package com.nice.nice.forgot

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.nice.nice.R
import com.nice.nice.login.LoginActivity
import kotlinx.android.synthetic.main.activity_forgot.*

class ForgotActivity : AppCompatActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot)

        btn_back.setOnClickListener(this)
        btn_reset_password.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.btn_back -> login()
            R.id.btn_reset_password -> forgot()
        }
    }

    fun forgot(){

    }

    fun login(){
        startActivity(LoginActivity.newIntent(this))
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, ForgotActivity::class.java)
        }
    }


}
