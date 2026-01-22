package xyz.neupokoev.forgottenstandards.advanced

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import xyz.neupokoev.forgottenstandards.R
import java.util.concurrent.ConcurrentHashMap

// The standard text size for list items in your app (in SP)
private const val BOLT_LABEL_TEXT_SIZE_SP = 16f
private const val METRIC_LABEL_X_POS_DP = 15 // Distance from left edge of view for metric labels
private const val IMPERIAL_LABEL_X_POS_DP = 15 // Distance from right edge of view for imperial labels

class NutBoltFragment : Fragment(), LabelUpdateListener {

    private var labelContainer: FrameLayout? = null
    private var renderer: TestRenderer? = null
    
    // Two maps to manage labels for metric and imperial columns
    private val metricLabels = ConcurrentHashMap<String, TextView>()
    private val imperialLabels = ConcurrentHashMap<String, TextView>()
    
    private var centeredBoltName: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_nut_bolt, container, false)

        val textureView = view.findViewById<NutBoltView>(R.id.texture_view)
        labelContainer = view.findViewById(R.id.texture_and_label_container)
        renderer = textureView.renderer

        renderer?.labelUpdateListener = this

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        renderer?.labelUpdateListener = null
        renderer = null
        labelContainer = null
        metricLabels.clear()
        imperialLabels.clear()
    }

    override fun onCenteredBoltChanged(boltName: String) {
        // This is only used to trigger the styling logic in updateLabels
        centeredBoltName = boltName
    }

    override fun onAllBoltsUpdated(boltData: List<BoltPair>) {
        activity?.runOnUiThread {
            updateLabels(boltData)
        }
    }

    private fun updateLabels(boltData: List<BoltPair>) {
        val container = labelContainer ?: return
        val renderer = renderer ?: return
        val screenWidth = renderer.width.toFloat()
        val screenHeight = renderer.height.toFloat()

        val activeMetricNames = mutableSetOf<String>()
        val activeImperialNames = mutableSetOf<String>()

        val horizontalMarginPx = resources.displayMetrics.density * METRIC_LABEL_X_POS_DP // Use one for both columns

        for (pair in boltData) {
            val worldY = pair.offset + renderer.positionY // Pair's World Y position relative to the camera
            
            // Screen Y calculation: worldY in [-1, 1] maps to screenY in [0, height]
            val screenY = (1f - worldY) / 2f * screenHeight

            // Check visibility
            val isVisible = screenY > -100f && screenY < screenHeight + 100f

            // --- METRIC Label ---
            val metricName = pair.metric.name
            activeMetricNames.add(metricName)
            val metricTextView = metricLabels.getOrPut(metricName) {
                createLabelTextView(container, Gravity.START)
            }
            updateSingleLabel(metricTextView, metricName, screenY, horizontalMarginPx, isVisible, container, true)

            // --- IMPERIAL Label ---
            val imperialName = pair.imperial.name
            activeImperialNames.add(imperialName)
            val imperialTextView = imperialLabels.getOrPut(imperialName) {
                createLabelTextView(container, Gravity.END)
            }
            updateSingleLabel(imperialTextView, imperialName, screenY, horizontalMarginPx, isVisible, container, false)
        }

        // Cleanup: remove any unused views (though likely static for bolt lists)
        metricLabels.keys.filter { it !in activeMetricNames }.forEach { key -> container.removeView(metricLabels.remove(key)) }
        imperialLabels.keys.filter { it !in activeImperialNames }.forEach { key -> container.removeView(imperialLabels.remove(key)) }
    }

    private fun updateSingleLabel(
        textView: TextView,
        name: String,
        screenY: Float,
        marginPx: Float,
        isVisible: Boolean,
        container: ViewGroup,
        isMetric: Boolean
    ) {
        if (isVisible) {
            val isCentered = name == centeredBoltName || (isMetric && name == centeredBoltName) || (!isMetric && name == imperialLabels.filterValues { it == textView }.keys.singleOrNull())

            // Style: Use consistent colors/sizes
            textView.text = name
            val textColor = if (isCentered) Color.WHITE else ContextCompat.getColor(requireContext(), R.color.fontPrimary)
            textView.setTextColor(textColor)
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, BOLT_LABEL_TEXT_SIZE_SP)

            val layoutParams = textView.layoutParams as FrameLayout.LayoutParams

            // Vertical Centering
            textView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            val halfLabelHeight = textView.measuredHeight / 2f
            layoutParams.topMargin = (screenY - halfLabelHeight).toInt()

            // Horizontal Positioning
            if (isMetric) {
                // Metric label is LEFT aligned and uses LEFT margin
                layoutParams.leftMargin = marginPx.toInt()
                layoutParams.rightMargin = 0
            } else {
                // Imperial label is RIGHT aligned and uses RIGHT margin
                layoutParams.rightMargin = marginPx.toInt()
                layoutParams.leftMargin = 0
            }
            
            // Ensure gravity matches the positioning strategy
            layoutParams.gravity = Gravity.TOP or if (isMetric) Gravity.LEFT else Gravity.RIGHT

            textView.visibility = View.VISIBLE
            textView.requestLayout()
        } else {
            textView.visibility = View.GONE
        }
    }

    private fun createLabelTextView(container: ViewGroup, gravity: Int): TextView {
        val textView = TextView(requireContext())
        // Use a color that contrasts with the background, like primary color
        textView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.backgroundPanel)) 
        textView.setPadding(8, 4, 8, 4)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, BOLT_LABEL_TEXT_SIZE_SP)
        textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.font))
        
        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.gravity = Gravity.TOP or gravity // TOP for margin, gravity for horizontal alignment
        container.addView(textView, layoutParams)
        return textView
    }
}
