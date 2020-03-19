package ru.mertsalovda.messager

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 100

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
        if(requestCode == RC_SIGN_IN){
            val response = IdpResponse.fromResultIntent(data)
            if(resultCode == Activity.RESULT_OK){
                val user = FirebaseAuth.getInstance().currentUser
            } else {
                Toast.makeText(this, "Auth error!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
