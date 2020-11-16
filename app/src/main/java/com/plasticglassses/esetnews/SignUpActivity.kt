package com.plasticglassses.esetnews

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    private val TAG = "Sign Up Activity"

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
        FirebaseAuth.getInstance().signOut()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            updateUI(currentUser)
        }

    }

    private fun updateUI(currentUser: Any?) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun createUser(emailInput: String, password: String) {
        auth.createUserWithEmailAndPassword(emailInput, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    createFirestoreDocument(user)
                    updateUI(user)
                } else {
                        // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    val snackbar3 = Snackbar.make(findViewById(android.R.id.content), "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                }
            }
    }

    private fun createFirestoreDocument(thisUser: FirebaseUser?) {
        val username = findViewById<EditText>(R.id.usernameText)
        val alertsList = arrayListOf<String>()
        val commentsList: ArrayList<String> = arrayListOf<String>()
        //ad new record to firebase
        //firestore
        val db = Firebase.firestore

        // Create a new user
        val user = hashMapOf(
            "authUserID" to thisUser!!.uid,
            "username" to username.text.toString(),
            "alerts" to alertsList,
            "comments" to commentsList
        )

        //Add a new document with a generated ID
        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
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
   Returns true if passwords are identical and false otherwise
    */
    private fun validPassLength(password: String): Boolean {
        return password.length >= 6

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

        //validation
        if (validPass() && validPassLength(password.text.toString())) {
            if (validEmail(email.text.toString())) {
                //complete firebase operations
                createUser(email.text.toString(), password.text.toString())
            }else{
                Snackbar.make(findViewById(android.R.id.content), "Invalid Email, Authentication Failed.", Snackbar.LENGTH_SHORT).show()
            }
        }else{
            Snackbar.make(findViewById(android.R.id.content), "invalid passwords, both must match and > 6 characters, Authentication Failed.", Snackbar.LENGTH_SHORT).show()
            //make text boxes red animate
            password.highlightColor
        }

   }

}


