package com.tuttle80.app.dionysus.ui.newFerment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.tuttle80.app.dionysus.R
import com.tuttle80.app.dionysus.ui.home.HomeListAdapter
import com.tuttle80.app.dionysus.ui.home.HomeListSimpleData


class NewFermentFragment : Fragment() {

    var listPhotoData = arrayListOf<NewFermentPhotoData>(
        NewFermentPhotoData(null),
        NewFermentPhotoData(null),
        NewFermentPhotoData(null),
    )

    private lateinit var dashboardViewModel: NewFermentFragmentViewModel
    private lateinit var photoRecyclerView : RecyclerView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
                ViewModelProvider(this).get(NewFermentFragmentViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_new_ferment, container, false)
//        val textView: TextView = root.findViewById(R.id.text_dashboard)
//        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })

        photoRecyclerView = root.findViewById<RecyclerView>(R.id.photo)
        photoRecyclerView.adapter = NewFermentPhotoAdapter(requireContext(), listPhotoData)
        photoRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)   // To Horizontal
        photoRecyclerView.setHasFixedSize(true)

        return root
    }
}