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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentToDoSaveBinding.inflate(layoutInflater, container, false)
        val args: ToDoSaveFragmentArgs by navArgs()
        setupUI(args)
        // TODO: Kaydetme fragmentından geriye doğru gelince task list'e dönüyor, düzelt.
        return binding.root
    }

    private fun setupUI(args: ToDoSaveFragmentArgs) {
        workListID = args.WorkListID
        toDo = args.toDo

        with(binding) {
            toDo?.let {
                editTextTitle.setText(it.title)
                editTextDescription.setText(it.description)
                dueDate.timeInMillis = it.dueDate ?: System.currentTimeMillis()
                updateDateInView()
            }
            fabSaveToDo.setOnClickListener { saveOrUpdateToDo() }
            editTextDuedate.setOnClickListener { showDatePickerDialog() }
            editTextReminder.setOnClickListener {
                if (!checkNotificationPermission(requireContext())) {
                    openNotificationSettingsForApp(requireContext())
                } else {
                    if (switchDailyReminder.isChecked) showTimePickerDialog() else showDateTimePickerDialog()
                }
            }
            deleteIcon.setOnClickListener { navigateBack() }
        }
    }

    private fun showTimePickerDialog() {
        TimePickerDialog(context, { _, hourOfDay, minute ->
            reminderTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
            reminderTime.set(Calendar.MINUTE, minute)
            binding.editTextReminder.setText(
                SimpleDateFormat("HH:mm", Locale.getDefault()).format(
                    reminderTime.time
                )
            )
        }, reminderTime.get(Calendar.HOUR_OF_DAY), reminderTime.get(Calendar.MINUTE), true).show()
    }

    private fun showDateTimePickerDialog() {
        DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                reminderTime.set(year, monthOfYear, dayOfMonth)
                showTimePickerDialog()
            },
            reminderTime.get(Calendar.YEAR),
            reminderTime.get(Calendar.MONTH),
            reminderTime.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.maxDate = dueDate.timeInMillis
        }.show()
    }

    private fun saveOrUpdateToDo() {
        if (!validateInput()) return

        val newToDo = ToDo(
            id = toDo?.id ?: 0,
            title = binding.editTextTitle.text.toString().trim(),
            description = binding.editTextDescription.text.toString().trim(),
            isCompleted = toDo?.isCompleted ?: false,
            dueDate = dueDate.timeInMillis,
            workListId = toDo?.workListId ?: workListID
        )
        if (toDo == null)
            viewModel.save(newToDo, workListID)
        else
            viewModel.update(newToDo, workListID)

        if (!binding.editTextReminder.text.isNullOrBlank())
            if (binding.switchDailyReminder.isChecked)
                scheduleDailyReminder(reminderTime)
            else
                scheduleReminder(reminderTime)

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
            if (editTextDuedate.text.isNullOrEmpty()) {
                editTextDuedate.error = "Due date cannot be empty"
                return false
            }
        }
        return true
    }


    private fun updateDateInView() {
        binding.editTextDuedate.setText(
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                dueDate.time
            )
        )
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

    private fun scheduleReminder(reminderTime: Calendar) {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderBroadcastReceiver::class.java).apply {
            putExtra("TODO_TITLE", binding.editTextTitle.text.toString())
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            startActivity(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
            return
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderTime.timeInMillis, pendingIntent)
        Toast.makeText(context, "Hatırlatıcı ayarlandı", Toast.LENGTH_SHORT).show()
    }

    private fun scheduleDailyReminder(reminderTime: Calendar) {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderBroadcastReceiver::class.java).apply {
            putExtra("TODO_TITLE", binding.editTextTitle.text.toString())
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            reminderTime.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
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