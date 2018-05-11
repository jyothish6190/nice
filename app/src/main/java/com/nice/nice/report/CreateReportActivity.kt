package com.nice.nice.report

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
import android.provider.OpenableColumns
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.nice.nice.R
import com.nice.nice.report.models.Report
import com.nice.nice.user.models.User
import kotlinx.android.synthetic.main.activity_create_report.*


class CreateReportActivity : AppCompatActivity() {
    var PICK_FILE_REQUEST = 111
    var filePath: Uri? = null

    private var mAuth: FirebaseAuth? = null
    private val firestore by lazy {
        FirebaseFirestore.getInstance().collection(User.COLLECTION_KEY)
    }

    private var storagePath = "reports/"
    private var storageReference: StorageReference? = null
    private var reportReference: StorageReference? = null

    private var fileDownloadUrl: String? = ""
    var pd: ProgressDialog? = null

    private val EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100
    private val REQUEST_PERMISSION_SETTING = 101
    private var sentToSettings = false
    private var permissionStatus: SharedPreferences? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_report)

        mAuth = FirebaseAuth.getInstance()

        storageReference = FirebaseStorage.getInstance().reference
        reportReference = storageReference!!.child(storagePath)

        pd = ProgressDialog(this)
        pd!!.setMessage("Uploading....")
        pd!!.setCancelable(false)

        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.report_menu, menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.actionDone -> {
                saveReport()

            }

            R.id.actioAttach -> {
                checkPermission()
            }
        }
        return false
    }

    private fun saveReport() {
        val user = mAuth!!.currentUser
        var name = reportName.text.toString()
        var desc = reportDesc.text.toString()

        if (!name.isEmpty() && !desc.isEmpty()) {
            pd!!.setMessage("Please wait....")
            pd!!.show()
            val reportMap = mapOf(
                    Report.NAME_KEY to name,
                    Report.DESC_KEY to desc,
                    Report.FILE_KEY to fileDownloadUrl
            )

            firestore.document(user!!.uid).collection(Report.COLLECTION_KEY).document().set(reportMap)
                    .addOnSuccessListener({
                        pd!!.dismiss()
                        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
                        finish()

                    })
                    .addOnFailureListener { e ->
                        Log.e("ERROR", e.message)
                        pd!!.dismiss()
                    }


        }
    }

    private fun attachFile() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_PICK
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_FILE_REQUEST)
    }

    private fun uploadImage() {
        if (filePath != null) {
            pd!!.show()

            var mReference = reportReference!!.child(filePath!!.lastPathSegment)
            val uploadTask = mReference.putFile(filePath!!)

            uploadTask.addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> {
                fileDownloadUrl = it.downloadUrl.toString()
                pd!!.dismiss()
                Toast.makeText(this, "Upload successful", Toast.LENGTH_SHORT).show()
            }).addOnFailureListener(OnFailureListener { e ->
                pd!!.dismiss()
                Toast.makeText(this, "Upload Failed -> $e", Toast.LENGTH_SHORT).show()
            })
        } else {
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode === PICK_FILE_REQUEST && resultCode === Activity.RESULT_OK && data != null && data.data != null) {
            filePath = data.data

            fileName.text = getFileName(data.data)
            uploadImage()

        }

        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                attachFile()
            }
        }
    }

    fun getFileName(uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } finally {
                cursor!!.close()
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result!!.lastIndexOf('/')
            if (cut != -1) {
                result = result.substring(cut + 1)
            }
        }
        return result
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
            context.startActivity(Intent(context, CreateReportActivity::class.java))
        }
    }
}
