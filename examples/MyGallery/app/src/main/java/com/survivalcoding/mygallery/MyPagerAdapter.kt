package com.survivalcoding.mygallery

import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    var uris = mutableListOf<Uri>()

    override fun getItemCount(): Int {
        return uris.size
    }

    override fun createFragment(position: Int): Fragment {
        return PhotoFragment.newInstance(uris[position])
    }
}