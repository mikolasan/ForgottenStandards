package io.github.mikolasan.imperialrussia

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class ImperialCategoryAdapter(private val categories: Array<ImperialUnitCategoryName>,
                              private val publishSubject: MainActivity)
    : RecyclerView.Adapter<ImperialCategoryAdapter.ViewHolder>()
{
    var selectedViewHolder: ViewHolder? = null
//    var selectedPosition: Int = 0

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        var position: Int = -1
        var category: ImperialUnitCategoryName? = null
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
        val viewCategory = categories[position]
        holder.category = viewCategory
        val selectedCategory = publishSubject.workingUnits.selectedCategory
        if (selectedCategory == categoryNameToType(viewCategory)) {
            selectedViewHolder = holder
            holder.space.setBackgroundResource(R.drawable.bg_rect_selected)
        } else {
            holder.space.setBackgroundResource(0)
        }
        holder.space.setOnClickListener {
            // unselect previous holder
            selectedViewHolder?.let {
                it.space.setBackgroundResource(0)
            }
            selectedViewHolder = holder
            publishSubject.onCategorySelected(holder.category!!)
            holder.space.setBackgroundResource(R.drawable.bg_rect_selected)
        }
    }
}