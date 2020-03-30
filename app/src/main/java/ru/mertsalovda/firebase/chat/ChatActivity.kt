package ru.mertsalovda.firebase.chat

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.ac_chat.*
import ru.mertsalovda.firebase.ProfileActivity
import ru.mertsalovda.firebase.R
import ru.mertsalovda.firebase.model.Message

class ChatActivity : AppCompatActivity() {

    private val storage = FirebaseFirestore.getInstance()
    private var adapter: ChatAdapter

    init {
        adapter = ChatAdapter()
    }

    companion object {
        fun start(context: AppCompatActivity) {
            val intent = Intent(context, ChatActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            context.startActivity(intent)
        }

        private const val COLLECTION = "messages"

    }

    override fun onStart() {
        super.onStart()
        storage.collection(COLLECTION).addSnapshotListener(this) { snapshot, e ->
            if (e != null) {
                log("ERROR ${e.message}")
                return@addSnapshotListener
            }
            val documents = snapshot?.documents
            if (documents != null) {
                val list = mutableListOf<Message>()
                for (doc in documents) {
                    val message = doc?.toObject(Message::class.java)
                    log("DATA $message")
                    if (message != null) {
                        list.add(message)
                    }
                }
                list.sortBy { it.date }
                adapter.addData(list, true)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_chat)

        val layoutManager = LinearLayoutManager(this)
        recycler.layoutManager = layoutManager
        recycler.adapter = adapter


        btnSend.setOnClickListener {
            val message = etMessage.text.toString().trim()
            if (isValidText(message)) {
                sendMessage(message)
            } else {
                etMessage.error = getString(R.string.empty_message)
            }

            etMessage.text = null
            hideKeyboard()
        }
    }

    private fun sendMessage(message: String) {
        val user = FirebaseAuth.getInstance().currentUser
        val mess = Message(
            user?.displayName.toString(),
            message,
            System.currentTimeMillis(),
            user?.photoUrl.toString()
        )
        val map = mapOf(user?.displayName to message)
        storage.collection(COLLECTION)
            .add(mess)
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
        this.also {
            val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as (InputMethodManager)
            var view = currentFocus
            if (view == null) {
                view = View(it)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
