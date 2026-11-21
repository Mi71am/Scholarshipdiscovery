package com.example.scholarshipdiscovery.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.scholarshipdiscovery.R
import com.example.scholarshipdiscovery.data.model.Scholarship
import com.example.scholarshipdiscovery.databinding.FragmentDashboardBinding
import com.example.scholarshipdiscovery.ui.discover.ScholarshipAdapter
import com.example.scholarshipdiscovery.ui.viewmodel.ScholarshipViewModel
import com.example.scholarshipdiscovery.utils.EligibilityStatus

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ScholarshipViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load Hero Photograph via Coil with CenterCrop
        binding.heroImageView.load("https://images.unsplash.com/photo-1523240795612-9a054b0db644?q=80&w=800&auto=format&fit=crop") {
            crossfade(true)
            placeholder(R.drawable.bg_chip_soft)
            error(R.drawable.bg_chip_soft)
        }

        binding.recommendedRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.userProfile.observe(viewLifecycleOwner) { profile ->
            if (profile != null && profile.fullName.isNotEmpty()) {
                val firstName = profile.fullName.split(" ")[0]
                binding.greetingTextView.text = "Good day, $firstName!"
            }
        }

        viewModel.allScholarships.observe(viewLifecycleOwner) { list ->
            val recommendedList = list.filter { viewModel.checkEligibility(it) == EligibilityStatus.ELIGIBLE }.take(2)
            val adapter = ScholarshipAdapter(
                savedIds = viewModel.savedScholarshipIds.value ?: emptySet(),
                checkEligibility = { scholarship -> viewModel.checkEligibility(scholarship) },
                onItemClick = { scholarship -> openDetails(scholarship) },
                onBookmarkClick = { scholarship -> viewModel.toggleSaveScholarship(scholarship.id) }
            )
            binding.recommendedRecyclerView.adapter = adapter
            adapter.submitList(if (recommendedList.isNotEmpty()) recommendedList else list.take(2))
        }

        viewModel.savedScholarships.observe(viewLifecycleOwner) { list ->
            binding.savedCountTextView.text = list.size.toString()
            if (list.isNotEmpty()) {
                binding.deadlineAlertTextView.text = "You have ${list.size} saved opportunities! Keep an eye on deadlines."
                binding.upcomingDeadlinesCountTextView.text = list.size.toString()
            } else {
                binding.deadlineAlertTextView.text = "Check upcoming deadlines for your saved scholarships regularly."
                binding.upcomingDeadlinesCountTextView.text = "0"
            }
        }

        viewModel.trackedApplications.observe(viewLifecycleOwner) { list ->
            binding.trackedCountTextView.text = list.size.toString()
        }

        // Navigation Handlers for Hero & 4-Step Journey Cards
        binding.profileAvatarButton.setOnClickListener {
            findNavController().navigate(R.id.profileFragment)
        }

        binding.heroCompleteProfileLink.setOnClickListener {
            findNavController().navigate(R.id.profileFragment)
        }

        binding.heroExploreButton.setOnClickListener {
            findNavController().navigate(R.id.discoverFragment)
        }

        binding.viewAllOpportunitiesButton.setOnClickListener {
            findNavController().navigate(R.id.discoverFragment)
        }

        binding.step1Button.setOnClickListener {
            findNavController().navigate(R.id.profileFragment)
        }

        binding.step2Button.setOnClickListener {
            findNavController().navigate(R.id.discoverFragment)
        }

        binding.step3Button.setOnClickListener {
            findNavController().navigate(R.id.savedScholarshipsFragment)
        }

        binding.step4Button.setOnClickListener {
            findNavController().navigate(R.id.applicationTrackerFragment)
        }
    }

    private fun openDetails(scholarship: Scholarship) {
        val action = DashboardFragmentDirections.actionDashboardFragmentToDiscoverFragment()
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
