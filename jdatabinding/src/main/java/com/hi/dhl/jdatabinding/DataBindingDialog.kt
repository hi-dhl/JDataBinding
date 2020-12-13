package com.hi.dhl.jdatabinding

import android.app.Dialog
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.viewbinding.ViewBinding
import com.hi.dhl.jdatabinding.ext.addObserver
import com.hi.dhl.jdatabinding.ext.inflateMethod
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * <pre>
 *     author: dhl
 *     date  : 2020/4/9
 *     desc  :
 * </pre>
 */

inline fun <reified T : ViewDataBinding> Dialog.binding() =
    DialogBindingDelegate(T::class.java)

class DialogBindingDelegate<T : ViewBinding>(
    classes: Class<T>,
    lifecycle: Lifecycle? = null
) : ReadOnlyProperty<Dialog, T> {

    private var viewBinding: T? = null
    private var layoutInflater = classes.inflateMethod()

    init {
        lifecycle?.addObserver { destroyed() }
    }

    override fun getValue(thisRef: Dialog, property: KProperty<*>): T {
        return viewBinding?.run {
            this

        } ?: let {

            val bind = layoutInflater.invoke(null, thisRef.layoutInflater) as T
            thisRef.setContentView(bind.root)
            return bind.apply { viewBinding = this }
        }

    }

    private fun destroyed() {
        viewBinding = null
    }

}