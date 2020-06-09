# <p align="center"> JDataBinding</p>

<p align="center">
JDataBinding 是基于 DataBinding 封装的 DataBindingActivity、DataBindingAppCompatActivity、DataBindingFragmentActivity、DataBindingFragment、DataBindingDialog、DataBindingListAdapter、DataBindingViewHolder 基础库，欢迎 start<br/>
</p>

<p align="center">
<a href="https://github.com/hi-dhl"><img src="https://img.shields.io/badge/GitHub-HiDhl-4BC51D.svg?style=flat"></a> <a href="https://opensource.org/licenses/Apache-2.0"><img src="https://img.shields.io/badge/license-Apache2.0-blue.svg?style=flat"></a> <img src="https://img.shields.io/badge/language-kotlin-orange.svg"/> <img src="https://img.shields.io/badge/platform-android-lightgrey.svg"/>
</p>

![DataBindingDialog](http://cdn.51git.cn/2020-04-19-DataBindingDialog.png) 

DataBinding是什么？查看[Google官网](https://developer.android.com/topic/libraries/data-binding)，会有更详细的介绍<br/>

> DataBinding 是 Google 在 Jetpack 中推出的一款数据绑定的支持库，利用该库可以实现在页面组件中直接绑定应用程序的数据源

利用Kotlin的inline、reified、DSL等等语法, 结合着 DataBinding，可以设计出更加简洁并利于维护的代码


## Download

**Gradle**

将下列代码添加进模块 build.gradle 文件内

```
dependencies {
    implementation 'com.hi-dhl:jdatabinding:1.0.0'
}
```

## DataBindingListAdapter

DataBindingListAdapter 是基于ListAdapter封装的，使用更少的代码快速实现 RecyclerView adapter and ViewHolder

**什么是ListAdapter？**

ListAdapter是 Google 推出的一个新的类库，相比传统的Adapter，它能够用较少的代码实现更多的RecylerView的动画，并且可以自动存储之前的list，ListAdapter还加入了DiffUtil的工具类，只有当items变化的时候进行刷新，而不用刷新整个list，大大提高RecyclerView的性能

**什么是DiffUtil？**

DiffUtil 主要在后台计算list是否相同，然后回到回主线程刷新数据，主要用了Myers Diff Algorithm, 而我们日常使用的git diff就用到了该算法

好了介绍完基础概念之后，来看一下DataBindingListAdapter是如何使用的，为什么我会说使用更少的代码快速实现 RecyclerView adapter and ViewHolder

### 如何使用 DataBindingListAdapter

**Step1: 继承BaseViewHolder**

创建一个自定义的ViewHolder类，继承DataBindingListAdapter，通过viewHolderBinding可以快速实现DataBinding的绑定

```
class TestViewHolder(view: View) : BaseViewHolder<Model>(view) {

    val binding: RecycieItemTestBinding by viewHolderBinding(view)

    override fun bindData(data: Model, position: Int) {
        binding.apply {
            model = data
            executePendingBindings()
        }
    }

}
```

**Step2: 继承DataBindingListAdapter**

实现带头部和尾部的Adapter，创建自定义的Adapter，继承DataBindingListAdapter

```
class TestAdapter : DataBindingListAdapter<Model>(Model.CALLBACK) {

    override fun viewHolder(layout: Int, view: View): DataBindingViewHolder<Model> = when (layout) {
        R.layout.recycie_item_header -> HeaderViewHolder(view)
        else -> TestViewHolder(view)
    }

    override fun layout(position: Int): Int = when (position) {
        0 -> R.layout.recycie_item_header
        getItemCount() - 1 -> R.layout.recycie_item_footer
        else -> R.layout.recycie_item_test
    }

    override fun getItemCount(): Int = super.getItemCount() + 2
}
```

构造方法传入了Model.CALLBACK，Model.CALLBACK实现了DiffUtil.ItemCallback，用于计算list的两个非空item的不同。具体要写两个抽象方法areItemsTheSame 和 areContentsTheSame

```
val CALLBACK: DiffUtil.ItemCallback<Model> = object : DiffUtil.ItemCallback<Model>() {
            // 判断两个Objects 是否代表同一个item对象， 一般使用Bean的id比较
            override fun areItemsTheSame(oldItem: Model, newItem: Model): Boolean =
                oldItem.id == newItem.id

            // 判断两个Objects 是否有相同的内容。
            override fun areContentsTheSame(oldItem: Model, newItem: Model): Boolean = true
        }
```

**Step3: 绑定RecyclerView和Adapter**

```
<data>

    <variable
        name="viewModel"
        type="com.hi.dhl.jdatabinding.demo.ui.MainViewModel" />

    <variable
        name="testAdapter"
        type="com.hi.dhl.jdatabinding.demo.ui.TestAdapter" />
</data>

<androidx.recyclerview.widget.RecyclerView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:adapter="@{testAdapter}"
        app:adapterList="@{viewModel.mLiveData}"
        app:layoutManager="androidx.recyclerview.widget.LinearLayoutManager" />

```

这里用到了DataBinding的自定义数据绑定部分，可以百度、Google具体的用法，具体实现可以参考demo下面fragment_test.xml文件

## DataBindingDialog

在Kotlin中应该尽量避免使用构建者模式，使用Kotlin的具名可选参数构造类，实现构建者模式，代码更加简洁<br/>

> 在 "Effective Java" 书中介绍构建者模式时，是这样子描述它的：本质上builder模式模拟了具名的可算参数，就像Ada和Python中的一样

幸运的是，Kotlin是一门拥有具名可选参数的变成语言，**DataBindingDialog** 在使用Kotlin的具名可选参数构造类实现Dailog构建者模式的基础上，用DataBinding进行二次封装，加上DataBinding数据绑定的特性，使Dialog变得更加简洁、易用<br/>

### 如何使用 DataBindingDialog

**Step1: 继承DataBindingDialog**

```
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
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        mBinding.apply {
            setContentView(root)
            display.text = message
            btnNo.setOnClickListener { dismiss() }
            btnYes.setOnClickListener { yes() }
        }

    }
}
```

**Step2: 简洁的调用方式**

```
AppDialog(
        context = this@MainActivity,
        message = msg,
        yes = {
            // do something
        }).show()
```

## DataBindingActivity

Kotlin中的函数和构造器都支持具名可选参数，在使用上更加灵活，在 **DataBindingActivity** 中使用Kotlin的inline、reified强大的特性，将类型参数实化，初始化View更加简洁

### 如何使用 DataBindingActivity

**继承DataBindingActivity**

```
class MainActivity : DataBindingActivity() {
    private val mBinding: ActivityMainBinding by binding(R.layout.activity_main)

    override fun onCreate(savedInstanceState: Bundle?) {
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
        }
    }
}
```

## DataBindingFragment

在Fragment当中如何使用 Kotlin的inline、reified初始化View，可以查看**DataBindingFragment**

### 如何使用 DataBindingFragment

**继承自DataBindingFragment**

```
class FragmentTest : DataBindingFragment() {
    val testViewModel: MainViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return binding<FragmentTestBinding>(
            inflater,
            R.layout.fragment_test, container
        ).apply {
            viewModel = testViewModel
            testAdapter = TestAdapter()
            lifecycleOwner = this@FragmentTest
        }.root
    }
}
```

关于基于DataBinding封装的DataBindingActivity、DataBindingFragment、DataBindingDialog、DataBindingListAdapter基础库，点击[JDataBinding](https://github.com/hi-dhl/JDataBinding)前往查看，欢迎start<br/>

[JDataBinding源码地址：https://github.com/hi-dhl/JDataBinding](https://github.com/hi-dhl/JDataBinding)

## 参考

[https://github.com/..BaseRecyclerViewAdapter](https://github.com/skydoves/BaseRecyclerViewAdapter)

## 结语

致力于分享一系列 Android 系统源码、逆向分析、算法、翻译、Jetpack  源码相关的文章，如果你同我一样喜欢研究 Android 源码和算法，可以看一下我另外两个库 [Leetcode-Solutions-with-Java-And-Kotlin](https://github.com/hi-dhl/Leetcode-Solutions-with-Java-And-Kotlin) 和 [Android10-Source-Analysis](https://github.com/hi-dhl/Android10-Source-Analysis)

### Leetcode-Solutions-with-Java-And-Kotlin

由于 LeetCode 的题库庞大，每个分类都能筛选出数百道题，由于每个人的精力有限，不可能刷完所有题目，因此我按照经典类型题目去分类、和题目的难易程度去排序

* 数据结构： 数组、栈、队列、字符串、链表、树……
* 算法： 查找算法、搜索算法、位运算、排序、数学、……

每道题目都会用 Java 和 kotlin 去实现，并且每道题目都有解题思路，如果你同我一样喜欢算法、LeetCode，可以关注我 GitHub 上的 LeetCode 题解：[Leetcode-Solutions-with-Java-And-Kotlin](https://github.com/hi-dhl/Leetcode-Solutions-with-Java-And-Kotlin)，一起来学习，期待与你一起成长

### Android10-Source-Analysis

正在写一系列的 Android 10 源码分析的文章，了解系统源码，不仅有助于分析问题，在面试过程中，对我们也是非常有帮助的，如果你同我一样喜欢研究 Android 源码，可以关注我 GitHub 上的 [Android10-Source-Analysis](https://github.com/hi-dhl/Android10-Source-Analysis)，文章都会同步到这个仓库

## License

```
Copyright 2020 hi-dhl (Jack Deng)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```


