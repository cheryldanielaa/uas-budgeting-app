package com.anmpdev.uas_budgeting_app.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.anmpdev.uas_budgeting_app.R
import com.anmpdev.uas_budgeting_app.databinding.FragmentListExpensesBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListExpensesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListExpensesFragment : Fragment() {
    private lateinit var binding:FragmentListExpensesBinding
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
        //klo misal diklik fabnya, maka dia akan pindah ke create expense (fragment)
        binding.btnAddExpense.setOnClickListener {
            //arahkan ke fragment tsb pke navigation
            val action = ListExpensesFragmentDirections.actionCreateNewExpenses()
            Navigation.findNavController(it).navigate(action)
            //kode dibawah dibuat klo udh kehubung sm adapterrr
//            val action = ListExpensesFragmentDirections.actionDetailExpenses()
//            Navigation.findNavController(it).navigate(action)
        }
    }
}