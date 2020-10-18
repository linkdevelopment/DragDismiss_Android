package com.linkdev.dragdismisssample.sample_activities.viewpager_sample

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

// Created by Mohammed Fareed on 10/7/2020.
// Copyright (c) 2020 Link Development All rights reserved.
class ViewPagerAdapter(private val fragmentManager: FragmentManager) :
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragments = arrayListOf(FragmentViewpagerSample(), FragmentViewpagerSample())

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position) {
            0 -> "1"
            else -> "2"
        }
    }
}