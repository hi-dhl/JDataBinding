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
    implementation 'com.hi-dhl:jdatabinding:1.0.4'
}
```

## Usage

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

* jdatabinding >= 1.0.4 的用法

```
class MainActivity : FragmentActivity() {
    private val mBinding: ActivityMainBinding by binding()
}

class MainActivity : AppCompatActivity() {
    private val mBinding: ActivityMainBinding by binding()
}

class MainActivity : Activity() {
    private val mBinding: ActivityMainBinding by binding()
}
```

* jdatabinding <= 1.0.3 的用法


```

class MainActivity : DataBindingAppCompatActivity() {
    private val mBinding: ActivityMainBinding by binding(R.layout.activity_main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.apply {}
    }
}

class MainActivity : DataBindingActivity() {
    private val mBinding: ActivityMainBinding by binding(R.layout.activity_main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.apply {}
    }
}

class MainActivity : DataBindingFragmentActivity() {
    private val mBinding: ActivityMainBinding by binding(R.layout.activity_main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.apply {}
    }
}
```

### 如何使用 DataBindingFragment

* jdatabinding >= 1.0.4 的用法

```
class FragmentTest(val mainViewModel: MainViewModel) : DataBindingFragment(R.layout.fragment_test) {

    val bind: FragmentTestBinding by binding()
}
```

* jdatabinding <= 1.0.3 的用法

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
        }.root
    }
}
```

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




### 联系我

* 个人微信：hi-dhl
* 公众号：ByteCode，包含 Jetpack ，Kotlin ，Android 10 系列源码，译文，LeetCode / 剑指 Offer / 多线程 / 国内外大厂算法题 等等一系列文章

<img src='http://cdn.51git.cn/2020-10-20-151047.png' width = 350px/>

---

最后推荐我一直在更新维护的项目和网站：

* 计划建立一个最全、最新的 AndroidX Jetpack 相关组件的实战项目 以及 相关组件原理分析文章，正在逐渐增加 Jetpack 新成员，仓库持续更新，欢迎前去查看：[AndroidX-Jetpack-Practice](https://github.com/hi-dhl/AndroidX-Jetpack-Practice)

* LeetCode / 剑指 offer / 国内外大厂面试题 / 多线程 题解，语言 Java 和 kotlin，包含多种解法、解题思路、时间复杂度、空间复杂度分析<br/>

    <image src="http://cdn.51git.cn/2020-10-04-16017884626310.jpg" width = "500px"/>
  
    * 剑指 offer 及国内外大厂面试题解：[在线阅读](https://offer.hi-dhl.com)
    * LeetCode 系列题解：[在线阅读](https://leetcode.hi-dhl.com)

* 最新 Android 10 源码分析系列文章，了解系统源码，不仅有助于分析问题，在面试过程中，对我们也是非常有帮助的，仓库持续更新，欢迎前去查看 [Android10-Source-Analysis](https://github.com/hi-dhl/Android10-Source-Analysis)

* 整理和翻译一系列精选国外的技术文章，每篇文章都会有**译者思考**部分，对原文的更加深入的解读，仓库持续更新，欢迎前去查看 [Technical-Article-Translation](https://github.com/hi-dhl/Technical-Article-Translation)

* 「为互联网人而设计，国内国外名站导航」涵括新闻、体育、生活、娱乐、设计、产品、运营、前端开发、Android 开发等等网址，欢迎前去查看 [为互联网人而设计导航网站](https://site.51git.cn)

## 感谢

[https://github.com/skydoves/BaseRecyclerViewAdapter](https://github.com/skydoves/BaseRecyclerViewAdapter)


请参看 BaseRecyclerViewAdapter 相关协议。

项目最初部分内容参考 BaseRecyclerViewAdapter，从 BaseRecyclerViewAdapter 扩展而来，

同时也要感谢这篇文章 [Simple one-liner ViewBinding in Fragments and Activities with Kotlin](https://medium.com/@Zhuinden/simple-one-liner-viewbinding-in-fragments-and-activities-with-kotlin-961430c6c07c) 
和 [ViewBindingDelegate](https://github.com/hoc081098/ViewBindingDelegate) 开源库提供了思路

欢迎大家前去查看，思路非常的好


