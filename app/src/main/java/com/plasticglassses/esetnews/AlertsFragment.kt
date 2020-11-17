package com.plasticglassses.esetnews

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AlertsFragment : Fragment() {

    private val TAG = "Alerts Fragment"
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        auth = Firebase.auth
        getUsersAlerts(auth)

        var rootView = inflater.inflate(R.layout.fragment_alerts, container, false)
        val alertChipGroup = rootView?.findViewById<ChipGroup>(R.id.alertChipGroup)

        val addAlertButton = rootView.findViewById<Button>(R.id.addAlert)
        addAlertButton.setOnClickListener {
            val newAlertText = rootView?.findViewById<EditText>(R.id.newAlertText)
            //add a chip to the alerts fragment
            addAlert(rootView, inflater, alertChipGroup, newAlertText)
        }

        return rootView
    }

    private fun getUsersAlerts(auth: FirebaseAuth) {
        var thisUid = ""
        //get users alerts from firebase
        //get user id of current user logged in
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            thisUid = user.uid
            Log.d(TAG, ("success we got the rabbi"+ thisUid))

            // User is signed in
        } else {
            Log.d(TAG, "no rabbi")
            // No user is signed in
        }

                //get the list of alerts fhat users wants from firebase
                //get userid of the firestore document
                val db = Firebase.firestore
                db.collection("users")
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            val value = document.getString("authUserID")
                            if (value == thisUid) {
                                Log.d(TAG, "Success, we have found the doucment relating to this user" + value)
                                val username = document.getString("username")
                                Log.d(TAG, ("${document.id} => ${document.data} " + username))
                            }
                        }

                //dynamically add the alert to the "current" chip group

            }
    }


    private fun addAlert(
        rootView: View?,
        inflater: LayoutInflater,
        alertChipGroup: ChipGroup?,
        newAlertText: EditText?
    ) {
        val chip = layoutInflater.inflate(R.layout.chip_layout, alertChipGroup, false) as Chip
        if (newAlertText != null) {
            chip.text = newAlertText.text

            val snackbar = Snackbar.make(rootView!!, "Alert Added", Snackbar.LENGTH_SHORT)
            snackbar.show()
            alertChipGroup!!.addView(chip)
            newAlertText.text.clear()

        }
    }
}
