package ru.mertsalovda.messager

import android.app.Activity
import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 100
    private val db = FirebaseDatabase.getInstance()
    private val myRef = db.getReference("message")
    private val store = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val providers: List<AuthUI.IdpConfig> = listOf(
                AuthUI.IdpConfig.EmailBuilder().build(),
//                AuthUI.IdpConfig.PhoneBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build()
        )
        auth(providers)
        logOut.setOnClickListener {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener { auth(providers) }
        }

        addInDB.setOnClickListener {
            myRef.setValue("Hello World!")
        }
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // этот метод вызывается при создании и каждый раз, когда данные в базе обновляются
                val value = dataSnapshot.getValue(String::class.java)
                Toast.makeText(applicationContext, value, Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(error: DatabaseError) {
                // ошибка
            }
        })
        addInStorage.setOnClickListener {
            // Создать нового пользователя с именем и фамилией
            val user = hashMapOf(
                    "first" to "Ada",
                    "last" to "Lovelace",
                    "born" to 1815
            )
            // Добавить новый документ с сгенерированным идентификатором
            store.collection("users")
                    .add(user)
                    .addOnSuccessListener {
                        Toast.makeText(applicationContext, user.toString(), Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
                        println(it.message)
                    }
        }
    }

    private fun auth(providers: List<AuthUI.IdpConfig>) {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser
            } else {
                Toast.makeText(this, "Auth error!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
