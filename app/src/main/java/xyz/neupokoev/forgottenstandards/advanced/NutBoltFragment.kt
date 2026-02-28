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
import xyz.neupokoev.forgottenstandards.getDpi
import xyz.neupokoev.forgottenstandards.getDisplayRefreshRate
import java.util.concurrent.ConcurrentHashMap

private const val BOLT_LABEL_TEXT_SIZE_SP = 16f
private const val BOLT_LABEL_CENTER_OFFSET_DP = 50 

class NutBoltFragment : Fragment(), LabelUpdateListener {

    private var labelContainer: FrameLayout? = null
    private var boltRenderer: BoltRenderer? = null
    
    private val metricLabels = ConcurrentHashMap<String, TextView>()
    private val imperialLabels = ConcurrentHashMap<String, TextView>()
    
    private var centeredBoltName: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_nut_bolt, container, false)
        val context = requireContext()
        
        // Construct BoltRenderer properly using context-aware utilities
        val dpi = getDpi(context)
        val refreshRate = getDisplayRefreshRate(context)
        boltRenderer = BoltRenderer(context, refreshRate, dpi)
        boltRenderer?.labelUpdateListener = this

        val glView = view.findViewById<GlView>(R.id.texture_view)
        glView.renderer = boltRenderer
        
        labelContainer = view.findViewById(R.id.texture_and_label_container)
        
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        boltRenderer?.stopRendering()
        boltRenderer?.labelUpdateListener = null
        boltRenderer = null
        labelContainer = null
        metricLabels.clear()
        imperialLabels.clear()
    }

    override fun onCenteredBoltChanged(boltName: String) {
        centeredBoltName = boltName
    }

    override fun onAllBoltsUpdated(boltData: List<BoltPair>) {
        activity?.runOnUiThread {
            updateLabels(boltData)
        }
    }

    private fun updateLabels(boltData: List<BoltPair>) {
        val container = labelContainer ?: return
        val renderer = boltRenderer ?: return
        val screenWidth = renderer.width.toFloat()
        val screenHeight = renderer.height.toFloat()

        val activeMetricNames = mutableSetOf<String>()
        val activeImperialNames = mutableSetOf<String>()

        val density = resources.displayMetrics.density
        val centerOffsetPx = density * BOLT_LABEL_CENTER_OFFSET_DP

        for (pair in boltData) {
            val worldY = pair.offset + renderer.positionY 
            val screenY = (1f - worldY) / 2f * screenHeight
            val isVisible = screenY > -100f && screenY < screenHeight + 100f

            val metricName = pair.metric.name
            activeMetricNames.add(metricName)
            val metricTextView = metricLabels.getOrPut(metricName) { createLabelTextView(container) }
            
            val imperialName = pair.imperial.name
            activeImperialNames.add(imperialName)
            val imperialTextView = imperialLabels.getOrPut(imperialName) { createLabelTextView(container) }

            if (isVisible) {
                metricTextView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
                val halfLabelHeight = metricTextView.measuredHeight / 2f
                val topMargin = (screenY - halfLabelHeight).toInt()

                updateSingleLabel(metricTextView, metricName, topMargin, (screenWidth/2 - centerOffsetPx).toInt(), true, metricName == centeredBoltName)
                updateSingleLabel(imperialTextView, imperialName, topMargin, (screenWidth/2 + centerOffsetPx).toInt(), false, pair.metric.name == centeredBoltName)
            } else {
                metricTextView.visibility = View.GONE
                imperialTextView.visibility = View.GONE
            }
        }

        metricLabels.keys.filter { it !in activeMetricNames }.forEach { key -> container.removeView(metricLabels.remove(key)) }
        imperialLabels.keys.filter { it !in activeImperialNames }.forEach { key -> container.removeView(imperialLabels.remove(key)) }
    }

    private fun updateSingleLabel(
        textView: TextView,
        name: String,
        topMargin: Int,
        targetX: Int,
        isMetric: Boolean,
        isCentered: Boolean
    ) {
        textView.text = name
        val textColor = if (isCentered) ContextCompat.getColor(requireContext(), R.color.primary) else ContextCompat.getColor(requireContext(), R.color.font)
        textView.setTextColor(textColor)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, BOLT_LABEL_TEXT_SIZE_SP)

        val layoutParams = textView.layoutParams as FrameLayout.LayoutParams
        layoutParams.topMargin = topMargin
        layoutParams.gravity = Gravity.TOP or Gravity.START
        
        if (isMetric) {
            textView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            layoutParams.leftMargin = targetX - textView.measuredWidth - 20
        } else {
            layoutParams.leftMargin = targetX + 20 
        }

        textView.visibility = View.VISIBLE
        textView.requestLayout()
    }

    private fun createLabelTextView(container: ViewGroup): TextView {
        val textView = TextView(requireContext())
        textView.setPadding(8, 4, 8, 4)
        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        container.addView(textView, layoutParams)
        return textView
    }
}
