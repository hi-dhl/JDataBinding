package com.hi.dhl.jdatabinding.demo.ui

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.hi.dhl.jdatabinding.binding
import com.hi.dhl.jdatabinding.demo.AppDialog
import com.hi.dhl.jdatabinding.demo.R
import com.hi.dhl.jdatabinding.demo.databinding.ActivityMainBinding
import org.koin.androidx.fragment.android.setupKoinFragmentFactory

class MainActivity : FragmentActivity() {
    private val mBinding: ActivityMainBinding by binding()

    override fun onCreate(savedInstanceState: Bundle?) {
        setupKoinFragmentFactory()
        super.onCreate(savedInstanceState)
        mBinding.apply {
            dialog.setOnClickListener {
                val msg = getString(R.string.dialog_msg)
                AppDialog(
                    context = this@MainActivity,
                    message = msg,
                    yes = {
                        Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
                    }).show()
            }

            if (savedInstanceState == null) {
                addFragment()
            }
        }
    }

    private fun addFragment() {
        val arguments = Bundle().apply {
            putString(FragmentTest.KEY_NAME, "来源于 MainActivity")
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, FragmentTest::class.java, arguments)
            .commit()
    }
}
