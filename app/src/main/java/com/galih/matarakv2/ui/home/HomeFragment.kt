package com.galih.matarakv2.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.galih.matarakv2.base.BaseFragment
import com.galih.matarakv2.data.model.Article
import com.galih.matarakv2.data.model.Banner
import com.galih.matarakv2.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeBinding
        = FragmentHomeBinding::inflate
    private val articleAdapter: ArticleAdapter by lazy { ArticleAdapter() }
    private val bannerAdapter: BannerAdapter by lazy { BannerAdapter() }
    private val viewModel: HomeViewModel by viewModels()

    override fun setup() {
        setupRecyclerView()
        getData()
    }

    private fun getData() {
        viewModel.getAllArticles().observe(this, setupArticleObserver())
        viewModel.getAllBanners().observe(this, setupBannerObserver())
    }

    private fun setupArticleObserver() = setObserver<List<Article>?>(
        onSuccess = { response ->
            binding.progressBar.gone()
            articleAdapter.differ.submitList(response.data)
        },
        onError = { response ->
            binding.progressBar.gone()
            showToast(response.message.toString())
        },
        onLoading = { binding.progressBar.visible() }
    )

    private fun setupBannerObserver() = setObserver<List<Banner>?>(
        onSuccess = { response ->
            binding.progressBar.gone()
            bannerAdapter.differ.submitList(response.data)
        },
        onError = { response ->
            binding.progressBar.gone()
            showToast(response.message.toString())
        },
        onLoading = { binding.progressBar.visible() }
    )

    private fun setupRecyclerView() {
        with(binding.rvArtivle) {
            adapter = articleAdapter
            layoutManager = LinearLayoutManager(context)
        }

        with(binding.rvBanner) {
            adapter = bannerAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

        articleAdapter.setOnItemClickListener {
            val intent = Intent(context, DetailArticleActivity::class.java)
            intent.putExtra(DetailArticleActivity.EXTRA_ARTICLE, it)
            startActivity(intent)
        }
    }
}