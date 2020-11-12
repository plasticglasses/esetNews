package com.plasticglassses.esetnews

class newsModel{
    private var headline: String = ""
    private var headlineImg: String = ""
    private var timestamp: String = ""
    private var publisher: String = ""

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
}