package com.nice.nice.attendance

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.nice.nice.R
import com.nice.nice.attendance.models.Attendance
import com.nice.nice.user.models.User
import com.nice.nice.utils.DateUtils
import kotlinx.android.synthetic.main.fragment_attendance.*
import java.util.*


class MarkAttendanceActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private var mAuth: FirebaseAuth? = null
    var pd: ProgressDialog? = null


    var isLeave = false
    var attendanceTypeArray = mutableListOf<String>()
    var attendanceType = "Present"

    lateinit var reason: String


    private val firestore by lazy {
        FirebaseFirestore.getInstance().collection(User.COLLECTION_KEY)
    }

    private var user: FirebaseUser? = null

    private lateinit var dateTime: String
    private lateinit var profilePic: String
    private lateinit var schoolName: String
    private lateinit var name: String
    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.fragment_attendance)

        attendanceTypeArray = resources.getStringArray(R.array.attendance_type_arrays).toMutableList()
        attendanceSpinner.onItemSelectedListener = this

        attendanceType = attendanceTypeArray[0]


        mAuth = FirebaseAuth.getInstance()

        pd = ProgressDialog(this)
        pd!!.setMessage("Please wait....")
        pd!!.setCancelable(false)

        pd!!.show()


        dateTime = DateUtils.getDateTime()

        user = mAuth!!.currentUser
        var uid = user!!.uid
        firestore.document(user!!.uid).addSnapshotListener { documentSnapshot, e ->
            when {
                e != null -> Log.e("ERROR", e.message)
                documentSnapshot != null && documentSnapshot.exists() -> {
                    with(documentSnapshot) {
                        pd!!.dismiss()

                        profilePic = documentSnapshot.getString(User.PROFILE_PIC_KEY)!!
                        schoolName = documentSnapshot.getString(User.SCHOOL_KEY)!!
                        name = documentSnapshot.getString(User.NAME_KEY)!!

                        year = Calendar.getInstance().get(Calendar.YEAR)
                        month = Calendar.getInstance().get(Calendar.MONTH) + 1
                        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                    }
                }
            }
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.attendance_menu, menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.actionDone -> {
                saveAttendance()

            }
        }
        return false
    }

    private fun saveAttendance() {
        pd!!.show()


        reason = if (isLeave) {
            reasonTxt.text.toString()
        } else {
            ""
        }


        val attendanceMap = mapOf(
                Attendance.ID_KEY to user!!.uid,
                Attendance.NAME_KEY to name,
                Attendance.PROFILE_PIC_KEY to profilePic,
                Attendance.ATTENDANCE_TYPE_KEY to attendanceType,
                Attendance.DATE_KEY to dateTime,
                Attendance.SCHOOL_KEY to schoolName,
                Attendance.REASON_KEY to reason)



        firestore.document(user!!.uid).collection(Attendance.COLLECTION_KEY).document(year.toString()).collection(month.toString()).document(day.toString()).set(attendanceMap)
                .addOnSuccessListener({
                    pd!!.dismiss()
                    Toast.makeText(this@MarkAttendanceActivity, "Attendance Submitted", Toast.LENGTH_SHORT).show()
                    finish()

                })
                .addOnFailureListener { e ->
                    Log.e("ERROR", e.message)
                    pd!!.dismiss()
                }

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, p3: Long) {
        isLeave = position === 1
        attendanceType = attendanceTypeArray[position]
        onLeave()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        isLeave = false
        attendanceType = attendanceTypeArray[0]
        onLeave()
    }

    private fun onLeave() {
        if (!isLeave) {
            reasonLayout.visibility = View.GONE
            reasonTxt.visibility = View.GONE
        } else {
            reasonTxt.visibility = View.VISIBLE
            reasonLayout.visibility = View.VISIBLE
        }
    }


    companion object {
        fun newInstance(context: Context) {
            context.startActivity(Intent(context, MarkAttendanceActivity::class.java))
        }
    }


}
