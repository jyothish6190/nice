package com.nice.nice.chat.data

import com.nice.nice.chat.models.Dialog
import com.nice.nice.chat.models.Message
import com.nice.nice.chat.models.User
import java.util.*




/**
 * Created by jyothish on 3/17/18.
 */
class DialogsFixtures private constructor() : FixturesData() {
    init {
        throw AssertionError()
    }

    companion object {

        val dialogs: ArrayList<Dialog>
            get() {
                val chats : ArrayList<Dialog> =  ArrayList()

                for (i in 0..19) {
                    val calendar = Calendar.getInstance()
                    calendar.add(Calendar.DAY_OF_MONTH, -(i * i))
                    calendar.add(Calendar.MINUTE, -(i * i))

                    chats.add(getDialog(i, calendar.time))
                }

                return chats
            }

        private fun getDialog(i: Int, lastMessageCreatedAt: Date): Dialog {
            val users = users
            return Dialog(
                    randomId,
                    if (users.size > 1) groupChatTitles[users.size - 2] else users[0].name,
                    if (users.size > 1) groupChatImages[users.size - 2] else randomAvatar,
                    users,
                    getMessage(lastMessageCreatedAt),
                    if (i < 3) 3 - i else 0)
        }

        private val users: ArrayList<User>
            get() {
                val users = ArrayList<User>()
                val usersCount = 1 + rnd.nextInt(4)

                for (i in 0 until usersCount) {
                    users.add(user)
                }

                return users
            }

        private val user: User
            get() = User(
                    randomId,
                    randomName,
                    randomAvatar,
                    randomBoolean)

        private fun getMessage(date: Date): Message {
            return Message(
                    randomId,
                    user,
                    randomMessage,
                    date)
        }
    }
}