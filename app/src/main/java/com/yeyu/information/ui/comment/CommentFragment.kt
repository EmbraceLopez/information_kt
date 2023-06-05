package com.yeyu.information.ui.comment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.yeyu.information.R
import com.yeyu.information.base.ui.BaseFragment
import com.yeyu.information.base.vm.ListObserver
import com.yeyu.information.base.vm.LoadingObserver
import com.yeyu.information.databinding.FragmentCommentBinding
import com.yeyu.information.databinding.FragmentNewsListBinding
import com.yeyu.information.viewmodels.InfoViewModel
import com.yeyu.information.viewmodels.UserViewModel
import com.yeyu.information.widget.DialogComment
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class CommentFragment : BaseFragment() {

    private lateinit var binding: FragmentCommentBinding
    private val userViewModel: UserViewModel by viewModels()
    private val infoViewModel: InfoViewModel by viewModels()

    private var rootViews: View? = null
    private var isRootViewInit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(rootViews == null) {
            binding = FragmentCommentBinding.inflate(inflater,container,false)
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
    }

    private fun init() {
        var page = 1
        val adapter = CommentAdapter()
        val infoId = arguments?.getString("infoId").toString()

        binding.topBar.tvMainTitle.text = "评论"

        binding.topBar.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.linearComment.setOnClickListener {
            userViewModel.getUser()
        }
        userViewModel.userInfo.observe(requireActivity(), Observer {
            if(null == it) {
                val dialog = AlertDialog.Builder(requireContext())
                    .setTitle("温馨提示")
                    .setMessage("请登录后操作")
                    .setNegativeButton("取消") { _, _ -> }
                    .setPositiveButton("去登录") { _, _ ->
                        findNavController().navigate(R.id.action_comment_to_login)
                    }
                dialog.show()
            } else {
                val commentDialog = DialogComment(requireContext(),R.style.CommonDialogStyle)
                commentDialog.setHint("友善评论")
                commentDialog.setListener {
                    if(it.isNotEmpty()) {
                        infoViewModel.publishComment(
                            infoId,it, SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date(System.currentTimeMillis()))
                        )
                    }
                    commentDialog.dismiss()
                }
                commentDialog.show()
            }
        })
        infoViewModel.commentState.observe(requireActivity(), Observer {
            infoViewModel.getCommentList(1,infoId)
        })

        binding.swipe.setOnRefreshListener {
            infoViewModel.getCommentList(1,infoId)
        }
        binding.commentRv.layoutManager = LinearLayoutManager(requireContext())
        binding.commentRv.adapter = adapter
        adapter.setEmptyView(R.layout.layout_empty)
        adapter.loadMoreModule.setOnLoadMoreListener {
            infoViewModel.getCommentList(page + 1,infoId)
        }
        //获取列表数据
        infoViewModel.getCommentList(1,infoId)
        infoViewModel.loadState.observe(requireActivity(),
            ListObserver(
                requireContext(),
                binding.swipe,
                {infoViewModel.paramCommentPage},
                {page = it},
                {infoViewModel.commentList.value},
                adapter
            )
        )
    }



}