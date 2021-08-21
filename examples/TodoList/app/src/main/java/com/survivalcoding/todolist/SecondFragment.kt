package com.survivalcoding.todolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.survivalcoding.todolist.databinding.FragmentSecondBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {
    private val viewModel by activityViewModels<MainViewModel>()

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.selectedTodo?.let {
            binding.todoEditText.setText(it.title)
            binding.calendarView.date = it.date
        }

        binding.doneFab.setOnClickListener {
            if (binding.todoEditText.text.toString().isNotEmpty()) {
                if (viewModel.selectedTodo != null) {
                    viewModel.updateTodo(binding.todoEditText.text.toString())
                } else {
                    viewModel.addTodo(binding.todoEditText.text.toString())
                }
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}