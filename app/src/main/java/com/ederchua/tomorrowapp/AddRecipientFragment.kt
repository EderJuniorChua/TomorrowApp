package com.ederchua.tomorrowapp

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.ederchua.tomorrowapp.databinding.AddRecipientFragmentBinding

class AddRecipientFragment : Fragment(){

    private var _binding: AddRecipientFragmentBinding? = null
    private val binding get() = _binding!!



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onResume(){
        super.onResume()

        val priority = resources.getStringArray(R.array.priority_group)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, priority)
        binding.inputPriorityGroup.setAdapter(arrayAdapter)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Inflate the layout for this fragment
        _binding = AddRecipientFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}