package com.nice.nice.chat


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nice.nice.R
import com.nice.nice.chat.data.DialogsFixtures
import com.nice.nice.chat.holders.CustomDialogViewHolder
import com.nice.nice.chat.message.MessageActivity
import com.nice.nice.chat.models.Dialog
import com.stfalcon.chatkit.dialogs.DialogsListAdapter
import kotlinx.android.synthetic.main.activity_chat_list.*


/**
 * A simple [Fragment] subclass.
 */
class MessagesFragment : BaseMessagesFragment(){
    override fun onDialogClick(dialog: Dialog?) {
        MessageActivity.open(activity)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initAdapter()

    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.activity_chat_list, container, false)
    }

    private fun initAdapter() {
        val manager = LinearLayoutManager(activity)
        super.dialogsAdapter = DialogsListAdapter(
                R.layout.item_custom_dialog_view_holder,
                CustomDialogViewHolder::class.java,
                super.imageLoader)


        super.dialogsAdapter?.setItems(DialogsFixtures.dialogs)

        super.dialogsAdapter?.setOnDialogClickListener(this)
        super.dialogsAdapter?.setOnDialogLongClickListener(this)

        dialogsList.setAdapter(super.dialogsAdapter)
        dialogsList.layoutManager = manager
    }

}
