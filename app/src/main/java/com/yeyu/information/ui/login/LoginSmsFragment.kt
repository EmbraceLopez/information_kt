package com.yeyu.information.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.yeyu.information.R
import com.yeyu.information.base.ui.BaseFragment
import com.yeyu.information.base.vm.LoadingObserver
import com.yeyu.information.databinding.FragmentLoginSmsBinding
import com.yeyu.information.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * author: yeyu
 * date: 2023.05.17
 */
@AndroidEntryPoint
class LoginSmsFragment : BaseFragment() {

    private lateinit var binding: FragmentLoginSmsBinding
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginSmsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initData()
    }

    private fun initData() {
        userViewModel.user.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(), "登录成功", Toast.LENGTH_LONG).show()
            this@LoginSmsFragment.findNavController().navigate(R.id.action_login_sms_to_personal)
        })
    }

    private fun initView() {

        binding.linearLoginPwd.setOnClickListener {
            it.findNavController().navigate(R.id.action_login_sms_to_login_pwd)
        }
        binding.topBar.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.tvGetCode.setListener {
            val phone: String = binding.etPhone.text.toString()
            if(phone.isEmpty()) {
                Toast.makeText(requireContext(), binding.etPhone.hint.toString(), Toast.LENGTH_LONG).show()
                return@setListener false
            } else {
                userViewModel.loadState.observe(viewLifecycleOwner,
                    LoadingObserver(requireContext(),dialog = dialog, msg = "发送中...")
                )
                userViewModel.sendSmsCode(phone,"1")
                userViewModel.sendResult.observe(viewLifecycleOwner, Observer {
                    Toast.makeText(requireContext(), "发送成功", Toast.LENGTH_LONG).show()
                })
                true
            }
        }

        binding.btnLogin.setOnClickListener { login() }

    }

    private fun login() {
        val phoneNumber = binding.etPhone.text.toString()
        val smsCode = binding.etCode.text.toString()
        if(phoneNumber.isEmpty()) {
            Toast.makeText(requireContext(), binding.etPhone.hint.toString(), Toast.LENGTH_LONG).show()
            return
        }
        if(smsCode.isEmpty()) {
            Toast.makeText(requireContext(), binding.etCode.hint.toString(), Toast.LENGTH_LONG).show()
            return
        }
        userViewModel.loadState.observe(viewLifecycleOwner,LoadingObserver(requireContext(),dialog = dialog, msg = "登录中..."))
        userViewModel.smsLogin(phoneNumber,smsCode)
    }



}