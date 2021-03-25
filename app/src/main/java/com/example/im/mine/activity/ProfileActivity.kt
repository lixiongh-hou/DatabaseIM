package com.example.im.mine.activity

import androidx.navigation.Navigation
import com.example.base.base.BaseActivity
import com.example.base.view.FixFragmentNavigator
import com.example.im.R
import com.example.im.databinding.ActivityProfileBinding

/**
 * @author 李雄厚
 *
 * @features ***
 */
class ProfileActivity : BaseActivity<ActivityProfileBinding>() {

    override fun initLayout(): Int = R.layout.activity_profile

    override fun initView() {

        val navController = Navigation.findNavController(this, R.id.container)
        // get fragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container)!!
        // setup custom navigator
        val navigator =
            FixFragmentNavigator(this, navHostFragment.childFragmentManager, R.id.container)
        navController.navigatorProvider.addNavigator(navigator)
        navController.setGraph(R.navigation.nav_profile)

    }


}