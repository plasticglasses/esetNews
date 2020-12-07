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
import com.plasticglassses.esetnews.markerModel
import com.plasticglassses.esetnews.newsModel

class MarkersAdapter(private val markerArrayList: MutableList<markerModel>): RecyclerView.Adapter<MarkersAdapter.ViewHolder>() {

//    var contextArray: Array<String> = emptyArray()

    override fun onCreateViewHolder(parent:ViewGroup,viewType:Int):ViewHolder{
        val inflater= LayoutInflater.from(parent.context)
        val v=inflater.inflate(R.layout.card_marker,parent,false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder:ViewHolder,position:Int){
        val info=markerArrayList[position]

        holder.uID.text = info.getUserID()
        holder.txtMsg.text=info.getMarkerText()
        holder.lng.text = info.getLongitude()
        holder.lat.text = info.getLatitude()

    }


    override fun getItemCount():Int{
        return markerArrayList.size
    }


    inner class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView),
        View.OnClickListener{

        val lng = itemView.findViewById<View>(R.id.longitudeText) as TextView
        var txtMsg=itemView.findViewById<View>(R.id.markerTextView)as TextView
        val lat = itemView.findViewById<View>(R.id.latitudeText) as TextView
        val uID = itemView.findViewById<View>(R.id.userText) as TextView

        init{
            itemView.setOnClickListener(this)
        }

        override fun onClick(v:View){
            val msg=txtMsg.text
            Snackbar.make(v,"$msg are the best!",Snackbar.LENGTH_LONG).show()

            //i need to check which type of newds article this is, tech, science or gerneal so that we cna find the articel more easily


//            //open separate view
//            val intent = Intent(v.context, HeadlineActivity::class.java)
//            val id=txtDocID.text
//            intent.putExtra("id", id)
//            //intent.putExtra("contextArray", contextArray)
//            v.context.startActivity(intent)

        }

    }

}




