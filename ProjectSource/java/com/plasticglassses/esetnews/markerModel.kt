package com.plasticglassses.esetnews

/**
 * To display markers in the markers chip underneath each article
 */
class markerModel() {

    private var latitude: String = ""
    private var longitude: String = ""
    private var userID: String = ""
    private var markerText: String = ""
    private var firebaseID: String = ""

    fun getLatitude(): String{
        return latitude.toString()
    }

    fun getLongitude(): String{
        return longitude
    }

    fun getUserID(): String{
        return userID.toString()
    }

    fun getMarkerText(): String{
        return markerText.toString()
    }

    fun getFirebaseID(): String{
        return firebaseID
    }

    fun setLatitude(latitude: String){
        this.latitude = latitude
    }

    fun setLongitude(longitude: String){
        this.longitude = longitude
    }

    fun setUserID(userID: String){
        this.userID = userID
    }

    fun setMarkerText(markerText: String){
        this.markerText = markerText
    }

    fun setFirebaseID(firebaseID: String){
        this.firebaseID = firebaseID
    }

    override fun toString(): String{
        return "marker at " + this.getLatitude() + " latitude and longitude value " + this.getLongitude() + " with text " + this.getMarkerText() + " written by " + this.getUserID()
    }

}