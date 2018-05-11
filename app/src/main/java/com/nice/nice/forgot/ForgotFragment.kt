package com.nice.nice.forgot

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.nice.nice.R
import com.nice.nice.splash.SplashActivity
import kotlinx.android.synthetic.main.fragment_register.*

class ForgotFragment : BottomSheetDialogFragment(), View.OnClickListener  {
    private var auth: FirebaseAuth? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.forgot_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.btn_reset_password -> forgot()
        }
    }

    private fun forgot(){
        val email = email.text.toString().trim()

        if (TextUtils.isEmpty(email)) {
            showMessage( "Enter your registered email id")
            return
        }


        auth?.sendPasswordResetEmail(email)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                showMessage("We have sent you instructions to reset your password. Please check email!")
            } else {
                showMessage( "Failed to send reset email!")
            }
            SplashActivity.newInstance(context)

        }

    }

    private fun showMessage( message: String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    companion object {

        fun newInstance(): ForgotFragment = ForgotFragment()

    }
}
