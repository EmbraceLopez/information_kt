package com.yeyu.information.ui.news

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.yeyu.information.R
import com.yeyu.information.base.Constant
import com.yeyu.information.base.MyApplication
import com.yeyu.information.base.ui.BaseFragment
import com.yeyu.information.base.vm.ListObserver
import com.yeyu.information.databinding.FragmentNewsListBinding
import com.yeyu.information.viewmodels.InfoViewModel
import com.yeyu.information.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * 注意navigation导航下fragment的生命周期，是否每次导航至新的页面都是重建View
 * 最佳解决方法：使用viewModel+dataBinding，即使重建View，也不会重新加载数据，除非整个Activity销毁了
 * 方法二：保存View，但会消耗内存
 */
@AndroidEntryPoint
class NewsListFragment : BaseFragment() {

    private lateinit var binding: FragmentNewsListBinding
    private val userViewModel: UserViewModel by viewModels()
    private val infoViewModel: InfoViewModel by viewModels()

    private var rootViews: View? = null
    private var isRootViewInit = false

    private val TIME = 2000
    private var beforeTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if(rootViews == null) {
            binding = FragmentNewsListBinding.inflate(inflater, container, false)
            rootViews = binding.root
        }

        return rootViews
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!isRootViewInit) {
            init()
            isRootViewInit = true
        }

        userViewModel.getUserInfo()
    }


    private fun init() {

        var page = 1
        val adapter = NewsListAdapter()

        binding.swipe.setOnRefreshListener {
            infoViewModel.getInfoList(1,"1","",1)
        }
        binding.newsRv.layoutManager = LinearLayoutManager(requireContext())
        binding.newsRv.adapter = adapter
        adapter.setEmptyView(R.layout.layout_empty)
        adapter.loadMoreModule.setOnLoadMoreListener {
            infoViewModel.getInfoList(page + 1,"1","",0)
        }
        adapter.setOnItemClickListener { _, _, position ->
            val bundle = bundleOf("infoId" to adapter.data[position].id,"url" to adapter.data[position].infoUrl)
            findNavController().navigate(R.id.action_news_list_to_h5,bundle)
        }

        binding.ivSearch.setOnClickListener {
            it.findNavController().navigate(R.id.action_news_list_to_search)
        }
        //判断登录状态
        binding.ivPhoto.setOnClickListener {
            userViewModel.getUser()
        }
        userViewModel.userInfo.observe(requireActivity(), Observer {
            if(null != it) {
                findNavController().navigate(R.id.action_news_list_to_personal)
            } else {
                findNavController().navigate(R.id.action_news_list_to_login)
            }
        })
        //获取用户信息
        userViewModel.user.observe(requireActivity(), Observer {
            Glide.with(requireContext()).load(Constant.fillImgUrl(it.headPicUrl)).circleCrop().placeholder(R.drawable.ic_default_head).into(binding.ivPhoto)
        })
        //获取列表数据
        infoViewModel.getInfoList(1,"1","",0)
        infoViewModel.loadState.observe(requireActivity(),ListObserver(
            requireContext(),
            binding.swipe,
            {infoViewModel.paramInfoPage},
            {page = it},
            {infoViewModel.infoList.value},
            adapter
        ))
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.
        addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val nowTime = System.currentTimeMillis()
                if(TIME > nowTime - beforeTime) {
                    MyApplication.getApp().clearActivities()
                } else {
                    Toast.makeText(requireContext(), "再按一次退出", Toast.LENGTH_LONG).show()
                }
                beforeTime = nowTime
            }
        })
    }

}