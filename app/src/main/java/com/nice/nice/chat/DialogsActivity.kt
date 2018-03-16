package com.nice.nice.chat

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.nice.nice.chat.models.Dialog
import com.nice.nice.utils.AppUtils
import com.squareup.picasso.Picasso
import com.stfalcon.chatkit.commons.ImageLoader
import com.stfalcon.chatkit.dialogs.DialogsListAdapter


/**
 * Created by jyothish on 3/17/18.
 */
abstract class DialogsActivity : AppCompatActivity(), DialogsListAdapter.OnDialogClickListener<Dialog>, DialogsListAdapter.OnDialogLongClickListener<Dialog> {

    protected var imageLoader: ImageLoader? = null
    protected var dialogsAdapter: DialogsListAdapter<Dialog>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        imageLoader = ImageLoader { imageView, url -> Picasso.with(this@DialogsActivity).load(url).into(imageView) }
    }

    override fun onDialogLongClick(dialog: Dialog) {
        AppUtils.showToast(
                this, dialog.dialogName,
                false)
    }
}