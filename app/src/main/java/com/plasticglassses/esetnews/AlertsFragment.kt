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

        val uID = getUserID(auth)
        var docID = ""
        getFirestoreID(uID, db) { result ->
            Log.d(TAG, ("fID returned " + result))
            getUsersAlerts(result) { result ->
                if (result != null) {
                    Log.d(TAG, ("success alert array found " + result))
                    for (alert in result) {
                        addAlertToXML(rootView, inflater, alertChipGroup, alert)
                    }
                }
            }
        }

        val addAlertButton = rootView.findViewById<Button>(R.id.addAlert)
        addAlertButton.setOnClickListener {
            val newAlertText = rootView?.findViewById<EditText>(R.id.newAlertText)
            //add a chip to the alerts fragment
            if (newAlertText != null) {
                //add alert to current alerts
                addAlertToXML(rootView, inflater, alertChipGroup, newAlertText.text.toString())

                addSingleAlertToFirebase(uID, db, newAlertText.text.toString())
                newAlertText.text.clear()
            }

        }

        //for alert in suggested alert chip group
            //add onclick method to add alert
        val suggestedAlertChipGroup = rootView?.findViewById<ChipGroup>(R.id.suggestedAlertsChipGroup)
        val HCIChip = rootView.findViewById<Chip>(R.id.HCIChip)
        HCIChip.setOnClickListener{
            addAlertToXML(rootView, inflater, alertChipGroup, "HCI")
            addSingleAlertToFirebase(uID, db, "HCI")
            suggestedAlertChipGroup!!.removeView(HCIChip)
        }
        val CERNChip = rootView.findViewById<Chip>(R.id.CERNChip)
        CERNChip.setOnClickListener{
            addAlertToXML(rootView, inflater, alertChipGroup, "CERN")
            addSingleAlertToFirebase(uID, db, "CERN")
            suggestedAlertChipGroup!!.removeView(CERNChip)
        }
        val CCChip = rootView.findViewById<Chip>(R.id.ClimateChangeChip)
        CCChip.setOnClickListener{
            addAlertToXML(rootView, inflater, alertChipGroup, "Climate Change")
            addSingleAlertToFirebase(uID, db, "Climate Change")
            suggestedAlertChipGroup!!.removeView(CCChip)
        }
        val PlasticChip = rootView.findViewById<Chip>(R.id.PlasticChip)
        PlasticChip.setOnClickListener{
            addAlertToXML(rootView, inflater, alertChipGroup, "Plastic")
            addSingleAlertToFirebase(uID, db, "Plastic")
            suggestedAlertChipGroup!!.removeView(PlasticChip)
        }
        val VRChip = rootView.findViewById<Chip>(R.id.VRChip)
        VRChip.setOnClickListener{
            addAlertToXML(rootView, inflater, alertChipGroup, "VR")
            addSingleAlertToFirebase(uID, db, "VR")
            suggestedAlertChipGroup!!.removeView(VRChip)
        }
        val nasaChip = rootView.findViewById<Chip>(R.id.NASAChip)
        nasaChip.setOnClickListener{
            addAlertToXML(rootView, inflater, alertChipGroup, "NASA")
            addSingleAlertToFirebase(uID, db, "NASA")
            suggestedAlertChipGroup!!.removeView(nasaChip)
        }
        val unityChip = rootView.findViewById<Chip>(R.id.UnityChip)
        unityChip.setOnClickListener{
            addAlertToXML(rootView, inflater, alertChipGroup, "Unity")
            addSingleAlertToFirebase(uID, db, "Unity")
            suggestedAlertChipGroup!!.removeView(unityChip)
        }

        return rootView
    }

    private fun addSingleAlertToFirebase(uID: String, db: FirebaseFirestore, newAlertText: String) {
        //add new alert to firebase
        getFirestoreID(uID, db) { fID ->
            Log.d(TAG, ("fID returned " + fID))
            getUsersAlerts(fID) { alertArray ->
                if (alertArray != null) {
                    Log.d(TAG, ("success alert array found " + alertArray))
                    alertArray!!.add(newAlertText)
                    addAlertToDocument(alertArray!!, db, fID)
                }
            }
        }
    }

    /*
    Get the id of the document which has the same authUserID sa the surrent loged in user
     */
    private fun getFirestoreID(uID: String, db: FirebaseFirestore, callback:(String) -> Unit): String {
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
                        callback.invoke(fID)
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
    private fun addAlertToDocument(alertsList: ArrayList<String>, db: FirebaseFirestore, fID: String?) {
        // Update one field, creating the document if it does not already exist.
        val data = hashMapOf("alerts" to alertsList)

        db.collection("users").document(fID!!)
            .set(data, SetOptions.merge())
    }

    /*
    get the current users alert array from firetore
     */
    private fun getUsersAlerts(
        docID: String?, callback:(ArrayList<String>) -> Unit
    ): ArrayList<String>? {
        var usersAlerts = arrayListOf<String>()
        val db = Firebase.firestore
        db.collection("users").document(docID!!)
            .get()
            .addOnSuccessListener { result ->
                //val username = result.getString("username")
                usersAlerts = (result["alerts"] as ArrayList<String>)
                //populate alerts fragment with chips of alerts
                Log.d(TAG, ("${result.id} => ${result.data} " +usersAlerts))
                callback.invoke(usersAlerts)
            }
        return usersAlerts
    }
    
    private fun addAlertToXML(
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
