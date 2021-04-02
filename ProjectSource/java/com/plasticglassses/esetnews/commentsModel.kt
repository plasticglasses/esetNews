package com.plasticglassses.esetnews

/**
 * To display comments in the comments chip underneath each article
 * feature unfinished and not in final project
 */
class commentsModel() {

    private var LongCommentPackage: String =
        "" //comments are stored as a larhge string that need to be reformatted
    private var docID: String = ""
    private var commentText: String = ""
    private var timestamp: String = ""

    fun getLongCommentPackage(): String {
        return LongCommentPackage.toString()
    }

    fun getDocID(): String {
        return docID
    }

    fun getCommentText(): String {
        return commentText.toString()
    }

    fun getTimestamp(): String {
        return timestamp.toString()
    }

    fun setLongCommentPackage(LongCommentPackage: String) {
        this.LongCommentPackage = LongCommentPackage
    }

    fun setDocID(docID: String) {
        this.docID = docID
    }

    fun setCommentText(commentText: String) {
        this.commentText = commentText
    }

    fun setTimestamp(timestamp: String) {
        this.timestamp = timestamp
    }

    override fun toString(): String {
        return "comment made: " + this.getCommentText() + " on document " + this.getDocID() + " at " + this.getTimestamp()
    }

}