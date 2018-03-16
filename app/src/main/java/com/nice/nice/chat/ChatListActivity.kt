package com.nice.nice.chat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.nice.nice.R
import com.nice.nice.chat.data.DialogsFixtures
import com.nice.nice.chat.holders.CustomDialogViewHolder
import com.nice.nice.chat.message.MessageActivity
import com.nice.nice.chat.models.Dialog
import com.stfalcon.chatkit.dialogs.DialogsListAdapter
import kotlinx.android.synthetic.main.activity_chat_list.*


class ChatListActivity : DialogsActivity() {


     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_list)

        initAdapter()
    }

    override fun onDialogClick(dialog: Dialog) {
        MessageActivity.open(this)
    }

    private fun initAdapter() {
        super.dialogsAdapter = DialogsListAdapter(
                R.layout.item_custom_dialog_view_holder,
                CustomDialogViewHolder::class.java,
                super.imageLoader)

        super.dialogsAdapter?.setItems(DialogsFixtures.dialogs)

        super.dialogsAdapter?.setOnDialogClickListener(this)
        super.dialogsAdapter?.setOnDialogLongClickListener(this)

        dialogsList.setAdapter(super.dialogsAdapter)
    }

    companion object {

        fun open(context: Context) {
            context.startActivity(Intent(context, ChatListActivity::class.java))
        }
    }
}


