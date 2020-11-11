package com.plasticglassses.esetnews

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val TAG = "MyActivity"

    /*on create*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

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

    private fun updateUI(currentUser: Any?) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun createUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                        // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    val snackbar3 = Snackbar.make(findViewById(android.R.id.content), "Authentication Failed.", Snackbar.LENGTH_SHORT).show()

                    updateUI(null)
                }
            }

    }

    /*
    Returns true if passwords are identical and false otherwise
     */
    private fun validPass(): Boolean {
        val password1 = findViewById<EditText>(R.id.signUpPassword)
        val password2 = findViewById<EditText>(R.id.signUpPassword2)

        return password1.text.toString() == password2.text.toString()
    }

    /*
    return true if email is valid
     */
    private fun validEmail(email: String): Boolean {
        return email.contains("@")
    }
    /*
    uses fierbase to authenticate new users
     */
    fun clickLoginButton(view: View) {
        val email = findViewById<EditText>(R.id.signupEmail)
        val password = findViewById<EditText>(R.id.signUpPassword)

        if (validPass()) {
            if (validEmail(email.text.toString())) {
                createUser(email.text.toString(), password.text.toString())
            }else{
                Snackbar.make(findViewById(android.R.id.content), "Invalid Email, Authentication Failed.", Snackbar.LENGTH_SHORT).show()
            }
        }else{
            Snackbar.make(findViewById(android.R.id.content), "Passwords did not match, Authentication Failed.", Snackbar.LENGTH_SHORT).show()
            //make text boxes red animate
            password.highlightColor
        }
    }



}

private fun String.contains(s: String, c: Char) {

}
