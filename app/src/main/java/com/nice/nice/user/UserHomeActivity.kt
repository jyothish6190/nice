package com.nice.nice.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.nice.nice.R
import com.nice.nice.attendance.MarkAttendanceActivity
import com.nice.nice.attendance.models.Attendance
import com.nice.nice.chat.MessagesFragment
import com.nice.nice.report.CreateReportActivity
import com.nice.nice.report.ReportListFragment
import com.nice.nice.todo.TaskListFragment
import com.nice.nice.todo.dummy.DummyContent
import com.nice.nice.user.models.User
import kotlinx.android.synthetic.main.activity_user_home.*
import java.util.*


class UserHomeActivity : AppCompatActivity(), TaskListFragment.OnListFragmentInteractionListener, ReportListFragment.OnListFragmentInteractionListener {
    var toolbar: ActionBar? = null
    private var fragmentId: Int = 0

    private var mAuth: FirebaseAuth? = null
    private val firestore by lazy {
        FirebaseFirestore.getInstance().collection(User.COLLECTION_KEY)
    }



    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val fragment: Fragment
        when (item.itemId) {
            R.id.navigation_home -> {
                fragmentId = 0
                invalidateOptionsMenu()
                toolbar?.title = getString(R.string.title_home)
                fragment = UserHomeFragment()
                loadFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_messages -> {
                fragmentId = 1
                invalidateOptionsMenu()
                toolbar?.title = getString(R.string.title_messages)
                fragment = MessagesFragment()
                loadFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_reports -> {
                fragmentId = 2
                invalidateOptionsMenu()
                toolbar?.title = getString(R.string.title_reports)
                fragment = ReportListFragment()
                loadFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_tasks -> {
                fragmentId = 3
                invalidateOptionsMenu()
                toolbar?.title = getString(R.string.title_tasks)
                fragment = TaskListFragment()
                loadFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                fragmentId = 4
                invalidateOptionsMenu()
                toolbar?.title = getString(R.string.title_profile)
                fragment = UserProfileFragment()
                loadFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_home)

        toolbar = supportActionBar

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)


        var fragment = UserHomeFragment()
        loadFragment(fragment)

        toolbar?.title = getString(R.string.title_home)

        mAuth = FirebaseAuth.getInstance()


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        val item = menu!!.findItem(R.id.actionCreate)
        item.isVisible = !(fragmentId === 1 || fragmentId === 4 || fragmentId === 3 )

        val user = mAuth!!.currentUser

        var year = Calendar.getInstance().get(Calendar.YEAR)
        var month = Calendar.getInstance().get(Calendar.MONTH) + 1
        var day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        if( fragmentId === 0) {
            item.isVisible = false

            firestore.document(user!!.uid).collection(Attendance.COLLECTION_KEY).document(year.toString()).collection(month.toString()).document(day.toString()).get().addOnCompleteListener(OnCompleteListener<DocumentSnapshot> {
                if (it.isSuccessful) {
                    val result = it.result
                    item.isVisible = !result.exists()
                } else {
                    item.isVisible = true
                }
            })
        }
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.actionCreate -> {
                if (fragmentId == 0) {

                    MarkAttendanceActivity.newInstance(this)

                    return true
                } else if (fragmentId == 2) {
                    CreateReportActivity.newInstance(this)
                }

            }
        }
        return false
    }

    //    Load fragment
    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onListFragmentInteraction(item: com.nice.nice.report.dummy.DummyContent.DummyItem) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onListFragmentInteraction(item: DummyContent.DummyItem) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    companion object {
        fun newInstance(context: Context) {
            var intent = Intent(context, UserHomeActivity::class.java)
            context.startActivity(intent)
        }

    }
}
