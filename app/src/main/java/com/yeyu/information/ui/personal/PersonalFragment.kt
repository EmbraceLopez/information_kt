package com.yeyu.information.ui.personal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.yeyu.information.R
import com.yeyu.information.base.Constant
import com.yeyu.information.base.ui.BaseFragment
import com.yeyu.information.databinding.FragmentPersonalBinding
import com.yeyu.information.databinding.LayoutTopBarBinding
import com.yeyu.information.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * 个人主页
 */
@AndroidEntryPoint
class PersonalFragment : BaseFragment() {

    private lateinit var binding: FragmentPersonalBinding
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPersonalBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        binding.topBar.tvMainTitle.text = "个人中心"
        //获取用户信息
        userViewModel.getUserInfo()
        userViewModel.user.observe(requireActivity(), Observer {
            Glide.with(requireContext()).load(Constant.fillImgUrl(it.headPicUrl)).circleCrop().placeholder(R.drawable.ic_default_head).into(binding.ivHeadPic)
            binding.tvUserName.text = it.username
        })
        binding.topBar.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.linearModifyMsg.setOnClickListener {
            findNavController().navigate(R.id.action_personal_to_my_msg)
        }
        binding.linearSet.setOnClickListener {
            findNavController().navigate(R.id.action_personal_to_setting)
        }
    }

}