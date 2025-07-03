package com.anmpdev.uas_budgeting_app.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anmpdev.uas_budgeting_app.R
import com.anmpdev.uas_budgeting_app.databinding.FragmentSignInBinding
import com.anmpdev.uas_budgeting_app.viewmodel.SignInViewModel
import androidx.lifecycle.ViewModelProvider
import android.widget.Toast
import androidx.navigation.Navigation

class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding
    private lateinit var viewModel: SignInViewModel
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[SignInViewModel::class.java]
        sharedPreferences = requireActivity().getSharedPreferences("SETTING", Context.MODE_PRIVATE)

        val loginState = sharedPreferences.getBoolean("LOGIN_STATE", false)

        if (loginState) {
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish() //supaya gabisa balik ke login
        }

        binding.btnSignIn.setOnClickListener {
            val username = binding.txtUsername.text.toString()
            val password = binding.txtPassword.text.toString()

            if (username.isBlank() || password.isBlank()) {
                Toast.makeText(requireContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.login(username, password)
            }
        }

        viewModel.loginResult.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                val editor = sharedPreferences.edit()
                editor.putBoolean("LOGIN_STATE", true)
                editor.putInt("user_id", user.uuid)
                editor.putString("user_full_name", "${user.fname} ${user.lname}")
                editor.putString("user_username", user.username)
                editor.apply()

                Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_SHORT).show()

                val intent = Intent(requireActivity(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish() //supaya gabisa balik ke login
            } else {
                Toast.makeText(requireContext(), "Invalid credentials", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnSignUp.setOnClickListener {
            //navigate ke Sign Up
            val action = SignInFragmentDirections.actionSignUpFragment()
            Navigation.findNavController(it).navigate(action)
        }
    }
}