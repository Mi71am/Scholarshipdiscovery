package com.example.scholarshipdiscovery.ui.discover

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scholarshipdiscovery.R
import com.example.scholarshipdiscovery.data.model.Scholarship
import com.example.scholarshipdiscovery.databinding.FragmentDiscoverBinding
import com.example.scholarshipdiscovery.ui.viewmodel.ScholarshipViewModel
import com.example.scholarshipdiscovery.utils.EligibilityStatus

class DiscoverFragment : Fragment() {

    private var _binding: FragmentDiscoverBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ScholarshipViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiscoverBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSearchAndFilterChips()

        val adapter = ScholarshipAdapter(
            savedIds = viewModel.savedScholarshipIds.value ?: emptySet(),
            checkEligibility = { scholarship -> viewModel.checkEligibility(scholarship) },
            onItemClick = { scholarship -> openDetails(scholarship) },
            onBookmarkClick = { scholarship -> viewModel.toggleSaveScholarship(scholarship.id) }
        )

        binding.scholarshipRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.scholarshipRecyclerView.adapter = adapter

        viewModel.savedScholarshipIds.observe(viewLifecycleOwner) { savedIds ->
            val currentList = viewModel.filteredScholarships.value ?: emptyList()
            binding.scholarshipRecyclerView.adapter = ScholarshipAdapter(
                savedIds = savedIds,
                checkEligibility = { scholarship -> viewModel.checkEligibility(scholarship) },
                onItemClick = { scholarship -> openDetails(scholarship) },
                onBookmarkClick = { scholarship -> viewModel.toggleSaveScholarship(scholarship.id) }
            ).apply { submitList(currentList) }
        }

        viewModel.filteredScholarships.observe(viewLifecycleOwner) { list ->
            (binding.scholarshipRecyclerView.adapter as? ScholarshipAdapter)?.submitList(list)
            binding.availableCountBannerTextView.text = "${list.size} opportunities available"

            if (list.isEmpty()) {
                binding.emptyStateView.visibility = View.VISIBLE
                binding.scholarshipRecyclerView.visibility = View.GONE
            } else {
                binding.emptyStateView.visibility = View.GONE
                binding.scholarshipRecyclerView.visibility = View.VISIBLE
            }
        }
    }

    private fun setupSearchAndFilterChips() {
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s?.toString() ?: ""
                viewModel.searchQuery.value = query
                binding.clearSearchButton.visibility = if (query.isNotEmpty()) View.VISIBLE else View.GONE
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.clearSearchButton.setOnClickListener {
            binding.searchEditText.setText("")
        }

        binding.filterChipGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.chipEligible -> {
                    viewModel.resetFilters()
                    viewModel.selectedEducationLevel.value = viewModel.userProfile.value?.educationLevel ?: "All"
                }
                R.id.chipEngineering -> {
                    viewModel.resetFilters()
                    viewModel.selectedFieldOfStudy.value = "Engineering & IT"
                }
                R.id.chipUndergrad -> {
                    viewModel.resetFilters()
                    viewModel.selectedEducationLevel.value = "Undergraduate"
                }
                R.id.chipFullTuition -> {
                    viewModel.resetFilters()
                    viewModel.selectedFundingType.value = "Full Tuition"
                }
                else -> {
                    viewModel.resetFilters()
                }
            }
        }
    }

    private fun openDetails(scholarship: Scholarship) {
        val action = DiscoverFragmentDirections.actionDiscoverFragmentToScholarshipDetailsFragment(scholarship)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
