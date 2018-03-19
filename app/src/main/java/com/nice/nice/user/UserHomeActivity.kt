package com.nice.nice.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import com.nice.nice.R
import com.nice.nice.chat.MessagesFragment
import com.nice.nice.report.ReportListFragment
import com.nice.nice.todo.TaskListFragment
import com.nice.nice.todo.dummy.DummyContent
import kotlinx.android.synthetic.main.activity_user_home.*

class UserHomeActivity : AppCompatActivity(), TaskListFragment.OnListFragmentInteractionListener, ReportListFragment.OnListFragmentInteractionListener {
    override fun onListFragmentInteraction(item: com.nice.nice.report.dummy.DummyContent.DummyItem) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onListFragmentInteraction(item: DummyContent.DummyItem) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private var toolbar: ActionBar? = null


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val fragment: Fragment
        when (item.itemId) {
            R.id.navigation_home -> {
                toolbar?.title = getString(R.string.title_home)
                fragment = UserHomeFragment()
                loadFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_messages -> {
                toolbar?.title = getString(R.string.title_messages)
                fragment = MessagesFragment()
                loadFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_reports -> {
                toolbar?.title = getString(R.string.title_reports)
                fragment = ReportListFragment()
                loadFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_tasks -> {
                toolbar?.title = getString(R.string.title_tasks)
                fragment = TaskListFragment()
                loadFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
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

        toolbar?.title = getString(R.string.title_home)

    }

//    Load fragment
    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, UserHomeActivity::class.java))
        }
    }
}
