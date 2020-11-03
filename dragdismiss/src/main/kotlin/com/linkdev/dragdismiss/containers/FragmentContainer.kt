/**
 * Copyright (c) 2020-present Link Development
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.linkdev.dragdismiss.containers

import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.linkdev.dragdismiss.views.DragDismissLayout
import java.lang.Exception

// Created on 29/9/2020.
// Copyright (c) 2020 Link Development All rights reserved.
internal class FragmentContainer : IContainer {

    private lateinit var mContainingFragment: Fragment

    override fun attach(
        container: Any,
        @LayoutRes layoutID: Int,
        dragDismissLayout: DragDismissLayout
    ): View {
        check(container is Fragment) { "FragmentContainer called with a non Fragment container" }

        mContainingFragment = container

        return container.layoutInflater.inflate(layoutID, dragDismissLayout)
    }

    override fun onDismiss() {
        val fragmentManager = mContainingFragment.fragmentManager

        if (fragmentManager?.findFragmentById(mContainingFragment.id) == null)
            throw Exception("Fragment is not added")

        fragmentManager.beginTransaction().remove(mContainingFragment).commit()
    }
}
