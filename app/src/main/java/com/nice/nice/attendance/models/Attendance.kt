package com.nice.nice.attendance.models

/**
 * Created by jyothish on 4/28/18.
 */
data class Attendance(val userId: String, val userName: String, val type: String, val schoolName: String, val profilePic: String,val dateTime: String, val reason: String)  {
    companion object {
        const val COLLECTION_KEY = "Attendance"
        const val NAME_KEY = "Name"
        const val ID_KEY = "UserID"
        const val PROFILE_PIC_KEY = "ProfilePic"
        const val SCHOOL_KEY = "School"
        const val ATTENDANCE_TYPE_KEY ="AttendanceType"
        const val DATE_KEY = "Date"
        const val REASON_KEY = "Reason"
    }
}