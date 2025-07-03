package com.anmpdev.uas_budgeting_app.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.anmpdev.uas_budgeting_app.R
import com.anmpdev.uas_budgeting_app.databinding.FragmentListReportBinding
import com.anmpdev.uas_budgeting_app.viewmodel.ReportViewModel
import java.text.NumberFormat
import java.util.Locale

class ListReportFragment : Fragment() {
    private lateinit var binding: FragmentListReportBinding
    private lateinit var viewModel: ReportViewModel
    private val reportAdapter = ListReportAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ReportViewModel::class.java)

        binding.recViewReport.layoutManager = LinearLayoutManager(context)
        binding.recViewReport.adapter = reportAdapter

        val sharedPreferences = requireActivity().getSharedPreferences("SETTING", Context.MODE_PRIVATE)
        val uuid = sharedPreferences.getInt("user_id", 0)

        viewModel.refresh(uuid)
        observeViewModel()
    }

    fun observeViewModel() {
        viewModel.reportLD.observe(viewLifecycleOwner, Observer {
            reportAdapter.updateReportList(it)

            if (it.isEmpty()) {
                binding.recViewReport.visibility = View.GONE
                binding.txtError.text = "No report data found"
                binding.txtError.visibility = View.VISIBLE
            }
            else {
                binding.recViewReport.visibility = View.VISIBLE
                binding.txtError.visibility = View.GONE

                //hitung totalUsed & totalBudget dari tiap expense buat ditampilin di txtTotal
                val totalUsed = it.sumOf { it.used ?: 0 }
                val totalBudget = it.sumOf { it.nominal }

                binding.txtTotal.text = "IDR ${formatRupiah(totalUsed)} / IDR ${formatRupiah(totalBudget)}"
            }
        })

        viewModel.loadingLD.observe(viewLifecycleOwner, Observer {
            if(it==false){
                binding.progressLoad.visibility = View.GONE
            }
            else{
                binding.progressLoad.visibility = View.VISIBLE
            }
        })

        viewModel.reportLoadErrorLD.observe(viewLifecycleOwner, Observer {
            if(it==false){
                binding.txtError.visibility = View.GONE
            }
            else{
                binding.txtError.visibility = View.VISIBLE
            }
        })
    }

    fun formatRupiah(value: Int): String {
        val formatter = NumberFormat.getInstance(Locale("in", "ID"))
        return formatter.format(value)
    }
}