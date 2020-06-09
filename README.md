# <p align="center"> JDataBinding </p>

<p align="center">
JDataBinding 是基于 DataBinding 封装的 DataBindingActivity、DataBindingAppCompatActivity、DataBindingFragmentActivity、DataBindingFragment、DataBindingDialog、DataBindingListAdapter、DataBindingViewHolder 基础库，欢迎 start<br/>
</p>

<p align="center">
<a href="https://github.com/hi-dhl"><img src="https://img.shields.io/badge/GitHub-HiDhl-4BC51D.svg?style=flat"></a> <a href="https://opensource.org/licenses/Apache-2.0"><img src="https://img.shields.io/badge/license-Apache2.0-blue.svg?style=flat"></a> <img src="https://img.shields.io/badge/language-kotlin-orange.svg"/> <img src="https://img.shields.io/badge/platform-android-lightgrey.svg"/>
</p>

![DataBindingDialog](http://cdn.51git.cn/2020-04-19-DataBindingDialog.png) 

关于 JDataBinding 的解析可以查看我在掘金上的文章 [如何在项目中封装 Kotlin + Android Databinding](https://juejin.im/post/5e9c434a51882573663f6cc6)

## Download

**Gradle**

将下列代码添加进模块 build.gradle 文件内

```
dependencies {
    implementation 'com.hi-dhl:jdatabinding:1.0.1'
}
```

## Usage

### 如何使用 DataBindingListAdapter

**Step1: 继承BaseViewHolder**

创建一个自定义的 ViewHolder 类，继承 DataBindingListAdapter，通过 viewHolderBinding 可以快速实现 DataBinding 的绑定

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

**Step2: 继承 DataBindingListAdapter**

实现带头部和尾部的 Adapter，创建自定义的 Adapter，继承 DataBindingListAdapter

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

构造方法传入了 Model.CALLBACK，Model.CALLBACK 实现了 DiffUtil.ItemCallback，用于计算 list 的两个非空 item 的不同。具体要写两个抽象方法 areItemsTheSame 和 areContentsTheSame

```
val CALLBACK: DiffUtil.ItemCallback<Model> = object : DiffUtil.ItemCallback<Model>() {
            // 判断两个Objects 是否代表同一个item对象， 一般使用Bean的id比较
            override fun areItemsTheSame(oldItem: Model, newItem: Model): Boolean =
                oldItem.id == newItem.id

            // 判断两个Objects 是否有相同的内容。
            override fun areContentsTheSame(oldItem: Model, newItem: Model): Boolean = true
        }
```

**Step3: 绑定 RecyclerView 和 Adapter**

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

这里用到了 DataBinding 的自定义数据绑定部分，可以百度、Google具体的用法，具体实现可以参考 demo 下面 fragment_test.xml 文件

### 如何使用 DataBindingDialog

**Step1: 继承 DataBindingDialog**

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

### 如何使用 DataBindingActivity

**继承 DataBindingActivity**

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

关于 DataBindingAppCompatActivity、DataBindingFragmentActivity 等等用法同 DataBindingActivity，这里就不展示了

### 如何使用 DataBindingFragment

**继承自 DataBindingFragment**

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


