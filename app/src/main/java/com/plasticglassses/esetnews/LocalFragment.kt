package com.plasticglassses.esetnews

import android.content.Intent
import android.os.Bundle
import android.system.Os.remove
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class LocalFragment : Fragment() {

    private lateinit var mMap: GoogleMap
    private lateinit var auth: FirebaseAuth

    private val TAG = "Local Fragment"

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        mMap = googleMap
        val db = Firebase.firestore


        val docRef = db.collection("markers")
        docRef.get()
            .addOnSuccessListener { documents ->
                if (documents != null) {

                    for (document in documents) {

                        var latitude = document.get("lat").toString()
                        var longitude = document.get("lng").toString()
                        var markerText = document.get("text").toString()

                        val newMarker = LatLng(latitude.toDouble(), longitude.toDouble())
                        mMap.addMarker(MarkerOptions().position(newMarker).title(markerText))
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(newMarker))

                        Log.d(TAG, "${document.id} => ${document.data}")
                    }
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }

        // Add a marker in Sydney and move the camera

        mMap.setMinZoomPreference(15.0f)

        mMap.setOnMapClickListener { point ->

            //get points from map
            val lng = point.longitude.toString()
            val lat = point.latitude.toString()

            //start pop up to decide what to do
            val intent = Intent(activity, LocalPopUpWindow::class.java)
            intent.putExtra("data", "windy window")
            intent.putExtra("lat", lat)
            intent.putExtra("lng", lng)
            startActivity(intent)
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView = inflater.inflate(R.layout.fragment_local, container, false)


        return rootView
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}
