package com.example.im.util

import com.example.im.entity.HeadEntity

/**
 * @author 李雄厚
 *
 * @features ***
 */
object DataUtil {

    fun getHeads(): MutableList<HeadEntity> {
        val head: MutableList<String> = ArrayList()
        val list: MutableList<HeadEntity> = ArrayList()
        head.add("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fwww.2qqtouxiang.com%2Fpic%2FTX8762_09.jpg&refer=http%3A%2F%2Fwww.2qqtouxiang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1617342578&t=09d4bf43a9278b3bec7936e715d1da45")
        head.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=501999121,786375788&fm=26&gp=0.jpg")
        head.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2905865909,2585005723&fm=26&gp=0.jpg")
        head.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1731066572,1656475898&fm=11&gp=0.jpg")
        head.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=859096889,4239663792&fm=11&gp=0.jpg")
        head.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2873976529,2559541999&fm=11&gp=0.jpg")
        head.add("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2261100632,3762446437&fm=11&gp=0.jpg")
        head.add("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3832204847,2962351744&fm=11&gp=0.jpg")
        head.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1474291947,2512988902&fm=26&gp=0.jpg")
        head.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3321714981,2208530333&fm=26&gp=0.jpg")
        head.add("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F201910%2F05%2F20191005155603_vrtct.thumb.400_0.jpeg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1617342810&t=fbf6033af875783ac2a22f55073233f9")
        head.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=35794866,682642488&fm=26&gp=0.jpg")
        for (str in head){
            list.add(HeadEntity(false, str))
        }
        return list
    }
}