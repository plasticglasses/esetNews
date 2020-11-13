package com.plasticglassses.esetnews

class newsModel(headline: String, urlToImage: String, author: String, publishedAt: String
) {
    private var headline: String = ""
    private var headlineImg: String = ""
    private var timestamp: String = ""
    private var publisher: String = ""

    fun newsModel(headline: String, headlineImg: String, publisher: String, timestamp: String){
        setHeadline(headline)
        setHeadlineImg(headlineImg)
        setPublisher(publisher)
        setTimestamp(timestamp)
    }

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

    override fun toString(): String{
        return "headline: " + this.getHeadline() +  "Img: " + this.getHeadlineImg() + "timestamp: " + this.getTimestamp() + "publisher: " + this.getPublisher()
    }

}