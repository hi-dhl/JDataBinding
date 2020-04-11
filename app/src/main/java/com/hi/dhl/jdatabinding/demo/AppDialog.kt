package com.hi.dhl.jdatabinding.demo

import android.content.Context
import android.os.Bundle
import android.view.Window
import com.hi.dhl.jdatabinding.DataBindingDialog
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
) : DataBindingDialog(context, R.style.AppDialog) {
    private val mBinding: DialogAppBinding by binding(R.layout.dialog_app)

    init {
        requireNotNull(message) { "message must be not null" }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(root)
            display.text = message
            btnNo.setOnClickListener { dismiss() }
            btnYes.setOnClickListener { yes() }
        }

    }
}