package com.jagoanstudio.app.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.jagoanstudio.app.R
import com.jagoanstudio.app.databinding.ItemListPostsBinding
import com.jagoanstudio.app.model.ModelPosts
import com.jagoanstudio.app.utils.view.ViewText

class ListPostsAdapter (private val activity: Activity, private val itemList: List<ModelPosts>) : RecyclerView.Adapter<ListPostsAdapter.ViewHolder>() {

    var onItemClick: ((ModelPosts) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(
        R.layout.item_list_posts, parent, false))

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var binding: ItemListPostsBinding = ItemListPostsBinding.bind(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        holder.binding.textviewPostsTitle.text = item.title

        holder.binding.textviewPostsBody.text = item.body

        if (item.name != null) {
            val name = ViewText.splitFullname(item.name)
            val drawableProfile = TextDrawable
                .builder()
                .beginConfig()
                .bold()
                .textColor(ContextCompat.getColor(activity, R.color.colorWhite))
                .endConfig()
                .buildRoundRect((name), ColorGenerator.MATERIAL.getColor(name), 30)
            holder.binding.textviewPostsAvatar.setImageDrawable(drawableProfile)

            holder.binding.textviewPostsName.text = item.name
        }

        if (item.company != null) {
            holder.binding.textviewPostsCompanyName.text = item.company?.name
        }

        holder.binding.relativePosts.setOnClickListener {
            onItemClick?.invoke(item)
        }
    }

    override fun getItemCount() = itemList.size

}