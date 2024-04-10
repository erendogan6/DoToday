package com.erendogan6.dotoday.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.erendogan6.dotoday.databinding.FragmentEisenHoverBinding

class EisenHowerMatrixFragment : Fragment() {
    private var _binding: FragmentEisenHoverBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEisenHoverBinding.inflate(inflater, container, false)

        binding.questionIcon.setOnClickListener {
            AlertDialog.Builder(context).setTitle("Eisenhower Matrix Info")
                .setMessage("The Eisenhower Matrix, also known as Urgent-Important Matrix, is a decision making principle and productivity tool that helps prioritize your many tasks. It is based on what is urgent and important, helping to decide on and prioritize tasks by urgency and importance, sorting out less urgent and important tasks which you should either delegate or not do at all.")
                .setPositiveButton("Got it!") { dialog, which ->
                    dialog.dismiss()
                }.create().show()
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}