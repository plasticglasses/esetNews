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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
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
        val db = Firebase.firestore
        var rootView = inflater.inflate(R.layout.fragment_alerts, container, false)
        val alertChipGroup = rootView?.findViewById<ChipGroup>(R.id.alertChipGroup)

        val addAlertButton = rootView.findViewById<Button>(R.id.addAlert)
        addAlertButton.setOnClickListener {
            val newAlertText = rootView?.findViewById<EditText>(R.id.newAlertText)
            //add a chip to the alerts fragment
            if (newAlertText != null) {
                addAlert(rootView, inflater, alertChipGroup, newAlertText.text.toString())
                //addAlertToFirebase(newAlertText.text.toString())
                newAlertText.text.clear()
            }

        }


        val uID = getUserID(auth)
        val docID = getFirestoreID(uID, db)

        val alertsList = arrayListOf<String>("Unity")
        addAlertToFirebase(alertsList, db)

        Log.d(TAG, ("uner the hanging tree " + uID))
        Log.d(TAG, ("uner the hanging tree " + docID))

//        var usersAlerts = getUsersAlerts(docID)
//        //var alerts = getUsersAlerts(auth, rootView, inflater, alertChipGroup, uID, docID)
//        if (usersAlerts != null) {
//            Log.d(TAG, ("success we got the rabbi " + usersAlerts))
//            for (alert in usersAlerts) {
//                addAlert(rootView, inflater, alertChipGroup, alert.toString())
//            }
//        }

        return rootView
    }

    /*
    Get the id of the document which has the same authUserID sa the surrent loged in user
     */
    private fun getFirestoreID(uID: String, db: FirebaseFirestore): String {
        var fID = ""

        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                //for each user in firestore
                for (document in result) {
                    //get userid of the firestore document
                    val docID = document.getString("authUserID")
                    //check that its correct user
                    if (docID == uID) {
                        fID = document.id
                        Log.d(TAG, ("found user firestore data" + fID))
                    }
                }
            }
        return fID
    }

    /*
    get the current logged in user id
     */
    private fun getUserID(auth: FirebaseAuth): String {
        var thisUid = ""

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            thisUid = user.uid
            Log.d(TAG, ("success we got the UID" + thisUid))

            // User is signed in
        }
        return thisUid
    }

    /*
    Add alert to firebase
     */
    private fun addAlertToFirebase(alertsList: ArrayList<String>, db: FirebaseFirestore) {
        var docID = "O4a9R4g9v9nooza5CELd"
        // Update one field, creating the document if it does not already exist.
        val data = hashMapOf("alerts" to alertsList)

        db.collection("users").document("O4a9R4g9v9nooza5CELd")
            .set(data, SetOptions.merge())
    }

    /*
    get the current users alert array from firetore
     */
    private fun getUsersAlerts(
        docID: String?
    ): List<String>? {
        var usersAlerts = listOf<String>()
        val db = Firebase.firestore
        db.collection("users").document(docID!!)
            .get()
            .addOnSuccessListener { result ->
                //val username = result.getString("username")
                usersAlerts = (result["alerts"] as List<String>?)!!
                //populate alerts fragment with chips of alerts
                Log.d(TAG, ("${result.id} => ${result.data} " +usersAlerts))
            }
        return usersAlerts
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
