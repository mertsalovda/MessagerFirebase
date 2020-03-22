package ru.mertsalovda.firebase

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.ac_profile.*

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_profile)

        val displayName = FirebaseAuth.getInstance().currentUser?.displayName
        tvUsername.text = displayName
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_profile, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mLogout -> {
                AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener {
                        LoginActivity.start(this)
                        this.finish()
                    }
                true
            }
            R.id.mDelete -> {
                AuthUI.getInstance()
                    .delete(this)
                    .addOnCompleteListener {
                        LoginActivity.start(this)
                        this.finish()
                    }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        fun start(context: AppCompatActivity){
            val intent = Intent(context, ProfileActivity::class.java)
            context.startActivity(intent)
        }
    }
}