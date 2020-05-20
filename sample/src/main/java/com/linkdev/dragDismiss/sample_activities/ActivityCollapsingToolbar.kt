package com.linkdev.dragDismiss.sample_activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.linkdev.dragDismiss.R
import com.linkdev.dragDismiss.utils.Extras
import com.linkdev.dragDismiss.utils.SampleDismissAttrs
import com.linkdev.dragDismiss.utils.applyAttrs
import kotlinx.android.synthetic.main.activity_collapsing_toolbar.*

class ActivityCollapsingToolbar : AppCompatActivity() {

    companion object {
        fun startActivity(context: Context, sampleAttrs: SampleDismissAttrs? = null) {
            val starter = Intent(context, ActivityCollapsingToolbar::class.java)
            starter.putExtra(Extras.EXTRA_SAMPLE_ATTRS, sampleAttrs)
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collapsing_toolbar)

        setSupportActionBar(toolbar)

        collapsingToolbar.title = "Swipe from left edge"

        val sampleAttrs = intent.getParcelableExtra<SampleDismissAttrs>(Extras.EXTRA_SAMPLE_ATTRS)
        if (sampleAttrs != null)
            dragDismissLayout.applyAttrs(sampleAttrs)
    }
}
