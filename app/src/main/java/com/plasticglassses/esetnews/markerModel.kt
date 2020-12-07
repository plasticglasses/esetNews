package com.plasticglassses.esetnews

class markerModel() {

    private var latitude: String = ""
    private var longitude: String = ""
    private var userID: String = ""
    private var markerText: String = ""

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

    override fun toString(): String{
        return "marker at " + this.getLatitude() + " latitude and longitude value " + this.getLongitude() + " with text " + this.getMarkerText() + " written by " + this.getUserID()
    }

}