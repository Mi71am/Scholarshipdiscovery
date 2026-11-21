package com.example.scholarshipdiscovery.ui.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scholarshipdiscovery.R
import com.example.scholarshipdiscovery.data.model.Scholarship
import com.example.scholarshipdiscovery.databinding.FragmentSavedScholarshipsBinding
import com.example.scholarshipdiscovery.ui.discover.ScholarshipAdapter
import com.example.scholarshipdiscovery.ui.viewmodel.ScholarshipViewModel

class SavedScholarshipsFragment : Fragment() {

    private var _binding: FragmentSavedScholarshipsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ScholarshipViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedScholarshipsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.savedRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.savedScholarshipsList.observe(viewLifecycleOwner) { list ->
            val savedIds = viewModel.savedScholarshipIds.value ?: emptySet()
            val adapter = ScholarshipAdapter(
                savedIds = savedIds,
                checkEligibility = { scholarship -> viewModel.checkEligibility(scholarship) },
                onItemClick = { scholarship -> openDetails(scholarship) },
                onBookmarkClick = { scholarship -> viewModel.toggleSaveScholarship(scholarship.id) }
            )
            binding.savedRecyclerView.adapter = adapter
            adapter.submitList(list)

            if (list.isEmpty()) {
                binding.savedEmptyStateLayout.visibility = View.VISIBLE
                binding.savedRecyclerView.visibility = View.GONE
            } else {
                binding.savedEmptyStateLayout.visibility = View.GONE
                binding.savedRecyclerView.visibility = View.VISIBLE
            }
        }

        binding.browseDiscoverButton.setOnClickListener {
            findNavController().navigate(R.id.discoverFragment)
        }
    }

    private fun openDetails(scholarship: Scholarship) {
        val action = SavedScholarshipsFragmentDirections.actionSavedScholarshipsFragmentToScholarshipDetailsFragment(scholarship)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
