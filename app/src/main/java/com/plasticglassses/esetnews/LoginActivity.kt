package com.plasticglassses.esetnews

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


public class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

    }

    @Override
    public override fun onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            updateUI(currentUser)
        }

    }

    private fun updateUI(currentUser: Any) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun clickLoginButton(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun clickRegisterLink(view: View) {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }


//    override fun onStart() {
//        super.onStart()
//        // Check if user is signed in (non-null) and update UI accordingly.
//        val currentUser = mAuth!!.currentUser
//        updateUI(currentUser)
//
//    }
}



