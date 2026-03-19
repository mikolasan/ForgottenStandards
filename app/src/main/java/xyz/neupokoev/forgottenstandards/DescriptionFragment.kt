package xyz.neupokoev.forgottenstandards

import android.app.Dialog
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Layout
import android.text.Spannable
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.LeadingMarginSpan
import android.text.style.LineBackgroundSpan
import android.text.style.LineHeightSpan
import android.text.style.RelativeSizeSpan
import android.text.style.ReplacementSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.github.mikolasan.ratiogenerator.ImperialUnit
import io.github.mikolasan.ratiogenerator.ImperialUnitName
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.MarkwonConfiguration
import io.noties.markwon.MarkwonSpansFactory
import io.noties.markwon.RenderProps
import io.noties.markwon.core.MarkwonTheme
import org.commonmark.node.BlockQuote
import xyz.neupokoev.forgottenstandards.menu.ImperialCategory
import java.io.IOException
import java.util.Locale
import kotlin.math.roundToInt

class DescriptionFragment : BottomSheetDialogFragment() {

    private var unitName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            unitName = it.getString(ARG_UNIT_NAME)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener {
            val bottomSheet = dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                // Force the background color on the container
                it.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.panel_back))
                
                // Ensure the container is tall enough to fill the screen when expanded
                val layoutParams = it.layoutParams
                layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                it.layoutParams = layoutParams

                val behavior = BottomSheetBehavior.from(it)
                behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                behavior.skipCollapsed = false
                behavior.isDraggable = true
                behavior.isFitToContents = false
                behavior.halfExpandedRatio = 0.6f
                // Set peek height to 40% of the screen
                behavior.peekHeight = (resources.displayMetrics.heightPixels * 0.4).toInt()
            }
        }
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_description, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val textView = view.findViewById<TextView>(R.id.description_text)
        val name = unitName ?: return

        val description = getDescription(name)
        
        val markwon = Markwon.builder(requireContext())
            .usePlugin(object : AbstractMarkwonPlugin() {
                override fun configureTheme(builder: MarkwonTheme.Builder) {
                    builder
                        .blockQuoteWidth(0) // Custom span draws it
                        .blockMargin(24)    // Paragraph spacing
                        .build()
                }

                override fun configureSpansFactory(builder: MarkwonSpansFactory.Builder) {
                    builder.setFactory(/* node = */ BlockQuote::class.java,
                        /* factory = */ fun(_: MarkwonConfiguration, _: RenderProps): Any {
                            val borderColor =
                                ContextCompat.getColor(requireContext(), R.color.action)
                            val bgColor =
                                ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark)
                            val texture =
                                ContextCompat.getDrawable(requireContext(), R.drawable.bg_damask)
                            // gapWidth: 48, verticalPadding: 32
                            return arrayOf(
                                CustomQuoteSpan(borderColor, bgColor, texture, 12, 48, 32),
                                StyleSpan(Typeface.ITALIC),
                                RelativeSizeSpan(1.15f)
                            )
                        })
                }
            })
            .build()

        markwon.setMarkdown(textView, description)
        
        val spannable = textView.text as? Spannable ?: return
        highlightUnits(spannable)
        
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.highlightColor = Color.TRANSPARENT 
    }

    class CustomQuoteSpan(
        private val borderColor: Int,
        private val backgroundColor: Int,
        private val texture: Drawable?,
        private val borderWidth: Int,
        private val gapWidth: Int,
        private val verticalPadding: Int
    ) : LeadingMarginSpan, LineBackgroundSpan, LineHeightSpan {

        override fun getLeadingMargin(first: Boolean): Int = borderWidth + gapWidth

        override fun drawLeadingMargin(
            c: Canvas, p: Paint, x: Int, dir: Int,
            top: Int, baseline: Int, bottom: Int,
            text: CharSequence, start: Int, end: Int,
            first: Boolean, layout: Layout
        ) {
            val oldStyle = p.style
            val oldColor = p.color
            p.style = Paint.Style.FILL
            p.color = borderColor
            c.drawRect(x.toFloat(), top.toFloat(), (x + dir * borderWidth).toFloat(), bottom.toFloat(), p)
            p.style = oldStyle
            p.color = oldColor
        }

        override fun drawBackground(
            c: Canvas, p: Paint, left: Int, right: Int,
            top: Int, baseline: Int, bottom: Int,
            text: CharSequence, start: Int, end: Int,
            lineNumber: Int
        ) {
            val spanned = text as? Spanned ?: return
            val spanStart = spanned.getSpanStart(this)
            
            val oldColor = p.color
            p.color = backgroundColor
            c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), p)
            
//            texture?.let {
//                it.setBounds(left, top, right, bottom)
//                it.alpha = 15 // Very subtle texture
//                it.draw(c)
//            }

            // Draw huge double quote symbol on the first line of THIS span
            if (start == spanStart) {
                val oldTextSize = p.textSize
                val oldTypeface = p.typeface
                val oldAlpha = p.alpha
                
                p.textSize = 120f
                p.typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
                p.color = borderColor
                p.alpha = 255
                
                // Positioned relative to the top of the first line of the block
                // Adjusting Y coordinate to be inside the vertical padding area
                c.drawText("“", left + borderWidth + 8f, top + 70f, p)
                
                p.textSize = oldTextSize
                p.typeface = oldTypeface
                p.alpha = oldAlpha
            }
            
            p.color = oldColor
        }

        override fun chooseHeight(
            text: CharSequence, start: Int, end: Int,
            spanstartv: Int, v: Int, fm: Paint.FontMetricsInt
        ) {
            val spanned = text as? Spanned ?: return
            val spanStart = spanned.getSpanStart(this)
            val spanEnd = spanned.getSpanEnd(this)
            
            // Apply padding only to the first and last lines of the blockquote
            if (start == spanStart) {
                fm.ascent -= verticalPadding
                fm.top -= verticalPadding
            }
            if (end == spanEnd) {
                fm.descent += verticalPadding
                fm.bottom += verticalPadding
            }
        }
    }

    class RoundedBackgroundSpan(
        private val backgroundColor: Int,
        private val textColor: Int
    ) : ReplacementSpan() {
        private val cornerRadius = 12f
        private val paddingHorizontal = 16f
        private val paddingVertical = 4f

        override fun getSize(paint: Paint, text: CharSequence, start: Int, end: Int, fm: Paint.FontMetricsInt?): Int {
            return (paint.measureText(text, start, end) + 2 * paddingHorizontal).roundToInt()
        }

        override fun draw(canvas: Canvas, text: CharSequence, start: Int, end: Int, x: Float, top: Int, y: Int, bottom: Int, paint: Paint) {
            val fm = paint.fontMetrics
            // Calculate badge vertical bounds relative to the baseline 'y'
            // instead of using the line's 'top' and 'bottom' which may include extra blockquote padding
            val badgeTop = y + fm.ascent - paddingVertical
            val badgeBottom = y + fm.descent + paddingVertical
            
            val rect = RectF(x, badgeTop, x + paint.measureText(text, start, end) + 2 * paddingHorizontal, badgeBottom)
            
            val oldColor = paint.color
            paint.color = backgroundColor
            canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint)
            paint.color = textColor
            canvas.drawText(text, start, end, x + paddingHorizontal, y.toFloat(), paint)
            paint.color = oldColor
        }
    }

    private fun highlightUnits(spannable: Spannable) {
        val text = spannable.toString().lowercase(Locale.getDefault())
        val allUnitNames = ImperialUnitName.values().filter { it != ImperialUnitName.NO_UNIT }
        
        val badgeColor = ContextCompat.getColor(requireContext(), R.color.action)
        val textColor = ContextCompat.getColor(requireContext(), R.color.fontPrimary)
        
        for (unitEnum in allUnitNames) {
            val unitString = unitEnum.name.lowercase(Locale.getDefault()).replace('_', ' ')
            if (unitString.isEmpty() || unitString.length < 3) continue 
            
            var start = text.indexOf(unitString)
            while (start >= 0) {
                val end = start + unitString.length
                val isStartBoundary = start == 0 || !text[start - 1].isLetter()
                val isEndBoundary = end == text.length || !text[end].isLetter()
                
                if (isStartBoundary && isEndBoundary) {
                    val existing = spannable.getSpans(start, end, RoundedBackgroundSpan::class.java)
                    if (existing.isEmpty()) {
                        val clickableSpan = object : ClickableSpan() {
                            override fun onClick(widget: View) {
                                navigateToUnit(unitEnum)
                                dismiss()
                            }
                            override fun updateDrawState(ds: TextPaint) {
                                ds.isUnderlineText = false
                            }
                        }
                        spannable.setSpan(clickableSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                        spannable.setSpan(
                            RoundedBackgroundSpan(badgeColor, textColor),
                            start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    }
                }
                start = text.indexOf(unitString, end)
            }
        }
    }

    private fun navigateToUnit(unitEnum: ImperialUnitName) {
        val mainActivity = activity as? MainActivity ?: return
        var foundUnit: ImperialUnit? = null
        for (category in ImperialCategory.typeMap.values) {
            foundUnit = category.units.find { it.unitName == unitEnum }
            if (foundUnit != null) break
        }
        foundUnit?.let {
            mainActivity.onUnitSelectedFromSearch(it)
        }
    }

    private fun getDescription(name: String): String {
        val resId = resources.getIdentifier("desc_$name", "string", requireContext().packageName)
        if (resId != 0) {
            return getString(resId)
        }
        return try {
            requireContext().assets.open("$name.txt").bufferedReader().use { it.readText() }
        } catch (e: IOException) {
            "No description available for $name"
        }
    }

    companion object {
        private const val ARG_UNIT_NAME = "unit_name"
        @JvmStatic
        fun newInstance(unitName: ImperialUnitName) =
            DescriptionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_UNIT_NAME, unitName.name)
                }
            }
    }
}
