package com.yeyu.information.ui.news

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.yeyu.information.base.ui.BaseFragment
import com.yeyu.information.viewmodels.InfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.just.agentweb.AgentWeb
import com.just.agentweb.DefaultWebClient
import com.yeyu.information.R
import com.yeyu.information.databinding.FragmentH5NewsBinding
import com.yeyu.information.databinding.FragmentNewsListBinding
import com.yeyu.information.viewmodels.UserViewModel

@AndroidEntryPoint
class NewsH5Fragment : BaseFragment() {

    private val infoViewModel: InfoViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var binding: FragmentH5NewsBinding

    private var rootViews: View? = null
    private var isRootViewInit = false

    private var infoId: Int? = null
    private var url: String? = null
    private var agentWeb: AgentWeb? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(rootViews == null) {
            binding = FragmentH5NewsBinding.inflate(inflater, container, false)
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
        //不保存状态，需要重建view时更新
        infoViewModel.getInfoDetail(infoId.toString())
        infoViewModel.getLikeState(infoId.toString())
        infoViewModel.getCollectState(infoId.toString())
    }

    private fun init() {

        infoId = arguments?.getInt("infoId")
        url = arguments?.getString("url")

        //数量
        var likeCount = 0
        var collectCount = 0
        var commentCount = 0

        //加载网页
        agentWeb = AgentWeb.with(requireActivity())
            .setAgentWebParent(binding.webContainer,-1, LinearLayout.LayoutParams(-1, -1))
            .useDefaultIndicator(R.color.colorPrimary)
            .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)
            .createAgentWeb()
            .ready()
            .go(url)

        //获取点赞、收藏、评论数据
        infoViewModel.infoDetail.observe(requireActivity(), Observer {
            likeCount = it.likeCount
            collectCount = it.collectCount
            commentCount = it.commentCount
            binding.tvLikeCount.text = likeCount.toString()
            binding.tvCollectCount.text = collectCount.toString()
            binding.tvCommentCount.text = commentCount.toString()
        })
        infoViewModel.likeState.observe(requireActivity(), Observer {
            if(it == 1) {
                Glide.with(requireContext()).load(R.drawable.ic_like_true).into(binding.ivLike)
                binding.tvLikeCount.setTextColor(resources.getColor(R.color.colorPrimary))
            } else {
                Glide.with(requireContext()).load(R.drawable.ic_like_false).into(binding.ivLike)
                binding.tvLikeCount.setTextColor(resources.getColor(R.color.black))
            }
        })
        infoViewModel.collectState.observe(requireActivity(), Observer {
            if(it == 1) {
                Glide.with(requireContext()).load(R.drawable.ic_collect_true).into(binding.ivCollect)
                binding.tvCollectCount.setTextColor(resources.getColor(R.color.colorPrimary))
            } else {
                Glide.with(requireContext()).load(R.drawable.ic_collect_false).into(binding.ivCollect)
                binding.tvCollectCount.setTextColor(resources.getColor(R.color.black))
            }
        })

        userViewModel.userInfo.observe(requireActivity(), Observer {
            if(null == it) {
                val dialog = AlertDialog.Builder(requireContext())
                    .setTitle("温馨提示")
                    .setMessage("请登录后操作")
                    .setNegativeButton("取消") { _, _ -> }
                    .setPositiveButton("去登录") { _, _ ->
                        findNavController().navigate(R.id.action_news_h5_to_login)
                    }
                dialog.show()
            }
        })

        //点赞、收藏、评论
        binding.linearLike.setOnClickListener {
            userViewModel.getUser()
            infoViewModel.executeLike(infoId.toString())
        }
        infoViewModel.executeLikeState.observe(requireActivity(), Observer {
            if(it == 1) {
                Glide.with(requireContext()).load(R.drawable.ic_like_true).placeholder(R.drawable.ic_like_false).into(binding.ivLike)
                likeCount++
                binding.tvLikeCount.text = likeCount.toString()
                binding.tvLikeCount.setTextColor(resources.getColor(R.color.colorPrimary))
            } else {
                Glide.with(requireContext()).load(R.drawable.ic_like_false).placeholder(R.drawable.ic_like_true).into(binding.ivLike)
                likeCount--
                binding.tvLikeCount.text = likeCount.toString()
                binding.tvLikeCount.setTextColor(resources.getColor(R.color.black))
            }
        })

        binding.linearCollect.setOnClickListener {
            userViewModel.getUser()
            infoViewModel.executeCollect(infoId.toString())
        }
        infoViewModel.executeCollectState.observe(requireActivity(), Observer {
            if(it == 1) {
                Glide.with(requireContext()).load(R.drawable.ic_collect_true).placeholder(R.drawable.ic_collect_false).into(binding.ivCollect)
                collectCount++
                binding.tvCollectCount.text = collectCount.toString()
                binding.tvCollectCount.setTextColor(resources.getColor(R.color.colorPrimary))
            } else {
                Glide.with(requireContext()).load(R.drawable.ic_collect_false).placeholder(R.drawable.ic_collect_true).into(binding.ivCollect)
                collectCount--
                binding.tvCollectCount.text = collectCount.toString()
                binding.tvCollectCount.setTextColor(resources.getColor(R.color.black))
            }
        })

        binding.linearComment.setOnClickListener {
            val bundle = bundleOf("infoId" to infoId.toString())
            findNavController().navigate(R.id.action_news_h5_to_comment,bundle)
        }

        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onPause() {
        super.onPause()
        agentWeb?.webLifeCycle?.onPause()
    }

    override fun onResume() {
        super.onResume()
        agentWeb?.webLifeCycle?.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        agentWeb?.webLifeCycle?.onDestroy()
    }

}