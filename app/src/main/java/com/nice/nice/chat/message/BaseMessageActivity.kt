package com.nice.nice.chat.message

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.nice.nice.R
import com.nice.nice.chat.data.MessagesFixtures
import com.nice.nice.chat.models.Message
import com.nice.nice.utils.AppUtils
import com.squareup.picasso.Picasso
import com.stfalcon.chatkit.commons.ImageLoader
import com.stfalcon.chatkit.messages.MessagesListAdapter
import java.text.SimpleDateFormat
import java.util.*


abstract class BaseMessageActivity : AppCompatActivity(), MessagesListAdapter.SelectionListener, MessagesListAdapter.OnLoadMoreListener {

    protected val senderId = "0"
    protected var imageLoader: ImageLoader? = null
    protected var messagesAdapter: MessagesListAdapter<Message>? = null

    private var menu: Menu? = null
    private var selectionCount: Int = 0
    private var lastLoadedDate: Date? = null

    private val messageStringFormatter: MessagesListAdapter.Formatter<Message>
        get() = MessagesListAdapter.Formatter { message ->
            val createdAt = SimpleDateFormat("MMM d, EEE 'at' h:mm a", Locale.getDefault())
                    .format(message.createdAt)

            var text = message.text
            if (text == null) text = "[attachment]"

            String.format(Locale.getDefault(), "%s: %s (%s)",
                    message.user.name, text, createdAt)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        imageLoader = ImageLoader { imageView, url -> Picasso.with(this@BaseMessageActivity).load(url).into(imageView) }
    }

    override fun onStart() {
        super.onStart()
        messagesAdapter!!.addToStart(MessagesFixtures.textMessage, true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        this.menu = menu
        menuInflater.inflate(R.menu.chat_menu, menu)
        onSelectionChanged(0)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> messagesAdapter!!.deleteSelectedMessages()
            R.id.action_copy -> {
                messagesAdapter!!.copySelectedMessagesText(this, messageStringFormatter, true)
                AppUtils.showToast(this, R.string.copied_message, true)
            }
        }
        return true
    }

    override fun onBackPressed() {
        if (selectionCount == 0) {
            super.onBackPressed()
        } else {
            messagesAdapter!!.unselectAllItems()
        }
    }

    override fun onLoadMore(page: Int, totalItemsCount: Int) {
        if (totalItemsCount < TOTAL_MESSAGES_COUNT) {
            loadMessages()
        }
    }

    override fun onSelectionChanged(count: Int) {
        this.selectionCount = count
        menu!!.findItem(R.id.action_delete).isVisible = count > 0
        menu!!.findItem(R.id.action_copy).isVisible = count > 0
    }

    protected fun loadMessages() {
        Handler().postDelayed(Runnable //imitation of internet connection
        {
            val messages = MessagesFixtures.getMessages(lastLoadedDate)
            lastLoadedDate = messages[messages.size - 1].createdAt
            messagesAdapter!!.addToEnd(messages, false)
        }, 1000)
    }

    companion object {

        private const val TOTAL_MESSAGES_COUNT = 100
    }
}
