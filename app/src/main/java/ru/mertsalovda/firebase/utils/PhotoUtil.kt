package ru.mertsalovda.firebase.utils

import android.text.TextUtils
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import ru.mertsalovda.firebase.R

class PhotoUtil {
    companion object {
        fun getPhoto(view: CircleImageView, uri: String) {
            Picasso.get().load(uri).error(R.drawable.person).into(view)
        }
    }
}
