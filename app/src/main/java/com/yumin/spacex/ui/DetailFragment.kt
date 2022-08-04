package com.yumin.spacex.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.yumin.spacex.R
import com.yumin.spacex.databinding.FragmentDetailBinding
import com.yumin.spacex.repository.RemoteRepository
import com.yumin.spacex.viewmodel.LaunchViewModel
import com.yumin.spacex.viewmodel.ViewModelFactory

class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private lateinit var launchViewModel: LaunchViewModel
    private lateinit var expandableAdapter: ExpandableAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentDetailBinding>(
            inflater,
            R.layout.fragment_detail,
            container,
            false
        )
        binding.executePendingBindings()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        expandableAdapter = ExpandableAdapter()
        binding.expandableListView.setAdapter(expandableAdapter)

        launchViewModel = ViewModelProvider(requireActivity(), ViewModelFactory(RemoteRepository()))
            .get(LaunchViewModel::class.java)

        launchViewModel.selectedItem.observe(viewLifecycleOwner, Observer {
            binding.rocketInfo = it
        })

        launchViewModel.groupList.observe(viewLifecycleOwner, Observer {
            expandableAdapter.setGroupList(it)
        })
    }
}