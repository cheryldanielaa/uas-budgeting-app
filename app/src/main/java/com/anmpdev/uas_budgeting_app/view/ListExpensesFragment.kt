package com.anmpdev.uas_budgeting_app.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.anmpdev.uas_budgeting_app.R
import com.anmpdev.uas_budgeting_app.databinding.FragmentListExpensesBinding
import com.anmpdev.uas_budgeting_app.viewmodel.ListExpenseViewModel


class ListExpensesFragment : Fragment() {
    private lateinit var binding:FragmentListExpensesBinding
    private lateinit var viewModel:ListExpenseViewModel
    //tambahin lambda function utk nambahin adapter utk list expense
    private val expenseListAdapter = ListExpenseAdapter(arrayListOf())
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListExpensesBinding.inflate(layoutInflater, container, false)
        //pake binding memungkinkan pake nama elemen yang sama di beda fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //inisialisasi viewmodel disini
        viewModel = ViewModelProvider(this).get(ListExpenseViewModel::class.java)
        //isi hasil load data disini
        viewModel.refresh() //isi data viewmodelnya
        //isi data adapter
        binding.recView.layoutManager=LinearLayoutManager(context)
        binding.recView.adapter = expenseListAdapter
        //klo misal diklik fabnya, maka dia akan pindah ke create expense (fragment)
        binding.btnAddExpense.setOnClickListener {
            //arahkan ke fragment tsb pke navigation
            val action = ListExpensesFragmentDirections.actionCreateNewExpenses()
            Navigation.findNavController(it).navigate(action)
            /*kode dibawah dibuat klo udh kehubung sm adapterrr
            val action = ListExpensesFragmentDirections.actionDetailExpenses()
            Navigation.findNavController(it).navigate(action)*/
        }
        observeViewModel()
    }

    fun observeViewModel(){
        //buat baca data scr terus menerus
        viewModel.expenseLD.observe(viewLifecycleOwner, Observer {
            //update data secara langsung dari data yang ada di database
            expenseListAdapter.updateListExpense(it) //perbaharui data di adapter
            if(it.isEmpty()){
                //jika tdk ada data yang terfetch/db kosong
                binding.recView?.visibility = View.GONE //sembunyikan recycler view
                binding.txtError.setText("You haven't listed any expenses yet!")
            }
            else{
                //klo data udah ada maka visible
                binding.recView?.visibility = View.VISIBLE
            }
        })

        //atur loading ld
        viewModel.loadingLD.observe(viewLifecycleOwner,Observer{
            if(it==false){
                //klo sattus loadingnya false, berarti progress loadnya sembunyikan
                //artinya klo udh slesai load progress bar hilang
                binding.progressLoad.visibility = View.GONE
            }
            else{
                binding.progressLoad.visibility = View.VISIBLE
            }
        })
        //atur error ldnya
        viewModel.expenseLoadErrorLD.observe(viewLifecycleOwner,Observer{
            if(it==false){
                binding.txtError.visibility = View.GONE
            }
            else{
                binding.txtError.visibility = View.VISIBLE
            }
        })
    }
}