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
import com.jagoanstudio.app.databinding.ItemListCommentsBinding
import com.jagoanstudio.app.model.ModelComments
import com.jagoanstudio.app.utils.view.ViewText

class ListCommentsAdapter (private val activity: Activity, private val itemList: List<ModelComments>) : RecyclerView.Adapter<ListCommentsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(
        R.layout.item_list_comments, parent, false))

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var binding: ItemListCommentsBinding = ItemListCommentsBinding.bind(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        val name = ViewText.splitFullname(item.name)
        val drawableProfile = TextDrawable
            .builder()
            .beginConfig()
            .bold()
            .textColor(ContextCompat.getColor(activity, R.color.colorWhite))
            .endConfig()
            .buildRoundRect((name), ColorGenerator.MATERIAL.getColor(name), 30)
        holder.binding.textviewCommentsAvatar.setImageDrawable(drawableProfile)

        holder.binding.textviewCommentsName.text = item.name

        holder.binding.textviewCommentsBody.text = item.body
    }

    override fun getItemCount() = itemList.size

}