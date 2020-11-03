package com.linkdev.dragdismisssample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.linkdev.dragdismisssample.sample_fragments.SampleFragment
import com.linkdev.dragdismisssample.utils.SampleDismissAttrs

class MainActivity : AppCompatActivity(), MainFragment.IMainFragmentInteraction {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, MainFragment.newInstance(), MainFragment.TAG)
                .commit()
        }
    }

    override fun onFragmentClicked(dragDismissAttrs: SampleDismissAttrs) {
        supportFragmentManager.beginTransaction()
            .add(
                R.id.fragmentContainer,
                SampleFragment.newInstance(dragDismissAttrs),
                SampleFragment.TAG
            )
            .addToBackStack(null)
            .commit()
    }
}
