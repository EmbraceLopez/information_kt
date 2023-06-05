package com.yeyu.information.ui.news

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yeyu.information.R
import com.yeyu.information.base.Constant
import com.yeyu.information.data.Info

class NewsListAdapter : BaseQuickAdapter<Info, BaseViewHolder>(R.layout.item_info_list), LoadMoreModule {

    override fun convert(holder: BaseViewHolder, item: Info) {
        item.apply {
            val image = holder.getView<ImageView>(R.id.iv_list_pic)
            Glide.with(image).load("${Constant.SERVER_URL}${infoPicUrl}").placeholder(R.drawable.ic_placeholder).into(image)
            holder.setText(R.id.tv_title,title)
            holder.setText(R.id.tv_dateline,dateline)
        }
    }
}