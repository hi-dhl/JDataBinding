package com.hi.dhl.jdatabinding.ext

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner


fun Lifecycle.addObserver(destroyed: () -> Unit) {
    addObserver(LifecycleObserver(this, destroyed))
}

class LifecycleObserver(var lifecycle: Lifecycle?, val destroyed: () -> Unit) :
    LifecycleEventObserver {
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        val lifecycleState = source.lifecycle.currentState
        if (lifecycleState == Lifecycle.State.DESTROYED) {
            destroyed()
            lifecycle?.apply {
                removeObserver(this@LifecycleObserver)
                lifecycle = null
            }
        }
    }

}
