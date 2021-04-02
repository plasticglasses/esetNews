package com.plasticglassses.esetnews.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.plasticglassses.esetnews.HeadlineActivity
import com.plasticglassses.esetnews.R
import com.plasticglassses.esetnews.newsModel

/**
 * This class will be used to implement a recycler view containing the news articles
 * it is reused on the home, science and tech fragments
 * The adapter connects the headline chip to the headline data
 */

class NewsAdapter(private val headlineArrayList: MutableList<newsModel>) :
    RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    var contextArray: Array<String> = emptyArray()

    /**
     * @param parent:ViewGroup The view group from the fragment page that teh article is displayed on.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.card_headline, parent, false)

        return ViewHolder(v)
    }


    /**
     * populate the card_headline xml
     * @param holder - The view from Headline Activity
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val info = headlineArrayList[position]

        holder.txtDocID.text = info.getFirebaseDocID()

        holder.txtMsg.text = info.getHeadline()
        val dateString = info.getTimestamp()
            .take(10) //split the timestamp and only get date for better formatting
        val timeString = info.getTimestamp().drop(11)
            .take(8)//split the timestamp and only get time for better formatting
        holder.headlineTimestamp.text = "$dateString $timeString"

        holder.txtFilePath.text = info.getFirebasePath()

        //holder.author.text = info.getPublisher()
        //holder.headlineImg.setImageDrawable(info.getHeadlineImg())

        //images, display the headline image using glide
        val image = info.getHeadlineImg()
        Glide.with(holder.headlineImg.getContext())
            .load(image)
            .into(holder.headlineImg)


    }

    /**
     * The number of headlines
     */
    override fun getItemCount(): Int {
        return headlineArrayList.size
    }


    /**
     * * Gets the xml ids from activity main fragments and sets the data from the comments model to the fields
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        val headlineTimestamp = itemView.findViewById<View>(R.id.timestamp) as TextView
        var txtMsg = itemView.findViewById<View>(R.id.headline) as TextView
        val author = itemView.findViewById<View>(R.id.author) as TextView
        val headlineImg = itemView.findViewById<View>(R.id.headlineImg) as ImageView
        val txtDocID = itemView.findViewById<View>(R.id.docID) as TextView
        val txtFilePath = itemView.findViewById<View>(R.id.filePathText) as TextView

        init {
            itemView.setOnClickListener(this)
        }

        /**
         * when a headline is selected from the recycler view, display the headline and then
         * display the article in full view mode
         */
        override fun onClick(v: View) {
            val msg = txtMsg.text
            Snackbar.make(v, "$msg are the best!", Snackbar.LENGTH_LONG).show()

            //i need to check which type of newds article this is, tech, science or gerneal so that we cna find the articel more easily


            //open separate view of activity headline and open the full document
            val intent = Intent(v.context, HeadlineActivity::class.java)
            val id = txtDocID.text
            val filepath = txtFilePath.text
            intent.putExtra("id", id)//pass in the id so the document can be found
            //intent.putExtra("contextArray", contextArray)
            v.context.startActivity(intent)

        }

    }

}




