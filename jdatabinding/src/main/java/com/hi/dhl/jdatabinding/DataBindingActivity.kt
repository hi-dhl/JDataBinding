package com.hi.dhl.jdatabinding

import android.app.Activity
import android.view.LayoutInflater
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import androidx.viewbinding.ViewBinding

/**
 * <pre>
 *     author: dhl
 *     date  : 2020/4/9
 *     desc  :
 * </pre>
 */
/**
 *  jdatabinding >=1.0.4 和  jdatabinding < 1.0.4 用法不一样，
 *
 *  1.0.4 统一了 AppCompatActivity、FragmentActivity、Activity，处于 Lifecycle.State.DESTROYED 将会销毁数据
 *  1.0.4 使用更加简单，请查看 README.md
 */
inline fun <reified T : ViewBinding> AppCompatActivity.binding() =
    ActivityDataBindingDelegate(T::class.java, this)

inline fun <reified T : ViewBinding> FragmentActivity.binding() =
    ActivityDataBindingDelegate(T::class.java, this)

inline fun <reified T : ViewBinding> Activity.binding() =
    ActivityDataBindingDelegate(T::class.java, this)