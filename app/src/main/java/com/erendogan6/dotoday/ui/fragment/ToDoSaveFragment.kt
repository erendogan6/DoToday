package com.erendogan6.dotoday.ui.fragment

import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.erendogan6.dotoday.data.model.ToDo
import com.erendogan6.dotoday.databinding.FragmentToDoSaveBinding
import com.erendogan6.dotoday.ui.fragment.viewmodel.ToDoListViewModel
import com.erendogan6.dotoday.utils.transition
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class ToDoSaveFragment : Fragment() {
    private lateinit var binding: FragmentToDoSaveBinding
    private lateinit var viewModel: ToDoListViewModel
    private var id: Int = 0
    private lateinit var date: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: ToDoListViewModel by viewModels()
        viewModel = tempViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val args: ToDoSaveFragmentArgs by navArgs()
        id = args.WorkListID
        binding = FragmentToDoSaveBinding.inflate(layoutInflater, container, false)
        setupDateDialog()
        setupFabButton()
        return binding.root
    }

    private fun setupFabButton() {
        binding.fabSaveToDo.setOnClickListener {
            val title = binding.editTextTitle.text.toString()
            val description = binding.editTextDescription.text.toString()
            if (title.isBlank()) {
                binding.editTextTitle.error = "Title cannot be empty"
                return@setOnClickListener
            }
            if (binding.editTextDuedate.text.isNullOrBlank()) {
                binding.editTextDuedate.error = "Date cannot be empty"
                return@setOnClickListener
            }
            val newToDo = ToDo(
                0,
                title,
                description,
                false,
                date.timeInMillis,
                id
            )
            viewModel.save(newToDo, id)
            val action = ToDoSaveFragmentDirections.actionToDoSaveFragmentToToDoListFragment(id)
            Navigation.transition(requireView(), action)
        }
    }

    private fun setupDateDialog() {
        binding.editTextDuedate.setOnClickListener {
            date = Calendar.getInstance()
            val year = date.get(Calendar.YEAR)
            val month = date.get(Calendar.MONTH)
            val day = date.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog =
                DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                    date.set(Calendar.YEAR, selectedYear)
                    date.set(Calendar.MONTH, selectedMonth)
                    date.set(Calendar.DAY_OF_MONTH, selectedDay)

                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    binding.editTextDuedate.setText(dateFormat.format(date.time))
                }, year, month, day)

            datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
            datePickerDialog.show()
        }
    }
}