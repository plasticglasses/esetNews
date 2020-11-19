package com.plasticglassses.esetnews

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class LocalFragment : Fragment() {
    private lateinit var mMap: GoogleMap
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

        // Add a marker in Sydney and move the camera
        val swanseamarker1 = LatLng(51.620378, -3.941528)
        mMap.addMarker(MarkerOptions().position(swanseamarker1).title("Rat spot reported on 23/10/2020 at 7pm by Liz"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(swanseamarker1))

        val swanseamarker2 = LatLng(51.616083, -3.946367)
        mMap.addMarker(MarkerOptions().position(swanseamarker2).title("Rat spot reported on 10/09/2020 at 5pm by Liz"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(swanseamarker2))

        val swanseamarker3 = LatLng(51.620486, -3.941152)
        mMap.addMarker(MarkerOptions().position(swanseamarker3).title("Swansea Castle, a spot of interest from 1107AD by Liz"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(swanseamarker3))

        val sm4 = LatLng(51.6205, -3.9413)
        mMap.addMarker(MarkerOptions().position(sm4).title("Rat Sport reported on 09/10/2020"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sm4))

        mMap.setMinZoomPreference(15.0f)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_local, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}