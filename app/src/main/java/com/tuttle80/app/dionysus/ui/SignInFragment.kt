package com.tuttle80.app.dionysus.ui

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tuttle80.app.dionysus.R
import com.tuttle80.app.dionysus.db.UserDatabase
import com.tuttle80.app.dionysus.db.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.util.regex.Pattern


class SignInFragment : Fragment() {

    // ViewModel
    private lateinit var signInViewModel: SignInViewModel

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

    // ViewModel 통지 받고 화며 갱신
    var accountCountObserver = Observer<Int> { count ->
        view?.let { view ->
            if (0 < count) {
                view.findViewById<ConstraintLayout>(R.id.verificationOKLayout).visibility = View.VISIBLE
                view.findViewById<LinearLayoutCompat>(R.id.verificationLayout).visibility = View.GONE
            }
            else {
                view.findViewById<ConstraintLayout>(R.id.verificationOKLayout).visibility = View.GONE
                view.findViewById<LinearLayoutCompat>(R.id.verificationLayout).visibility = View.VISIBLE
            }
        }
    };

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        val inflater = TransitionInflater.from(requireContext())
//        exitTransition = inflater.inflateTransition(R.anim.fragment_open_exit)
//    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // ViewModel
        signInViewModel = ViewModelProvider(this).get(SignInViewModel::class.java)
        signInViewModel.getCount(requireContext()).observe(viewLifecycleOwner, accountCountObserver)

        // Layout
        val root = inflater.inflate(R.layout.fragment_signin, container, false)

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
            val regex: Boolean = Pattern.matches(pattern, text.toString())

            sendEmailButton.isEnabled = regex
        }

        countDownTimerText = root.findViewById<AppCompatTextView>(R.id.CountDownTimer)

        root.findViewById<AppCompatButton>(R.id.checkVerified).setOnClickListener {
            checkVerifiedOnClick()
        }

        root.findViewById<AppCompatButton>(R.id.signOut).setOnClickListener {
            CoroutineScope(IO).launch {
                // 모두 지우기
                var userDatabase = UserDatabase.getInstance(requireContext())
                userDatabase?.userDao()?.deleteAll()
            }.start()
        }

        return root
    }

    private fun onSendEMail() {
        val email = view?.findViewById<AppCompatEditText>(R.id.emailText)?.text
        email?.let {
            sendEmailTo(it.toString())

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

    private fun checkVerifiedOnClick() {
        val insertCode =  view?.findViewById<AppCompatEditText>(R.id.verifyCodeText)?.text.toString()
        if (verifiedCode.compareTo(insertCode) == 0) {
            Toast.makeText(context, "Verified OK", Toast.LENGTH_LONG).show()
            validateTimer.cancel(); // 시간 정지
            validateTimer.onFinish();

            val emailAddress = view?.findViewById<AppCompatEditText>(R.id.emailText)?.text.toString()

            /* 새로운 객체를 생성, id 이외의 값을 지정 후 DB에 추가 */
            CoroutineScope(IO).launch {
                val newUser = UserEntity()
                newUser.verifiedType = "email"
                newUser.dateTime = System.currentTimeMillis()
                newUser.email = emailAddress

                var userDatabase = UserDatabase.getInstance(requireContext())
                userDatabase?.userDao()?.insert(newUser)
            }.start()
        }
        else {
            Toast.makeText(context, "Fail", Toast.LENGTH_LONG).show()
        }
    }
}