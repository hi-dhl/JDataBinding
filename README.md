# JDataBinding

JDataBinding是基于DataBinding封装的DataBindingActivity、DataBindingFragment、DataBindingDialog基础库相关代码，后续也会陆续完善基础库

DataBinding是什么？查看[Google官网](https://developer.android.com/topic/libraries/data-binding)，会有更详细的介绍<br/>

> DataBinding 是 Google 在 Jetpack 中推出的一款数据绑定的支持库，利用该库可以实现在页面组件中直接绑定应用程序的数据源

利用Kotlin的inline、reified、DSL等等语法, 结合着 DataBinding，可以设计出更加简洁并利于维护的代码

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding<TestFragmentBinding>(inflater, R.layout.test_fragment, container).apply {
            display.text = getString(R.string.app_name)
        }.root
    }
}
```

