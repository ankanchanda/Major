package com.example.majormajor

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = Firebase.auth
        findViewById<TextView>(R.id.clickableSignUpText).setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }
        onClick()
    }

    private fun onClick() {
        findViewById<MaterialButton>(R.id.loginSubmitBtn).setOnClickListener {
            val email =
                findViewById<TextInputLayout>(R.id.usernameText).editText?.text.toString()
                    .trim()
            val password =
                findViewById<TextInputLayout>(R.id.passwordText).editText?.text.toString()
                    .trim()

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = auth.currentUser
                        startActivity(Intent(this, DashboardActivity::class.java).apply {
                            putExtra("userEmail", email)
                        })
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        }
    }
}