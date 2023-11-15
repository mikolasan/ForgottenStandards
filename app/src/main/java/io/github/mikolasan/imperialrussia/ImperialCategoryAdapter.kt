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
//        val lastPosition = selectedPosition
//        selectedPosition = holder.bindingAdapterPosition
//        if (selectedPosition == holder.bindingAdapterPosition) {
//            holder.space.setBackgroundResource(R.drawable.bg_side_from)
//        } else {
//            holder.space.setBackgroundResource(R.drawable.bg_input_panel)
//        }
//        holder.position = position
        holder.categoryTitle.text = categories[position].name
        holder.category = categories[position]
        holder.space.setBackgroundResource(R.drawable.bg_input_panel)
//        val type = categoryNameToType(holder.category!!)
//            holder.space.setBackgroundResource(R.drawable.bg_side_from)
//            holder.space.setBackgroundResource(R.drawable.bg_input_panel)

        holder.space.setOnClickListener {
            // unselect previous holder
            selectedViewHolder?.let {
                it.space.setBackgroundResource(R.drawable.bg_input_panel)
            }
//            val layout = it as ConstraintLayout
//            val h = layout.parent as ViewHolder
//            selectedPosition = h.position

            selectedViewHolder = holder
            publishSubject.onCategorySelected(holder.category!!)
            holder.space.setBackgroundResource(R.drawable.bg_side_from)
        }
    }
}