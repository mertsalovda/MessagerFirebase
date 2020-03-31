package ru.mertsalovda.firebase.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import ru.mertsalovda.firebase.R
import ru.mertsalovda.firebase.model.Message
import ru.mertsalovda.firebase.utils.MessagesDiffUtilsCallback

class ChatAdapter : RecyclerView.Adapter<MessageHolder>() {
    private val messages = mutableListOf<Message>()
    private val user = FirebaseAuth.getInstance().currentUser

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = when (viewType) {
            MessageType.OUTGOING.ordinal -> inflater.inflate(R.layout.li_outgoing_message, parent, false)
            else -> inflater.inflate(R.layout.li_incoming_message, parent, false)
        }
        return MessageHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        return when (message.author) {
            user?.uid -> MessageType.OUTGOING.ordinal
            else -> MessageType.INCOMING.ordinal
        }
    }

    override fun getItemCount(): Int = messages.size

    override fun onBindViewHolder(holder: MessageHolder, position: Int) {
        holder.bind(messages[position])
    }

    fun addData(list: List<Message>, clear: Boolean) {
        val messagesDiffUtils = MessagesDiffUtilsCallback(messages, list)
        val diffUtils = DiffUtil.calculateDiff(messagesDiffUtils)
        if (clear) {
            messages.clear()
        }
        messages.addAll(list)
        diffUtils.dispatchUpdatesTo(this)
    }
}

enum class MessageType {
    INCOMING,
    OUTGOING
}