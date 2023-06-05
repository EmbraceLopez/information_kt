package com.yeyu.information.ui.personal

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.yeyu.information.R
import com.yeyu.information.base.ui.BaseFragment
import com.yeyu.information.base.vm.LoadingObserver
import com.yeyu.information.databinding.FragmentSettingBinding
import com.yeyu.information.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * 设置
 */
@AndroidEntryPoint
class SettingFragment : BaseFragment() {

    private lateinit var binding: FragmentSettingBinding
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        binding.topBar.tvMainTitle.text = "设置"

        binding.linearLoginOut.setOnClickListener {
            val dialog = AlertDialog.Builder(requireContext())
                .setTitle("温馨提示")
                .setMessage("是否退出登录")
                .setNegativeButton("取消") { _, _ ->
                }
                .setPositiveButton("退出") { _, _ ->
                    userViewModel.clearUser()
                }
            dialog.show()
        }
        userViewModel.loadState.observe(viewLifecycleOwner,
            LoadingObserver(requireContext(),dialog = dialog, msg = "退出中...")
        )
        userViewModel.loginOutState.observe(requireActivity(), Observer {
            findNavController().navigate(R.id.action_setting_to_new_list)
        })

        binding.topBar.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.linearAbout.setOnClickListener {
            findNavController().navigate(R.id.action_setting_to_about)
        }

        binding.linearModifyPwd.setOnClickListener {
            findNavController().navigate(R.id.action_setting_to_modify_pwd)
        }

    }

}