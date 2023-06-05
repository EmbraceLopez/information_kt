package com.yeyu.information.ui.personal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.yeyu.information.base.ui.BaseFragment
import com.yeyu.information.databinding.FragmentModifyPwdBinding

/**
 * 修改密码
 * 暂未实现，短信已过期
 */
class ModifyPwdFragment : BaseFragment() {

    private lateinit var binding: FragmentModifyPwdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentModifyPwdBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        binding.topBar.tvMainTitle.text = "修改密码"
        binding.topBar.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }


}