package com.example.rapidroad.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rapidroad.data.repository.ResultState
import com.example.rapidroad.databinding.FragmentDashboardBinding
import com.example.rapidroad.view.DetailActivity
import com.example.rapidroad.view.adapter.DashboardAdapter
import com.example.rapidroad.viewmodel.DashboardViewModel
import com.example.rapidroad.viewmodel.ViewModelFactory
import androidx.appcompat.widget.SearchView
import com.example.rapidroad.view.LaporActivity

class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: DashboardViewModel
    private lateinit var adapter: DashboardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = DashboardAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
        binding.floatingActionButton.setOnClickListener {
            startActivity(Intent(requireContext(), LaporActivity::class.java))
        }
        val factory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, factory)[DashboardViewModel::class.java]

        viewModel.getAllReportLocation().observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultState.Loading -> {
                    // Show loading state if needed
                }

                is ResultState.Success -> {
                    adapter.setDataList(result.data)
                }

                is ResultState.Error -> {
                    // Show error message if needed
                }
            }
        }

        setupSearchView()
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Not used, but required to be overridden
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter(newText ?: "")
                return true
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}