package com.plasticglassses.esetnews.adapters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.plasticglassses.esetnews.newsModel
import java.io.InputStream
import java.net.URL


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

        Glide.with(this).load(URL_TO_IMAGE).into(imageView);
        val bmp: Bitmap

        val imageUrl = info.getHeadlineImg()
        val `in` = URL(imageUrl).openStream()

        bmp = BitmapFactory.decodeStream(`in`)

        val img_results_experience = view.findViewById(R.id.img_results_experience)
        img_id.setImageBitmap(bmp)
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
        }

    }

    fun LoadImageFromWebOperations(url: String?): Drawable? {
        return try {
            val `is`: InputStream = URL(url).getContent() as InputStream
            Drawable.createFromStream(`is`, "src name")
        } catch (e: Exception) {
            null
        }
    }
}



