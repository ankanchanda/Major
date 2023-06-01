package com.example.majormajor

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.view.View

import android.widget.Button
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class DiarizationActivity : AppCompatActivity() {


    private lateinit var selectAudioButton: MaterialButton
    private lateinit var storageRef: StorageReference
    private lateinit var progressBar: ProgressBar
    private lateinit var chartLayout: ConstraintLayout
    private lateinit var overallSentiment: TextView



    private val audioRequestCode = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diarization)

        storageRef = FirebaseStorage.getInstance().reference

        selectAudioButton = findViewById(R.id.audioButton)
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.GONE
        chartLayout = findViewById(R.id.chartLayout)
        chartLayout.visibility = View.GONE
        overallSentiment = findViewById(R.id.overallSentiment)
        overallSentiment.visibility = View.GONE

        selectAudioButton.setOnClickListener {
            selectAudioSample()
        }
    }

    private fun selectAudioSample() {
        if (isPermissionGranted()) {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "audio/*"
            startActivityForResult(intent, audioRequestCode)
        } else {
            requestPermission()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == audioRequestCode && resultCode == RESULT_OK && data != null) {
            val audioUri: Uri? = data.data
            if (audioUri != null) {
                uploadAudioFile(audioUri)
            }
        }
    }

    private fun uploadAudioFile(audioUri: Uri) {
        val audioFileName = getFileNameFromUri(audioUri)
        val audioRef = storageRef.child(audioFileName.toString())

        val uploadTask = audioRef.putFile(audioUri)
        progressBar.visibility = View.VISIBLE

        uploadTask.addOnProgressListener { taskSnapshot ->
            val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toInt()
            progressBar.progress = progress
        }.addOnSuccessListener {
            progressBar.visibility = View.GONE
            Toast.makeText(this, "SUCCESSFUL", Toast.LENGTH_SHORT).show();
            buildChart(30, 70)
            }
            .addOnFailureListener {
                progressBar.visibility = View.GONE
                Toast.makeText(this, "FAILED TO UPLOAD", Toast.LENGTH_SHORT).show();
            }
    }

    private fun buildChart(heightP: Int, heightN: Int) {
        val dpWidth = 175
        val dpHeightP = heightP*9
        val dpHeightN = heightN*9
        val positive: MaterialButton = findViewById(R.id.positive)
        val negative: MaterialButton = findViewById(R.id.negative)
        val buttonLayoutParamsP = positive.layoutParams
        val buttonLayoutParamsN = negative.layoutParams

        buttonLayoutParamsP.width = dpWidth // Set the width to 200 pixels
        buttonLayoutParamsP.height = dpHeightP // Set the height to 100 pixels


        buttonLayoutParamsN.width = dpWidth // Set the width to 200 pixels
        buttonLayoutParamsN.height = dpHeightN // Set the height to 100 pixels

        positive.layoutParams = buttonLayoutParamsP
        if(heightP>heightN) {
            overallSentiment.text = "Overall Sentiment: Positive"
        }
        else if(heightN>heightP){
            overallSentiment.text = "Overall Sentiment: Negative"
        }
        else{
            overallSentiment.text = "Overall Sentiment: Neutral"
        }
        chartLayout.visibility = View.VISIBLE
        overallSentiment.visibility = View.VISIBLE

    }

    private fun getFileNameFromUri(uri: Uri): String? {
        var fileName: String? = null
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                fileName = it.getString(it.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME))
            }
        }
        return fileName
    }

    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            audioRequestCode
        )
    }
}
