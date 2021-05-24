package com.tuttle80.app.dionysus.ui.account

import android.content.res.ColorStateList
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.widget.addTextChangedListener
import androidx.navigation.Navigation
import com.tuttle80.app.dionysus.R
import com.tuttle80.app.dionysus.db.AccountRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class SignUpFragment : Fragment() {

    companion object {
        fun newInstance() = SignUpFragment()
    }

    private lateinit var viewModel: SignUpViewModel

    private lateinit var checkImageEmail: ImageView
    private lateinit var checkImagePassword: ImageView
    private lateinit var checkImagePassword2: ImageView

    private var flagSubmit = 0

    private val FLAG_CHECK_OK_EMAIL = 0x01
    private val FLAG_CHECK_OK_PASSWORD = 0x02
    private val FLAG_CHECK_OK_PASSWORD2 = 0x04

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_signup, container, false)

        initWidget(root)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)
    }

    private fun initWidget(rootView : View) {
        // Back button
        rootView.findViewById<AppCompatImageButton>(R.id.backButton).setOnClickListener {
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

        val editPassword2 = rootView.findViewById<AppCompatEditText>(R.id.signInPassword2)
        editPassword2.addTextChangedListener { text ->
            // 상태에 따라 색상을 지정한다.
            val textLength = text?.length ?: 0
            val color = if (4 <= textLength) {
                flagSubmit = flagSubmit or FLAG_CHECK_OK_PASSWORD2;
                requireContext().getColor(R.color.check_color_ok)
            }
            else {
                flagSubmit = flagSubmit and (FLAG_CHECK_OK_PASSWORD2.inv());
                requireContext().getColor(R.color.check_color_fail)
            }

            if (::checkImagePassword2.isInitialized == true) {
                checkImagePassword2.imageTintList = ColorStateList.valueOf(color)
            }
        }

        checkImageEmail = rootView.findViewById(R.id.checkEmail)
        checkImagePassword = rootView.findViewById(R.id.checkPassword)
        checkImagePassword2 = rootView.findViewById(R.id.checkPassword2)


        // 로그인 버튼
        rootView.findViewById<AppCompatButton>(R.id.submitButton).setOnClickListener {
            if (flagSubmit != (FLAG_CHECK_OK_EMAIL or FLAG_CHECK_OK_PASSWORD)) {
                return@setOnClickListener
            }

            if (editPassword.text != editPassword2.text) {
                return@setOnClickListener
            }

            /* 새로운 객체를 생성, id 이외의 값을 지정 후 DB에 추가 */
            CoroutineScope(Dispatchers.IO).launch {
                val accountRepo = AccountRepo()
                val ret = accountRepo.addAccount(requireContext(), editEmail.text.toString(), editPassword.text.toString())

                Log.d("BugFix", "Account Ret = " + ret)
            }.start()
        }

//        // 비밀번호 찾기
//        rootView.findViewById<AppCompatButton>(R.id.forgotPasswordButton).setOnClickListener {
//            Toast.makeText(requireContext(), "비밀번호 찾기", Toast.LENGTH_LONG).show()
//            //Toast.makeText(context, "Verified OK", Toast.LENGTH_LONG).show()
//        }
//
//        // 새로 가입하기
//        rootView.findViewById<AppCompatButton>(R.id.singUpButton).setOnClickListener {
//            Navigation.findNavController(rootView).navigate(R.id.action_navigation_signin_to_navigation_signup)
//        }

    }

}