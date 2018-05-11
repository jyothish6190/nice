package com.nice.nice.splash

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.nice.nice.R
import com.nice.nice.config.ForceUpdateChecker
import com.nice.nice.login.LoginFragment
import com.nice.nice.register.RegisterActivity
import com.nice.nice.user.UserHomeActivity
import kotlinx.android.synthetic.main.activity_splash.*




/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class SplashActivity : AppCompatActivity(), View.OnClickListener, ForceUpdateChecker.OnUpdateNeededListener {


    private var auth: FirebaseAuth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        supportActionBar?.hide()

        setContentView(R.layout.activity_splash)




        registerBtn.setOnClickListener(this)
        loginBtn.setOnClickListener(this)


    }

    override fun onStart() {
        ForceUpdateChecker.with(this).onUpdateNeeded(this).check()
        super.onStart()


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
            UserHomeActivity.newInstance(this )
            finish()
        }

    }

    private fun register() {
        RegisterActivity.newInstance(this)
    }

    private fun login() {
        var fragment = LoginFragment.newInstance()
        fragment.show(supportFragmentManager, fragment.tag)

    }

    companion object {
        fun newInstance(context: Context) {
            context.startActivity(Intent(context, SplashActivity::class.java))
        }
    }

    override fun onUpdateNeeded(updateUrl: String) {


        val dialog = AlertDialog.Builder(this)
                .setTitle("New version available")
                .setMessage("Please, update app to new version to continue reposting.")
                .setCancelable(false)
                .setPositiveButton("Update",
                        { _, _ -> redirectStore(updateUrl) }).setNegativeButton("No, thanks",
                        { _, _ -> finish() }).create()
        dialog.show()
    }

    override fun onUpdateNotNeeded() {
        isloggedIn()
    }

    private fun redirectStore(updateUrl: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

}
