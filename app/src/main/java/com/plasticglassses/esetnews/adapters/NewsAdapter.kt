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

class NewsAdapter(private val headlineArrayList: MutableList<newsModel>): RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    var contextArray: Array<String> = emptyArray()

    override fun onCreateViewHolder(parent:ViewGroup,viewType:Int):ViewHolder{
        val inflater= LayoutInflater.from(parent.context)
        val v=inflater.inflate(R.layout.card_headline,parent,false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder:ViewHolder,position:Int){
        val info=headlineArrayList[position]

        holder.txtDocID.text = info.getFirebaseDocID()

        holder.txtMsg.text=info.getHeadline()
        val dateString = info.getTimestamp().take(10)
        val timeString = info.getTimestamp().drop(11).take(8)
        holder.headlineTimestamp.text = dateString + " " + timeString

        holder.txtFilePath.text = info.getFirebasePath()

        //holder.author.text = info.getPublisher()
        //holder.headlineImg.setImageDrawable(info.getHeadlineImg())

        //images
        var image = info.getHeadlineImg()
        if (image !== null) {
            Glide.with(holder.headlineImg.getContext())
                .load(image)
                .into(holder.headlineImg)
        } else {
            holder.headlineImg.setImageResource(R.drawable.ic_launcher_background)
        }


    }


    override fun getItemCount():Int{
        return headlineArrayList.size
    }


    inner class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView),
        View.OnClickListener{

        val headlineTimestamp = itemView.findViewById<View>(R.id.timestamp) as TextView
        var txtMsg=itemView.findViewById<View>(R.id.headline)as TextView
        val author = itemView.findViewById<View>(R.id.author) as TextView
        val headlineImg = itemView.findViewById<View>(R.id.headlineImg) as ImageView
        val txtDocID = itemView.findViewById<View>(R.id.docID) as TextView
        val txtFilePath = itemView.findViewById<View>(R.id.filePathText) as TextView

        init{
            itemView.setOnClickListener(this)
        }

        override fun onClick(v:View){
            val msg=txtMsg.text
            Snackbar.make(v,"$msg are the best!",Snackbar.LENGTH_LONG).show()

            //i need to check which type of newds article this is, tech, science or gerneal so that we cna find the articel more easily


            //open separate view
            val intent = Intent(v.context, HeadlineActivity::class.java)
            val id=txtDocID.text
            val filepath=txtFilePath.text
            intent.putExtra("id", id)
            intent.putExtra("filePath", filepath)
            //intent.putExtra("contextArray", contextArray)
            v.context.startActivity(intent)

        }

    }

}




