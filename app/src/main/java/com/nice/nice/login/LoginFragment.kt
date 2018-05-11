package com.nice.nice.login

import android.content.ContentValues.TAG
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.nice.nice.R
import com.nice.nice.forgot.ForgotFragment
import com.nice.nice.user.UserHomeActivity
import com.nice.nice.utils.StringUtils
import kotlinx.android.synthetic.main.login_fragment.*


class LoginFragment : BottomSheetDialogFragment(), View.OnClickListener {
    private var auth: FirebaseAuth? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        loginBtn.setOnClickListener(this)
        forgotBtn.setOnClickListener(this)

        auth = FirebaseAuth.getInstance()

    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.loginBtn -> login()
            R.id.forgotBtn -> forgot()
        }
    }

    private fun forgot() {

        var fragment = ForgotFragment.newInstance()
        fragment.show(activity.supportFragmentManager, fragment.tag)
    }


    private fun login() {
        val email = email.text.toString()
        val password = password.text.toString()

        if (TextUtils.isEmpty(email) || !StringUtils.isEmailValid(email)) {
            Toast.makeText(context, "Enter email address!", Toast.LENGTH_SHORT).show()
            return
        }

        if (TextUtils.isEmpty(password) || password.length < 6) {
            Toast.makeText(context, "Enter password!", Toast.LENGTH_SHORT).show()
            return
        }


        auth!!.signInWithEmailAndPassword(email!!, password!!).addOnCompleteListener(activity) { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "signInWithEmail:success")
                UserHomeActivity.newInstance(activity)
                activity.finish()


            } else {
                Toast.makeText(context, "Login Failed!", Toast.LENGTH_SHORT).show()
            }
        }


    }


    companion object {

        fun newInstance(): LoginFragment = LoginFragment()
    }
}
