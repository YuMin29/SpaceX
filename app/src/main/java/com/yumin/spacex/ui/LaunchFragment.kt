package com.yumin.spacex.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.yumin.spacex.R
import com.yumin.spacex.viewmodel.ViewModelFactory
import com.yumin.spacex.databinding.FragmentLaunchBinding
import com.yumin.spacex.databinding.SortBottomDialogBinding
import com.yumin.spacex.repository.RemoteRepository
import com.yumin.spacex.viewmodel.LaunchViewModel

class LaunchFragment : Fragment() {
    private lateinit var binding: FragmentLaunchBinding

    private lateinit var launchViewModel: LaunchViewModel

    private lateinit var adapter: RecyclerViewAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentLaunchBinding>(
            inflater,
            R.layout.fragment_launch,
            container,
            false
        )
        binding.executePendingBindings()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner

        launchViewModel = ViewModelProvider(this, ViewModelFactory(RemoteRepository()))
            .get(LaunchViewModel::class.java)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        val launchAdapter = RecyclerViewAdapter()
        binding.launchRecyclerView.adapter = launchAdapter
        adapter = launchAdapter


        launchViewModel.rocketList.observe(viewLifecycleOwner, Observer { result ->
            adapter.updateItems(result)
        })

        launchViewModel.sortOldest.observe(viewLifecycleOwner, Observer {
            if (it)
                binding.sortTextView.setText(R.string.sort_oldest)
            else
                binding.sortTextView.setText(R.string.sort_newest)
        })

        binding.callback = object : SortCallback {
            override fun openSortDialog() {
                val dialog = context?.let { BottomSheetDialog(it) }
                val dialogBinding = DataBindingUtil.inflate<SortBottomDialogBinding>(
                    layoutInflater, R.layout.sort_bottom_dialog, null, false
                )
                dialogBinding.root.setBackgroundResource(R.drawable.dialog_rounded_corner)
                dialogBinding.viewModel = launchViewModel
                binding.callback.also { dialogBinding.callback = it }

                dialog?.setContentView(dialogBinding.root)
                dialog?.show()
            }

            override fun sortChanged(radioGroup: RadioGroup, id: Int) {
                var useOldest: Boolean = id == R.id.sortOldest
                launchViewModel.sortChanged(useOldest)
            }
        }
    }

    companion object {
        val TAG: String = LaunchFragment::class.java.simpleName
    }
}