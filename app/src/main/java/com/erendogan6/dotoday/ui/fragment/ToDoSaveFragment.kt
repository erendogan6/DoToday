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

@AndroidEntryPoint class ToDoSaveFragment : Fragment() {
    private var _binding: FragmentToDoSaveBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ToDoListViewModel by viewModels()
    private val args: ToDoSaveFragmentArgs by navArgs()
    private var workListID: Int = 0
    private var toDo: ToDo? = null
    private var dueDate: Calendar = Calendar.getInstance()
    private var reminderTime: Calendar = Calendar.getInstance()
    private val LOW_PRIORITY = "low"
    private val MEDIUM_PRIORITY = "medium"
    private val HIGH_PRIORITY = "high"
    private var selectedPriority: String = LOW_PRIORITY

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentToDoSaveBinding.inflate(inflater, container, false).apply {
            setupUI()
        }
        return binding.root
    }

    private fun FragmentToDoSaveBinding.setupUI() {
        toDo = args.toDo
        workListID = args.WorkListID
        setupPrioritySelection()
        populateToDoFields(toDo)
        fabSaveToDo.setOnClickListener { saveOrUpdateToDo() }
        editTextDuedate.setOnClickListener { showDatePickerDialog() }
        editTextReminder.setOnClickListener { handleReminderSetting() }
        deleteIcon.setOnClickListener { navigateBack() }
    }


    private fun FragmentToDoSaveBinding.setupPrioritySelection() {
        priorityRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            resetPriorityIconsAndSize()
            when (checkedId) {
                R.id.radio_low_priority -> setSelectedPriority(LOW_PRIORITY, radioLowPriority, R.drawable.lowselected_icon)
                R.id.radio_medium_priority -> setSelectedPriority(MEDIUM_PRIORITY, radioMediumPriority, R.drawable.mediumselected_icon)
                R.id.radio_high_priority -> setSelectedPriority(HIGH_PRIORITY, radioHighPriority, R.drawable.highselected_icon)
            }
        }
    }

    private fun FragmentToDoSaveBinding.populateToDoFields(toDo: ToDo?) {
        toDo?.let { it ->
            editTextTitle.setText(it.title)
            editTextDescription.setText(it.description)
            it.dueDate?.also { dueDate.timeInMillis = it }
            updateDueDateInView()
            switchDailyReminder.isChecked = it.isDailyReminder
            editTextReminder.setText(formatReminderDate(it))
            when (it.priority) {
                LOW_PRIORITY -> radioLowPriority.isChecked = true
                MEDIUM_PRIORITY -> radioMediumPriority.isChecked = true
                HIGH_PRIORITY -> radioHighPriority.isChecked = true
            }
        }
    }

    private fun setSelectedPriority(priority: String, view: View, drawableId: Int) {
        view.setBackgroundResource(drawableId)
        view.animatePriorityIcon(isSelected = true)
        selectedPriority = priority
    }

    private fun FragmentToDoSaveBinding.resetPriorityIconsAndSize() {
        listOf(radioLowPriority, radioMediumPriority, radioHighPriority).forEach { radioButton ->
            val drawableId = when (radioButton.id) {
                R.id.radio_low_priority -> R.drawable.low_icon
                R.id.radio_medium_priority -> R.drawable.medium_icon
                R.id.radio_high_priority -> R.drawable.high_icon
                else -> 0
            }
            radioButton.setBackgroundResource(drawableId)
            radioButton.animatePriorityIcon(isSelected = false)
        }
    }

    private fun View.animatePriorityIcon(isSelected: Boolean) {
        val scale = if (isSelected) 1.3f else 1.0f
        animate().scaleX(scale).scaleY(scale).setDuration(300).start()
    }


    private fun handleReminderSetting() {
        if (!checkNotificationPermission(requireContext())) {
            openNotificationSettingsForApp(requireContext())
        } else {
            if (binding.switchDailyReminder.isChecked) {
                showTimePickerDialog()
            } else {
                showTimeDatePickerDialog()
            }
        }
    }

    private fun showTimePickerDialog() {
        val now = Calendar.getInstance()
        val isToday = reminderTime.isSameDay(now)

        if (isToday && reminderTime.before(now)) {
            reminderTime.apply {
                set(Calendar.HOUR_OF_DAY, now.get(Calendar.HOUR_OF_DAY))
                set(Calendar.MINUTE, now.get(Calendar.MINUTE) + 1)
            }
        }

        TimePickerDialog(context, { _, hourOfDay, minute ->
            reminderTime.apply {
                set(Calendar.HOUR_OF_DAY, hourOfDay)
                set(Calendar.MINUTE, minute)
            }
            updateReminderDateInView()
        }, reminderTime.get(Calendar.HOUR_OF_DAY), reminderTime.get(Calendar.MINUTE), true).show()
    }

    private fun Calendar.isSameDay(other: Calendar): Boolean =
        this.get(Calendar.YEAR) == other.get(Calendar.YEAR) && this.get(Calendar.MONTH) == other.get(Calendar.MONTH) && this.get(
            Calendar.DAY_OF_MONTH
        ) == other.get(Calendar.DAY_OF_MONTH)

    private fun formatReminderDate(toDo: ToDo): String {
        val format = if (toDo.isDailyReminder) "HH:mm" else "yyyy-MM-dd HH:mm"
        return toDo.reminderDate?.let { SimpleDateFormat(format, Locale.getDefault()).format(it) } ?: ""
    }

    private fun updateReminderDateInView() {
        val isTimeOnly = binding.switchDailyReminder.isChecked
        binding.editTextReminder.setText(formatDateTime(reminderTime, isTimeOnly))
    }

    private fun formatDateTime(calendar: Calendar, isTimeOnly: Boolean = false): String {
        val pattern = if (isTimeOnly) "HH:mm" else "yyyy-MM-dd HH:mm"
        return SimpleDateFormat(pattern, Locale.getDefault()).format(calendar.time)
    }

    private fun showTimeDatePickerDialog() {
        val year = reminderTime.get(Calendar.YEAR)
        val month = reminderTime.get(Calendar.MONTH)
        val day = reminderTime.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            reminderTime.set(selectedYear, selectedMonth, selectedDay)

            showTimePickerDialog()
        }, year, month, day)

        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        if (!binding.editTextDuedate.text.isNullOrBlank()) {
            datePickerDialog.datePicker.maxDate = dueDate.timeInMillis
        }
        datePickerDialog.show()
    }

    private fun FragmentToDoSaveBinding.updateDueDateInView() {
        editTextDuedate.setText(formatDateTime(dueDate))
    }

    private fun FragmentToDoSaveBinding.showDatePickerDialog() {
        DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                dueDate.set(year, monthOfYear, dayOfMonth)
                updateDueDateInView()
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

        val newToDo = ToDo(
            id = toDo?.id ?: 0,
            title = binding.editTextTitle.text.toString().trim(),
            description = binding.editTextDescription.text.toString().trim(),
            isCompleted = toDo?.isCompleted ?: false,
            dueDate = if (binding.editTextDuedate.text.isNullOrEmpty()) null else dueDate.timeInMillis,
            workListId = workListID,
            reminderDate = if (binding.editTextReminder.text.isNullOrEmpty()) null else reminderTime.timeInMillis,
            isDailyReminder = binding.switchDailyReminder.isChecked,
            priority = selectedPriority
        )

        val message = if (toDo == null) {
            viewModel.save(newToDo, workListID)
            "ToDo Created"
        } else {
            viewModel.update(newToDo, workListID)
            "ToDo Updated"
        }

        scheduleReminderIfNeeded()

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        navigateBack()
    }

    private fun scheduleReminderIfNeeded() {
        if (!binding.editTextReminder.text.isNullOrBlank()) {
            val isDaily = binding.switchDailyReminder.isChecked
            scheduleReminder(reminderTime, isDaily)
        }
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

    private fun scheduleReminder(reminderTime: Calendar, isDaily: Boolean) {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderBroadcastReceiver::class.java).apply {
            putExtra("TODO_TITLE", binding.editTextTitle.text.toString())
        }
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            startActivity(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
            return
        }

        if (isDaily) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, reminderTime.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderTime.timeInMillis, pendingIntent)
        }
    }

    private fun checkNotificationPermission(context: Context): Boolean {
        return NotificationManagerCompat.from(context).areNotificationsEnabled()
    }

    private fun openNotificationSettingsForApp(context: Context) {
        val intent = Intent().apply {
            action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        }
        context.startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}