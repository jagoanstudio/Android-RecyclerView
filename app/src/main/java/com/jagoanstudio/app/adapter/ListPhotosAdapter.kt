package com.jagoanstudio.app.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jagoanstudio.app.R
import com.jagoanstudio.app.databinding.ItemListPhotosBinding
import com.jagoanstudio.app.model.ModelPhotos
import com.squareup.picasso.Picasso

class ListPhotosAdapter (private val activity: Activity, private val itemList: List<ModelPhotos>) : RecyclerView.Adapter<ListPhotosAdapter.ViewHolder>() {

    var onItemClick: ((ModelPhotos) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(
        R.layout.item_list_photos, parent, false))

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var binding: ItemListPhotosBinding = ItemListPhotosBinding.bind(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        Picasso.get().load(item.thumbnailUrl).into(holder.binding.imageviewPhotosThumbnail)

        holder.binding.imageviewPhotosThumbnail.setOnClickListener {
            onItemClick?.invoke(item)
        }
    }

    override fun getItemCount() = itemList.size

}