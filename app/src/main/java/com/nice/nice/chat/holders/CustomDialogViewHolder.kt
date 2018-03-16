package com.nice.nice.chat.holders

import android.view.View
import com.nice.nice.R
import com.nice.nice.chat.models.Dialog
import com.stfalcon.chatkit.dialogs.DialogsListAdapter



/**
 * Created by jyothish on 3/17/18.
 */
class CustomDialogViewHolder(itemView: View) : DialogsListAdapter.DialogViewHolder<Dialog>(itemView) {

    private val onlineIndicator: View = itemView.findViewById(R.id.onlineIndicator)

    override fun onBind(dialog: Dialog) {
        super.onBind(dialog)

        if (dialog.users.size > 1) {
            onlineIndicator.visibility = View.GONE
        } else {
            val isOnline = dialog.users[0].isOnline
            onlineIndicator.visibility = View.VISIBLE
            if (isOnline) {
                onlineIndicator.setBackgroundResource(R.drawable.shape_bubble_online)
            } else {
                onlineIndicator.setBackgroundResource(R.drawable.shape_bubble_offline)
            }
        }
    }
}