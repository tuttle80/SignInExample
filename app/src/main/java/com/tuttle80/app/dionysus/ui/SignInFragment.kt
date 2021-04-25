package com.tuttle80.app.dionysus.ui

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.tuttle80.app.dionysus.R
import java.util.regex.Pattern


class SignInFragment : Fragment() {

    // Member
    private val validateTimer = ValidateTimer(3 * 60000, 1000)  // 3분간 기다림

    private var verifiedCode = "12345678"


    // Member widget
    private lateinit var countDownTimerText : AppCompatTextView
    private lateinit var sendEmailButton : AppCompatButton

    // ------------------------------------------------------------------------
    // Inner class
    inner class ValidateTimer(millisInFuture: Long, countDownInterval: Long) :
        CountDownTimer(millisInFuture, countDownInterval) {
        override fun onTick(millisUntilFinished: Long) {
            val min = 1000 * 60
            var current = ""
            if (min < millisUntilFinished) {
                current = (millisUntilFinished / min).toString() + " : "
            }

            current += ((millisUntilFinished % min) / 1000).toString()
            countDownTimerText.text = current
        }

        override fun onFinish() {
            countDownTimerText.text = ""    // Empty text

            view?.findViewById<AppCompatEditText>(R.id.verifyCodeText)?.isEnabled = false
            view?.findViewById<AppCompatButton>(R.id.checkVerified)?.isEnabled = false
        }
    }


    // ------------------------------------------------------------------------
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


        // Back button
        root.findViewById<AppCompatImageButton>(R.id.back_button).setOnClickListener {
            activity?.onBackPressed()
        }

        // -------------------------------------------
        sendEmailButton = root.findViewById<AppCompatButton>(R.id.sendEMail)
        sendEmailButton.setOnClickListener {
            onSendEMail()
        }

        root.findViewById<AppCompatEditText>(R.id.emailText).addTextChangedListener { text ->
            // EMail 정규식을 보고 버튼 활성화를 결정한다.
            val pattern = "\\w+@\\w+\\.\\w+(\\.\\w+)?" // email 정규식
            val regex: Boolean = Pattern.matches(pattern, text)

            sendEmailButton.isEnabled = regex
        }

        countDownTimerText = root.findViewById<AppCompatTextView>(R.id.CountDownTimer)

        root.findViewById<AppCompatButton>(R.id.checkVerified).setOnClickListener {
            val insertCode =  root.findViewById<AppCompatEditText>(R.id.verifyCodeText).text.toString()
            if (verifiedCode.compareTo(insertCode) == 0) {
                Toast.makeText(context, "Verified OK", Toast.LENGTH_LONG).show()
                validateTimer.cancel(); // 시간 정지

                // DB에 기록

                // 인증 된것에 대한 화면은 Notify 전달되면 수정
            }
            else {
                Toast.makeText(context, "Fail", Toast.LENGTH_LONG).show()
            }
        }

        return root
    }

    private fun onSendEMail() {
        val email = view?.findViewById<AppCompatEditText>(R.id.emailText)?.text
        email?.let {
            sendEmailTo(it.toString())
            //sendEmail(it.toString())

            // Start timer
            validateTimer.cancel(); // 종료 시키고
            validateTimer.start();  // 새로 시작

            // Enable
            view?.findViewById<AppCompatEditText>(R.id.verifyCodeText)?.isEnabled = true
            view?.findViewById<AppCompatButton>(R.id.checkVerified)?.isEnabled = true
        }
    }

    private fun sendEmailTo(receiver: String) {
        val email = Intent(Intent.ACTION_SEND)
        email.putExtra(Intent.EXTRA_SUBJECT, "메일 인증 테스트")
        email.putExtra(Intent.EXTRA_EMAIL, receiver)
        email.putExtra(
                Intent.EXTRA_TEXT,
                java.lang.String.format(
                        "Test verified code : %s",
                        verifiedCode
                )
        )
        email.type = "message/rfc822"
        context?.startActivity(email)
    }

}