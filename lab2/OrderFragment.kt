package com.am24.lab2android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.TextView

class OrderFragment : Fragment(R.layout.fragment_order) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tvResult = view.findViewById<TextView>(R.id.tvResult)
        val btnBack = view.findViewById<Button>(R.id.btnBack)

        val orderText = OrderFragmentArgs.fromBundle(requireArguments()).orderText
        tvResult.text = orderText

        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}