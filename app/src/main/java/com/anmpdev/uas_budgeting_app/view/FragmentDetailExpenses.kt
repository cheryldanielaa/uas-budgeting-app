package com.anmpdev.uas_budgeting_app.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.anmpdev.uas_budgeting_app.R
import com.anmpdev.uas_budgeting_app.databinding.DialogCardBinding


class DialogFragment : DialogFragment() {
    private  var _binding: DialogCardBinding?=null
    private val binding get()=_binding!! //binding itu equeals to _binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = DialogCardBinding.inflate(layoutInflater,container,false)
        //atur value dari tiap data
        binding.txtTanggal.text = "21 Maret 2017"
        binding.txtNotes.text="Bayar sampah bulanan"
        binding.txtBudgetName.text="Rumah Tangga"

        //jika diklik maka close
        binding.btnClose.setOnClickListener {
            dismiss() //tutup
        }
        return binding.root
    }
    //harus dioverride on destroy view
    //karena bs jadi dialog fragment hidup lebih lama dr viewnya
    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}
