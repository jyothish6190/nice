package com.nice.nice.chat.message.holders

import android.view.View
import com.nice.nice.chat.models.Message
import com.stfalcon.chatkit.messages.MessageHolders

/**
 * Created by jyothish on 3/17/18.
 */
class CustomOutcomingTextMessageViewHolder(itemView: View) : MessageHolders.OutcomingTextMessageViewHolder<Message>(itemView) {

    override fun onBind(message: Message) {
        super.onBind(message)

        time.text = message.status + " " + time.text
    }
}
