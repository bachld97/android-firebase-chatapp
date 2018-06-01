package com.example.cpu02351_local.firebasechatapp.changeinfoscreen

import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.widget.Toast
import com.example.cpu02351_local.firebasechatapp.R
import com.example.cpu02351_local.firebasechatapp.utils.DaggerFirebaseReferenceComponent
import com.example.cpu02351_local.firebasechatapp.utils.FirebaseHelper
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_change_info.*
import javax.inject.Inject

class ChangeInfoActivity : AppCompatActivity() {

    private lateinit var displayName: TextInputEditText
    private lateinit var oldPass: TextInputEditText
    private lateinit var newPass: TextInputEditText
    private lateinit var confirmNewPass: TextInputEditText

    @Inject
    lateinit var databaseRef: DatabaseReference

    init {
        DaggerFirebaseReferenceComponent.create().injectInto(this)
    }


    private lateinit var thisUser: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_info)

        displayName = findViewById(R.id.displayName)
        oldPass = findViewById(R.id.passOld)
        newPass = findViewById(R.id.passNew)
        confirmNewPass = findViewById(R.id.passConfirm)

        thisUser = intent.getStringExtra("current_id")
        displayName.setText(intent.getStringExtra("current_name"))

        btnCancel.setOnClickListener {
            finish()
        }

        btnChangeName.setOnClickListener {
            requestChangeName()
            Toast.makeText(this, "Your name cannot be empty", Toast.LENGTH_SHORT).show()
        }

        btnChangePass.setOnClickListener {
            requestChangePass()
        }
    }

    private fun requestChangeName() {
        val newName = displayName.text.trim()
        if (newName.isEmpty())
            return

        val pd = ProgressDialog(this)
        pd.setMessage("Updating display name")
        pd.incrementProgressBy(20)
        pd.show()

        databaseRef.child("${ FirebaseHelper.USERS}/$thisUser/${FirebaseHelper.USERNAME}")
                .setValue(newName.toString())
                .addOnSuccessListener {
                    pd.dismiss()
                    finish()
                }
    }

    private fun requestChangePass() {
        Toast.makeText(this, "This feature is not yet supported, please come again later", Toast.LENGTH_SHORT).show()
    }

}
