package com.yeyu.information.base.vm

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.yeyu.information.base.Constant
import com.yeyu.information.data.Info
import com.yeyu.information.data.LoadState
import com.yeyu.information.widget.CustomLoadingDialog

/**
 * 列表加载状态监听
 * 仅适用于BaseQuickAdapter适配器
 * 若使用其他适配器则需要更改加载成功和失败的状态监听
 */
class ListObserver<T : List<Any>, K>(
    private val context: Context,
    private val swipe: SwipeRefreshLayout? = null,
    private val getRequestPage: () -> Int,
    private val updateCurrentPage: (Int) -> Unit,
    private val getData: () -> T?,
    private val adapter: BaseQuickAdapter<K,*>,
    private val pageSize: Int = Constant.DEFAULT_PAGE_SIZE,
    private val dialog: CustomLoadingDialog? = null,
    private val msg: String? = null,
) : Observer<LoadState> {

    override fun onChanged(t: LoadState?) {
        val requestPage = getRequestPage()
        when(t) {

            is LoadState.Loading -> {
                if(requestPage == 1) {
                    if(swipe != null && !swipe.isRefreshing) {
                        swipe.isRefreshing = true
                    }
                    dialog?.setMessage(msg!!)
                    dialog?.show()
                }
            }

            is LoadState.Success -> {
                val dataList = getData() as List<K>?

                if(requestPage == 1) {
                    if (swipe != null) {
                        dismissSwipe(swipe)
                    }
                    dialog?.setMessage(msg!!)
                    dialog?.dismiss()
                    adapter.setList(dataList)
                    if(dataList != null) {
                        adapter.loadMoreModule.isEnableLoadMore = dataList.size >= pageSize
                    }
                    updateCurrentPage(1)
                } else {
                    if(null == dataList || dataList.isEmpty()) {
                        adapter.loadMoreModule.loadMoreEnd()
                    } else {
                        adapter.addData(dataList)
                        if(dataList.size < pageSize) {
                            adapter.loadMoreModule.loadMoreEnd()
                        } else {
                            adapter.loadMoreModule.loadMoreComplete()
                        }
                        updateCurrentPage(requestPage)
                    }
                }
            }

            is LoadState.Fail -> {
                Toast.makeText(context,t.msg, Toast.LENGTH_LONG).show()
                if(requestPage == 1) {
                    if (swipe != null) {
                        dismissSwipe(swipe)
                    }
                    dialog?.setMessage(msg!!)
                    dialog?.dismiss()
                } else {
                    adapter.loadMoreModule.loadMoreFail()
                }
            }

            else -> {}
        }
    }

    private fun dismissSwipe(swipe: SwipeRefreshLayout) {
        if(swipe.isRefreshing) {
            swipe.postDelayed({swipe.isRefreshing = false},500)
        }
    }

}