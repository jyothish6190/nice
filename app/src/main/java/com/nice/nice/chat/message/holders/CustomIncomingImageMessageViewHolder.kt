package com.nice.nice.chat.message.holders

import android.view.View
import com.nice.nice.R
import com.nice.nice.chat.models.Message
import com.stfalcon.chatkit.messages.MessageHolders

/**
 * Created by jyothish on 3/17/18.
 */
class CustomIncomingImageMessageViewHolder(itemView: View) : MessageHolders.IncomingImageMessageViewHolder<Message>(itemView) {

    private val onlineIndicator: View

    init {
        onlineIndicator = itemView.findViewById(R.id.onlineIndicator)
    }

    override fun onBind(message: Message) {
        super.onBind(message)

        val isOnline = message.user.isOnline
        if (isOnline) {
            onlineIndicator.setBackgroundResource(R.drawable.shape_bubble_online)
        } else {
            onlineIndicator.setBackgroundResource(R.drawable.shape_bubble_offline)
        }
    }
}