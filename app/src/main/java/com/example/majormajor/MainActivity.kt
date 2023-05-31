package com.example.majormajor

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
        findViewById<TextView>(R.id.forgetPassword).setOnClickListener{
            val builder = AlertDialog.Builder(this)
            val inflater:LayoutInflater = layoutInflater
            val dialogLayout: View = inflater.inflate(R.layout.forget_password_layout, null)
            val emailAddress: EditText = dialogLayout.findViewById(R.id.resetPasswordEmail)
            with(builder){
                setTitle("Email to receive reset password link")
                setPositiveButton("Reset Password") {dialog, which ->
                    Firebase.auth.sendPasswordResetEmail(emailAddress.text.toString())
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d(TAG, "Email sent.")
                            }
                        }
                }
                setView(dialogLayout)
                show()
            }
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