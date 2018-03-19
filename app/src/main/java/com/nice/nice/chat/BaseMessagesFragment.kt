package com.nice.nice.chat

import android.os.Bundle
import android.support.v4.app.Fragment
import com.nice.nice.chat.models.Dialog
import com.nice.nice.utils.AppUtils
import com.squareup.picasso.Picasso
import com.stfalcon.chatkit.commons.ImageLoader
import com.stfalcon.chatkit.dialogs.DialogsListAdapter


abstract class BaseMessagesFragment : Fragment(),  DialogsListAdapter.OnDialogClickListener<Dialog>, DialogsListAdapter.OnDialogLongClickListener<Dialog> {
    protected var imageLoader: ImageLoader? = null
    protected var dialogsAdapter: DialogsListAdapter<Dialog>? = null

    override fun onDialogLongClick(dialog: Dialog?) {
        AppUtils.showToast(
                activity, dialog?.dialogName!!,
                false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        imageLoader = ImageLoader { imageView, url -> Picasso.with(activity).load(url).into(imageView) }

    }


}
