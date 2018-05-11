package com.nice.nice.register

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.Snackbar
import android.support.v4.content.FileProvider
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.nice.nice.R
import com.nice.nice.user.UserHomeActivity
import kotlinx.android.synthetic.main.fragment_register.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class RegisterFragment : BottomSheetDialogFragment(), View.OnClickListener {
    private var mAuth: FirebaseAuth? = null
    private val REQUEST_IMAGE_CAPTURE = 1
    private var mCurrentPhotoPath: String? = null
    private var photoFile: File? = null
    private var photoURI: Uri? = null
    private var storagePath = "profile_pic/"
    private var storageReference: StorageReference? = null
    private var profileReference: StorageReference? = null
    private var FilePathUri: Uri? = null

    private var fileDownloadUrl: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        mAuth = FirebaseAuth.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        profileReference = storageReference!!.child(storagePath)
        registerBtn.setOnClickListener(this)
        uploadBtn.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.registerBtn -> register(view)
            R.id.uploadBtn -> takePicture()
        }
    }

    private fun register(view: View) {
        if (isFileUploaded()) {
            val email = email.text.toString()
            val password = password.text.toString()
            val schoolName = schoolName.text.toString()

//            if (TextUtils.isEmpty(email) || !StringUtils.isEmailValid(email)) {
//                showMessage(view, "Enter email address!")
//                return
//            }
//
//            if (TextUtils.isEmpty(password) || password.length < 6) {
//                showMessage(view, "Enter password!")
//                return
//            }
//
//            if (TextUtils.isEmpty(schoolName) || schoolName.length < 6) {
//                showMessage(view, "Enter school name!")
//                return
//            }


            mAuth!!.createUserWithEmailAndPassword(email, password)?.addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    UserHomeActivity.newInstance(activity)

                } else {
                    // If sign in fails, display a message to the user.
                    showMessage(view, "Error: ${task.exception?.message}")
                }

            }
        }

    }

    private fun uploadImage(){
        if (FilePathUri != null) {
            var mReference = profileReference!!.child(FilePathUri!!.lastPathSegment)
            try {
                mReference!!.putFile(FilePathUri!!).addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot? ->
                    var url = taskSnapshot!!.downloadUrl
                    fileDownloadUrl = taskSnapshot!!.downloadUrl.toString()
                }
            } catch (e: Exception) {
//                showMessage(view, "Upload Failed")
            }
        } else {
//            showMessage(view, "Please upload an image")
        }
    }

    private fun isFileUploaded(): Boolean {
        return fileDownloadUrl != null
    }

    private fun takePicture() {
        dispatchTakePictureIntent()
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(activity.packageManager) != null) {
            photoFile = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(activity,
                        "com.nice.nice.fileprovider",
                        photoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath()
        return image
    }

    private fun setPic() {
        // Get the dimensions of the View
        val targetW = profilePic.width
        val targetH = profilePic.height

        // Get the dimensions of the bitmap
        val bmOptions = BitmapFactory.Options()
        bmOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions)
        val photoW = bmOptions.outWidth
        val photoH = bmOptions.outHeight

        Log.d("width", targetW.toString() + "bmw" + photoW)
        Log.d("height", targetH.toString() + "bmh" + photoH)

        // Determine how much to scale down the image
        val scaleFactor = Math.max(photoW / targetW, photoH / targetH)

        Log.d("scale", scaleFactor.toString())

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false
        bmOptions.inSampleSize = scaleFactor
        bmOptions.inPurgeable = true

        val bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions)
        profilePic.setImageBitmap(bitmap)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            FilePathUri = data?.data
            if (data?.data != null)
                setPic()

            uploadImage()
        }
    }

    private fun showMessage(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction("Action", null).show()
    }

    companion object {
        fun newInstance(): RegisterFragment = RegisterFragment()
    }
}
