package com.example.taskappwithsql

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.taskappwithsql.databinding.ActivityLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding
    private lateinit var Email: EditText
    private lateinit var Password: EditText
    private lateinit var Login: Button
    private lateinit var Register: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()    // Initialize Firebase Auth
        // Initialize EditText fields
        Email = findViewById(R.id.etEmail)
        Password = findViewById(R.id.etPassword)

        Login = findViewById(R.id.btnLogin) // Initialize Login button
        Register = findViewById(R.id.tvRegister) // Initialize Register TextView
        // Set click listener for Register TextView
        Register.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)// Create an intent for LoginActivity to RegisterActivity
            startActivity(intent)
        }
        // Set click listener for Login button
        Login.setOnClickListener{
            val Email = Email.text.toString()
            val Password = Password.text.toString()
            // Check if Email and Password are not empty
                if (Email.isNotEmpty() && Password.isNotEmpty()) {
                    // Sign in using Firebase Auth
                    auth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(this) { task ->
                        // If sign in is successful, navigate to MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)}
                }else{
                    // If Email or Password is empty, show a Toast message
                    Toast.makeText(this, "Please Enter Email and Password", Toast.LENGTH_SHORT).show()
                }
        }
    }
}