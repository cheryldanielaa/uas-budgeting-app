package com.anmpdev.uas_budgeting_app.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anmpdev.uas_budgeting_app.databinding.FragmentSignUpBinding
import com.anmpdev.uas_budgeting_app.viewmodel.SignUpViewModel
import com.anmpdev.uas_budgeting_app.model.User
import android.widget.Toast
import android.text.TextWatcher
import android.text.Editable
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var viewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[SignUpViewModel::class.java]

        //buat back button
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)

        binding.btnCreateAccount.setOnClickListener {
            val username = binding.txtUsername.text.toString()
            val fname = binding.txtFirstName.text.toString()
            val lname = binding.txtLastName.text.toString()
            val password = binding.txtPassword.text.toString()
            val repeatPassword = binding.txtRepeatPassword.text.toString()

            if (username.isBlank() || fname.isBlank() || lname.isBlank() || password.isBlank() || repeatPassword.isBlank()) {
                Toast.makeText(requireContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            }
            else {
                val newUser = User(username, fname, lname, password)
                viewModel.insertUser(newUser)
            }
        }

        // Password match checker
        binding.txtRepeatPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = binding.txtPassword.text.toString()
                val repeatPassword = binding.txtRepeatPassword.text.toString()
                if (repeatPassword != password) {
                    binding.repeatPasswordInputLayout.error = "Passwords do not match"
                } else {
                    binding.repeatPasswordInputLayout.error = null
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        viewModel.statusInsert.observe(viewLifecycleOwner) { success ->
            if (success == true) {
                Toast.makeText(requireContext(), "Account created!", Toast.LENGTH_SHORT).show()

                //navigate ke Sign In
                val action = SignUpFragmentDirections.actionSignInFragment()
                Navigation.findNavController(requireView()).navigate(action)
            } else {
                Toast.makeText(requireContext(), "Username already exists!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                findNavController().navigateUp() //kembali ke fragment sebelumnya
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    //destory back button kalau keluar dari fragment
    override fun onDestroyView() {
        super.onDestroyView()
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }
}