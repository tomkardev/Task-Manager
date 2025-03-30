package com.example.taskmanagerapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.taskmanagerapp.R

import com.example.taskmanagerapp.databinding.FragmentTaskDetailsBinding
import com.example.taskmanagerapp.models.TaskData
import com.example.taskmanagerapp.viewmodels.TaskViewModel
import java.util.Locale

class TaskDetailsFragment : Fragment() {

    private var _binding: FragmentTaskDetailsBinding? = null
    private val binding get() = _binding!!

    val args by navArgs<TaskDetailsFragmentArgs>()

    private val taskViewModel by activityViewModels<TaskViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentTaskDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.titleText.text = args.TaskData.title
        binding.descText.text = args.TaskData.description

        val simpleDateFormat = java.text.SimpleDateFormat("dd-MMMM-yyyy", Locale.getDefault())

        binding.dueDateText.text = simpleDateFormat.format(args.TaskData.dueDate.time)

        when (args.TaskData.priority) {
            0 -> {
                binding.priorityChip.text = getString(R.string.low)
                binding.priorityChip.chipBackgroundColor =
                    ContextCompat.getColorStateList(requireContext(), R.color.low)
            }

            1 -> {
                binding.priorityChip.text = getString(R.string.medium)
                binding.priorityChip.chipBackgroundColor =
                    ContextCompat.getColorStateList(requireContext(), R.color.low)
            }

            2 -> {
                binding.priorityChip.text = getString(R.string.high)
                binding.priorityChip.chipBackgroundColor =
                    ContextCompat.getColorStateList(requireContext(), R.color.low)
            }
        }

        binding.statusChip.text = args.TaskData.status

        binding.statusChip.setOnCheckedChangeListener { chip, isChecked ->
            if (isChecked) {
                val data = TaskData(
                    args.TaskData.id,
                    args.TaskData.title,
                    args.TaskData.description,
                    args.TaskData.priority,
                    args.TaskData.dueDate,
                    getString(R.string.completed)
                )
                taskViewModel.updateTask(data)
                binding.statusChip.chipBackgroundColor =
                    ContextCompat.getColorStateList(requireContext(), R.color.green)
                binding.statusChip.text = getString(R.string.completed)
                taskViewModel.getAllTasks()
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null


    }

}