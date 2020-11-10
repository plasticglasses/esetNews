package com.plasticglassses.esetnews

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val TAG = "MyActivity"

    /*on create*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

    }

    /*
    uses fierbase to authenticate new users
     */
    fun clickLoginButton(view: View) {

//        val intent = Intent(this, MainActivity::class.java)
//        startActivity(intent)

        val email = findViewById<EditText>(R.id.signupEmail)
        val password = findViewById<EditText>(R.id.signUpPassword)

        Snackbar.make(email, email.text.toString(), Snackbar.LENGTH_SHORT).show()
        Snackbar.make(password, password.text.toString(), Snackbar.LENGTH_SHORT).show()



//        auth.createUserWithEmailAndPassword(email.toString(), password.toString())
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
////
//                    val snackbar = Snackbar.make(findViewById(android.R.id.content), "Authentication success.", Snackbar.LENGTH_SHORT).show()
//
//
////                    // Sign in success, update UI with the signed-in user's information
////                    Log.d(TAG, "createUserWithEmail:success")
////                    val user = auth.currentUser
////
////                    val intent = Intent(this, MainActivity::class.java)
////                    startActivity(intent)
//
//                } else {
//                    // If sign in fails, display a message to the user.
////                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
//
//                    val snackbar = Snackbar.make(findViewById(android.R.id.content), "Authentication failed.", Snackbar.LENGTH_SHORT).show()
//
//                }
//
//            }
    }

}