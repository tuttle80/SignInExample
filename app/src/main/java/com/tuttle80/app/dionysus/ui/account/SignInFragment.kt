package com.tuttle80.app.dionysus.ui.account

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.*
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.tuttle80.app.dionysus.R
import com.tuttle80.app.dionysus.Util
import com.tuttle80.app.dionysus.db.AccountRepo
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

//            view?.findViewById<AppCompatEditText>(R.id.verifyCodeText)?.isEnabled = false
//            view?.findViewById<AppCompatButton>(R.id.checkVerified)?.isEnabled = false
        }
    }

    // ViewModel 통지 받고 화면 갱신
    var accountCountObserver = Observer<Int> { count ->
//        view?.let { view ->
//            if (0 < count) {
//                view.findViewById<ConstraintLayout>(R.id.verificationOKLayout).visibility = View.VISIBLE
//                view.findViewById<LinearLayoutCompat>(R.id.verificationLayout).visibility = View.GONE
//            }
//            else {
//                view.findViewById<ConstraintLayout>(R.id.verificationOKLayout).visibility = View.GONE
//                view.findViewById<LinearLayoutCompat>(R.id.verificationLayout).visibility = View.VISIBLE
//            }
//        }
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
//        signInViewModel.getCount(requireContext()).observe(viewLifecycleOwner, accountCountObserver)

        // Layout
        val root = inflater.inflate(R.layout.fragment_signin, container, false)

        initWidget(root)


        /*


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
*/
        return root
    }

    private lateinit var checkImageEmail: ImageView
    private lateinit var checkImagePassword: ImageView



    private var flagSubmit = 0

    private val FLAG_CHECK_OK_EMAIL = 0x01
    private val FLAG_CHECK_OK_PASSWORD = 0x02

    private fun initWidget(rootView : View) {
        // Back button
        rootView.findViewById<AppCompatImageButton>(R.id.backButton).setOnClickListener {
            Util.hideKeypad(requireContext(), rootView)
            activity?.onBackPressed()
        }

        val editEmail = rootView.findViewById<AppCompatEditText>(R.id.signInEmail)
        editEmail.addTextChangedListener { text ->
            // EMail 정규식을 보고 버튼 활성화를 결정한다.
            val pattern = "\\w+@\\w+\\.\\w+(\\.\\w+)?" // email 정규식

            // 상태에 따라 색상을 지정한다.
            val color = if (Pattern.matches(pattern, text.toString())) {
                flagSubmit = flagSubmit or FLAG_CHECK_OK_EMAIL;
                requireContext().getColor(R.color.check_color_ok)
            }
            else {
                flagSubmit = flagSubmit and (FLAG_CHECK_OK_EMAIL.inv());
                requireContext().getColor(R.color.check_color_fail)
            }

            if (::checkImageEmail.isInitialized == true) {
                checkImageEmail.imageTintList = ColorStateList.valueOf(color)
            }
        }


        val editPassword = rootView.findViewById<AppCompatEditText>(R.id.signInPassword)
        editPassword.addTextChangedListener { text ->
            // 상태에 따라 색상을 지정한다.
            val textLength = text?.length ?: 0
            val color = if (4 <= textLength) {
                flagSubmit = flagSubmit or FLAG_CHECK_OK_PASSWORD;
                requireContext().getColor(R.color.check_color_ok)
            }
            else {
                flagSubmit = flagSubmit and (FLAG_CHECK_OK_PASSWORD.inv());
                requireContext().getColor(R.color.check_color_fail)
            }

            if (::checkImagePassword.isInitialized == true) {
                checkImagePassword.imageTintList = ColorStateList.valueOf(color)
            }
        }


//        editPassword.setOnFocusChangeListener(OnFocusChangeListener { _, hasFocus ->
//            if (!hasFocus) {
//                val imm = requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//                imm.hideSoftInputFromWindow(editPassword.windowToken, 0)
//            }
//        })


        checkImageEmail = rootView.findViewById(R.id.checkEmail)
        checkImagePassword = rootView.findViewById(R.id.checkPassword)


        // 로그인 버튼
        rootView.findViewById<AppCompatButton>(R.id.submitButton).setOnClickListener {
            Util.hideKeypad(requireContext(), rootView)

            if (flagSubmit != (FLAG_CHECK_OK_EMAIL or FLAG_CHECK_OK_PASSWORD)) {
                return@setOnClickListener
            }

            /* 새로운 객체를 생성, id 이외의 값을 지정 후 DB에 추가 */
            CoroutineScope(IO).launch {
                val accountRepo = AccountRepo()
                val ret = accountRepo.isValidAccount(requireContext(), editEmail.text.toString(), editPassword.text.toString())

                Log.d("BugFix", "Account Ret = " + ret)
            }.start()
        }

        // 비밀번호 찾기
        rootView.findViewById<AppCompatButton>(R.id.forgotPasswordButton).setOnClickListener {
            Util.hideKeypad(requireContext(), rootView)
            Toast.makeText(requireContext(), "비밀번호 찾기", Toast.LENGTH_LONG).show()
            //Toast.makeText(context, "Verified OK", Toast.LENGTH_LONG).show()
        }

        // 새로 가입하기
        rootView.findViewById<AppCompatButton>(R.id.singUpButton).setOnClickListener {
            Util.hideKeypad(requireContext(), rootView)
            Navigation.findNavController(rootView).navigate(R.id.action_navigation_signin_to_navigation_signup)
        }
//        Navigation.findNavController(root).navigate(R.id.action_navigation_home_to_navigation_signin)
    }

/*
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
    */

}
