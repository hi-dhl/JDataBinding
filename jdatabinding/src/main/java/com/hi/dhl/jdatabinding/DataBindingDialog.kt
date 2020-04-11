package com.hi.dhl.jdatabinding

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull
import androidx.annotation.StyleRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * <pre>
 *     author: dhl
 *     date  : 2020/4/9
 *     desc  :
 * </pre>
 */

abstract class DataBindingDialog(@NonNull context: Context, @StyleRes themeResId: Int) :
    Dialog(context, themeResId) {

    protected inline fun <reified T : ViewDataBinding> binding(@LayoutRes resId: Int): Lazy<T> =
        lazy {
            requireNotNull(
                DataBindingUtil.bind<T>(LayoutInflater.from(context).inflate(resId, null))
            ) { "cannot find the matched view to layout." }
        }

}