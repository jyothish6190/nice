package com.nice.nice.chat.message

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.nice.nice.R
import com.nice.nice.chat.data.MessagesFixtures
import com.nice.nice.chat.message.holders.CustomIncomingImageMessageViewHolder
import com.nice.nice.chat.message.holders.CustomIncomingTextMessageViewHolder
import com.nice.nice.chat.message.holders.CustomOutcomingImageMessageViewHolder
import com.nice.nice.chat.message.holders.CustomOutcomingTextMessageViewHolder
import com.nice.nice.chat.models.Message
import com.nice.nice.utils.AppUtils
import com.stfalcon.chatkit.messages.MessageHolders
import com.stfalcon.chatkit.messages.MessageInput
import com.stfalcon.chatkit.messages.MessagesListAdapter
import kotlinx.android.synthetic.main.activity_message.*


class MessageActivity : BaseMessageActivity(), MessagesListAdapter.OnMessageLongClickListener<Message>, MessageInput.InputListener, MessageInput.AttachmentsListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        initAdapter()

        input.setInputListener(this)
        input.setAttachmentsListener(this)
    }

    override fun onSubmit(input: CharSequence): Boolean {
        messagesAdapter?.addToStart(
                MessagesFixtures.getTextMessage(input.toString()), true)
        return true
    }

    override fun onAddAttachments() {
        messagesAdapter?.addToStart(MessagesFixtures.imageMessage, true)
    }

    override fun onMessageLongClick(message: Message) {
        AppUtils.showToast(this, R.string.on_log_click_message, false)
    }

    private fun initAdapter() {
        val holdersConfig = MessageHolders()
                .setIncomingTextConfig(
                        CustomIncomingTextMessageViewHolder::class.java!!,
                        R.layout.item_custom_incoming_text_message)
                .setOutcomingTextConfig(
                        CustomOutcomingTextMessageViewHolder::class.java!!,
                        R.layout.item_custom_outcoming_text_message)
                .setIncomingImageConfig(
                        CustomIncomingImageMessageViewHolder::class.java!!,
                        R.layout.item_custom_incoming_image_message)
                .setOutcomingImageConfig(
                        CustomOutcomingImageMessageViewHolder::class.java!!,
                        R.layout.item_custom_outcoming_image_message)

        super.messagesAdapter = MessagesListAdapter(super.senderId, holdersConfig, super.imageLoader)
        super.messagesAdapter?.setOnMessageLongClickListener(this)
        super.messagesAdapter?.setLoadMoreListener(this)
        messagesList.setAdapter(super.messagesAdapter)
    }

    companion object {

        fun open(context: Context) {
            context.startActivity(Intent(context, MessageActivity::class.java))
        }
    }
}

