package com.tuttle80.app.dionysus.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import com.tuttle80.app.dionysus.BuildConfig
import com.tuttle80.app.dionysus.R


class SignInFragment : Fragment() {

//    private lateinit var notificationsViewModel: OptionViewModel

//    var lazyinit editEMail : AppCompatEditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        notificationsViewModel =
//                ViewModelProvider(this).get(OptionViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_signin, container, false)
//        val textView: TextView = root.findViewById(R.id.text_notifications)
//        notificationsViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })


  //      getSupportFragmentManager()

        root.findViewById<AppCompatImageButton>(R.id.back_button).setOnClickListener {

            activity?.onBackPressed()
//            parentFragmentManager.popBackStack()
//            getFragmentManager().popBackStackImmediate();
//            getF
//            super.onBackPr
        }


        root.findViewById<AppCompatButton>(R.id.sendEMail).setOnClickListener {
//            Toast.makeText(context, "EMail", Toast.LENGTH_LONG).show()
            onSendEMail()
        }


        return root
    }



    private fun onSendEMail() {
        val email = view?.findViewById<AppCompatEditText>(R.id.eMailAddress)?.text
        email?.let {
//            EmailUtils.sendEmailToAdmin(this, "개발자에게 메일보내기", new String []{ "admin@hello.bryan" });
            sendEmailTo(it.toString())
        }

    }

    fun sendEmailTo(receiver: String) {
        val email = Intent(Intent.ACTION_SEND)
        email.putExtra(Intent.EXTRA_SUBJECT, "메일 테스트")
        email.putExtra(Intent.EXTRA_EMAIL, receiver)
        email.putExtra(
            Intent.EXTRA_TEXT,
            java.lang.String.format(
                "Test verified code : %s",
                "12345678"
            )
        )
        email.type = "message/rfc822"
        context?.startActivity(email)

        startActivity(Intent.createChooser(email, "이메일 클라이언트 선택하기 :"));
    }
}