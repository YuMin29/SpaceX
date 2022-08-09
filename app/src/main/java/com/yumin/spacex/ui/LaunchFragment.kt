package com.yumin.spacex.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.yumin.spacex.R
import com.yumin.spacex.databinding.FragmentLaunchBinding
import com.yumin.spacex.databinding.SortBottomDialogBinding
import com.yumin.spacex.model.RocketItem
import com.yumin.spacex.repository.RemoteRepository
import com.yumin.spacex.viewmodel.LaunchViewModel
import com.yumin.spacex.viewmodel.ViewModelFactory

class LaunchFragment : Fragment(), RecyclerViewAdapter.OnItemClickListener {
    private lateinit var fragmentLaunchBinding: FragmentLaunchBinding
    private lateinit var launchViewModel: LaunchViewModel
    private lateinit var recyclerViewAdapter: RecyclerViewAdapter
    private lateinit var getContext: Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentLaunchBinding = FragmentLaunchBinding.inflate(inflater)
        return fragmentLaunchBinding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.getContext = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        launchViewModel =
            ViewModelProvider(requireActivity(), ViewModelFactory(RemoteRepository(), getContext))
                .get(LaunchViewModel::class.java)

        launchViewModel.openItemEvent.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                findNavController().navigate(R.id.showDetail)
            }
        })

        launchViewModel.rocketList.observe(viewLifecycleOwner, Observer { result ->
            recyclerViewAdapter = RecyclerViewAdapter(this, getContext, result)
            fragmentLaunchBinding.launchRecyclerView.adapter = recyclerViewAdapter
        })

        launchViewModel.sortOldest.observe(viewLifecycleOwner, Observer {
            if (it)
                fragmentLaunchBinding.sortTextView.setText(R.string.sort_oldest)
            else
                fragmentLaunchBinding.sortTextView.setText(R.string.sort_newest)

        })

        launchViewModel.isLoading.observe(viewLifecycleOwner, Observer {
            if (it)
                fragmentLaunchBinding.progressBar.visibility = View.VISIBLE
            else
                fragmentLaunchBinding.progressBar.visibility = View.INVISIBLE
        })

        fragmentLaunchBinding.sortTextView.setOnClickListener {
            openSortDialog(it.context)
        }
    }

    override fun onItemClick(view: View, item: RocketItem) {
        launchViewModel.openRocketItem(item);
    }

    private fun openSortDialog(context: Context) {
        val bottomSheetDialog = BottomSheetDialog(context)
        val sortBottomDialogBinding = SortBottomDialogBinding.inflate(LayoutInflater.from(context))
        sortBottomDialogBinding.root.setBackgroundResource(R.drawable.dialog_rounded_corner)

        if (launchViewModel.sortOldest.value == true)
            sortBottomDialogBinding.sortGroup.check(R.id.sortOldest)
        else
            sortBottomDialogBinding.sortGroup.check(R.id.sortNewest)

        sortBottomDialogBinding.sortGroup.setOnCheckedChangeListener { radioGroup, id ->
            var useOldest: Boolean = id == R.id.sortOldest
            launchViewModel.sortChanged(useOldest)
        }
        bottomSheetDialog.setContentView(sortBottomDialogBinding.root)
        bottomSheetDialog.show()
    }
}