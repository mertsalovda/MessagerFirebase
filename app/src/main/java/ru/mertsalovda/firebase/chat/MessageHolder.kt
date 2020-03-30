package ru.mertsalovda.firebase.chat

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView
import ru.mertsalovda.firebase.R
import ru.mertsalovda.firebase.utils.PhotoUtil
import ru.mertsalovda.firebase.model.Message
import java.text.SimpleDateFormat
import java.util.*

class MessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var tvName: TextView = itemView.findViewById(R.id.tvName)
    private var tvDate: TextView = itemView.findViewById(R.id.tvDate)
    private var tvText: TextView = itemView.findViewById(R.id.tvText)
    private var ivPhoto: CircleImageView = itemView.findViewById(R.id.ivPhoto)

    fun bind(message: Message){
        tvName.text = message.name
        tvDate.text = convertLongToTime(message.date)
        tvText.text = message.text
        PhotoUtil.getPhoto(ivPhoto, message.photoUrl)
    }

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("HH:mm dd.MM.yyyy ")
        return format.format(date)
    }

}
