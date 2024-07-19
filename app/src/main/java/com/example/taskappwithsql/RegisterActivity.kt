package com.example.taskappwithsql

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.taskappwithsql.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var FName: EditText
    private lateinit var LName: EditText
    private lateinit var Email: EditText
    private lateinit var Password: EditText
    private lateinit var Register: Button
    private lateinit var Login: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()// Initialize Firebase Auth
        // Initialize UI elements
        FName = findViewById(R.id.etFName)
        LName = findViewById(R.id.etLName)
        Email = findViewById(R.id.etEmail)
        Password = findViewById(R.id.etPassword)

        Register = findViewById(R.id.btnRegister)// Initialize Register button
        Login = findViewById(R.id.tvLogin) // Initialize Login TextView\
        // Set click listener for Login TextView
        Login.setOnClickListener{
            // create an intent for RegisterActivity to LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        // Set click listener for Register button
        Register.setOnClickListener {
            val FName = FName.text.toString()
            val LName = LName.text.toString()
            val Email = Email.text.toString()
            val Password = Password.text.toString()
            // Check if all fields are filled
                if (FName.isNotEmpty() && LName.isNotEmpty() && Email.isNotEmpty() && Password.isNotEmpty()) {
                    // Register user with Firebase Authentication
                    auth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(this) { task ->
                    val intent = Intent(this, MainActivity::class.java) // Create an intent for MainActivity
                    startActivity(intent)}
                }else{
                    // Display toast message if any field is empty
                    Toast.makeText(this, "Please Fill the all details", Toast.LENGTH_SHORT).show()
                }
        }
    }
}