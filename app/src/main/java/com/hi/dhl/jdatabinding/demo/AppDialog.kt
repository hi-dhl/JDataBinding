package com.hi.dhl.jdatabinding.demo

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import com.hi.dhl.jdatabinding.binding
import com.hi.dhl.jdatabinding.demo.databinding.DialogAppBinding

/**
 * <pre>
 *     author: dhl
 *     date  : 2020/4/9
 *     desc  :
 * </pre>
 */
class AppDialog(
    context: Context,
    val title: String? = null,
    val message: String? = null,
    val yes: AppDialog.() -> Unit
) : Dialog(context, R.style.AppDialog) {
    private val mBinding: DialogAppBinding by binding()

    init {
        requireNotNull(message) { "message must be not null" }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        mBinding.apply {
            display.text = message
            btnNo.setOnClickListener { dismiss() }
            btnYes.setOnClickListener { yes() }
        }

    }
}