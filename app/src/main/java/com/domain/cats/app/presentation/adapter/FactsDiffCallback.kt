package com.domain.cats.app.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.domain.cats.app.domain.models.Cat

class FactsDiffCallback(
    private val oldList: List<Cat>,
    private val newList: List<Cat>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] === newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size
}