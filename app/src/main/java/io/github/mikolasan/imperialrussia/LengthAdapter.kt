package io.github.mikolasan.imperialrussia

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import java.text.ParsePosition

class LengthAdapter(context: Context, private val units: Array<ImperialUnit>) : BaseAdapter() {
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
        FROM,
        TO,
        OTHER
    }

    private val backgrounds = mapOf(
        ViewState.FROM to R.drawable.ic_side_from_back,
        ViewState.TO to R.drawable.ic_side_to_back,
        ViewState.OTHER to R.drawable.ic_side_panel_back
    )
    private val nameColors = mapOf(
        ViewState.FROM to R.color.inputNormal,
        ViewState.TO to R.color.inputSelected,
        ViewState.OTHER to R.color.colorPrimary
    )
    val valueColors = mapOf(
        ViewState.FROM to R.color.keyboardDigit,
        ViewState.TO to R.color.keyboardDigit,
        ViewState.OTHER to R.color.keyboardDigit
    )

    private var fromUnit: ImperialUnit? = null
    private var toUnit: ImperialUnit? = null
    private var selectedUnit: ImperialUnit? = null
    private var secondUnit: ImperialUnit? = null
    private var selectedUnits: List<ImperialUnit?> = mutableListOf()

    fun resetValues() {
        for (listUnit in units) {
            listUnit.value = null
        }
        notifyDataSetChanged()
    }

    fun setCurrentValue(unit: ImperialUnit?, value: Double) {
        val masterUnit = unit ?: return
        for (listUnit in units) {
            listUnit.value = LengthUnits.convertValue(unit, listUnit, value)
            if (listUnit == masterUnit) {
                selectFromUnit(masterUnit)
            }
        }
        notifyDataSetChanged()
    }

    fun selectFromUnit(unit: ImperialUnit?) {
        fromUnit = unit
        selectedUnit = fromUnit
        secondUnit = toUnit
    }

    fun selectToUnit(unit: ImperialUnit?) {
        toUnit = unit
        selectedUnit = toUnit
        secondUnit = fromUnit
    }

    fun setSelectedUnit(unit: ImperialUnit?) {
        selectedUnit = unit
    }

    fun swapSelection() {
        val temp = selectedUnit
        selectedUnit = secondUnit
        secondUnit = temp
    }

    private fun updateViewColors(layout: ConstraintLayout, dataPosition: Int) {
        val data: ImperialUnit = getItem(dataPosition) as ImperialUnit
        val nameTextView: TextView = layout.findViewById(R.id.unit_name)
        val panelLock: ImageView = layout.findViewById(R.id.panel_lock)
        val valueLock: ImageView = layout.findViewById(R.id.value_lock)
        val unitLock: ImageView = layout.findViewById(R.id.unit_lock)
        when (data) {
            secondUnit -> {
                layout.setBackgroundResource(backgrounds.getValue(ViewState.FROM))
                nameTextView.setTextColor(getColorFromRsourceId(nameColors.getValue(ViewState.FROM)))
                if (secondUnit == fromUnit) {
                    panelLock.visibility = View.VISIBLE
                    valueLock.visibility = View.INVISIBLE
                    unitLock.visibility = View.INVISIBLE
                } else if (secondUnit == toUnit) {
                    panelLock.visibility = View.INVISIBLE
                    valueLock.visibility = View.INVISIBLE
                    unitLock.visibility = View.VISIBLE
                } else {
                    panelLock.visibility = View.INVISIBLE
                    valueLock.visibility = View.INVISIBLE
                    unitLock.visibility = View.INVISIBLE
                }
            }
            selectedUnit -> {
                layout.setBackgroundResource(backgrounds.getValue(ViewState.TO))
                nameTextView.setTextColor(getColorFromRsourceId(nameColors.getValue(ViewState.TO)))
                if (selectedUnit == fromUnit) {
                    panelLock.visibility = View.INVISIBLE
                    valueLock.visibility = View.VISIBLE
                    unitLock.visibility = View.INVISIBLE
                } else {
                    panelLock.visibility = View.INVISIBLE
                    valueLock.visibility = View.INVISIBLE
                    unitLock.visibility = View.INVISIBLE
                }
            }
            else -> {
                layout.setBackgroundResource(backgrounds.getValue(ViewState.OTHER))
                nameTextView.setTextColor(getColorFromRsourceId(nameColors.getValue(ViewState.OTHER)))
                panelLock.visibility = View.INVISIBLE
                valueLock.visibility = View.INVISIBLE
                unitLock.visibility = View.INVISIBLE
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

    override fun getView(position: Int, contentView: View?, parent: ViewGroup?): View {
        if (contentView != null) {
            if (contentView is ConstraintLayout) {
                updateViewData(contentView, position)
                updateViewColors(contentView, position)
                return contentView
            }
            return contentView
        } else {
            val view = inflater.inflate(R.layout.listview_item, parent, false) as ConstraintLayout
            updateViewData(view, position)
            updateViewColors(view, position)
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