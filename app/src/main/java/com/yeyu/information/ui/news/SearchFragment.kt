package com.yeyu.information.ui.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.yeyu.information.R
import com.yeyu.information.base.Constant
import com.yeyu.information.base.ui.BaseFragment
import com.yeyu.information.base.vm.ListObserver
import com.yeyu.information.databinding.FragmentSearchBinding
import com.yeyu.information.viewmodels.InfoViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * 搜索
 */
@AndroidEntryPoint
class SearchFragment : BaseFragment() {

    private lateinit var binding: FragmentSearchBinding
    private val infoViewModel: InfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        var page = 1
        val adapter = NewsListAdapter()
        var keyword = ""
        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        binding.rv.adapter = adapter
        adapter.setEmptyView(R.layout.layout_empty)
        adapter.loadMoreModule.setOnLoadMoreListener {
            infoViewModel.searchInfoList(page + 1,"",keyword)
        }
        adapter.setOnItemClickListener { _, _, position ->
            val bundle = bundleOf("infoId" to adapter.data[position].id,"url" to adapter.data[position].infoUrl)
            findNavController().navigate(R.id.action_search_to_h5,bundle)
        }
        binding.tvSearch.setOnClickListener {
            keyword = binding.etSearch.text.toString()
            if(keyword.isEmpty()) {
                Toast.makeText(requireContext(),"请输入搜索关键字", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            //获取列表数据
            infoViewModel.searchInfoList(1,"",keyword)
        }

        infoViewModel.loadState.observe(requireActivity(),
            ListObserver(
                requireContext(),
                dialog = dialog,
                msg = "搜索中...",
                getRequestPage = {infoViewModel.paramSearchInfoPage},
                updateCurrentPage = {page = it},
                getData = {infoViewModel.searchInfoList.value},
                adapter = adapter
            )
        )
    }

}