package com.yumin.spacex

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yumin.spacex.ui.LaunchFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val launchFragment = LaunchFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, launchFragment, LaunchFragment.TAG).commit()
        }
    }
}