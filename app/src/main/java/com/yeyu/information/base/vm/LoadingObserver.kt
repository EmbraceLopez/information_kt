package com.yeyu.information.base.vm

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.yeyu.information.data.LoadState
import com.yeyu.information.widget.CustomLoadingDialog

/**
 * 通用网络请求加载状态监听
 * 加载中可用swipeRefreshLayout以及自定义的dialog显示
 */
class LoadingObserver(
    private val context: Context,
    private val dialog: CustomLoadingDialog? = null,
    private val msg: String? = null,
    private val swipeRefresh: SwipeRefreshLayout? = null,
    private val onSuccess: (() -> Unit)? = null
) : Observer<LoadState> {

    override fun onChanged(t: LoadState?) {
        when (t) {
            is LoadState.Loading -> {
                dialog?.setMessage(msg!!)
                dialog?.show()
                swipeRefresh?.isRefreshing = true
            }
            is LoadState.Success -> {
                dialog?.setMessage(msg!!)
                dialog?.dismiss()
                swipeRefresh?.postDelayed({swipeRefresh.isRefreshing = false},500)
                onSuccess?.invoke()
            }
            is LoadState.Fail -> {
                dialog?.setMessage(msg!!)
                dialog?.dismiss()
                swipeRefresh?.postDelayed({swipeRefresh.isRefreshing = false},500)
                Toast.makeText(context,t.msg,Toast.LENGTH_LONG).show()
            }
            else -> {}
        }
    }

}