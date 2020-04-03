package ru.mertsalovda.firebase.utils

import androidx.recyclerview.widget.DiffUtil
import ru.mertsalovda.firebase.model.Message

internal class MessagesDiffUtilsCallback(
    private val oldList: List<Message>,
    private val newList: List<Message>
) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldMessage = oldList[oldItemPosition]
        val newMessage = newList[newItemPosition]
        return oldMessage.name == newMessage.name
                && oldMessage.date == newMessage.date
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldMessage = oldList[oldItemPosition]
        val newMessage = newList[newItemPosition]
        return oldMessage.name == newMessage.name
                && oldMessage.date == newMessage.date
                && oldMessage.text == newMessage.text
                && oldMessage.photoUrl == newMessage.photoUrl

    }
}