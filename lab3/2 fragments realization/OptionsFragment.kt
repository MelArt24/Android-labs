package com.am24.lab3android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.am24.lab3android.databinding.FragmentOptionsBinding

class OptionsFragment : Fragment() {

    private var _binding: FragmentOptionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOptionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sizeGroup = listOf(binding.cbSmall, binding.cbMedium, binding.cbLarge)
        val typeGroup = listOf(binding.cbPepperoni, binding.cbMargherita, binding.cbBBQChicken)

        sizeGroup.forEach { box ->
            box.setOnClickListener { sizeGroup.forEach { if (it != box) it.isChecked = false } }
        }
        typeGroup.forEach { box ->
            box.setOnClickListener { typeGroup.forEach { if (it != box) it.isChecked = false } }
        }

        binding.btnOrder.setOnClickListener {
            val result = StringBuilder("Your order:\n\n")
            var isValidOrder = true

            val selectedSize = sizeGroup.find { it.isChecked }?.text
            if (selectedSize == null) {
                Toast.makeText(activity, "You must choose size for pizza", Toast.LENGTH_SHORT).show()
                isValidOrder = false
            } else {
                result.append("Size: $selectedSize\n")
            }

            val selectedType = typeGroup.find { it.isChecked }?.text
            if (selectedType == null) {
                Toast.makeText(activity, "You must choose type for pizza", Toast.LENGTH_SHORT).show()
                isValidOrder = false
            } else {
                result.append("Type: $selectedType\n")
            }

            if (isValidOrder) {
                val extras = mutableListOf<String>()
                if (binding.cbTomatoes.isChecked) extras.add("Tomatoes")
                if (binding.cbPineapple.isChecked) extras.add("Pineapple")
                if (binding.cbCheese.isChecked) extras.add("Cheese")
                if (binding.cbMushrooms.isChecked) extras.add("Mushrooms")

                result.append("Extras: ${if(extras.isEmpty()) "none" else extras.joinToString(", ")}")

                val dataFile = DataFile(requireContext())
                val dataToSave = result.toString()
                dataFile.saveToFile("my_data_for_lab3.txt", dataToSave)
            }
        }

        binding.btnOpen.setOnClickListener {
            val action = OptionsFragmentDirections.actionOptionsToOrder()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}