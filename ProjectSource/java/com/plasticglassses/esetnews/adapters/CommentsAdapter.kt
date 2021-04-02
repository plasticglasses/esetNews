package com.plasticglassses.esetnews.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.plasticglassses.esetnews.R
import com.plasticglassses.esetnews.commentsModel

/**
 * This class will be used to implement a recycler view containing the commemts for each article
 * The adapter connects the comments chip to the comment data if the feature was fully implemented
 */

class CommentsAdapter(private val commentsArrayList: MutableList<commentsModel>) :
    RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {
    val db = Firebase.firestore
//    var contextArray: Array<String> = emptyArray()

    /**
     * @param parent:ViewGroup The view group from the headline Activity page
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.card_marker, parent, false)

        return ViewHolder(v)
    }

    /**
     * populate the card_comments xml
     * @param holder - The view from Headline Activity
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val info = commentsArrayList[position]

        holder.commentText.text = info.getCommentText()
        holder.timestamp.text = info.getTimestamp()
        holder.docID.text = info.getDocID()

    }

    /**
     * The number of comments
     */
    override fun getItemCount(): Int {
        return commentsArrayList.size
    }


    /**
     * * Gets the xml ids from activity_headline and sets the data from the comments model to the fields
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        val commentText = itemView.findViewById<View>(R.id.commentText) as TextView
        var timestamp = itemView.findViewById<View>(R.id.timestamp) as TextView
        val docID = itemView.findViewById<View>(R.id.docID) as TextView

        init {
            itemView.setOnClickListener(this)
        }

        /**
        when a comment is selected from the recycler view, display the comment text
         */
        override fun onClick(v: View) {
            val msg = commentText.text
            Snackbar.make(v, "$msg are the best!", Snackbar.LENGTH_LONG).show()
        }
    }
}




