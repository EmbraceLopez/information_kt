package com.yeyu.information.widget

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.yeyu.information.databinding.DialogCustomBinding

/**
 * author: yeyu
 * 自定义加载dialog
 * progressBar+textView
 */
class CustomLoadingDialog(context: Context, themeResId: Int) : Dialog(context, themeResId) {

    private lateinit var binding: DialogCustomBinding

    private var _msg: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogCustomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        _msg.let {
            binding.tvMsg.text = it
        }

        setCanceledOnTouchOutside(false)
        setCancelable(false)
    }

    fun setMessage(msg: String) {
        _msg = msg
    }

}