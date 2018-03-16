package com.nice.nice.chat.models

import com.stfalcon.chatkit.commons.models.IDialog



/**
 * Created by jyothish on 3/17/18.
 */
class Dialog(private val id: String, private val dialogName: String, private val dialogPhoto: String,
             private val users: ArrayList<User>, private var lastMessage: Message?, private var unreadCount: Int) : IDialog<Message> {

    override fun getId(): String {
        return id
    }

    override fun getDialogPhoto(): String {
        return dialogPhoto
    }

    override fun getDialogName(): String {
        return dialogName
    }

    override fun getUsers(): ArrayList<User> {
        return users
    }

    override fun getLastMessage(): Message? {
        return lastMessage
    }

    override fun setLastMessage(lastMessage: Message) {
        this.lastMessage = lastMessage
    }

    override fun getUnreadCount(): Int {
        return unreadCount
    }

    fun setUnreadCount(unreadCount: Int) {
        this.unreadCount = unreadCount
    }
}
