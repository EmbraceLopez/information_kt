1、遵循MVVM架构
   
   数据层：
     组成：包含应用数据和业务逻辑，业务逻辑决定应用的价值，决定应用数据的创建、存储和更改方法。
     数据层由多个存储库类组成，其中每个储存库都可以包含零到多个数据源。
   
   界面层：
     作用：显示用户数据
     组成：
       1）在屏幕上呈现数据的界面元素
       2）用于存储数据、向界面提供数据以及处理逻辑的状态容器（viewModel）
       3）viewModel：负责提供界面状态和对数据层的访问权限

   网域层：位于界面与数据层之间的可选层
     作用：负责封装复杂的业务逻辑，或由多个ViewModel重复使用的简单业务逻辑。

   
2、使用navigation进行导航，会重建fragment，所以应将数据保存至ViewModel，防止重建后数据丢失
3、考虑将界面分为状态和事件的组合
4、遵循单向数据流，采用Flow+LiveData的高级协程
   Flow相当于rxjava，需使用其操作符
5、数据库使用Room进行操作
   1）数据缓存：列表每次刷新清空列表数据表，插入刷新后的一页数据即可，在没有网络的情况下从数据库中取出数据
   2）个人信息：缓存除头像以外其他信息、token
   3）详情不缓存
   4）需要实现数据库版本升级功能：修改了数据库结构或数据库操作都需要进行升级，否则无法正常更新应用
6、Hilt实现依赖注入，便于解耦

二、参考项目
1、https://github.com/android/sunflower
2、https://github.com/android/architecture-samples
3、https://github.com/googlecodelabs/android-paging

三、参考开发文档
1、https://developer.android.google.cn/guide?hl=zh-cn
