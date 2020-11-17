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

        var rootView = inflater.inflate(R.layout.fragment_alerts, container, false)
        val alertChipGroup = rootView?.findViewById<ChipGroup>(R.id.alertChipGroup)

        val addAlertButton = rootView.findViewById<Button>(R.id.addAlert)
        addAlertButton.setOnClickListener {
            val newAlertText = rootView?.findViewById<EditText>(R.id.newAlertText)
            //add a chip to the alerts fragment
            if (newAlertText != null) {
                addAlert(rootView, inflater, alertChipGroup, newAlertText.text.toString())
                newAlertText.text.clear()
            }

        }
        auth = Firebase.auth
        getUsersAlerts(auth, rootView, inflater, alertChipGroup)

        return rootView
    }

    private fun getUsersAlerts(
        auth: FirebaseAuth,
        rootView: View,
        inflater: LayoutInflater,
        alertChipGroup: ChipGroup?
    ) {

        var thisUid = ""
        //get users alerts from firebase
        //get user id of current user logged in
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            thisUid = user.uid
            Log.d(TAG, ("success we got the rabbi"+ thisUid))

            // User is signed in
        }

        //go to firebase and get users information
                val db = Firebase.firestore
                db.collection("users")
                    .get()
                    .addOnSuccessListener { result ->
                        //for each user in firestore
                        for (document in result) {
                            //get userid of the firestore document
                            val value = document.getString("authUserID")
                            //check that its correct user
                            if (value == thisUid) {
                                //get the list of alerts that users wants from firebase
                                val username = document.getString("username")
                                var usersAlerts = document["alerts"] as List<String>?
                                //populate alerts fragment with chips of alerts
                                if (usersAlerts != null) {
                                    for (alert in usersAlerts){
                                        addAlert(rootView, inflater, alertChipGroup, alert.toString())
                                    }
                                }
                                Log.d(TAG, ("${document.id} => ${document.data} " + usersAlerts))
                            }
                        }

                //dynamically add the alert to the "current" chip group

            }
    }


    private fun addAlert(
        rootView: View?,
        inflater: LayoutInflater,
        alertChipGroup: ChipGroup?,
        newAlertText: String
    ) {
        val chip = layoutInflater.inflate(R.layout.chip_layout, alertChipGroup, false) as Chip
        if (newAlertText != null) {
            chip.text = newAlertText

            val snackbar = Snackbar.make(rootView!!, "Alert Added", Snackbar.LENGTH_SHORT)
            snackbar.show()
            alertChipGroup!!.addView(chip)

        }
    }
}
