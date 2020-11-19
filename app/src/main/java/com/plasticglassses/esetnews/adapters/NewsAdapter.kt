package com.plasticglassses.esetnews.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.plasticglassses.esetnews.HeadlineContextFragment
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
        holder.headlineTimestamp.text = info.getTimestamp()
        holder.author.text = info.getPublisher()
        //holder.headlineImg.setImageDrawable(info.getHeadlineImg())
        if (info.getHeadlineImg() !== null) {
            Glide.with(holder.headlineImg.getContext())
                .load(info.getHeadlineImg())
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


        init{
            itemView.setOnClickListener(this)
        }

        override fun onClick(v:View){
            val msg=txtMsg.text
            val snackbar= Snackbar.make(v,"$msg are the best!",Snackbar.LENGTH_LONG)
            snackbar.show()

            val activity=v!!.context as AppCompatActivity
            val contentFragment = HeadlineContextFragment()
            activity.supportFragmentManager.beginTransaction().replace(R.id.main, contentFragment).addToBackStack(null).commit()
        }

    }

}




