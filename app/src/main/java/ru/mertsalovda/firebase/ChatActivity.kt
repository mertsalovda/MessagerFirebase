package ru.mertsalovda.firebase

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.ac_chat.*

class ChatActivity : AppCompatActivity() {

    private val storage = FirebaseFirestore.getInstance()

    companion object {
        fun start(context: AppCompatActivity) {
            val intent = Intent(context, ChatActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_chat)

        btnSend.setOnClickListener {
            val message = etMessage.text.toString().trim()
            if (isValidText(message)) {
                sendMessage(message)
            } else {
                toast(getString(R.string.empty_message))
            }

            etMessage.text = null
            hideKeyboard()
        }
    }

    private fun sendMessage(message: String) {
        val user = FirebaseAuth.getInstance().currentUser
        val map = mapOf(user?.displayName to message)
        storage.collection("messages")
            .add(map)
            .addOnSuccessListener {
                log("Сообщение отправлено!")
            }
            .addOnFailureListener {
                log("ОШИБКА. Сообщение не отправлено!")
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_chat, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mProfile -> {
                ProfileActivity.start(this)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun isValidText(text: String) = !TextUtils.isEmpty(text)

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun log(message: String) {
        Log.d("TAG", message)
    }

    private fun hideKeyboard() {
        val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as (InputMethodManager)
        var view = this.currentFocus
        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
