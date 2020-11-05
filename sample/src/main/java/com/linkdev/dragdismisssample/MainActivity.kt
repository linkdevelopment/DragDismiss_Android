package com.linkdev.dragdismisssample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.linkdev.dragdismisssample.sample_fragments.ActivitySample

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

    override fun onFragmentClicked(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, fragment, null)
            .addToBackStack(null)
            .commit()
    }
}
