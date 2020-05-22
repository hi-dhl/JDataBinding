# JDataBinding

[DataBindingDialog](http://cdn.51git.cn/2020-04-19-DataBindingDialog.png)

JDataBinding是基于DataBinding封装的DataBindingActivity、DataBindingFragment、DataBindingDialog、DataBindingListAdapter基础库，欢迎start<br/>

DataBinding是什么？查看[Google官网](https://developer.android.com/topic/libraries/data-binding)，会有更详细的介绍<br/>

> DataBinding 是 Google 在 Jetpack 中推出的一款数据绑定的支持库，利用该库可以实现在页面组件中直接绑定应用程序的数据源

利用Kotlin的inline、reified、DSL等等语法, 结合着 DataBinding，可以设计出更加简洁并利于维护的代码

## DataBindingListAdapter

DataBindingListAdapter 是基于ListAdapter封装的，使用更少的代码快速实现 RecyclerView adapter and ViewHolder

**什么是ListAdapter？**

ListAdapter是 Google 推出的一个新的类库，相比传统的Adapter，它能够用较少的代码实现更多的RecylerView的动画，并且可以自动存储之前的list，ListAdapter还加入了DiffUtil的工具类，只有当items变化的时候进行刷新，而不用刷新整个list，大大提高RecyclerView的性能

**什么是DiffUtil？**

DiffUtil 主要在后台计算list是否相同，然后回到回主线程刷新数据，主要用了Myers Diff Algorithm, 而我们日常使用的git diff就用到了该算法

好了介绍完基础概念之后，来看一下DataBindingListAdapter是如何使用的，为什么我会说使用更少的代码快速实现 RecyclerView adapter and ViewHolder

**Step1: 继承BaseViewHolder**

创建一个自定义的ViewHolder类，继承DataBindingListAdapter，通过viewHolderBinding可以快速实现DataBinding的绑定

```
class TestViewHolder(view: View) : BaseViewHolder<Model>(view) {

    val binding: RecycieItemTestBinding by viewHolderBinding(view)

    override fun bindData(data: Model) {
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

致力于分享一系列Android系统源码、逆向分析、算法相关的文章，每篇文章都会反复推敲，结合新的技术，带来一些新的思考，写出更通俗易懂的文章，如果你同我一样喜欢coding，一起来学习，期待与你一起成长

### 系列文章

#### Android 10 源码系列

* [0xA01 Android 10 源码分析：Apk是如何生成的](https://juejin.im/post/5e4366c3f265da57397e1189)
* [0xA02 Android 10 源码分析：Apk的安装流程](https://juejin.im/post/5e5a1e6a6fb9a07cb427d8cd)
* [0xA03 Android 10 源码分析：Apk加载流程之资源加载](https://juejin.im/post/5e6c8c14f265da574b792a1a)
* [0xA04 Android 10 源码分析：Apk加载流程之资源加载（二）](https://juejin.im/post/5e7f0f2c51882573c4676bc7)
* [0xA05 Android 10 源码分析：Dialog加载绘制流程以及在Kotlin、DataBinding中的使用](https://juejin.im/post/5e9199db6fb9a03c7916f635)

#### 工具系列

* [为数不多的人知道的AndroidStudio快捷键(一)](https://juejin.im/post/5df4933e518825126e639d62)
* [为数不多的人知道的AndroidStudio快捷键(二)](https://juejin.im/post/5df986d66fb9a016613903da)
* [关于adb命令你所需要知道的](https://juejin.im/post/5d57cfff51882505a87a8526)
* [如何高效获取视频截图](https://juejin.im/post/5d11d8835188251c10631ffd)
* [10分钟入门Shell脚本编程](https://juejin.im/post/5a6378055188253dc332130a)

#### 逆向系列

* [基于Smali文件 Android Studio 动态调试 APP](https://juejin.im/post/5c8ce8b76fb9a049e30900bf)
* [解决在Android Studio 3.2找不到Android Device Monitor工具](https://juejin.im/post/5c556ff7f265da2dbe02ba3c)