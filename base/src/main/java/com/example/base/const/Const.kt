package com.example.base.const

/**
 * @author 李雄厚
 *
 * @features 公共常量管理类
 */
object Host {
    /**
     * 基础地址
     */
    const val HOST = "https://www.wanandroid.com/"
}

object Url {
    /**
     * 登录
     */
    const val Login = "user/login"

    /**
     * 注册
     */
    const val Register = "user/register"

    /**
     * 首页Banner
     */
    const val Banner = "banner/json"

    /**
     * 首页文章
     */
    const val Article = "article/list/{page}/json"

    /**
     * 搜索
     */
    const val Search = "article/query/{page}/json"

    /**
     * 首页文章置顶
     */
    const val ArticleTop = "article/top/json"

    /**
     * 收藏文章
     */
    const val Collect = "lg/collect/{id}/json"

    /**
     * 取消收藏文章
     */
    const val UnCollect = "lg/uncollect_originId/{id}/json"

    /**
     * 取消我的收藏页面文章
     */
    const val MyUnCollect = "lg/uncollect/{id}/json"

    /**
     * 我的收藏数据
     */
    const val MyCollect = "lg/collect/list/{page}/json"

    /**
     * 体系
     */
    const val System = "tree/json"

    /**
     * 公众号名称
     */
    const val ProjectName = "wxarticle/chapters/json"

    /**
     * 公众号名称对应数据
     */
    const val ProjectList = "wxarticle/list/{id}/{page}/json"

    /**
     * 导航
     */
    const val Navigation = "project/tree/json"

    /**
     * 导航名称对应的数据
     */
    const val NavigationList = "project/list/{page}/json"


}

object Field {
    /**
     * SP存储名
     */
    const val SP_NAME = "WanAndroid"

    /**
     * 持久化存储的登录状态
     */
    const val COOKIE = "Cookie"
}

object Status {
    /**
     * 成功
     */
    const val SUCCESS = 0

    /**
     * 登录失败
     */
    const val LOGIN_ERROR = -1001
}