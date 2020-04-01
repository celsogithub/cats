package com.domain.cats.app.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.domain.cats.app.databinding.ListItemFactsBinding
import com.domain.cats.app.domain.models.Cat
import com.domain.cats.app.presentation.models.toUiModel

class FactsAdapter : RecyclerView.Adapter<FactsAdapter.FactViewHolder>() {

    private val items = mutableListOf<Cat>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FactViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemFactsBinding.inflate(layoutInflater, parent, false)
        return FactViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: FactViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun setData(items: List<Cat>) {
        val diffCallback = FactsDiffCallback(this.items, items)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.items.clear()
        this.items.addAll(items)

        diffResult.dispatchUpdatesTo(this)
    }

    inner class FactViewHolder(
        private val binding: ListItemFactsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(cat: Cat) {
            binding.uiModel = cat.toUiModel()
        }
    }
}