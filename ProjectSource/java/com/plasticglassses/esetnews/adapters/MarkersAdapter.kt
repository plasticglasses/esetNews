package com.plasticglassses.esetnews.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.plasticglassses.esetnews.R
import com.plasticglassses.esetnews.markerModel

/**
 * This class will be used to implement a recycler view containing the markers for each local point
 * The adapter connects the markers chip to the marker data
 * Is called from Profile, markers fragment to display all the markers that have been set
 */

class MarkersAdapter(private val markerArrayList: MutableList<markerModel>) :
    RecyclerView.Adapter<MarkersAdapter.ViewHolder>() {
    val db = Firebase.firestore
//    var contextArray: Array<String> = emptyArray()

    /**
     * @param parent:ViewGroup The view group from the Profile - markers Fragment page
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.card_marker, parent, false)

        return ViewHolder(v)
    }

    /**
     * populate the card_markers xml with each marker data
     * @param holder - The view from Markers Fragment
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val info = markerArrayList[position]

        holder.uID.text = info.getUserID()
        holder.txtMsg.text = info.getMarkerText()
        holder.lng.text = info.getLongitude()
        holder.lat.text = info.getLatitude()
        holder.docID.text = info.getFirebaseID()

        //allow markers to be delete from firebase and removed from the map once the activity restarts
        holder.deleteButton.setOnClickListener(View.OnClickListener {

            db.collection("markers").document(info.getFirebaseID())
                .delete()
                .addOnSuccessListener { Log.d("Markers", "DocumentSnapshot successfully deleted!") }
                .addOnFailureListener { e -> Log.w("Markers", "Error deleting document", e) }
        })
    }

    /**
     * The number of markers
     */
    override fun getItemCount(): Int {
        return markerArrayList.size
    }

    /**
     *  Gets the xml ids from markers_hip and sets the data from the comments model to the fields
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        val lng = itemView.findViewById<View>(R.id.longitudeText) as TextView
        var txtMsg = itemView.findViewById<View>(R.id.markerTextView) as TextView
        val lat = itemView.findViewById<View>(R.id.latitudeText) as TextView
        val uID = itemView.findViewById<View>(R.id.userText) as TextView
        val deleteButton = itemView.findViewById<View>(R.id.deleteButton) as Button
        val docID = itemView.findViewById<View>(R.id.firebaseIDMarker) as TextView

        init {
            itemView.setOnClickListener(this)
        }

        /**
         *when a marker is selected from the recycler view, display the marker text
         */
        override fun onClick(v: View) {
            val msg = txtMsg.text
            Snackbar.make(v, "$msg are the best!", Snackbar.LENGTH_LONG).show()
        }
    }
}




