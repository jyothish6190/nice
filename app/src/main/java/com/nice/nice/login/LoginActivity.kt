package com.nice.nice.login

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import com.nice.nice.R
import com.nice.nice.forgot.ForgotActivity
import com.nice.nice.register.RegisterActivity
import kotlinx.android.synthetic.main.activity_login.*
import android.widget.Toast
import android.text.TextUtils
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.nice.nice.HomeActivity
import com.nice.nice.utils.StringUtils


class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        registerBtn.setOnClickListener(this)
        loginBtn.setOnClickListener(this)
        forgotBtn.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.loginBtn ->  login(view)
            R.id.registerBtn -> register()
            R.id.forgotBtn -> forgot()
        }
    }

    private fun forgot(){
        startActivity(ForgotActivity.newIntent(this))
    }

    private fun register(){
        startActivity(RegisterActivity.newIntent(this))
    }


    private fun login(view: View){
        val email = email.text.toString()
        val password = password.text.toString()

        if (TextUtils.isEmpty(email) && StringUtils.isEmailValid(email)) {
            Toast.makeText(applicationContext, "Enter email address!", Toast.LENGTH_SHORT).show()
            return
        }

        if (TextUtils.isEmpty(password) || password.length < 6) {
            Toast.makeText(applicationContext, "Enter password!", Toast.LENGTH_SHORT).show()
            return
        }

        progressBar.visibility = View.VISIBLE

        auth?.signInWithEmailAndPassword(email, password)?.addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
            if(task.isSuccessful){
                progressBar.visibility = View.INVISIBLE
                startActivity(HomeActivity.newIntent(this, auth?.currentUser))

            }else{
                showMessage(view,"Error: ${task.exception?.message}")
            }
        })



    }

    fun showMessage(view:View, message: String){
        progressBar.visibility = View.INVISIBLE
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction("Action", null).show()
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }

}
