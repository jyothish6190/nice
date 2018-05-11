package com.nice.nice.user


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nice.nice.R
import com.nice.nice.user.models.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_user_profile.*


/**
 * A simple [Fragment] subclass.
 */
class UserProfileFragment : Fragment() {
    private var mAuth: FirebaseAuth? = null
    var TAG: String = "UserProfileFragment"


    private val firestore by lazy {
        FirebaseFirestore.getInstance().collection(User.COLLECTION_KEY)
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_user_profile, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mAuth = FirebaseAuth.getInstance()

        fetchProfile(mAuth!!.currentUser!!.uid)

    }

    private fun fetchProfile(uid: String){
        firestore.document(uid).addSnapshotListener { documentSnapshot, e ->
            when {
                e != null -> Log.e("ERROR", e.message)
                documentSnapshot != null && documentSnapshot.exists() -> {
                    with(documentSnapshot) {
                        if(documentSnapshot.exists()){
                            name.text = documentSnapshot.getString(User.NAME_KEY)
                            Picasso.with(activity).load(documentSnapshot.getString(User.PROFILE_PIC_KEY)!!).fit().centerCrop().into(profilePic)
                            email.text = documentSnapshot.getString(User.EMAIL_KEY)
                            schoolName.text = documentSnapshot.getString(User.SCHOOL_KEY)
                        }
                    }
                }
            }
        }
    }


}// Required empty public constructor
