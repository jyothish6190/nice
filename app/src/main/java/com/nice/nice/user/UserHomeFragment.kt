package com.nice.nice.user


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nice.nice.R
import com.nice.nice.attendance.models.Attendance
import com.nice.nice.user.adapter.AnimalAdapter
import com.nice.nice.user.models.User
import kotlinx.android.synthetic.main.fragment_user_home.*
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class UserHomeFragment : Fragment() {

    private var mAuth: FirebaseAuth? = null


    private val firestore by lazy {
        FirebaseFirestore.getInstance().collection(User.COLLECTION_KEY)
    }

    private var attendaneArray: ArrayList<Attendance> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_user_home, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mAuth = FirebaseAuth.getInstance()

        fetchAttendance()

        rv_animal_list.layoutManager = LinearLayoutManager(activity)
        rv_animal_list.adapter = AnimalAdapter(attendaneArray, activity)
    }

    private fun fetchAttendance() {
        attendaneArray.clear()
        var user = mAuth!!.currentUser
        var year = Calendar.getInstance().get(Calendar.YEAR)
        var month = Calendar.getInstance().get(Calendar.MONTH) + 1
        var day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        firestore.document(user!!.uid).collection(Attendance.COLLECTION_KEY).document(year.toString()).collection(month.toString()).addSnapshotListener { documentSnapshot, e ->
            when {
                e != null -> Log.e("ERROR", e.message)
                documentSnapshot != null -> {
                    with(documentSnapshot) {
                        for (item in documentSnapshot.documents) {
                            if(item.exists()){
                                var attendance = Attendance(item.getString(Attendance.ID_KEY)!!, item.getString(Attendance.NAME_KEY)!!, item.getString(Attendance.ATTENDANCE_TYPE_KEY)!!,
                                        item.getString(Attendance.SCHOOL_KEY)!!, item.getString(Attendance.PROFILE_PIC_KEY)!!, item.getString(Attendance.DATE_KEY)!!,
                                        item.getString(Attendance.REASON_KEY)!!)

                                attendaneArray.add(attendance)
                            }
                        }
                        rv_animal_list?.adapter?.notifyDataSetChanged()
                    }
                }
            }
        }
    }


}// Required empty public constructor
