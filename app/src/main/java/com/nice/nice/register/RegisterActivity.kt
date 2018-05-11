package com.nice.nice.register

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.nice.nice.R
import com.nice.nice.user.UserHomeActivity
import com.nice.nice.user.models.User
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    var PICK_FILE_REQUEST = 111
    var filePath: Uri? = null

    private val EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100
    private val REQUEST_PERMISSION_SETTING = 101
    private var sentToSettings = false
    private var permissionStatus: SharedPreferences? = null

    var isUploadSuccessfull = false;

    private var mAuth: FirebaseAuth? = null


    private var storagePath = "profile_pic/"
    private var storageReference: StorageReference? = null
    private var reportReference: StorageReference? = null

    private var fileDownloadUrl: String? = null
    var pd: ProgressDialog? = null

    private val firestore by lazy {
        FirebaseFirestore.getInstance().collection(User.COLLECTION_KEY)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth = FirebaseAuth.getInstance()


        storageReference = FirebaseStorage.getInstance().reference
        reportReference = storageReference!!.child(storagePath)

        pd = ProgressDialog(this)
        pd!!.setMessage("Uploading....")
        pd!!.setCancelable(false)


        uploadBtn.setOnClickListener(View.OnClickListener { checkPermission() })
        registerBtn.setOnClickListener(View.OnClickListener { register() })


        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);


    }

    private fun register() {
        pd!!.setMessage("Registering....")
        if (isUploadSuccessfull) {
            pd!!.show()
            val name = name.text.toString()
            val email = email.text.toString()
            val password = password.text.toString()
            val schoolName = schoolName.text.toString()

            mAuth!!.createUserWithEmailAndPassword(email, password)?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    createUserProfile(name, schoolName)
                } else {
                    pd!!.dismiss()
                    Toast.makeText(this, "Register failed", Toast.LENGTH_SHORT).show()
                }

            }
        }

    }

    private fun createUserProfile(name: String, school: String) {
        val user = mAuth!!.currentUser
        val newUser = User(name, user!!.email!!, user.uid, fileDownloadUrl!!, school)

        val userMap = mapOf(
                User.ID_KEY to newUser.id,
                User.EMAIL_KEY to newUser.email,
                User.NAME_KEY to newUser.name,
                User.PROFILE_PIC_KEY to newUser.profilePic,
                User.SCHOOL_KEY to newUser.school
        )

        firestore.document(newUser.id).set(userMap)
                .addOnSuccessListener({
                    pd!!.dismiss()
                    Toast.makeText(this, "Register successful", Toast.LENGTH_SHORT).show()
                    UserHomeActivity.newInstance(this)

                    finish()

                })
                .addOnFailureListener { e ->
                    Log.e("ERROR", e.message)
                    pd!!.dismiss()
                }


    }

    private fun attachFile() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_PICK
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_FILE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode === PICK_FILE_REQUEST && resultCode === Activity.RESULT_OK && data != null && data.data != null) {
            filePath = data.data

            uploadImage()

        }

        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                attachFile()
            }
        }
    }

    private fun uploadImage() {
        if (filePath != null) {
            pd!!.show()

            var mReference = reportReference!!.child(filePath!!.lastPathSegment)
            val uploadTask = mReference.putFile(filePath!!)

            uploadTask.addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> {
                profilePic.setImageURI(filePath)
                pd!!.dismiss()
                fileDownloadUrl = it.downloadUrl.toString()
                Toast.makeText(this, "Upload successful", Toast.LENGTH_SHORT).show()
                uploadBtn.visibility = View.GONE
                isUploadSuccessfull = true
            }).addOnFailureListener(OnFailureListener { e ->
                pd!!.dismiss()
                Toast.makeText(this, "Upload Failed -> $e", Toast.LENGTH_SHORT).show()
                uploadBtn.visibility = View.VISIBLE
                isUploadSuccessfull = false
            })
        } else {
        }
    }

    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Need Storage Permission")
                builder.setMessage("This app needs storage permission.")
                builder.setPositiveButton("Grant", DialogInterface.OnClickListener { dialog, which ->
                    dialog.cancel()
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), EXTERNAL_STORAGE_PERMISSION_CONSTANT)
                })
                builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
                builder.show()
            } else if (permissionStatus!!.getBoolean(Manifest.permission.READ_EXTERNAL_STORAGE, false)) {

                val builder = AlertDialog.Builder(this)
                builder.setTitle("Need Storage Permission")
                builder.setMessage("This app needs storage permission.")
                builder.setPositiveButton("Grant", DialogInterface.OnClickListener { dialog, which ->
                    dialog.cancel()
                    sentToSettings = true
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivityForResult(intent, REQUEST_PERMISSION_SETTING)
                    Toast.makeText(baseContext, "Go to Permissions to Grant Storage", Toast.LENGTH_LONG).show()
                })
                builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
                builder.show()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), EXTERNAL_STORAGE_PERMISSION_CONSTANT)
            }

            val editor = permissionStatus!!.edit()
            editor.putBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE, true)
            editor.commit()


        } else {
            //You already have the permission, just go ahead.
            attachFile()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == EXTERNAL_STORAGE_PERMISSION_CONSTANT) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...
                attachFile()
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    //Show Information about why you need the permission
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Need Storage Permission")
                    builder.setMessage("This app needs storage permission")
                    builder.setPositiveButton("Grant") { dialog, which ->
                        dialog.cancel()


                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), EXTERNAL_STORAGE_PERMISSION_CONSTANT)
                    }
                    builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
                    builder.show()
                } else {
                    Toast.makeText(baseContext, "Unable to get Permission", Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    override fun onPostResume() {
        super.onPostResume()
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                attachFile()
            }
        }
    }

    companion object {
        fun newInstance(context: Context) {
            context.startActivity(Intent(context, RegisterActivity::class.java))
        }
    }
}
