package com.galih.matarakv2.ui.detection

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.galih.matarakv2.base.BaseFragment
import com.galih.matarakv2.data.model.DetectionResult
import com.galih.matarakv2.databinding.FragmentDetectionHistoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetectionHistoryFragment : BaseFragment<FragmentDetectionHistoryBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentDetectionHistoryBinding
        = FragmentDetectionHistoryBinding::inflate
    private val historyAdapter: HistoryAdapter by lazy { HistoryAdapter() }
    private val viewModel: DetectionViewModel by viewModels()

    override fun setup() {
        setupRecyclerView()
        viewModel.getHistories().observe(viewLifecycleOwner, setupHistoryObserver())
    }

    private fun setupHistoryObserver() = setObserver<List<DetectionResult>?>(
        onSuccess = { response ->
            binding.progressBar.gone()
            historyAdapter.differ.submitList(response.data)
        },
        onError = { response ->
            binding.progressBar.gone()
            showToast(response.message.toString())
        },
        onLoading = { binding.progressBar.visible() }
    )

    private fun setupRecyclerView() {
        with(binding.rvHistory) {
            adapter = historyAdapter
            layoutManager = LinearLayoutManager(context)
        }

        historyAdapter.setOnItemClickListener {
            val intent = Intent(binding.root.context, ResultActivity::class.java)
            intent.putExtra(ResultActivity.EXTRA_DATA, it)
            startActivity(intent)
        }
    }
}