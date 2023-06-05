package com.yeyu.information.ui.comment

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yeyu.information.R
import com.yeyu.information.base.Constant
import com.yeyu.information.data.Comment

class CommentAdapter : BaseQuickAdapter<Comment, BaseViewHolder>(R.layout.item_comment),
    LoadMoreModule {

    override fun convert(holder: BaseViewHolder, item: Comment) {
        item.apply {
            val image = holder.getView<ImageView>(R.id.iv_head)
            Glide.with(image).load(Constant.fillImgUrl(headPicAddress)).circleCrop().placeholder(R.drawable.ic_default_head).into(image)
            holder.setText(R.id.tv_username,username)
            holder.setText(R.id.tv_content,content)
            holder.setText(R.id.tv_time,publishTime)
        }
    }
}