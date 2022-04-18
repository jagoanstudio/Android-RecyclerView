package com.jagoanstudio.app.ui.users

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.jagoanstudio.app.R
import com.jagoanstudio.app.adapter.ListAlbumsAdapter
import com.jagoanstudio.app.databinding.ActivityDetailUsersBinding
import com.jagoanstudio.app.model.ModelAlbums
import com.jagoanstudio.app.model.ModelPhotos
import com.jagoanstudio.app.model.ModelUsers
import com.jagoanstudio.app.network.CallBackClient
import com.jagoanstudio.app.utils.view.ViewText
import com.jagoanstudio.app.viewmodel.AlbumsViewModel
import com.jagoanstudio.app.viewmodel.PhotosViewModel
import com.jagoanstudio.app.viewmodel.UsersViewModel

class DetailUsersActivity : AppCompatActivity(), CallBackClient, UsersViewModel.CallBackUsers, AlbumsViewModel.CallBackAlbums, PhotosViewModel.CallBackPhotos {

    private lateinit var binding: ActivityDetailUsersBinding
    private lateinit var usersViewModel: UsersViewModel
    private lateinit var albumsViewModel: AlbumsViewModel
    private lateinit var photosViewModel: PhotosViewModel
    private lateinit var listAlbumsAdapter: ListAlbumsAdapter
    private var userId: String? = null
    private var itemListAlbums: MutableList<ModelAlbums> = mutableListOf()
    private var statusLoadAlbums = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_users)

        usersViewModel = ViewModelProviders.of(this).get(UsersViewModel::class.java)
        usersViewModel.init(this, this, this)
        albumsViewModel = ViewModelProviders.of(this).get(AlbumsViewModel::class.java)
        albumsViewModel.init(this, this, this)
        photosViewModel = ViewModelProviders.of(this).get(PhotosViewModel::class.java)
        photosViewModel.init(this, this, this)

        loadIntent()
        loadData()
        loadView()
    }

    private fun loadIntent() {
        val dataIntent = intent.extras
        if (dataIntent != null) {
            userId = intent.getStringExtra("userId")
        }
    }

    private fun loadData() {
        usersViewModel.fetchDetailUsers(userId).observe(this, Observer {
            usersViewModel.processDetailUsers(it)
        })
    }

    private fun loadView() {
        binding.toolbar.toolbarBack.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }

        binding.toolbar.toolbarTitle.text = resources.getString(R.string.user_detail)

        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        listAlbumsAdapter = ListAlbumsAdapter(this, itemListAlbums)
        binding.recyclerAlbums.layoutManager = linearLayoutManager
        binding.recyclerAlbums.adapter = listAlbumsAdapter
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    override fun loading() {
        if (!statusLoadAlbums) {
            binding.shimmerDetailUsers.visibility = View.VISIBLE
            binding.shimmerDetailUsers.startShimmer()
        }
    }

    override fun success(message: String?, code: Int?) {
        binding.shimmerDetailUsers.visibility = View.GONE
        binding.shimmerDetailUsers.stopShimmer()
    }

    override fun failed(message: String?) {
        binding.shimmerDetailUsers.visibility = View.GONE
        binding.shimmerDetailUsers.stopShimmer()
    }

    override fun errorConnection(t: Throwable?) {
        binding.shimmerDetailUsers.visibility = View.GONE
        binding.shimmerDetailUsers.stopShimmer()
    }

    override fun error(t: Throwable?) {
        binding.shimmerDetailUsers.visibility = View.GONE
        binding.shimmerDetailUsers.stopShimmer()
    }

    override fun resultDetailUsers(message: String?, data: ModelUsers?) {
        binding.relativeDetailUsers.visibility = View.VISIBLE

        val name = ViewText.splitFullname(data?.name)
        val drawableProfile = TextDrawable
            .builder()
            .beginConfig()
            .bold()
            .textColor(ContextCompat.getColor(this, R.color.colorWhite))
            .endConfig()
            .buildRoundRect((name), ColorGenerator.MATERIAL.getColor(name), 30)
        binding.textviewUsersAvatar.setImageDrawable(drawableProfile)

        binding.textviewUsersName.text = data?.name

        binding.textviewUsersEmail.text = data?.email

        binding.textviewUsersStreet.text = data?.address?.street

        binding.textviewUsersSuite.text = data?.address?.suite

        binding.textviewUsersCity.text = data?.address?.city

        binding.textviewUsersZipcode.text = data?.address?.zipcode

        binding.textviewUsersLatitude.text = data?.address?.geo?.lat

        binding.textviewUsersLongitude.text = data?.address?.geo?.lng

        binding.textviewUsersCompanyName.text = data?.company?.name

        binding.textviewUsersCatchPhrase.text = data?.company?.catchPhrase

        binding.textviewUsersBs.text = data?.company?.bs

        statusLoadAlbums = true
        albumsViewModel.fetchListAlbums(userId).observe(this, {
            albumsViewModel.processListAlbums(it)
        })
    }

    override fun resultListAlbums(message: String?, data: List<ModelAlbums>?) {
        data?.map {
            itemListAlbums.add(it)
            listAlbumsAdapter?.notifyDataSetChanged()

            photosViewModel.fetchListPhotos(it.id.toString()).observe(this, {
                photosViewModel.processListPhotos(it)
            })
        }
    }

    override fun resultListPhotos(message: String?, data: List<ModelPhotos>?) {
        statusLoadAlbums = false

        itemListAlbums.map { albums ->
            val itemListPhotos = mutableListOf<ModelPhotos>()
            data?.map { photos ->
                if (albums.id == photos.albumId) {
                    itemListPhotos.add(photos)
                    albums.photos = itemListPhotos
                }
            }
            listAlbumsAdapter.notifyDataSetChanged()
        }
    }

}