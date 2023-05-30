package com.example.majormajor

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Register : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = Firebase.auth
        findViewById<TextView>(R.id.clickableLoginText).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        onClick()
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

            auth.createUserWithEmailAndPassword(patientEmail, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information

                        Log.d(TAG, "createUserWithEmail:success")
                        val userInfo = db.collection("users")
                        userInfo.document(patientEmail).set(userData)


                        startActivity(Intent(this, MainActivity::class.java))
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "Creating User failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }

        }
    }

}