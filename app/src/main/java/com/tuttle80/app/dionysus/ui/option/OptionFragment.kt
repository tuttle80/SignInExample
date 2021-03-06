package com.tuttle80.app.dionysus.ui.option

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tuttle80.app.dionysus.R
import com.tuttle80.app.dionysus.ui.notifications.NotificationsViewModel

class OptionFragment : Fragment() {

    private lateinit var notificationsViewModel: OptionViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
                ViewModelProvider(this).get(OptionViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_option, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        notificationsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}