package ru.mertsalovda.firebase.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.mertsalovda.firebase.R
import ru.mertsalovda.firebase.model.Message
import ru.mertsalovda.firebase.utils.MessagesDiffUtilsCallback

class ChatAdapter : RecyclerView.Adapter<MessageHolder>() {
    private val messages = mutableListOf<Message>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.li_message, parent, false)
        return MessageHolder(view)
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
//        messages.addAll(list)
//        notifyDataSetChanged()
    }
}