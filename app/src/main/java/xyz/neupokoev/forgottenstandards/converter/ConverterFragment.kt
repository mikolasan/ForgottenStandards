package xyz.neupokoev.forgottenstandards.converter

import android.app.AlertDialog
import android.os.Bundle
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.Fragment
import io.github.mikolasan.ratiogenerator.ImperialUnit
import io.github.mikolasan.ratiogenerator.MinCookingUnits
import xyz.neupokoev.forgottenstandards.MainActivity
import xyz.neupokoev.forgottenstandards.R
import xyz.neupokoev.forgottenstandards.convertValueWrapper
import xyz.neupokoev.forgottenstandards.getConversionRatio
import xyz.neupokoev.forgottenstandards.patternForDisplay
import kotlin.math.abs

class ConverterFragment : Fragment() {
    lateinit var bottomPanel: ImperialUnitPanel
    lateinit var topPanel: ImperialUnitPanel
    lateinit var selectedPanel: ImperialUnitPanel
    private lateinit var ratioLabel: TextView
    private lateinit var ingredientSelector: View
    private lateinit var ingredientName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as? MainActivity)?.setSubscriber(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_converter, container, false)
        bottomPanel = view.findViewById(R.id.convert_from)
        topPanel = view.findViewById(R.id.convert_to)
        topPanel.setHintText(view.context.resources.getString(R.string.select_unit_hint))
        bottomPanel.setHintText(view.context.resources.getString(R.string.select_unit_2_hint))
        ratioLabel = view.findViewById(R.id.ratio_label)
        ingredientSelector = view.findViewById(R.id.ingredient_selector)
        ingredientName = view.findViewById(R.id.ingredient_name)
        
        selectedPanel = topPanel // init before use
        setPanelListeners(view)
        setIngredientListeners()

        return view
    }

    override fun onStart() {
        super.onStart()

        (activity as? MainActivity)?.workingUnits?.let { workingUnits ->
            restoreTopPanel(workingUnits.mainUnit)
            selectPanel(topPanel, bottomPanel)
            updateIngredientSelectorVisibility(workingUnits.selectedCategory?.name)
            displayUnitValues()
        }
    }

    private fun updateIngredientSelectorVisibility(categoryName: String?) {
        if (categoryName == "Cooking") {
            ingredientSelector.visibility = View.VISIBLE
            ingredientName.text = MinCookingUnits.currentIngredient
        } else {
            ingredientSelector.visibility = View.GONE
        }
    }

    private fun setIngredientListeners() {
        ingredientName.setOnClickListener {
            val ingredients = MinCookingUnits.ingredients.keys.toTypedArray()
            AlertDialog.Builder(requireContext())
                .setTitle("Select Ingredient")
                .setItems(ingredients) { _, which ->
                    val selected = ingredients[which]
                    MinCookingUnits.setIngredient(selected)
                    onIngredientChanged()
                }
                .show()
        }

        val gestureDetector = GestureDetectorCompat(requireContext(), object : GestureDetector.SimpleOnGestureListener() {
            private var accumulatedDistanceY = 0f
            private val threshold = 50f

            override fun onScroll(e1: MotionEvent?, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
                accumulatedDistanceY += distanceY
                if (abs(accumulatedDistanceY) > threshold) {
                    if (accumulatedDistanceY > 0) {
                        MinCookingUnits.nextIngredient()
                    } else {
                        MinCookingUnits.previousIngredient()
                    }
                    onIngredientChanged()
                    accumulatedDistanceY = 0f
                }
                return true
            }

            override fun onDown(e: MotionEvent): Boolean {
                accumulatedDistanceY = 0f
                return true
            }
        })

        ingredientSelector.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            true
        }
    }

    private fun onIngredientChanged() {
        ingredientName.text = MinCookingUnits.currentIngredient
        
        val oppositePanel = if (selectedPanel == topPanel) bottomPanel else topPanel
        if (selectedPanel.unit != null && oppositePanel.unit != null) {
            convertValueWrapper(selectedPanel.unit!!, selectedPanel.unit!!.value, oppositePanel.unit!!)
        }
        
        updateRatioLabel()
        displayUnitValues()
    }

    private fun selectPanel(new: ImperialUnitPanel, old: ImperialUnitPanel) {
        selectedPanel = new
        new.setHighlight(true)
        old.setHighlight(false)
        updateRatioLabel()
    }

    private fun restoreTopPanel(unit: ImperialUnit) {
        topPanel.activate()
        topPanel.changeUnit(unit)
    }

    private fun displayUnitValues() {
        topPanel.updateDisplayValue()
        bottomPanel.updateDisplayValue()
    }

    private fun setPanelListeners(view: View) {

        val topPanelOnClickListener: (View) -> Unit = {
            if (selectedPanel != topPanel) {
                selectPanel(topPanel, bottomPanel)
                (activity as MainActivity).onPanelSelected(selectedPanel)
            }
        }
        val bottomPanelOnClickListener: (View) -> Unit = {
            if (selectedPanel != bottomPanel) {
                selectPanel(bottomPanel, topPanel)
                (activity as MainActivity).onPanelSelected(selectedPanel)
            }
        }

        topPanel.setOnClickListener(topPanelOnClickListener)
        bottomPanel.setOnClickListener(bottomPanelOnClickListener)

        val topInput = topPanel.input
        val bottomInput = bottomPanel.input
        topInput.setOnClickListener(topPanelOnClickListener)
        bottomInput.setOnClickListener(bottomPanelOnClickListener)
        topInput.addTextChangedListener(object : ImperialTextWatcher(topPanel, this, activity as MainActivity) {})
        bottomInput.addTextChangedListener(object : ImperialTextWatcher(bottomPanel, this, activity as MainActivity) {})
    }

    fun updateRatioLabel() {
        val fromUnit = selectedPanel.unit
        val toUnit = if (fromUnit == topPanel.unit) bottomPanel.unit else topPanel.unit
        if (fromUnit == null || toUnit == null) {
            ratioLabel.text = ""
        } else {
            val ratio = getConversionRatio(fromUnit, toUnit)
            val format = "1 ${fromUnit.unitName.name} = [value] ${toUnit.unitName.name}"
            ratioLabel.text = patternForDisplay(format, ratio)
        }
    }

}