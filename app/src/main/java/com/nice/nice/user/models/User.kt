package com.nice.nice.user.models

data class User(val name: String, val email: String, val id: String, val profilePic: String, val school: String){
    companion object {
        const val COLLECTION_KEY = "Users"
        const val NAME_KEY = "Name"
        const val EMAIL_KEY = "Email"
        const val ID_KEY = "UserID"
        const val PROFILE_PIC_KEY = "ProfilePic"
        const val SCHOOL_KEY = "School"
    }
}