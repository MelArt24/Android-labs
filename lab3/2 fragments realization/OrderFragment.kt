package com.am24.lab3android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.am24.lab3android.databinding.FragmentOrderBinding

class OrderFragment : Fragment() {

    private var _binding: FragmentOrderBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dataFile = DataFile(requireContext())
        val orderText = dataFile.readFile("my_data_for_lab3.txt")

        binding.tvResult.text = orderText

        binding.btnEdit.setOnClickListener {
            val context = requireContext()
            val editText = android.widget.EditText(context)
            editText.setText(binding.tvResult.text)

            androidx.appcompat.app.AlertDialog.Builder(context)
                .setTitle("Edit")
                .setView(editText)
                .setPositiveButton("Save") { _, _ ->
                    val newText = editText.text.toString()
                    dataFile.saveToFile("my_data_for_lab3.txt", newText)
                    binding.tvResult.text = newText
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        binding.btnClear.setOnClickListener {
            dataFile.clearFile("my_data_for_lab3.txt")
        }

        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}