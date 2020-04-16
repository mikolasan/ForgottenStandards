package io.github.mikolasan.imperialrussia

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.*
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import java.text.ParsePosition

class LengthAdapter(private val context: Context, private val units: MutableList<ImperialUnit>) : BaseAdapter() {
    private val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private val getStringFromResourceId: (Int) -> String = { i: Int -> context.resources.getString(i) }
//    private val getDrawableFromResourceId: (Int) -> Drawable = { i: Int ->
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // >= (API 21) Android 5.0 Lollipop
//            context.resources.getDrawable(i, null)
//        } else {
//            @Suppress("DEPRECATION")
//            context.resources.getDrawable(i)
//        }
//    }
    private val getColorFromRsourceId: (Int) -> Int = { i: Int ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // >= (API 23) Android 6.0 Marshmallow
            val theme = null
            context.resources.getColor(i, theme)
        } else {
            @Suppress("DEPRECATION")
            context.resources.getColor(i)
        }
    }

    enum class ViewState {
        SECOND,
        SELECTED,
        NORMAL
    }

    private val backgrounds = mapOf(
        ViewState.SECOND to R.drawable.ic_side_from_back,
        ViewState.SELECTED to R.drawable.ic_side_to_back,
        ViewState.NORMAL to R.drawable.ic_side_panel_back
    )
    private val nameColors = mapOf(
        ViewState.SECOND to R.color.inputNormal,
        ViewState.SELECTED to R.color.inputSelected,
        ViewState.NORMAL to R.color.colorPrimary
    )
    private val valueColors = mapOf(
        ViewState.SECOND to R.color.keyboardDigit,
        ViewState.SELECTED to R.color.colorPrimaryDark,
        ViewState.NORMAL to R.color.keyboardDigit
    )

    private var selectedUnit: ImperialUnit? = null
    private var secondUnit: ImperialUnit? = null

    private var arrowClickListener: (Int, View, ImperialUnit) -> Unit = { position, _, _ ->
        System.out.println("arrowClickListener ${position}")
    }
    private var arrowLongClickListener: (Int, View, ImperialUnit) -> Unit = { position, _, _ ->
        System.out.println("arrowLongClickListener ${position}")
    }

    fun setOnArrowClickListener(listener: (Int, View, ImperialUnit) -> Unit) {
        arrowClickListener = listener
    }

    fun setOnArrowLongClickListener(listener: (Int, View, ImperialUnit) -> Unit) {
        arrowLongClickListener = listener
    }

    fun resetValues() {
        for (listUnit in units) {
            listUnit.value = null
        }
        notifyDataSetChanged()
    }

    fun setCurrentValue(unit: ImperialUnit?, value: Double) {
        for (listUnit in units) {
            listUnit.value = LengthUnits.convertValue(unit, listUnit, value)
        }
        notifyDataSetChanged()
    }

    fun setSelectedUnit(unit: ImperialUnit?) {
        selectedUnit = unit
    }

    fun setSecondUnit(unit: ImperialUnit?) {
        secondUnit = unit
    }

    fun swapSelection() {
        val temp = selectedUnit
        selectedUnit = secondUnit
        secondUnit = temp
    }

    private fun updateViewColors(layout: ConstraintLayout, dataPosition: Int) {
        val data: ImperialUnit = getItem(dataPosition) as ImperialUnit
        val nameTextView: TextView = layout.findViewById(R.id.unit_name)
        val valueTextView: TextView = layout.findViewById(R.id.unit_value)
        val panelLock: ImageView = layout.findViewById(R.id.panel_lock)
        val valueLock: ImageView = layout.findViewById(R.id.value_lock)
        val unitLock: ImageView = layout.findViewById(R.id.unit_lock)
        val arrowUp: ImageView = layout.findViewById(R.id.arrow_up)
        when (data) {
            selectedUnit -> {
                layout.setBackgroundResource(backgrounds.getValue(ViewState.SELECTED))
                nameTextView.setTextColor(getColorFromRsourceId(nameColors.getValue(ViewState.SELECTED)))
                valueTextView.setTextColor(getColorFromRsourceId(valueColors.getValue(ViewState.SELECTED)))
                panelLock.visibility = View.INVISIBLE
                valueLock.visibility = View.INVISIBLE
                unitLock.visibility = View.INVISIBLE
                arrowUp.visibility = if (dataPosition == 0) View.INVISIBLE else View.VISIBLE
            }
            secondUnit -> {
                layout.setBackgroundResource(backgrounds.getValue(ViewState.SECOND))
                nameTextView.setTextColor(getColorFromRsourceId(nameColors.getValue(ViewState.SECOND)))
                valueTextView.setTextColor(getColorFromRsourceId(valueColors.getValue(ViewState.SECOND)))
                panelLock.visibility = View.INVISIBLE
                valueLock.visibility = View.INVISIBLE
                unitLock.visibility = View.INVISIBLE
                arrowUp.visibility = if (dataPosition == 0) View.INVISIBLE else View.VISIBLE
            }
            else -> {
                layout.setBackgroundResource(backgrounds.getValue(ViewState.NORMAL))
                nameTextView.setTextColor(getColorFromRsourceId(nameColors.getValue(ViewState.NORMAL)))
                valueTextView.setTextColor(getColorFromRsourceId(valueColors.getValue(ViewState.NORMAL)))
                panelLock.visibility = View.INVISIBLE
                valueLock.visibility = View.INVISIBLE
                unitLock.visibility = View.INVISIBLE
                arrowUp.visibility = View.INVISIBLE
            }
        }
    }

    private fun updateViewData(layout: ConstraintLayout, dataPosition: Int) {
        val data: ImperialUnit = getItem(dataPosition) as ImperialUnit
        val nameTextView: TextView = layout.findViewById(R.id.unit_name)
        nameTextView.text = getStringFromResourceId(data.resourceId)
        val valueTextView: TextView = layout.findViewById(R.id.unit_value)
        valueTextView.text = valueForDisplay(data.value)
    }

    private fun updateArrowListener(layout: ConstraintLayout, dataPosition: Int) {
        val arrowUp: ImageView = layout.findViewById(R.id.arrow_up)
        val unit = units[dataPosition]
        arrowUp.setOnClickListener {
            if (dataPosition != 0) {
                units.moveToFrontFrom(dataPosition)
                arrowClickListener(dataPosition, it, unit)
                notifyDataSetChanged()
            }
        }
        arrowUp.setOnLongClickListener {
            if (dataPosition != 0) {
                units.moveToFrontFrom(dataPosition)
                notifyDataSetChanged()
                arrowLongClickListener(dataPosition, it, unit)
            }
            dataPosition != 0
        }
    }

    override fun getView(position: Int, contentView: View?, parent: ViewGroup?): View {
        if (contentView != null) {
            if (contentView is ConstraintLayout) {
                updateViewData(contentView, position)
                updateViewColors(contentView, position)
                updateArrowListener(contentView, position)
                return contentView
            }
            return contentView
        } else {
            val view = inflater.inflate(R.layout.listview_item, parent, false) as ConstraintLayout
            updateViewData(view, position)
            updateViewColors(view, position)
            updateArrowListener(view, position)
            return view
        }

    }

    override fun getItem(position: Int): Any {
        return units[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return units.size
    }

}