package com.nixcvf18.myunitdemo.data;

import java.util.List;

public class GHADetailsBean {
    /**
     * _id : 5f02eb55808d6d2fe6b56f31
     * author : Espoir
     * category : GanHuo
     * content :
     * createdAt : 2020-07-06 17:13:57
     * desc : 是时候提高你撸RecycleView的效率了，简单而方便！
     * email : 386707112@qq.com
     * images : ["https://gank.io/images/52f5fcc58dcd4f8c854f073e13a88d30"]
     * index : 0
     * isOriginal : false
     * license :
     * likeCounts : 0
     * likes : []
     * markdown :
     ## 前言
     在项目开发中，总离不开列表，说到列表，就会有无穷无尽的 Adapter 需要你去实现。从而出现了很多优秀的 adapter 库。
     不过很多库功能都很全面了，有很多个类，很多功能，但是实际上只用其中的一两个我感觉是很多人的现状。所以自己实现一个使用起来顺手的是一个不错的选择，特别是用在一些不是很复杂的列表时。

     ## 效果
     先看看使用效果，激发一下你为数不多继续阅读的激情：
     ```kotlin
     //单类型列表，默认 LinearLayoutManager
     recycleView.setup<NumberInfo> {
     dataSource(initData())
     adapter {
     addItem(R.layout.layout_item) {
     bindViewHolder { data, _, _ ->
     setText(R.id.number, data?.number.toString())
     }
     }
     }
     }

     //多类型列表
     recycleView.setup<Any> {
     withLayoutManager { LinearLayoutManager(context) }
     dataSource(data)
     adapter {
     addItem(R.layout.item_setion_header) {
     isForViewType { data, _ -> data is SectionHeader }
     bindViewHolder { data, _, _ ->
     val header = data as SectionHeader
     setText(R.id.section_title, header.title)
     }
     }
     addItem(R.layout.item_user) {
     isForViewType { data, _ -> data is User }
     bindViewHolder { data, _, _ ->
     val user = data as User
     setText(R.id.name, user.name)
     setImageResource(R.id.avatar, user.avatarRes)
     //如果你的控件找不到方便赋值的方法，可以通过 findViewById 去查找
     val phone = findViewById<TextView>(R.id.phone)
     phone.text = user.phone
     }
     }
     }
     }
     ```

     嗯....，感觉还可以，最少的情况下可以把一个列表代码用 10 几行就完成了。

     ## 完整代码地址
     先贴完整代码地址，没地址的文章是没灵魂的：[EfficientAdapter](https://github.com/EspoirX/EfficientAdapter)
     我把它命名为 EfficientAdapter ，意为高效的意思，事实上它只有 3 个文件。
     至于如何使用，在地址上已经描述了，所以这篇文章主要是讲一下实现的思路。


     ![](https://user-gold-cdn.xitu.io/2020/7/6/17321d4bc743afc4?w=240&h=240&f=gif&s=12507)


     ## 实现思路
     对 Adapter 的封装，其实无非就是对 Adapter 里面的几个回调方法进行封装罢了，最常用的方法是先定义好一个存放 ViewHolder
     的列表，然后在各个回调中获取这些 ViewHolder，然后实现逻辑。

     那么其中最操蛋的是哪个回调方法的封装呢？我认为是 getItemViewType。事实上你可以在很多框架中看到让你实现获取 ViewType 的回调方法。

     一步一步来，先说 ViewHolder 的封装

     在 EfficientAdapter 里面，我把 ViewHolder 的封装写成了 BaseViewHolder：
     ```kotlin
     class BaseViewHolder(parent: ViewGroup, resource: Int) : RecyclerView.ViewHolder(
     LayoutInflater.from(parent.context).inflate(resource, parent, false)
     )
     ```
     这就是我的封装，够简单吧。

     想什么呢，当然没这么简单，想要在上面使用效果代码中那样实现 ViewHolder 的具体逻辑，还需要有 isForViewType，bindViewHolder 等方法。所以我要定义一个类，去提供这些方法：

     ```kotlin
     abstract class ViewHolderCreator<T> {
     abstract fun isForViewType(data: T?, position: Int): Boolean
     abstract fun getResourceId(): Int
     abstract fun onBindViewHolder(
     data: T?, items: MutableList<T>?,
     position: Int,  holder: ViewHolderCreator<T>
     )

     var itemView: View? = null

     fun registerItemView(itemView: View?) {
     this.itemView = itemView
     }

     fun <V : View> findViewById(viewId: Int): V {
     checkItemView()
     return itemView!!.findViewById(viewId)
     }

     private fun checkItemView() {
     if (itemView == null) {
     throw NullPointerException("itemView is null")
     }
     }
     }
     ```
     在 ViewHolderCreator 中，getResourceId 和 onBindViewHolder 方法相信都知道是干嘛的，而 isForViewType 方法是用来判断 ViewType 的，注意它返回类型是 Boolean，这个方法会在下面讲到。因为我想在 onBindViewHolder 中能方便的拿到 view，所以有了 registerItemView 和 findViewById 等其他方法。

     以上就是 ViewHolder 的所有封装，接下来就对 Adapter 的封装。

     ```kotlin
     open class EfficientAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
     var items: MutableList<T>? = mutableListOf()
     private val typeHolders: SparseArrayCompat<ViewHolderCreator<T>> = SparseArrayCompat()
     }
     ```
     Adapter 首先需要一个泛型用来表示传入的实体类类型，定义了一个 item 列表用来做数据源。ViewHolder 的集合使用一个 SparseArrayCompat 去存储。之所以用 SparseArray ，是因为我想把 ViewType 做为 key。

     所以，在 onCreateViewHolder 回调方法中，需要根据 viewType 参数在 typeHolders 中取到具体的 ViewHolderCreator：

     ```kotlin
     private fun getHolderForViewType(viewType: Int): ViewHolderCreator<T>? {
     return typeHolders.get(viewType)
     }

     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
     val holder = getHolderForViewType(viewType)
     ?: throw NullPointerException("No Holder added for ViewType $viewType")
     return BaseViewHolder(parent, holder.getResourceId())
     }
     ```
     这样，就可以通过 getHolderForViewType 方法，在 typeHolders 中获取到对应的 ViewHolderCreator，然后根据 ViewHolderCreator 中的信息去创建一个新的 ViewHolder。如果找不到，就抛一个空指针异常。

     同样道理，onBindViewHolder 回调方法也可以这么做：

     ```kotlin
     override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
     onBindViewHolder(viewHolder, position, mutableListOf())
     }

     override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int, payloads:MutableList<Any>) {
     val holder = getHolderForViewType(viewHolder.itemViewType)
     ?: throw NullPointerException("No Holder added for ViewType " + viewHolder.itemViewType)
     holder.registerItemView(viewHolder.itemView)
     holder.onBindViewHolder(items?.get(position), items, position, holder)
     }
     ```
     注意的是 onBindViewHolder 回调方法有两个，他们的区别就不说了，这里两个都实现了逻辑，当然你也可以只实现一个。

     还剩下 getItemCount 和 getItemViewType 回调方法了，getItemCount 其实没什么好说的：

     ```kotlin
     override fun getItemCount(): Int = items?.size ?: 0
     ```

     先不说如何实现 getItemViewType ，先说说怎么添加数据到 typeHolders 中：

     ```kotlin
     fun register(holder: ViewHolderCreator<T>) = apply {
     var viewType: Int = typeHolders.size()
     while (typeHolders.get(viewType) != null) {
     viewType++
     }
     typeHolders.put(viewType, holder)
     }
     ```
     typeHolders 的类型是 SparseArrayCompat，这里我用 ViewType 作为 key，register 方法中，可以看到没每注册一次，viewType 就自动加一（因为 typeHolders 的长度会变长），达到了不会重复的效果，到时候在实现 getItemViewType 的时候，就直接取出来即可。避免了具体业务的干扰。

     最后看看 getItemViewType 的实现：

     ```kotlin
     override fun getItemViewType(position: Int): Int {
     if (items == null) {
     throw NullPointerException("adapter data source is null")
     }
     for (i in 0 until typeHolders.size()) {
     val holder = typeHolders.valueAt(i)
     val data = items?.getOrNull(position)
     if (holder.isForViewType(data, position)) {
     return typeHolders.keyAt(i)
     }
     }

     //找不到匹配的 viewType
     throw NullPointerException(
     "No holder added that matches at position=$position in data source")
     }
     ```

     该方法的思路是通过遍历 typeHolders，通过 ViewHolderCreator 的 isForViewType 方法来判断是否符合条件，如果符合，则在 typeHolders 中取出 viewType 出来返回。

     因为 typeHolders 中的 viewType 是自增的，所以 getItemViewType 的返回值会是 0，1，2，3...

     isForViewType 在实际中如何实现？

     举个例子：
     如果你的数据源由多个实体类组成，比如：
     ```java
     private List<Object> data = new ArrayList<>();
     data.add(new User("Marry", 17, R.drawable.icon2, "123456789XX"));
     data.add(new SectionHeader("My Images"));
     data.add(new Image(R.drawable.cover1));
     ```

     那么在构建 EfficientAdapter 时，泛型传入的自然是 Object，然后在 isForViewType 方法中你可以这样区分类型：
     ```java
     // 代表这是 User 类型
     public boolean isForViewType(Object data, int position) {
     return data instanceof User;
     }

     // 代表这是 SectionHeader 类型
     public boolean isForViewType(Object data, int position) {
     return data instanceof SectionHeader;
     }

     // 代表这是 Image 类型
     public boolean isForViewType(Object data, int position) {
     return data instanceof Image;
     }
     ```

     如果你的数据源只有一个实体类，但是实体类里面有某个字段可以区分类型，你可以这样：
     ```java
     // 代表这是 User 类型
     public boolean isForViewType(ListInfo data, int position) {
     return data.type = ListInfo.USER
     }

     // 代表这是 SectionHeader 类型
     public boolean isForViewType(ListInfo data, int position) {
     return data.type = ListInfo.HEADER
     }

     // 代表这是 Image 类型
     public boolean isForViewType(ListInfo data, int position) {
     return data.type = ListInfo.IMAGE
     }
     ```
     其他情况可以根据具体的情况而定。

     到这里，已经完成 Adapter 的封装了，接下来可以定义一些数据源的增删查改的方法，比如：
     ```kotlin
     //绑定 RecyclerView
     fun attach(recyclerView: RecyclerView) = apply { recyclerView.adapter = this }

     //提交数据
     fun submitList(list: MutableList<T>) {
     this.items?.clear()
     this.items?.addAll(list)
     notifyDataSetChanged()
     }
     ```

     到这里，已经可以简单粗暴的使用了：
     ```kotlin
     adapter = EfficientAdapter<SectionHeader>()
     .register(object : ViewHolderCreator<SectionHeader>() {
     override fun isForViewType(data: SectionHeader?, position: Int) = data != null
     override fun getResourceId() = R.layout.item_setion_header

     override fun onBindViewHolder(
     data: SectionHeader?,
     items: MutableList<SectionHeader>?, position: Int,
     holder: ViewHolderCreator<SectionHeader>
     ) {
     setText(R.id.section_title, data.title)
     }
     }).attach(recycle_view)
     adapter?.submitList(data)
     ```

     但和使用效果差别有点大啊。所以，接下来就是 kotlin 发挥的时候了。

     ## 扩展函数 与 DSL

     相信学过 kotlin 的都知道这两个东西，他们可以为我们的代码提供更多的可能。

     ### ViewHolderCreator DSL

     由于 ViewHolderCreator 是一个抽象类，对它进行 DSL 封装需要一个默认的实现类（也许可以直接封装，但是我只能想到这种方法）：
     ```kotlin
     class ViewHolderDsl<T>(private val resourceId: Int) : ViewHolderCreator<T>() {
     private var viewType: ((data: T?, position: Int) -> Boolean)? = null
     private var viewHolder: ((data: T?, position: Int, holder: ViewHolderCreator<T>) -> Unit)? = null

     fun isForViewType(viewType: (data: T?, position: Int) -> Boolean) {
     this.viewType = viewType
     }

     fun bindViewHolder(holder: (data: T?, position: Int, holder: ViewHolderCreator<T>) -> Unit) {
     viewHolder = holder
     }

     override fun isForViewType(data: T?, position: Int): Boolean {
     return viewType?.invoke(data) ?: (data != null)
     }

     override fun getResourceId() = resourceId

     override fun onBindViewHolder(
     data: T?, items: MutableList<T>?, position: Int, holder: ViewHolderCreator<T>,
     payloads: MutableList<Any>
     ) {
     viewHolder?.invoke(data, position, holder)
     }
     }
     ```
     代码比较清晰，就是对三个抽象方法的实现。由于 getResourceId 比较简单，所以直接放在构造方法中传值就好。

     实现好了 ViewHolderDsl，我们给 EfficientAdapter 定义一个扩展函数，用 DSL 的方式去调用 register 方法：

     ```kotlin
     fun <T : Any> EfficientAdapter<T>.addItem(resourceId: Int, init: ViewHolderDsl<T>.() -> Unit) {
     val holder = ViewHolderDsl<T>(resourceId)
     holder.init()
     register(holder)
     }
     ```
     比较简单，就是创建好 ViewHolderDsl 后，调用 register 方法即可。

     到这里，其实已经可以用了，只要我们再写一个函数，用 DSL 的方式创建 Adapter 即可：

     ```kotlin
     fun <T : Any> efficientAdapter(init: EfficientAdapter<T>.() -> Unit): EfficientAdapter<T> {
     val adapter = EfficientAdapter<T>()
     adapter.init()
     return adapter
     }
     ```

     所以上面那个简单粗暴的示例代码就可以变成这样：
     ```kotlin
     adapter = efficientAdapter<Any> {
     addItem(R.layout.item_setion_header) {
     isForViewType { it != null }
     bindViewHolder { data, _, _ ->
     setText(R.id.section_title, data.title)
     }
     }
     }.attach(recycle_view)
     adapter?.submitList(data)
     ```
     代码又清晰和简单了很多。由于在 ViewHolderDsl 中，isForViewType 的默认实现是 data!=null，所以如果是单类型列表，这个方法可以直接不写。


     虽然代码简单了很多，但这样总要定义 adapter 对象和绑定 RecycleView，所以更加优雅的方式就是给 RecycleView 定义一个扩展函数，把这些操作都包装起来。

     首先我们实现一个叫 RecycleSetup 的类，在这个类里面，把 RecycleView 的配置以及 Adapter 操作，数据源操作等通通包装起来：

     ```kotlin
     class RecycleSetup<T> internal constructor(private val recyclerView: RecyclerView) {

     var items = mutableListOf<T>()
     var adapter: EfficientAdapter<T>? = null
     var context = recyclerView.context

     fun dataSource(items: MutableList<T>) {
     this.items.clear()
     this.items = items
     }

     fun withLayoutManager(init: RecycleSetup<T>.() -> RecyclerView.LayoutManager) =
     apply { recyclerView.layoutManager = init() }

     fun adapter(init: EfficientAdapter<T>.() -> Unit) {
     this.adapter = EfficientAdapter()
     init.invoke(adapter!!)
     recyclerView.adapter = adapter
     adapter?.submitList(this.items)
     }

     fun submitList(list: MutableList<T>) {
     this.items.clear()
     this.items = list
     adapter?.submitList(this.items)
     }

     fun getItem(position: Int): T = items[position]
     }
     ```

     代码简单，相信大家都能看懂。

     有了这个类，最后，就可以给 RecycleView 实现扩展函数了：

     ```kotlin
     fun <T> RecyclerView.setup(block: RecycleSetup<T>.() -> Unit): RecycleSetup<T> {
     val setup = RecycleSetup<T>(this).apply(block)
     if (layoutManager == null) {
     layoutManager = LinearLayoutManager(context)
     }
     return setup
     }

     fun <T> RecyclerView.submitList(items: MutableList<T>) {
     if (adapter != null && adapter is EfficientAdapter<*>) {
     (adapter as EfficientAdapter<T>).submitList(items)
     }
     }
     ```

     layoutManager 为空就默认实现 LinearLayoutManager。最后，上面那个简单粗暴的代码就可以写成跟一开始说那个效果一样了：
     ```kotlin
     recycleView.setup<SectionHeader> {
     adapter {
     addItem(R.layout.item_setion_header) {
     bindViewHolder { data, _, _ ->
     setText(R.id.section_title, data.title)
     }
     }
     }
     }
     recycleView.submitList(data)
     ```

     完整代码和例子都在这里 [EfficientAdapter](https://github.com/EspoirX/EfficientAdapter)  ，有兴趣可以看看。
     * originalAuthor :
     * publishedAt : 2020-07-06 17:13:57
     * stars : 1
     * status : 0
     * tags : []
     * title : 是时候提高你撸RecycleView的效率了
     * type : Android
     * updatedAt : 2020-07-06 17:13:57
     * url : https://github.com/EspoirX/EfficientAdapter
     * views : 3
     */

    private String _id;
    private String author;
    private String category;
    private String content;
    private String createdAt;
    private String desc;
    private String email;
    private int index;
    private boolean isOriginal;
    private String license;
    private int likeCounts;
    private String markdown;
    private String originalAuthor;
    private String publishedAt;
    private int stars;
    private int status;
    private String title;
    private String type;
    private String updatedAt;
    private String url;
    private int views;
    private List<String> images;
    private List<?> likes;
    private List<?> tags;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isIsOriginal() {
        return isOriginal;
    }

    public void setIsOriginal(boolean isOriginal) {
        this.isOriginal = isOriginal;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public int getLikeCounts() {
        return likeCounts;
    }

    public void setLikeCounts(int likeCounts) {
        this.likeCounts = likeCounts;
    }

    public String getMarkdown() {
        return markdown;
    }

    public void setMarkdown(String markdown) {
        this.markdown = markdown;
    }

    public String getOriginalAuthor() {
        return originalAuthor;
    }

    public void setOriginalAuthor(String originalAuthor) {
        this.originalAuthor = originalAuthor;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<?> getLikes() {
        return likes;
    }

    public void setLikes(List<?> likes) {
        this.likes = likes;
    }

    public List<?> getTags() {
        return tags;
    }

    public void setTags(List<?> tags) {
        this.tags = tags;
    }
}
