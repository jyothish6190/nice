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
import android.util.Log
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.nice.nice.HomeActivity


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

        Log.d(email, password)

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext, "Enter email address!", Toast.LENGTH_SHORT).show()
            return
        }

        if (TextUtils.isEmpty(password) || password.length < 6) {
            Toast.makeText(applicationContext, "Enter password!", Toast.LENGTH_SHORT).show()
            return
        }

        progressBar.visibility = View.VISIBLE

//        //authenticate user
//        auth?.signInWithEmailAndPassword(email, password)?.addOnCompleteListener(this@LoginActivity, OnCompleteListener<AuthResult> { task ->
//            // If sign in fails, display a message to the user. If sign in succeeds
//            // the auth state listener will be notified and logic to handle the
//            // signed in user can be handled in the listener.
//            progressBar.visibility = View.GONE
//            if (!task.isSuccessful) {
//                // there was an error
//                if (password.length < 6) {
////                    password.(getString(R.string.minimum_password))
//                    Toast.makeText(this@LoginActivity, getString(R.string.minimum_password), Toast.LENGTH_LONG).show()
//
//                } else {
//                    Toast.makeText(this@LoginActivity, getString(R.string.auth_failed), Toast.LENGTH_LONG).show()
//                }
//            } else {
//
//                startActivity(HomeActivity.newIntent(this, auth?.currentUser))
//                finish()
//            }
//        })

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
