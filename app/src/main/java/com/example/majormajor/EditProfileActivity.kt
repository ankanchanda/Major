package com.example.majormajor

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EditProfileActivity : AppCompatActivity() {
    val db = Firebase.firestore

    private lateinit var docRef: DocumentReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val userEmail: String? = intent.getStringExtra("userEmail")
        docRef = db.collection("users").document(userEmail!!)
        var patientName: String = ""
        var patientPhone: String = ""
        var patientEmail: String = ""
        var psychiatristName: String = ""
        var psychiatristPhone: String = ""
        var psychiatristEmail: String = ""
        var emergencyName: String  = ""
        var emergencyPhone: String = ""
        var emergencyEmail: String = ""
        docRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document != null) {
                    patientName = document.getString("patientName")!!
                    patientPhone = document.getString("patientPhone")!!
                    patientEmail = document.getString("patientEmail")!!
                    psychiatristName = document.getString("psychiatristName")!!
                    psychiatristPhone = document.getString("psychiatristPhone")!!
                    psychiatristEmail = document.getString("psychiatristEmail")!!
                    emergencyName = document.getString("emergencyName")!!
                    emergencyPhone = document.getString("emergencyPhone")!!
                    emergencyEmail = document.getString("emergencyEmail")!!

                    handleData(patientName, patientPhone, patientEmail, psychiatristName, psychiatristPhone, psychiatristEmail, emergencyName, emergencyPhone, emergencyEmail)


                } else {
                    Log.d("LOGGER", "No such document")
                }
            } else {
                Log.d("LOGGER", "get failed with ", task.exception)
            }
        }
        onClick()

    }

    fun handleData(
        patientName: String?,
        patientPhone: String?,
        patientEmail: String?,
        psychiatristName: String?,
        psychiatristPhone: String?,
        psychiatristEmail: String?,
        emergencyName: String?,
        emergencyPhone: String?,
        emergencyEmail: String?
    ) {
        // Access and use the retrieved values here
        Log.i("LOGGERR", "patientName $patientName")
        Log.i("LOGGERR", "patientPhone $patientPhone")
        Log.i("LOGGERR", "patientEmail $patientEmail")
        Log.i("LOGGERR", "psychiatristName $psychiatristName")
        Log.i("LOGGERR", "psychiatristPhone $psychiatristPhone")
        Log.i("LOGGERR", "psychiatristEmail $psychiatristEmail")
        Log.i("LOGGERR", "emergencyName $emergencyName")
        Log.i("LOGGERR", "emergencyPhone $emergencyPhone")
        Log.i("LOGGERR", "emergencyEmail $emergencyEmail")


        findViewById<EditText>(R.id.patientNameEdit).setText(patientName)
        findViewById<EditText>(R.id.patientPhoneEdit).setText(patientPhone)
        findViewById<EditText>(R.id.patientEmailEdit).setText(patientEmail)
        findViewById<EditText>(R.id.psychiNameEdit).setText(psychiatristName)
        findViewById<EditText>(R.id.psychiPhoneEdit).setText(psychiatristPhone)
        findViewById<EditText>(R.id.psychiEmailEdit).setText(psychiatristEmail)
        findViewById<EditText>(R.id.emergencyNameEdit).setText(emergencyName)
        findViewById<EditText>(R.id.emergencyPhoneEdit).setText(emergencyPhone)
        findViewById<EditText>(R.id.emergencyEmailEdit).setText(emergencyEmail)


    }
        private fun onClick() {
            findViewById<MaterialButton>(R.id.registerSubmitButton).setOnClickListener {
                val patientName =
                    findViewById<TextInputLayout>(R.id.patientNameText).editText?.text.toString()
                        .trim()
                val patientPhone =
                    findViewById<TextInputLayout>(R.id.patientPhoneText).editText?.text.toString()
                        .trim()

                val patientEmail =
                    findViewById<TextInputLayout>(R.id.patientEmailText).editText?.text.toString()
                        .trim()
                val password =
                    findViewById<TextInputLayout>(R.id.patientPasswordText).editText?.text.toString()
                        .trim()

                val psychiatristName =
                    findViewById<TextInputLayout>(R.id.psychiNameText).editText?.text.toString()
                        .trim()
                val psychiatristPhone =
                    findViewById<TextInputLayout>(R.id.psychiPhoneText).editText?.text.toString()
                        .trim()

                val psychiatristEmail =
                    findViewById<TextInputLayout>(R.id.psychiEmailText).editText?.text.toString()
                        .trim()
                val emergencyName =
                    findViewById<TextInputLayout>(R.id.emergencyNameText).editText?.text.toString()
                        .trim()
                val emergencyPhone =
                    findViewById<TextInputLayout>(R.id.emergencyPhoneText).editText?.text.toString()
                        .trim()

                val emergencyEmail =
                    findViewById<TextInputLayout>(R.id.emergencyEmailText).editText?.text.toString()
                        .trim()
                val userData = hashMapOf(
                    "patientName" to patientName,
                    "patientPhone" to patientPhone,
                    "patientEmail" to patientEmail,
                    "psychiatristName" to psychiatristName,
                    "psychiatristPhone" to psychiatristPhone,
                    "psychiatristEmail" to psychiatristEmail,
                    "emergencyName" to emergencyName,
                    "emergencyPhone" to emergencyPhone,
                    "emergencyEmail" to emergencyEmail,
                )

                val userInfo = db.collection("users")
                userInfo.document(patientEmail).set(userData)

                Toast.makeText(
                    baseContext,
                    "Update User Done.",
                    Toast.LENGTH_SHORT,
                ).show()

                startActivity(Intent(this, DashboardActivity::class.java))

            }
        }

    }

