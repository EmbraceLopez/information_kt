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
import com.yeyu.information.databinding.FragmentLoginPwdBinding
import com.yeyu.information.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * author: yeyu
 * date: 2023.05.17
 */
@AndroidEntryPoint
class LoginPwdFragment : BaseFragment() {

    private lateinit var binding: FragmentLoginPwdBinding
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginPwdBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initData()
    }

    private fun initView() {
        binding.tvGetPwd.setOnClickListener {
            findNavController().navigate(R.id.action_login_pwd_to_find_pwd)
        }
        binding.btnLogin.setOnClickListener {
            login()
        }
        binding.topBar.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

    }

    private fun initData() {
        userViewModel.user.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(), "登录成功", Toast.LENGTH_LONG).show()
            this@LoginPwdFragment.findNavController().navigate(R.id.action_login_pwd_to_personal)
        })
    }

    private fun login() {
        val phoneNumber = binding.etPhone.text.toString()
        val pwd = binding.etPwd.text.toString()
        if(phoneNumber.isEmpty()) {
            Toast.makeText(requireContext(), binding.etPhone.hint.toString(), Toast.LENGTH_LONG).show()
            return
        }
        if(pwd.isEmpty()) {
            Toast.makeText(requireContext(), binding.etPwd.hint.toString(), Toast.LENGTH_LONG).show()
            return
        }
        userViewModel.loadState.observe(viewLifecycleOwner,LoadingObserver(requireContext(),dialog = dialog, msg = "登录中..."))
        userViewModel.pwdLogin(phoneNumber,pwd)
    }


}