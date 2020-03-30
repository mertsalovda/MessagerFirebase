package ru.mertsalovda.firebase

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import ru.mertsalovda.firebase.chat.ChatActivity

class LoginActivity : AppCompatActivity() {

    override fun onStart() {
        super.onStart()
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            auth()
        } else {
            ChatActivity.start(this)
            this.finish()
        }
    }

    private fun auth() {

        val providers: List<AuthUI.IdpConfig> = listOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.drawable.ic_logo_foreground)
                .setTheme(R.style.LoginStyle)
                .build(),
            RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                ChatActivity.start(this)
                this.finish()
            } else {
                this.finish()
            }
        }
    }

    companion object {
        private const val RC_SIGN_IN = 100

        fun start(context: AppCompatActivity) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }
}
