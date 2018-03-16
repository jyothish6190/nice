package com.nice.nice.forgot

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.nice.nice.R
import com.nice.nice.login.LoginActivity
import kotlinx.android.synthetic.main.activity_forgot.*


class ForgotActivity : AppCompatActivity(), View.OnClickListener {
    private var auth: FirebaseAuth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot)

        auth = FirebaseAuth.getInstance()

        btn_back.setOnClickListener(this)
        btn_reset_password.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.btn_back -> login()
            R.id.btn_reset_password -> forgot(view)
        }
    }

    private fun forgot(view: View){
        val email = email.text.toString().trim()

        if (TextUtils.isEmpty(email)) {
            showMessage(view, "Enter your registered email id")
            return
        }

        progressBar.visibility = View.VISIBLE

        auth?.sendPasswordResetEmail(email)?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                       showMessage(view, "We have sent you instructions to reset your password. Please check email!")
                        LoginActivity.newIntent(this)
                    } else {
                        showMessage(view, "Failed to send reset email!")
                    }
                }

    }

    private fun login(){
        startActivity(LoginActivity.newIntent(this))
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, ForgotActivity::class.java)
        }
    }

    private fun showMessage(view:View, message: String){
        progressBar.visibility = View.INVISIBLE
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction("Action", null).show()
    }

}
