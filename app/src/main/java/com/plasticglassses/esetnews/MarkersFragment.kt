package com.plasticglassses.esetnews

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.plasticglassses.esetnews.adapters.MarkersAdapter

class MarkersFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val db = Firebase.firestore

        var rootView = inflater.inflate(R.layout.fragment_markers, container, false)

        //get documents from firestore and populate modles
        populateList(db) { markersArrayList ->
            Log.d("mylist", markersArrayList.toString())
            val recyclerMarkersView =
                rootView.findViewById<View>(R.id.markers_recycler_view) as RecyclerView
            val markersLayoutManager = LinearLayoutManager(activity)
            recyclerMarkersView.layoutManager = markersLayoutManager
            val headlineAdapter = MarkersAdapter(markersArrayList)
            recyclerMarkersView.adapter = headlineAdapter
        }

        return rootView
    }

    private fun populateList(db: FirebaseFirestore, callback: (ArrayList<markerModel>) -> Unit) {
        val list = ArrayList<markerModel>()
        var markerRef =
            db.collection("markers")
        markerRef.get().addOnSuccessListener { documents ->
            for (document in documents) {
                val any = if (document != null) {

                    //make model with data from firestore
                    val thisModel = markerModel()
                    thisModel.setLatitude(document.get("lat").toString())
                    thisModel.setLongitude(document.get("lng").toString())
                    thisModel.setMarkerText(document.get("text").toString())
                    thisModel.setUserID(document.get("user").toString())
                    thisModel.setFirebaseID(document.id)

                    //add to list so that recycler view can take data
                    list.add(thisModel)
                } else {
                    Log.d("POPULATE LIST", "No such document")
                }
                callback.invoke(list)

            }
        }
            .addOnFailureListener { exception ->
                Log.d("POPULATE LIST", "get failed with ", exception)
            }
    }

}