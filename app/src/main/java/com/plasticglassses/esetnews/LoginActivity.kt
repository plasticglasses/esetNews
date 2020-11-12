package com.plasticglassses.esetnews

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


public class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val TAG = "Sign In Activity"

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

    private fun signIn(email: String, password: String){
    //TODO add validation for email and username
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Snackbar.make(findViewById(android.R.id.content), "Passwords did not match, Authentication Failed.", Snackbar.LENGTH_SHORT).show()

                }

            }

    }

    private fun updateUI(currentUser: Any?) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun clickLoginButton(view: View) {23
        val email = findViewById<EditText>(R.id.emailSignInText)
        val password = findViewById<EditText>(R.id.passwordSignInText)

        if (validPass(password.text.toString())) {
            if (validEmail(email.text.toString())) {
                signIn(email.text.toString(), password.text.toString())
            }else{
                Snackbar.make(findViewById(android.R.id.content), "Invalid Email, Authentication Failed.", Snackbar.LENGTH_SHORT).show()
            }
        }else{
            Snackbar.make(findViewById(android.R.id.content), "Passwords did not match, Authentication Failed.", Snackbar.LENGTH_SHORT).show()
            //make text boxes red animate
            password.highlightColor
        }
    }

    /*
    validate email
     */
    private fun validEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email)
    }

    /*
    validate password
     */
    private fun validPass(password: String): Boolean {
        return !TextUtils.isEmpty(password)
    }

    fun clickRegisterLink(view: View) {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

}



