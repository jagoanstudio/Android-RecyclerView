package com.jagoanstudio.app.ui.photos

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.davemorrissey.labs.subscaleview.ImageSource
import com.google.gson.Gson
import com.jagoanstudio.app.R
import com.jagoanstudio.app.databinding.ActivityDetailPhotosBinding
import com.jagoanstudio.app.model.ModelPhotos
import com.squareup.picasso.Picasso

class DetailPhotosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailPhotosBinding
    private var dataPhotos: ModelPhotos? = ModelPhotos()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_photos)

        loadIntent()
        loadData()
        loadView()
    }

    private fun loadIntent() {
        val dataIntent = intent.extras
        if (dataIntent != null) {
            dataPhotos = Gson().fromJson(intent.getStringExtra("photos"), ModelPhotos::class.java)
        }
    }

    private fun loadData() {
        binding.textviewPhotosTitle.text = dataPhotos?.title

        Picasso.get().load(dataPhotos?.url).into(object : com.squareup.picasso.Target {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                binding.imageviewPhotos.setImage(ImageSource.bitmap(bitmap!!))
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {

            }
        })
    }

    private fun loadView() {
        binding.toolbar.toolbarBack.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }

        binding.toolbar.toolbarTitle.text = resources.getString(R.string.photo_detail)
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

}