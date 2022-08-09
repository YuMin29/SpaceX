package com.yumin.spacex.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.yumin.spacex.databinding.FragmentDetailBinding
import com.yumin.spacex.repository.RemoteRepository
import com.yumin.spacex.viewmodel.LaunchViewModel
import com.yumin.spacex.viewmodel.ViewModelFactory

class DetailFragment : Fragment() {
    private lateinit var fragmentDetailBinding: FragmentDetailBinding
    private lateinit var launchViewModel: LaunchViewModel
    private lateinit var expandableAdapter: ExpandableAdapter
    private lateinit var getContext: Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentDetailBinding = FragmentDetailBinding.inflate(inflater)
        return fragmentDetailBinding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getContext = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        launchViewModel =
            ViewModelProvider(requireActivity(), ViewModelFactory(RemoteRepository(), getContext))
                .get(LaunchViewModel::class.java)

        launchViewModel.selectedRocketItem.observe(viewLifecycleOwner, Observer {
            fragmentDetailBinding.itemFlightNumber.text = it.flightNumber.toString()
            fragmentDetailBinding.itemMissionName.text = it.missionName
            Glide.with(getContext).load(it.links.missionPatchSmall)
                .thumbnail(0.25f).into(fragmentDetailBinding.itemImage)
            fragmentDetailBinding.itemLaunchDate.text = it.launchDateUtc
            fragmentDetailBinding.itemLaunchSite.text = it.launchSite.siteName
        })

        launchViewModel.expandableItem.observe(viewLifecycleOwner, Observer {
            expandableAdapter = ExpandableAdapter(getContext, it)
            fragmentDetailBinding.expandableListView.setAdapter(expandableAdapter)
        })
    }
}