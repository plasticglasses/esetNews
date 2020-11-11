package com.plasticglassses.esetnews

import java.sql.Time

class newsModel{
    private var headline: String = ""
    private var headlineImg: Int = 0
    private var timestamp: String = ""

    fun getHeadline(): String{
        return headline.toString()
    }

    fun getHeadlineImg(): Int{
        return headlineImg
    }

    fun getTimestamp(): String{
        return timestamp.toString()
    }

    fun setHeadline(headline: String){
        this.headline = headline
    }

    fun setHeadlineImg(img: Int){
        this.headlineImg = img
    }

    fun setTimestamp(timestamp: String){
        this.timestamp = timestamp
    }

}