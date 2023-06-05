package com.yeyu.information.ui.personal

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.window.OnBackInvokedCallback
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.huantansheng.easyphotos.EasyPhotos
import com.huantansheng.easyphotos.callback.SelectCallback
import com.huantansheng.easyphotos.models.album.entity.Photo
import com.yeyu.information.R
import com.yeyu.information.base.Constant
import com.yeyu.information.base.ui.BaseFragment
import com.yeyu.information.base.vm.LoadingObserver
import com.yeyu.information.databinding.FragmentMyMsgBinding
import com.yeyu.information.util.GlideEngine
import com.yeyu.information.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList
import java.util.Calendar

/**
 * 个人资料
 */
@AndroidEntryPoint
class MyMsgFragment : BaseFragment() {

    private lateinit var binding: FragmentMyMsgBinding
    private val userViewModel: UserViewModel by viewModels()

    private var headPicPath = ""
    private var gender = ""
    private var index = 0
    private var birthday = ""

    //判断是否修改
    var headPicUrlStr = ""
    var usernameStr = ""
    var genderStr = ""
    var birthdayStr = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyMsgBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {

        binding.topBar.tvMainTitle.text = "修改资料"
        userViewModel.getUserInfo()
        userViewModel.user.observe(requireActivity(), Observer {
            headPicUrlStr = it.headPicUrl
            headPicPath = it.headPicUrl
            usernameStr = it.username
            genderStr = it.gender
            birthdayStr = it.birthday
            birthday = it.birthday
            Glide.with(requireContext()).load(Constant.fillImgUrl(it.headPicUrl)).circleCrop().placeholder(
                R.drawable.ic_default_head).into(binding.ivHeadPic)
            binding.etUsername.setText(it.username)
            binding.tvGender.text = it.gender
            binding.tvBirthday.text = it.birthday
        })
        binding.topBar.ivBack.setOnClickListener {
            back()
        }
        binding.linearPickHead.setOnClickListener {
            choosePic(this, selectCallback = object : SelectCallback() {
                override fun onResult(photos: ArrayList<Photo>?, isOriginal: Boolean) {
                    photos?.apply {
                        val path = photos[0].path
                        Glide.with(this@MyMsgFragment).load(path).circleCrop().into(binding.ivHeadPic)
                        userViewModel.loadState.observe(viewLifecycleOwner,LoadingObserver(requireContext(),dialog = dialog, msg = "上传中..."))
                        userViewModel.uploadPic(listOf(path))
                    }
                }
            })
        }
        binding.linearSelectTime.setOnClickListener {
            val calender = Calendar.getInstance()
            var year = calender[Calendar.YEAR]
            var month = calender[Calendar.MONTH]
            var day = calender[Calendar.DAY_OF_MONTH]

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, i, i2, i3 ->
                    year = i
                    month = i2
                    day = i3
                    birthday = "${year}.${month + 1}.${day}"
                    binding.tvBirthday.text = birthday
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }
        binding.linearSelectGender.setOnClickListener {
            val genderArray = arrayOf("男","女")
            val dialog = AlertDialog.Builder(requireContext())
                .setTitle("请选择您的性别")
                .setSingleChoiceItems(genderArray,index) { _, i ->
                    index = i
                }
                .setPositiveButton("确定") { _, i ->
                    gender = genderArray[index]
                    binding.tvGender.text = gender
                }
            dialog.show()
        }

        //获取所上传图片的信息
        userViewModel.picMsgList.observe(requireActivity(), Observer {
            it.forEach { picMsg ->
                headPicPath = picMsg.path
            }
        })
        userViewModel.modifyState.observe(requireActivity(), Observer {
            userViewModel.getUserByPhoneNumber()
        })
        userViewModel.userInfo.observe(requireActivity(), Observer {
            findNavController().navigateUp()
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.
        addCallback(this/*LifecycleOwner*/, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                back()
            }
        })
    }


    /**
     * 选择图片
     */
    private fun choosePic(fragment: Fragment,count: Int = 1,selectCallback: SelectCallback) {
        val builder = EasyPhotos.createAlbum(fragment,true,GlideEngine.getInstance())
            .setCount(count)
            .setFileProviderAuthority("com.yeyu.information.fileprovider")
            .setCleanMenu(false).setGif(false).setMinFileSize(1024 * 5.toLong())
            .start(selectCallback)
    }

    private fun back() {
        if(headPicPath == headPicUrlStr &&
            binding.etUsername.text.toString() == usernameStr &&
            binding.tvGender.text.toString() == genderStr &&
            binding.tvBirthday.text.toString() == birthdayStr )
        {
            findNavController().navigateUp()
        } else {
            val dialog = AlertDialog.Builder(requireContext())
                .setTitle("温馨提示")
                .setMessage("是否保存更改")
                .setNegativeButton("取消") { _, _ ->
                    findNavController().navigateUp()
                }
                .setPositiveButton("保存") { _, _ ->
                    userViewModel.loadState.observe(viewLifecycleOwner,LoadingObserver(requireContext(),dialog = dialog, msg = "保存中..."))
                    userViewModel.modifyUserMsg(binding.etUsername.text.toString(),gender,birthday,headPicPath)
                }
            dialog.show()
        }
    }





}