package com.yeyu.information.base.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentHostCallback
import com.yeyu.information.R
import com.yeyu.information.util.PermissionUtils
import com.yeyu.information.util.PermissionUtils.OnPermissionListener
import com.yeyu.information.widget.CustomLoadingDialog

/**
 * 所有fragment的基类
 */
abstract class BaseFragment : Fragment() {

    protected val dialog by lazy {
        CustomLoadingDialog(requireContext(), R.style.CustomLoadingDialog)
    }

    private val mPermissionUtils by lazy {
        PermissionUtils()
    }

    private var mRequestCode = 0

    /**
     * 加载对话框显示与隐藏
     */
    protected fun showLoading(msg: String) {
        if(dialog.isShowing)
            return
        dialog.setMessage(msg)
        dialog.show()
    }

    protected fun dismissLoading(){
        if (dialog.isShowing)
            dialog.dismiss()
    }

    /**
     * 请求权限
     */
    protected fun requestPermissions(
        requestCode: Int,
        permission: Array<String?>?,
        callback: OnPermissionListener?
    ){
        mRequestCode = requestCode
        mPermissionUtils.requestPermissionsResult(this,mRequestCode,permission,callback)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        mPermissionUtils.onRequestPermissionsResult(mRequestCode,permissions,grantResults)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (dialog.isShowing)
            dialog.dismiss()
    }

}