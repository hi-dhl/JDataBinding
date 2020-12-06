package com.hi.dhl.jdatabinding

import android.app.Activity
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
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
            is FragmentActivity -> act.lifecycle.addObserver(LifeCalcyObserver())
            is AppCompatActivity -> act.lifecycle.addObserver(LifeCalcyObserver())
        }
    }

    private val layoutInflater = classes.getMethod("inflate", LayoutInflater::class.java)
    private var viewBinding: T? = null

    override fun getValue(thisRef: Activity, property: KProperty<*>): T {

        viewBinding?.also {
            return it
        }

        val bind = layoutInflater.invoke(null, thisRef.layoutInflater) as T
        thisRef.setContentView(bind.root)

        return bind.also { viewBinding = it }
    }

    inner class LifeCalcyObserver : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            val state = source.lifecycle.currentState
            if (state == Lifecycle.State.DESTROYED) {
                viewBinding = null
            }
        }

    }
}