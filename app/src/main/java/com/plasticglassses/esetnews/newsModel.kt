package com.plasticglassses.esetnews

class newsModel() {

    private var headline: String = ""
    private var headlineImg: String = ""
    private var timestamp: String = ""
    private var publisher: String = ""
    private var firebaseDocID: String = ""

    fun getHeadline(): String{
        return headline.toString()
    }

    fun getHeadlineImg(): String{
        return headlineImg
    }

    fun getPublisher(): String{
        return publisher.toString()
    }

    fun getTimestamp(): String{
        return timestamp.toString()
    }

    fun getFirebaseDocID(): String{
        return firebaseDocID.toString()
    }

    fun setHeadline(headline: String){
        this.headline = headline
    }

    fun setHeadlineImg(img: String){
        this.headlineImg = img
    }

    fun setTimestamp(timestamp: String){
        this.timestamp = timestamp
    }

    fun setPublisher(publisher: String){
        this.publisher = publisher
    }

    fun setFirebaseDocID(id: String){
        this.firebaseDocID = id
    }

    override fun toString(): String{
        return "headline: " + this.getHeadline() +  "Img: " + this.getHeadlineImg() + "timestamp: " + this.getTimestamp() + "publisher: " + this.getPublisher()
    }

}