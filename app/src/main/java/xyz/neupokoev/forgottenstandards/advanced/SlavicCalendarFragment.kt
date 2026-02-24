package xyz.neupokoev.forgottenstandards.advanced

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import xyz.neupokoev.forgottenstandards.R
import xyz.neupokoev.forgottenstandards.getDpi
import xyz.neupokoev.forgottenstandards.getDisplayRefreshRate
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private const val SLAVIC_YEAR_OFFSET = 5508
// Assuming the first day of the current Slavic year (Sep 22) starts on Ponedelnik (Index 0)
private const val YEAR_START_WEEKDAY_INDEX = 0 

class SlavicCalendarFragment : Fragment(), CalendarLabelUpdateListener, GlViewClickListener {

    private var calendarRenderer: CalendarRenderer? = null
    private var container: FrameLayout? = null
    private val monthLabels = ConcurrentHashMap<String, TextView>()

    private val slavicWeekdays = listOf(
        "Ponedelnik", "Vtornik", "Triteynik", "Chetverik", 
        "Pyatnitsa", "Shestitsa", "Sedmitsa", "Osmitsa", "Nedelya"
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_slavic_calendar, container, false)
        val context = requireContext()
        
        val dpi = getDpi(context)
        val refreshRate = getDisplayRefreshRate(context)
        
        calendarRenderer = CalendarRenderer(refreshRate, dpi)
        calendarRenderer?.labelUpdateListener = this

        this.container = view.findViewById(R.id.calendar_container)
        val glView = view.findViewById<GlView>(R.id.calendar_gl_view)
        
        glView.renderer = calendarRenderer
        glView.clickListener = this

        val slavicMainText = view.findViewById<TextView>(R.id.slavic_info_text)
        val gregorianMainText = view.findViewById<TextView>(R.id.gregorian_info_text)
        setupCurrentDateInfo(slavicMainText, gregorianMainText)

        return view
    }

    private fun setupCurrentDateInfo(slavicView: TextView, gregorianView: TextView) {
        val today = LocalDate.now()
        val isPostEquinox = today.monthValue > 9 || (today.monthValue == 9 && today.dayOfMonth >= 22)
        val startYearGregorian = if (isPostEquinox) today.year else today.year - 1
        val yearStart = LocalDate.of(startYearGregorian, 9, 22)
        val daysElapsed = ChronoUnit.DAYS.between(yearStart, today).toInt()

        val monthsList = calendarRenderer?.months ?: emptyList()
        var accumulatedDays = 0
        var currentMonthIndex = 0
        var dayInMonth = 1

        for (i in monthsList.indices) {
            val monthDays = monthsList[i].days
            if (daysElapsed < accumulatedDays + monthDays) {
                currentMonthIndex = i
                dayInMonth = daysElapsed - accumulatedDays + 1
                break
            }
            accumulatedDays += monthDays
        }

        val currentMonth = monthsList[currentMonthIndex]
        updateInfoTextInternal(slavicView, gregorianView, currentMonth, currentMonthIndex, dayInMonth, today)

        val monthCenterAngle = currentMonth.startAngle + currentMonth.sweepAngle / 2f
        calendarRenderer?.angle = monthCenterAngle - 90f
    }

    override fun onCalendarUpdated(months: List<Month>, globalAngle: Float) {
        activity?.runOnUiThread { updateLabels(months, globalAngle) }
    }

    override fun onGlViewClicked(x: Float, y: Float) {
        val clickedMonth = calendarRenderer?.resolveMonth(x, y)
        if (clickedMonth != null) {
            val monthIndex = calendarRenderer?.months?.indexOf(clickedMonth) ?: -1
            showMonthPopup(clickedMonth, monthIndex)
        }
    }

    private fun showMonthPopup(month: Month, monthIndex: Int) {
        val context = requireContext()
        val rootLayout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 32, 32, 32)
        }

        val slavicText = createInfoTextView(context, R.color.font)
        rootLayout.addView(wrapInPanel(context, slavicText, R.color.backgroundPanel))
        rootLayout.addView(View(context).apply { layoutParams = LinearLayout.LayoutParams(1, 16) })

        val gregorianText = createInfoTextView(context, R.color.fontPrimary)
        rootLayout.addView(wrapInPanel(context, gregorianText, R.color.inputNormal))
        rootLayout.addView(View(context).apply { layoutParams = LinearLayout.LayoutParams(1, 32) })

        // Calculate offset for the grid: weekday of the 1st day of this month
        var daysBeforeMonth = 0
        val monthsList = calendarRenderer?.months ?: emptyList()
        for (i in 0 until monthIndex) daysBeforeMonth += monthsList[i].days
        val startOffset = (daysBeforeMonth + YEAR_START_WEEKDAY_INDEX) % 9

        val recyclerView = RecyclerView(context)
        recyclerView.layoutManager = GridLayoutManager(context, 9)
        recyclerView.adapter = MonthDaysAdapter(month.days, startOffset) { day ->
            val today = LocalDate.now()
            val isPostEquinox = today.monthValue > 9 || (today.monthValue == 9 && today.dayOfMonth >= 22)
            val baseYear = if (isPostEquinox) today.year else today.year - 1
            val targetDate = LocalDate.of(baseYear, 9, 22).plusDays((daysBeforeMonth + day - 1).toLong())
            updateInfoTextInternal(slavicText, gregorianText, month, monthIndex, day, targetDate)
        }
        rootLayout.addView(recyclerView)

        AlertDialog.Builder(context).setTitle(month.name).setView(rootLayout).setPositiveButton("Close", null).show()
    }

    private fun createInfoTextView(context: android.content.Context, textColorRes: Int) = TextView(context).apply {
        textSize = 14f
        setTextColor(ContextCompat.getColor(context, textColorRes))
        text = "Select a day"
    }

    private fun wrapInPanel(context: android.content.Context, view: View, bgColorRes: Int) = LinearLayout(context).apply {
        orientation = LinearLayout.VERTICAL
        setBackgroundColor(ContextCompat.getColor(context, bgColorRes))
        setPadding(24, 24, 24, 24)
        addView(view)
    }

    private fun updateInfoTextInternal(slavicView: TextView, gregorianView: TextView, month: Month, monthIndex: Int, day: Int, targetGregorianDate: LocalDate) {
        var daysBeforeMonth = 0
        val monthsList = calendarRenderer?.months ?: emptyList()
        for (i in 0 until monthIndex) daysBeforeMonth += monthsList[i].days
        
        val totalDaysSinceYearStart = daysBeforeMonth + day - 1
        val weekdayIndex = (totalDaysSinceYearStart + YEAR_START_WEEKDAY_INDEX) % 9
        val weekdayOrdinal = weekdayIndex + 1
        
        // Slavic Year: Gregorian + 5508 (Reference: Autumn Equinox)
        val slavicYear = targetGregorianDate.year + SLAVIC_YEAR_OFFSET + (if (targetGregorianDate.monthValue > 9 || (targetGregorianDate.monthValue == 9 && targetGregorianDate.dayOfMonth >= 22)) 1 else 0)
        
        slavicView.text = "$day ${month.name} $slavicYear (${slavicWeekdays[weekdayIndex]}, ${weekdayOrdinal}${getOrdinalSuffix(weekdayOrdinal)} day)"

        val gMonthName = targetGregorianDate.month.getDisplayName(TextStyle.FULL, Locale.US)
        val gWeekdayName = targetGregorianDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.US)
        val gWeekdayOrdinal = targetGregorianDate.dayOfWeek.value
        gregorianView.text = "${targetGregorianDate.dayOfMonth} $gMonthName ${targetGregorianDate.year} ($gWeekdayName, ${gWeekdayOrdinal}${getOrdinalSuffix(gWeekdayOrdinal)} day)"
    }

    private fun getOrdinalSuffix(v: Int) = when (v) { 1 -> "st"; 2 -> "nd"; 3 -> "rd"; else -> "th" }

    private fun updateLabels(months: List<Month>, globalAngle: Float) {
        val container = this.container ?: return
        val renderer = calendarRenderer ?: return
        val width = renderer.width.toFloat()
        val height = renderer.height.toFloat()
        if (width <= 0 || height <= 0) return
        val ratio = width / height
        for (month in months) {
            val textView = monthLabels.getOrPut(month.name) {
                createLabelTextView(container, month.name)
            }
            val angleRad = (month.startAngle + month.sweepAngle / 2f - globalAngle) * PI.toFloat() / 180f
            val screenX = (cos(angleRad) * 0.65f + ratio) / (2f * ratio) * width
            val screenY = (1f - sin(angleRad) * 0.65f) / (2f * height) * (height * height) // Corrected screen mapping
            textView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            textView.translationX = screenX - textView.measuredWidth / 2f
            textView.translationY = screenY - textView.measuredHeight / 2f
        }
    }

    private fun createLabelTextView(container: ViewGroup, name: String) = TextView(requireContext()).apply {
        text = name; setTextColor(Color.BLACK); setPadding(8, 4, 8, 4)
        setBackgroundColor(Color.parseColor("#80FFFFFF")); setSingleLine(true)
        container.addView(this, FrameLayout.LayoutParams(-2, -2).apply { gravity = Gravity.TOP or Gravity.START })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        calendarRenderer?.stopRendering()
        calendarRenderer = null
        monthLabels.clear()
    }

    private class MonthDaysAdapter(val dayCount: Int, val startOffset: Int, val onDaySelected: (Int) -> Unit) : RecyclerView.Adapter<MonthDaysAdapter.DayViewHolder>() {
        private var selectedPosition = -1
        class DayViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DayViewHolder(TextView(parent.context).apply {
            gravity = Gravity.CENTER; setPadding(8, 24, 8, 24)
            layoutParams = ViewGroup.LayoutParams(-1, -2)
        })
        override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
            if (position < startOffset) {
                holder.textView.text = ""; holder.textView.setBackgroundColor(0)
                holder.itemView.setOnClickListener(null)
            } else {
                val day = position - startOffset + 1
                holder.textView.text = day.toString()
                holder.textView.setBackgroundColor(if (position == selectedPosition) Color.LTGRAY else 0)
                holder.itemView.setOnClickListener {
                    val old = selectedPosition; selectedPosition = holder.adapterPosition
                    notifyItemChanged(old); notifyItemChanged(selectedPosition); onDaySelected(day)
                }
            }
        }
        override fun getItemCount() = dayCount + startOffset
    }
}
