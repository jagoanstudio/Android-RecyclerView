package com.jagoanstudio.app.ui.posts

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.jagoanstudio.app.R
import com.jagoanstudio.app.adapter.ListPostsAdapter
import com.jagoanstudio.app.databinding.ActivityListPostsBinding
import com.jagoanstudio.app.model.ModelPosts
import com.jagoanstudio.app.model.ModelUsers
import com.jagoanstudio.app.network.CallBackClient
import com.jagoanstudio.app.viewmodel.PostsViewModel
import com.jagoanstudio.app.viewmodel.UsersViewModel

class ListPostsActivity : AppCompatActivity(), CallBackClient, PostsViewModel.CallBackPosts, UsersViewModel.CallBackUsers {

    private lateinit var binding: ActivityListPostsBinding
    private lateinit var listPostsAdapter: ListPostsAdapter
    private lateinit var postsViewModel: PostsViewModel
    private lateinit var usersViewModel: UsersViewModel
    private var itemList: MutableList<ModelPosts> = mutableListOf()
    private var statusLoadUsers: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_list_posts)

        postsViewModel = ViewModelProviders.of(this).get(PostsViewModel::class.java)
        postsViewModel.init(this, this, this)
        usersViewModel = ViewModelProviders.of(this).get(UsersViewModel::class.java)
        usersViewModel.init(this, this, this)

        loadData(false)
        loadView()
    }

    private fun loadData(isReload : Boolean) {
        if (isReload) {
            statusLoadUsers = false
            binding.swipePosts.isRefreshing = false
        }

        if (itemList.isNotEmpty()) {
            itemList.clear()
            listPostsAdapter.notifyDataSetChanged()
        }

        postsViewModel.fetchListPosts().observe(this, Observer {
            postsViewModel.processListPosts(it)
        })
    }

    private fun loadView() {
        binding.toolbar.toolbarBack.visibility = View.GONE
        binding.toolbar.toolbarTitle.text = resources.getString(R.string.app_name)

        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        listPostsAdapter = ListPostsAdapter(this, itemList)
        binding.recyclerPosts.layoutManager = linearLayoutManager
        binding.recyclerPosts.adapter = listPostsAdapter

        listPostsAdapter.onItemClick = { posts ->
            if (posts?.name != null) {
                val intent = Intent(this, DetailPostsActivity::class.java)
                intent.putExtra("posts", Gson().toJson(posts))
                startActivity(intent)
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }
        }

        binding.swipePosts.setOnRefreshListener {
            loadData(true)
        }
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    override fun loading() {
        if (!statusLoadUsers) {
            binding.shimmerRecyclerPosts.visibility = View.VISIBLE
            binding.shimmerRecyclerPosts.startShimmer()
        }
    }

    override fun success(message: String?, code: Int?) {
        binding.shimmerRecyclerPosts.visibility = View.GONE
        binding.shimmerRecyclerPosts.stopShimmer()
    }

    override fun failed(message: String?) {
        binding.shimmerRecyclerPosts.visibility = View.GONE
        binding.shimmerRecyclerPosts.stopShimmer()
    }

    override fun errorConnection(t: Throwable?) {
        binding.shimmerRecyclerPosts.visibility = View.GONE
        binding.shimmerRecyclerPosts.stopShimmer()
    }

    override fun error(t: Throwable?) {
        binding.shimmerRecyclerPosts.visibility = View.GONE
        binding.shimmerRecyclerPosts.stopShimmer()
    }

    override fun resultListPosts(message: String?, data: List<ModelPosts>?) {
        data?.map {
            itemList.add(it)
            listPostsAdapter.notifyDataSetChanged()

            statusLoadUsers = true
            usersViewModel.fetchDetailUsers(it.userId.toString()).observe(this, Observer {
                usersViewModel.processDetailUsers(it)
            })
        }
    }

    override fun resultDetailPosts(message: String?, data: ModelPosts?) {

    }

    override fun resultDetailUsers(message: String?, data: ModelUsers?) {
        statusLoadUsers = false

        itemList.map {
            if (it.userId == data?.id) {
                it.name = data?.name
                it.company = data?.company
            }
            listPostsAdapter.notifyDataSetChanged()
        }
    }

}