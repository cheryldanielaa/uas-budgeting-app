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

class MyProfileFragment : Fragment() {
    private lateinit var binding: FragmentMyProfileBinding
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
        binding = FragmentMyProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sharedPreferences = requireActivity().getSharedPreferences("SETTING", Context.MODE_PRIVATE)

        binding.btnLogOut.setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.putBoolean("LOGIN_STATE", false)
            editor.apply()

            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        val name = sharedPreferences.getString("user_full_name", "")

        //supaya cuma name yg bold
        binding.txtHello.text = Html.fromHtml("Hey, <b>$name</b>! How are you today?", Html.FROM_HTML_MODE_LEGACY)

        binding.txtRepeatPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = binding.txtNewPassword.text.toString()
                val repeatPassword = binding.txtRepeatPassword.text.toString()
                if (repeatPassword != password) {
                    binding.repeatPasswordInputLayout.error = "Passwords do not match"
                } else {
                    binding.repeatPasswordInputLayout.error = null
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }
}