package com.tuttle80.app.dionysus.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tuttle80.app.dionysus.R

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    var simpleList = arrayListOf<HomeListSimpleData>(
        HomeListSimpleData("포두주", "음..."),
        HomeListSimpleData("사과주", "달콤"),
        HomeListSimpleData("딸기주", "상큼"),
        HomeListSimpleData("당근", "오잉?"),
        HomeListSimpleData("당근", "당근?"),
        HomeListSimpleData("당근", "아삭아삭아..."),
        HomeListSimpleData("당근", "으...."),
        HomeListSimpleData("당근 으....", "아직도 당근"),
    )
    private lateinit var simpleRecyclerView : RecyclerView



    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
//        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
  //          textView.text = it
        })


        // Sign in button
        root.findViewById<AppCompatImageButton>(R.id.signIn).setOnClickListener {
            Navigation.findNavController(root).navigate(R.id.action_navigation_home_to_navigation_signin)
        }

        root.findViewById<AppCompatImageButton>(R.id.newFerment).setOnClickListener {
            Navigation.findNavController(root).navigate(R.id.action_navigation_home_to_navigation_newFerment)
        }

        simpleRecyclerView = root.findViewById<RecyclerView>(R.id.simpleRecycleList)
        simpleRecyclerView.adapter = HomeListAdapter(requireContext(), simpleList)
        simpleRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        simpleRecyclerView.setHasFixedSize(true)

        return root
    }
}