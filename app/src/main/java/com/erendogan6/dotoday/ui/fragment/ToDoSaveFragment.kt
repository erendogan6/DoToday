package com.erendogan6.dotoday.ui.fragment

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.erendogan6.dotoday.R
import com.erendogan6.dotoday.data.model.ToDo
import com.erendogan6.dotoday.databinding.FragmentToDoSaveBinding
import com.erendogan6.dotoday.ui.fragment.viewmodel.ToDoListViewModel
import com.erendogan6.dotoday.utils.ReminderBroadcastReceiver
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class ToDoSaveFragment : Fragment() {
    private lateinit var binding: FragmentToDoSaveBinding
    private val viewModel: ToDoListViewModel by viewModels()
    private var workListID: Int = 0
    private var toDo: ToDo? = null
    private var dueDate: Calendar = Calendar.getInstance()
    private var reminderTime: Calendar = Calendar.getInstance()
    private var selectedPriority: String = "low"

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentToDoSaveBinding.inflate(layoutInflater, container, false)
        val args: ToDoSaveFragmentArgs by navArgs()
        setupPrioritySelection()
        setupUI(args)
        return binding.root
    }


    private fun setupPrioritySelection() {
        binding.priorityRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            resetPriorityIconsAndSize()
            when (checkedId) {
                R.id.radio_low_priority -> {
                    binding.radioLowPriority.setBackgroundResource(R.drawable.lowselected_icon)
                    animatePriorityIcon(binding.radioLowPriority, true)
                    selectedPriority = "low"
                }

                R.id.radio_medium_priority -> {
                    binding.radioMediumPriority.setBackgroundResource(R.drawable.mediumselected_icon)
                    animatePriorityIcon(binding.radioMediumPriority, true)
                    selectedPriority = "medium"
                }

                R.id.radio_high_priority -> {
                    binding.radioHighPriority.setBackgroundResource(R.drawable.highselected_icon)
                    animatePriorityIcon(binding.radioHighPriority, true)
                    selectedPriority = "high"
                }
            }
        }
    }

    private fun resetPriorityIconsAndSize() {
        with(binding) {
            listOf(radioLowPriority,
                   radioMediumPriority,
                   radioHighPriority).forEach { radioButton ->
                radioButton.apply {
                    when (this.id) {
                        R.id.radio_low_priority -> this.setBackgroundResource(R.drawable.low_icon)
                        R.id.radio_medium_priority -> this.setBackgroundResource(R.drawable.medium_icon)
                        R.id.radio_high_priority -> this.setBackgroundResource(R.drawable.high_icon)
                    }
                    animatePriorityIcon(this, false)
                }
            }
        }
    }

    private fun animatePriorityIcon(view: View, isSelected: Boolean) {
        val scale = if (isSelected) 1.2f else 1.0f
        view.animate().scaleX(scale).scaleY(scale).setDuration(300).start()
    }


    private fun setupUI(args: ToDoSaveFragmentArgs) {
        workListID = args.WorkListID
        if (args.toDo != null) {
            toDo = args.toDo
        }

        with(binding) {
            toDo?.let { it ->
                editTextTitle.setText(it.title)
                editTextDescription.setText(it.description)
                it.dueDate?.let {
                    dueDate.timeInMillis = it
                    updateDateInView()
                }
                switchDailyReminder.isChecked = it.isDailyReminder
                if (it.isDailyReminder && it.reminderDate != null) {
                    editTextReminder.setText((SimpleDateFormat("HH:mm", Locale.getDefault()).format(
                        it.reminderDate)))
                } else if (!it.isDailyReminder && it.reminderDate != null) {
                    editTextReminder.setText(SimpleDateFormat("yyyy-MM-dd HH:mm",
                                                              Locale.getDefault()).format(it.reminderDate))
                }
                when (it.priority) {
                    "low" -> radioLowPriority.isChecked = true
                    "medium" -> radioMediumPriority.isChecked = true
                    "high" -> radioHighPriority.isChecked = true
                }
            }

            fabSaveToDo.setOnClickListener { saveOrUpdateToDo() }
            editTextDuedate.setOnClickListener { showDatePickerDialog() }
            editTextReminder.setOnClickListener {
                if (!checkNotificationPermission(requireContext())) {
                    openNotificationSettingsForApp(requireContext())
                } else {
                    if (switchDailyReminder.isChecked) {
                        showTimePickerDialog(false)
                    } else {
                        showTimeDatePickerDialog()
                    }
                }
            }
            deleteIcon.setOnClickListener { navigateBack() }
        }
    }

    private fun showTimePickerDialog(isToday: Boolean) {
        val timePickerDialog = TimePickerDialog(context, { _, hourOfDay, minute ->
            reminderTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
            reminderTime.set(Calendar.MINUTE, minute)
            val format = if (binding.switchDailyReminder.isChecked) "HH:mm" else "yyyy-MM-dd HH:mm"
            binding.editTextReminder.setText(SimpleDateFormat(format, Locale.getDefault()).format(
                reminderTime.time))
        }, reminderTime.get(Calendar.HOUR_OF_DAY), reminderTime.get(Calendar.MINUTE), true)

        if (isToday) {
            val now = Calendar.getInstance()
            if (reminderTime.before(now)) {
                timePickerDialog.updateTime(now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE))
            }
        }

        timePickerDialog.show()
    }


    private fun showTimeDatePickerDialog() {
        val datePickerDialog =
            DatePickerDialog(requireContext(),
                             { _, year, monthOfYear, dayOfMonth ->
                                 reminderTime.set(year, monthOfYear, dayOfMonth)
                                 showTimePickerDialog(reminderTime.get(Calendar.YEAR) == Calendar.getInstance()
                                     .get(Calendar.YEAR) && reminderTime.get(Calendar.MONTH) == Calendar.getInstance()
                                     .get(Calendar.MONTH) && reminderTime.get(Calendar.DAY_OF_MONTH) == Calendar.getInstance()
                                     .get(Calendar.DAY_OF_MONTH))
                             },
                             reminderTime.get(Calendar.YEAR),
                             reminderTime.get(Calendar.MONTH),
                             reminderTime.get(Calendar.DAY_OF_MONTH))

        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        if (!binding.editTextDuedate.text.isNullOrBlank()) {
            datePickerDialog.datePicker.maxDate = dueDate.timeInMillis
        }
        datePickerDialog.show()
    }

    private fun updateDateInView() {
        binding.editTextDuedate.setText(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
            dueDate.time))
    }

    private fun showDatePickerDialog() {
        DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                dueDate.set(year, monthOfYear, dayOfMonth)
                updateDateInView()
            },
            dueDate.get(Calendar.YEAR),
            dueDate.get(Calendar.MONTH),
            dueDate.get(Calendar.DAY_OF_MONTH),
        ).apply {
            datePicker.minDate = System.currentTimeMillis() - 1000
        }.show()
    }


    private fun saveOrUpdateToDo() {
        if (!validateInput()) return

        val newToDo = ToDo(id = toDo?.id ?: 0,
                           title = binding.editTextTitle.text.toString().trim(),
                           description = binding.editTextDescription.text.toString().trim(),
                           isCompleted = toDo?.isCompleted ?: false,
                           dueDate = if (binding.editTextDuedate.text.isNullOrEmpty()) null else dueDate.timeInMillis,
                           workListId = toDo?.workListId ?: workListID,
                           reminderDate = if (binding.editTextReminder.text.isNullOrEmpty()) null else reminderTime.timeInMillis,
                           isDailyReminder = binding.switchDailyReminder.isChecked,
                           priority = selectedPriority)

        if (toDo == null) {
            viewModel.save(newToDo, workListID)
        } else {
            viewModel.update(newToDo, workListID)
        }

        if (!binding.editTextReminder.text.isNullOrBlank()) {
            if (binding.switchDailyReminder.isChecked) {
                scheduleDailyReminder(reminderTime)
            } else {
                scheduleReminder(reminderTime)
            }
        }

        Toast.makeText(context, "ToDo Created", Toast.LENGTH_SHORT).show()
        navigateBack()
    }

    private fun navigateBack() {
        val action = ToDoSaveFragmentDirections.actionToDoSaveFragmentToToDoListFragment(workListID)
        Navigation.findNavController(requireView()).navigate(action)
    }

    private fun validateInput(): Boolean {
        with(binding) {
            if (editTextTitle.text.isNullOrEmpty()) {
                editTextTitle.error = "Title cannot be empty"
                return false
            }
        }
        return true
    }


    private fun scheduleReminder(reminderTime: Calendar) {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderBroadcastReceiver::class.java).apply {
            putExtra("TODO_TITLE", binding.editTextTitle.text.toString())
        }
        val pendingIntent = PendingIntent.getBroadcast(context,
                                                       0,
                                                       intent,
                                                       PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            startActivity(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
            return
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderTime.timeInMillis, pendingIntent)
    }

    private fun scheduleDailyReminder(reminderTime: Calendar) {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderBroadcastReceiver::class.java).apply {
            putExtra("TODO_TITLE", binding.editTextTitle.text.toString())
        }
        val pendingIntent = PendingIntent.getBroadcast(context,
                                                       0,
                                                       intent,
                                                       PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                                  reminderTime.timeInMillis,
                                  AlarmManager.INTERVAL_DAY,
                                  pendingIntent)
    }

    fun checkNotificationPermission(context: Context): Boolean {
        return NotificationManagerCompat.from(context).areNotificationsEnabled()
    }

    fun openNotificationSettingsForApp(context: Context) {
        val intent = Intent().apply {
            action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        }
        context.startActivity(intent)
    }


}