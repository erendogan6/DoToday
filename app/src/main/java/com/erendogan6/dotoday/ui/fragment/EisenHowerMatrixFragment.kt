package com.erendogan6.dotoday.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.erendogan6.dotoday.databinding.FragmentEisenHoverBinding

class EisenHowerMatrixFragment : Fragment() {
    private var _binding: FragmentEisenHoverBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEisenHoverBinding.inflate(inflater, container, false).apply {
            setupClickListeners()
            setupQuestion()
        }
        return binding.root
    }

    private fun FragmentEisenHoverBinding.setupQuestion() {
        questionIcon.setOnClickListener {
            AlertDialog.Builder(context).setTitle("Eisenhower Matrix Info")
                .setMessage("The Eisenhower Matrix, also known as Urgent-Important Matrix, is a decision making principle and productivity tool that helps prioritize your many tasks. It is based on what is urgent and important, helping to decide on and prioritize tasks by urgency and importance, sorting out less urgent and important tasks which you should either delegate or not do at all.")
                .setPositiveButton("Got it!") { dialog, which ->
                    dialog.dismiss()
                }.create().show()
        }
    }

    private fun FragmentEisenHoverBinding.setupClickListeners() {
        importantAndUrgent.setOnClickListener {
            navigateToListFragment("important_and_urgent")
            showToast("Do the task yourself immediately")
        }

        importantAndNotUrgent.setOnClickListener {
            navigateToListFragment("important_and_not_urgent")
            showToast("Plan the task and complete it yourself")
        }

        notImportantAndUrgent.setOnClickListener {
            navigateToListFragment("not_important_and_urgent")
            showToast("Delegate/Automate, if not possible, do it yourself after a time")
        }

        notImportantAndNotUrgent.setOnClickListener {
            navigateToListFragment("not_important_and_not_urgent")
            showToast("Forget it and come back later")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity().applicationContext, message, Toast.LENGTH_LONG).show()
    }

    private fun navigateToListFragment(categoryName: String) {
        val action = EisenHowerMatrixFragmentDirections.actionEisenHowerMatrixFragmentToEisenHowerList(categoryName)
        findNavController().navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}