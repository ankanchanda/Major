package com.example.majormajor

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text


class DashboardActivity : AppCompatActivity() {

    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        val userEmail: String? = intent.getStringExtra("userEmail")
        val docRef: DocumentReference = db.collection("users").document(userEmail!!)
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

        findViewById<TextView>(R.id.hiText).text= "HI $patientName !!"
        val quote = "You don’t have to control your thoughts. You just have to stop letting them control you."
        findViewById<TextView>(R.id.quoteText).text ="$quote — Dan Millman"
    }
}