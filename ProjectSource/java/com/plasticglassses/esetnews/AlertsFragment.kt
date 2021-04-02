package com.plasticglassses.esetnews

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.app.NotificationCompat
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
import kotlin.collections.ArrayList as ArrayList1

/**
 * Fragement on the profile activity
 * display and edit alerts
 */
class AlertsFragment : Fragment() {

    private val TAG = "Alerts Fragment"
    private lateinit var auth: FirebaseAuth

    /**
     * on start of fragment, populate all users alerts
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //defining resources
        auth = Firebase.auth
        val db = Firebase.firestore
        var rootView = inflater.inflate(R.layout.fragment_alerts, container, false) //profile page is root
        val alertChipGroup = rootView?.findViewById<ChipGroup>(R.id.alertChipGroup) //on alert fragment

        val uID = getUserID() //get the user that is logged in

        /**
         * get document that holds users ids
         */
        getFirestoreID(uID, db) { fID ->
            Log.d(TAG, ("fID returned " + fID))
            //get array from firebase
            getUsersAlerts(fID) { alertArray ->
                Log.d(TAG, ("success alert array found " + alertArray))
                for (alert in alertArray) {
                    //add chip to ui
                    addNewAlertToXML(rootView, alertChipGroup, alert, fID)
                }
            }

        }

        /**
         * allow alerts to be added in the ui and updated in firebase
         */
        val addAlertButton = rootView.findViewById<Button>(R.id.addAlert)
        addAlertButton.setOnClickListener {
            val newAlertText = rootView?.findViewById<EditText>(R.id.newAlertText)
            //add a chip to the alerts fragment
            if (newAlertText != null) {
                //add alert to current alerts
                addSingleAlertToFirebase(rootView, inflater, alertChipGroup, newAlertText.text.toString(),uID, db)
                newAlertText.text.clear()
            }

        }




        //add suggested alerts
        val suggestedAlertChipGroup =
            rootView?.findViewById<ChipGroup>(R.id.suggestedAlertsChipGroup)
        val HCIChip = rootView.findViewById<Chip>(R.id.HCIChip)
        //allow the chip to be added to firebase and the ui
        HCIChip.setOnClickListener {
            addSingleAlertToFirebase(rootView, inflater, alertChipGroup, "HCI",uID, db)
            suggestedAlertChipGroup!!.removeView(HCIChip)
        }
        val CERNChip = rootView.findViewById<Chip>(R.id.CERNChip)
        //allow the chip to be added to firebase and the ui

        CERNChip.setOnClickListener {
            addSingleAlertToFirebase(rootView, inflater, alertChipGroup, "CERN",uID, db)
            suggestedAlertChipGroup!!.removeView(CERNChip)
        }
        val CCChip = rootView.findViewById<Chip>(R.id.ClimateChangeChip)
        //allow the chip to be added to firebase and the ui
        CCChip.setOnClickListener {
            addSingleAlertToFirebase(rootView, inflater, alertChipGroup, "Climate Change",uID, db)
            suggestedAlertChipGroup!!.removeView(CCChip)
        }
        val PlasticChip = rootView.findViewById<Chip>(R.id.PlasticChip)
        //allow the chip to be added to firebase and the ui
        PlasticChip.setOnClickListener {
            addSingleAlertToFirebase(rootView, inflater, alertChipGroup, "Plastic",uID, db)
            suggestedAlertChipGroup!!.removeView(PlasticChip)
        }
        val VRChip = rootView.findViewById<Chip>(R.id.VRChip)
        //allow the chip to be added to firebase and the ui
        VRChip.setOnClickListener {
            addSingleAlertToFirebase(rootView, inflater, alertChipGroup, "VR",uID, db)
            suggestedAlertChipGroup!!.removeView(VRChip)
        }
        val nasaChip = rootView.findViewById<Chip>(R.id.NASAChip)
        //allow the chip to be added to firebase and the ui
        nasaChip.setOnClickListener {
            addSingleAlertToFirebase(rootView, inflater, alertChipGroup, "NASA",uID, db)
            suggestedAlertChipGroup!!.removeView(nasaChip)
        }
        val unityChip = rootView.findViewById<Chip>(R.id.UnityChip)
        //allow the chip to be added to firebase and the ui
        unityChip.setOnClickListener {
            addSingleAlertToFirebase(rootView, inflater, alertChipGroup, "Unity",uID, db)
            suggestedAlertChipGroup!!.removeView(unityChip)
        }

        return rootView
    }



    /*
    add a single alert to firebase to correct docuemnt users/userid/alert array
     */
    private fun addSingleAlertToFirebase(rootView: View,inflater: LayoutInflater,
                                         alertChipGroup: ChipGroup?,newAlertText: String,uID: String,db: FirebaseFirestore) {
        //add new alert to firebase
        getFirestoreID(uID, db) { fID ->
            Log.d(TAG, ("fID returned $fID"))
            getUsersAlerts(fID) { alertArray ->
                Log.d(TAG, ("success alert array found $alertArray"))
                alertArray.add(newAlertText)
                updateAlertDocument(alertArray, db, fID)
            }
            addNewAlertToXML(rootView, alertChipGroup, newAlertText, fID)
        }

    }

    /**
    Get the id of the document which has the same authUserID sa the surrent loged in user
     */
    private fun getFirestoreID(
        uID: String,
        db: FirebaseFirestore,
        callback: (String) -> Unit
    ): String {
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

    /**
    get the current logged in user id
     */
    private fun getUserID(): String {
        var thisUid = ""

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            thisUid = user.uid
            Log.d(TAG, ("success we got the UID" + thisUid))

            // User is signed in
        }
        return thisUid
    }

    /**
    Add alert to firebase
     */
    private fun updateAlertDocument(
        alertsList: ArrayList1<String>,
        db: FirebaseFirestore,
        fID: String?
    ) {
        // Update one field, creating the document if it does not already exist.
        val data = hashMapOf("alerts" to alertsList)

        db.collection("users").document(fID!!)
            .set(data, SetOptions.merge())
    }

    /**
    get the current users alert array from firetore
     */
    private fun getUsersAlerts(
        docID: String?, callback: (ArrayList1<String>) -> Unit
    ): ArrayList1<String>? {
        var usersAlerts = arrayListOf<String>()
        val db = Firebase.firestore
        db.collection("users").document(docID!!)
            .get()
            .addOnSuccessListener { result ->
                //val username = result.getString("username")
                usersAlerts = (result["alerts"] as ArrayList1<String>)
                //populate alerts fragment with chips of alerts
                Log.d(TAG, ("${result.id} => ${result.data} " + usersAlerts))
                callback.invoke(usersAlerts)
            }
        return usersAlerts
    }

    /**
    add an alert to the xml with style
     */
    private fun addSuggestedAlertToXML(
        rootView: View?,
        alertChipGroup: ChipGroup?,
        newAlertText: String
    ) {
        val chip = layoutInflater.inflate(R.layout.chip_layout, alertChipGroup, false) as Chip
        chip.text = newAlertText

        Snackbar.make(rootView!!, "Alert Added", Snackbar.LENGTH_SHORT).show()
        alertChipGroup!!.addView(chip)

    }

    /**
    add an alert to the xml with style
     */
    private fun addNewAlertToXML(
        rootView: View?,
        alertChipGroup: ChipGroup?,
        newAlertText: String,
        fID: String
    ) {
        val chip = layoutInflater.inflate(R.layout.chip_layout, alertChipGroup, false) as Chip
        chip.text = newAlertText

        val snackbar = Snackbar.make(rootView!!, "Alert Added", Snackbar.LENGTH_SHORT)
        snackbar.show()
        alertChipGroup!!.addView(chip)

        chip.setOnCloseIconClickListener {
            //remove from xml
            removeAlertChipXML(alertChipGroup, chip)

            //remove from firebase
            removeAlertFromFirebase(newAlertText, fID)
            Log.d(TAG, ("Alert Removed"))
        }

    }

    /**
     * get the alert, remove from alert array for user
     */
    private fun removeAlertFromFirebase(alertToRemove: String, fID: String) {
        var usersAlerts = arrayListOf<String>()
        val db = Firebase.firestore
        db.collection("users").document(fID)
            .get()
            .addOnSuccessListener { result ->
                //val username = result.getString("username")
                usersAlerts = (result["alerts"] as ArrayList1<String>)
                usersAlerts.remove(alertToRemove)

                updateAlertDocument(usersAlerts, db, fID)

            }
    }

    /**
     * remove the chip from the ui
     */
    private fun removeAlertChipXML(alertChipGroup: ChipGroup, chip: Chip) {
        alertChipGroup.removeView(chip)
    }
}
