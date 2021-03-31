package io.github.mikolasan.imperialrussia

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import io.github.mikolasan.ratiogenerator.ImperialUnit

class ImperialCategoryAdapter(private val categories: Array<ImperialUnitCategory>, private val publishSubject: MainActivity) : RecyclerView.Adapter<ImperialCategoryAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var category: ImperialUnitCategory? = null
        val categoryTitle: TextView = view.findViewById(R.id.category_title)
        val space: ConstraintLayout = view.findViewById(R.id.category_space)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.category_space, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.categoryTitle.text = categories[position].name
        holder.category = categories[position]
        holder.space.setOnClickListener {
            publishSubject.onCategorySelected(holder.category!!)
        }
    }
}