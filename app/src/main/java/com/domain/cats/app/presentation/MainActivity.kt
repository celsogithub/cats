package com.domain.cats.app.presentation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.domain.cats.app.R
import com.domain.cats.app.databinding.ActivityMainBinding
import com.domain.cats.app.presentation.adapter.FactsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var dataBinding: ActivityMainBinding
    private val viewModel by viewModels<FactsViewModel>()
    private val adapter by lazy {
        FactsAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        dataBinding.lifecycleOwner = this

        setupAdapter()
        setupObservers()

        viewModel.fetchFacts()
    }

    private fun setupAdapter() {
        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        dataBinding.factsList.addItemDecoration(itemDecoration)
        dataBinding.factsList.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.loading.observe(this, Observer {
            dataBinding.loading.visibility = if (it == true) View.VISIBLE else View.GONE
        })

        viewModel.error.observe(this, Observer { throwable ->
            throwable?.let {
                Toast.makeText(this@MainActivity, it.localizedMessage, Toast.LENGTH_SHORT)
                    .show()
            }
        })

        viewModel.catsFacts.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                dataBinding.emptyFactsList.visibility = View.GONE
                adapter.setData(it)
            } else {
                dataBinding.emptyFactsList.visibility = View.VISIBLE
            }
        })
    }
}
