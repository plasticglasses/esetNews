package com.plasticglassses.esetnews.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.plasticglassses.esetnews.R
import com.plasticglassses.esetnews.newsModel

class NewsAdapter(private val headlineArrayList: MutableList<newsModel>): RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent:ViewGroup,viewType:Int):ViewHolder{
        val inflater= LayoutInflater.from(parent.context)
        val v=inflater.inflate(R.layout.card_headline,parent,false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder:ViewHolder,position:Int){
        val info=headlineArrayList[position]

        holder.txtMsg.text=info.getHeadline()
    }

    override fun getItemCount():Int{
        return headlineArrayList.size
    }


    inner

    class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView),
        View.OnClickListener{

        var imgView=itemView.findViewById<View>(R.id.icon)as ImageView
        var txtMsg=itemView.findViewById<View>(R.id.firstline)as TextView

        init{
            itemView.setOnClickListener(this)
        }

        override fun onClick(v:View){
            val msg=txtMsg.text
            val snackbar= Snackbar.make(v,"$msg are the best!",Snackbar.LENGTH_LONG)
            snackbar.show()
        }

    }


}

