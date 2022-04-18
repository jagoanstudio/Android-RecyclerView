package com.jagoanstudio.app.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.jagoanstudio.app.R
import com.jagoanstudio.app.databinding.ItemListAlbumsBinding
import com.jagoanstudio.app.model.ModelAlbums
import com.jagoanstudio.app.ui.photos.DetailPhotosActivity

class ListAlbumsAdapter (private val activity: Activity, private val itemList: List<ModelAlbums>) : RecyclerView.Adapter<ListAlbumsAdapter.ViewHolder>() {

    private lateinit var listPhotosAdapter: ListPhotosAdapter

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(
        R.layout.item_list_albums, parent, false))

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var binding: ItemListAlbumsBinding = ItemListAlbumsBinding.bind(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        holder.binding.textviewAlbumsTitle.text = item.title

        if (item.photos != null) {
            val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            listPhotosAdapter = ListPhotosAdapter(activity, item.photos!!)
            holder.binding.recyclerPhotos.layoutManager = linearLayoutManager
            holder.binding.recyclerPhotos.adapter = listPhotosAdapter

            listPhotosAdapter.onItemClick = { photos ->
                val intent = Intent(activity, DetailPhotosActivity::class.java)
                intent.putExtra("photos", Gson().toJson(photos))
                activity.startActivity(intent)
                activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }
        }
    }

    override fun getItemCount() = itemList.size

}