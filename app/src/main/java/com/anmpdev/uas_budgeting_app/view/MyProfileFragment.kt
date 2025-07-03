package com.anmpdev.uas_budgeting_app.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anmpdev.uas_budgeting_app.R
import com.anmpdev.uas_budgeting_app.databinding.FragmentMyProfileBinding
import android.content.Intent
import android.graphics.Typeface
import android.text.Editable
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.StyleSpan
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.anmpdev.uas_budgeting_app.viewmodel.BudgetViewModel
import com.anmpdev.uas_budgeting_app.viewmodel.UserViewModel

class MyProfileFragment : Fragment(), ProfileActionListener {
    private lateinit var binding: FragmentMyProfileBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var viewModel: UserViewModel
    private lateinit var username: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sharedPreferences = requireActivity().getSharedPreferences("SETTING", Context.MODE_PRIVATE)
        binding.handler = this
        viewModel = ViewModelProvider(this)[UserViewModel::class.java]

//        binding.btnLogOut.setOnClickListener {
//            val editor = sharedPreferences.edit()
//            editor.putBoolean("LOGIN_STATE", false)
//            editor.apply()
//
//            val intent = Intent(requireActivity(), MainActivity::class.java)
//            startActivity(intent)
//            requireActivity().finish()
//        }

        sharedPreferences = requireActivity().getSharedPreferences("SETTING", Context.MODE_PRIVATE)
        username = sharedPreferences.getString("user_username", "") ?: ""
        Log.d("usernameBapakmu" , username)
        loadUserData()

    }

    override fun onSignOutClick(v: View) {
        sharedPreferences.edit().putBoolean("LOGIN_STATE", false).apply()
        startActivity(Intent(requireActivity(), MainActivity::class.java))
        requireActivity().finish()
    }

    override fun onChangePasswordClick(v: View) {
        val oldPassword = binding.txtOldPassword.text.toString()
        val newPassword = binding.txtNewPassword.text.toString()
        val repeatPassword = binding.txtRepeatPassword.text.toString()

//        if (newPassword != repeatPassword) {
//            binding.repeatPasswordInputLayout.error = "Password do not match"
//            return
//        }
        binding.txtRepeatPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (repeatPassword != newPassword) {
                    binding.repeatPasswordInputLayout.error = "Passwords do not match"
                } else {
                    binding.repeatPasswordInputLayout.error = null
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        viewModel.changePassword(username, oldPassword, newPassword)
        viewModel.changeSuccess.observe(viewLifecycleOwner, Observer {
            if(it==false){
                Toast.makeText(requireContext(), "Old Password is wrong!", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(requireContext(), "Password changed!", Toast.LENGTH_SHORT).show()
            }
        })



    }

    override fun loadUserData() {
        viewModel.readUser(username)
        viewModel.userLD.observe(viewLifecycleOwner, Observer {
            binding.name = it
        })
    }

}