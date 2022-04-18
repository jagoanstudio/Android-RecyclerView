package com.jagoanstudio.app.ui.posts

import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.jagoanstudio.app.R
import com.jagoanstudio.app.adapter.ListCommentsAdapter
import com.jagoanstudio.app.databinding.ActivityDetailPostsBinding
import com.jagoanstudio.app.model.ModelComments
import com.jagoanstudio.app.model.ModelPosts
import com.jagoanstudio.app.network.CallBackClient
import com.jagoanstudio.app.ui.users.DetailUsersActivity
import com.jagoanstudio.app.viewmodel.CommentsViewModel
import com.jagoanstudio.app.viewmodel.PostsViewModel

class DetailPostsActivity : AppCompatActivity(), CallBackClient, PostsViewModel.CallBackPosts, CommentsViewModel.CallBackComments {

    private lateinit var binding: ActivityDetailPostsBinding
    private lateinit var postsViewModel: PostsViewModel
    private lateinit var commentsViewModel: CommentsViewModel
    private lateinit var listCommentsAdapter: ListCommentsAdapter
    private var dataPosts: ModelPosts? = ModelPosts()
    private var itemList: MutableList<ModelComments> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_posts)

        postsViewModel = ViewModelProviders.of(this).get(PostsViewModel::class.java)
        postsViewModel.init(this, this, this)
        commentsViewModel = ViewModelProviders.of(this).get(CommentsViewModel::class.java)
        commentsViewModel.init(this, this, this)

        loadIntent()
        loadData(false)
        loadView()
    }

    private fun loadIntent() {
        val dataIntent = intent.extras
        if (dataIntent != null) {
            dataPosts = Gson().fromJson(intent.getStringExtra("posts"), ModelPosts::class.java)
        }
    }

    private fun loadData(isReload: Boolean) {
        if (isReload) {
            binding.relativeDetailPosts.visibility = View.GONE
            binding.swipeDetailPosts.isRefreshing = false
        }

        postsViewModel.fetchDetailPosts(dataPosts?.id.toString()).observe(this, Observer {
            postsViewModel.processDetailPosts(it)
        })

        commentsViewModel.fetchListComments(dataPosts?.id.toString()).observe(this, Observer {
            commentsViewModel.processListComments(it)
        })
    }

    private fun loadView() {
        binding.shimmerDetailPosts.visibility = View.GONE
        binding.shimmerDetailPosts.stopShimmer()

        binding.toolbar.toolbarBack.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }

        binding.toolbar.toolbarTitle.text = resources.getString(R.string.post_detail)

        binding.textviewPostsName.setOnClickListener {
            val intent = Intent(this, DetailUsersActivity::class.java)
            intent.putExtra("userId", dataPosts?.userId.toString())
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }

        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        listCommentsAdapter = ListCommentsAdapter(this, itemList)
        binding.recyclerComments.layoutManager = linearLayoutManager
        binding.recyclerComments.adapter = listCommentsAdapter

        binding.swipeDetailPosts.setOnRefreshListener {
            loadData(true)
        }
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    override fun loading() {
        binding.shimmerDetailPosts.visibility = View.VISIBLE
        binding.shimmerDetailPosts.startShimmer()
    }

    override fun success(message: String?, code: Int?) {
        binding.shimmerDetailPosts.visibility = View.GONE
        binding.shimmerDetailPosts.stopShimmer()
    }

    override fun failed(message: String?) {
        binding.shimmerDetailPosts.visibility = View.GONE
        binding.shimmerDetailPosts.stopShimmer()
    }

    override fun errorConnection(t: Throwable?) {
        binding.shimmerDetailPosts.visibility = View.GONE
        binding.shimmerDetailPosts.stopShimmer()
    }

    override fun error(t: Throwable?) {
        binding.shimmerDetailPosts.visibility = View.GONE
        binding.shimmerDetailPosts.stopShimmer()
    }

    override fun resultListPosts(message: String?, data: List<ModelPosts>?) {

    }

    override fun resultDetailPosts(message: String?, data: ModelPosts?) {
        binding.relativeDetailPosts.visibility = View.VISIBLE

        binding.textviewPostsTitle.text = data?.title

        binding.textviewPostsName.text = dataPosts?.name
        binding.textviewPostsName.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        binding.textviewPostsBody.text = data?.body
    }

    override fun resultListComments(message: String?, data: List<ModelComments>?) {
        data?.map {
            itemList.add(it)
            listCommentsAdapter.notifyDataSetChanged()
        }
    }

}