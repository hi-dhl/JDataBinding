package com.hi.dhl.jdatabinding

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
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


class ActivityDataBindingDelegate<T : ViewBinding>(
    classes: Class<T>,
    act: Activity
) : ReadOnlyProperty<Activity, T> {

    init {
        when (act) {
            is FragmentActivity -> act.lifecycle.addObserver { destroyed() }
            is AppCompatActivity -> act.lifecycle.addObserver { destroyed() }
        }
    }

    private val layoutInflater = classes.inflateMethod()
    private var viewBinding: T? = null

    override fun getValue(thisRef: Activity, property: KProperty<*>): T {

        return viewBinding?.run {
            this

        } ?: let {

            val bind = layoutInflater.invoke(null, thisRef.layoutInflater) as T
            thisRef.setContentView(bind.root)

            bind.apply { viewBinding = this }
        }

    }

    private fun destroyed() {
        viewBinding = null
    }
}