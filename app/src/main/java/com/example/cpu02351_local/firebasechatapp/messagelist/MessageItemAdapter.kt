package com.example.cpu02351_local.firebasechatapp.messagelist

import android.content.Context
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.cpu02351_local.firebasechatapp.messagelist.model.MessageItem
import com.example.cpu02351_local.firebasechatapp.model.User
import com.example.cpu02351_local.firebasechatapp.utils.BaseItemAdapter
import com.example.cpu02351_local.firebasechatapp.utils.BaseItemHolder
import com.example.cpu02351_local.firebasechatapp.utils.ListItem
import android.content.ClipData
import android.content.ClipboardManager
import android.util.Log
import android.widget.Toast
import com.example.cpu02351_local.firebasechatapp.databinding.*
import com.example.cpu02351_local.firebasechatapp.messagelist.model.VideoMessageItem
import com.example.cpu02351_local.firebasechatapp.previewscreen.ImagePreviewActivity
import com.example.cpu02351_local.firebasechatapp.messagelist.viewholder.*
import com.example.cpu02351_local.firebasechatapp.previewscreen.VideoPreviewActivity


class MessageItemAdapter(context: Context, private val mViewModel: MessageViewModel)
    : BaseItemAdapter<MessageItem>() {
    private var infoMap: HashMap<String, User> ?= null

    companion object {
        const val TEXT_MINE = 0
        const val TEXT_OTHER = 1
        const val IMAGE_MINE = 2
        const val IMAGE_OTHER = 3
        const val VIDEO_MINE = 4
        const val VIDEO_OTHER = 5
    }

    fun updateUserInfo(info: HashMap<String, User> ) {
        infoMap = info
        listItems?.mapNotNull { it as? MessageItem }
                ?.forEachIndexed { index, item ->
                    if (item.hasUserInfoChange(info))
                        notifyItemChanged(index)
               }
    }

    override fun calculateDiffResult(newItems: List<ListItem>?): DiffUtil.DiffResult? {
        return null
    }

    override fun setItems(items: List<ListItem>?, fromNetwork: Boolean) {
        super.setItems(items, true)
        if (infoMap != null)
            updateUserInfo(infoMap!!)
    }

    override fun getItemViewType(position: Int): Int {
        return when ((listItems?.get(position) as? MessageItem)?.getType()) {
            "text_mine" -> TEXT_MINE
            "text_other" -> TEXT_OTHER
            "image_other" -> IMAGE_OTHER
            "image_mine" -> IMAGE_MINE
            "video_mine" -> VIDEO_MINE
            "video_other" -> VIDEO_OTHER
            else -> throw IllegalStateException("ListItems cannot be null at " +
                    "this time or unsupported messageType found [application is outdated]")
        }
    }

    private val textLongClicked = object : ItemLongClickCallback {
        override fun onLongClick(item: MessageItem) {
            Toast.makeText(context, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(null, item.getContent())
            clipboard.primaryClip = clip
        }
    }

    private val messageClick = object : ItemClickCallback {
        override fun onClick(item: MessageItem) {
            val pos = findItem(item)
            if (pos != -1) {
                item.shouldDisplayTime = !item.shouldDisplayTime
                notifyItemChanged(pos)
            }
        }
    }

    private val imageClick = object : ItemClickCallback {
        override fun onClick(item: MessageItem) {
            val intent = ImagePreviewActivity.fromDownloadUrl(context, item.getContent())
            context.startActivity(intent)
        }
    }

    private val imageSendRetry = object : ItemClickCallback {
        override fun onClick(item: MessageItem) {
            mViewModel.retrySendImage(item.getContent(), item.message.id)
        }
    }

    private val videoClick = object : ItemClickCallback {
        override fun onClick(item: MessageItem) {
            val videoItem = item as VideoMessageItem
            val intent = VideoPreviewActivity.videoPreviewIntent(context, videoItem.thumbnailUrl, videoItem.videoUrl)
            context.startActivity(intent)
        }
    }

    private val videoRetryClick = object : ItemClickCallback {
        override fun onClick(item: MessageItem) {
            // Do nothing for now
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseItemHolder<out ListItem> {
        val layoutInflater = LayoutInflater.from(parent.context)
        lateinit var holder: BaseItemHolder<MessageItem>
        holder = when (viewType) {
            TEXT_MINE -> {
                val binding = ItemTextMessageBinding.inflate(layoutInflater, parent, false)
                MessageTextMineHolder(binding, messageClick, textLongClicked)
            }
            TEXT_OTHER -> {
                val binding = ItemTextMessageFromOtherBinding.inflate(layoutInflater, parent, false)
                MessageTextOtherHolder(binding, messageClick, textLongClicked)
            }
            IMAGE_MINE -> {
                val binding = ItemImageMessageBinding.inflate(layoutInflater, parent, false)
                MessageImageMineHolder(binding, imageClick, imageSendRetry)
            }
            IMAGE_OTHER -> {
                val binding = ItemImageMessageFromOtherBinding.inflate(layoutInflater, parent, false)
                MessageImageOtherHolder(binding, imageClick)
            }
            VIDEO_MINE -> {
                val binding = ItemVideoMessageBinding.inflate(layoutInflater, parent, false)
                MessageVideoMineHolder(binding, videoClick, videoRetryClick)
            }
            VIDEO_OTHER -> {
                val binding = ItemVideoMessageFromOtherBinding.inflate(layoutInflater, parent, false)
                MessageVideoOtherHolder(binding, videoClick)
            }
            else -> throw RuntimeException("Unsupported viewType")
        }
        return holder
    }

    interface ItemLongClickCallback {
        fun onLongClick(item: MessageItem)
    }

    interface ItemClickCallback {
        fun onClick(item: MessageItem)
    }
}