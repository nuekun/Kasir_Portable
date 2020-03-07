package com.nuedevlop.kasirportable

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.nuedevlop.kasirportable.user.LoginFragment
import com.nuedevlop.kasirportable.user.RegisterFragment
import kotlinx.android.synthetic.main.activity_email_password.*

class EmailPasswordActivity : AppCompatActivity(), View.OnClickListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_password)


        btnLogin.setOnClickListener(this@EmailPasswordActivity)
        btnSignup.setOnClickListener(this@EmailPasswordActivity)

        // default fragment
        btnLogin.callOnClick()
    }

    override fun onClick(clickedView: View?) {

        lateinit var fragment: Fragment
        when (clickedView!!.id) {
            R.id.btnLogin -> {

                fragment = LoginFragment()
                findViewById<View>(R.id.btnSignup).setBackgroundResource(R.drawable.button_background)
            }
            else -> {
                fragment = RegisterFragment()
                findViewById<View>(R.id.btnLogin).setBackgroundResource(R.drawable.button_background)
            }
        }

        supportFragmentManager.beginTransaction()
                .replace(frameLayout.id, fragment)
                .commit()

        clickedView.setBackgroundResource(R.drawable.button_background_selected)
    }

}