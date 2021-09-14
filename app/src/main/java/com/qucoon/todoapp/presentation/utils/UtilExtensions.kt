package com.qucoon.todoapp.presentation.utils

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.qucoon.todoapp.DateType
import com.qucoon.todoapp.domain.model.Item
import com.qucoon.todoapp.domain.model.Priority
import com.thetechnocafe.gurleensethi.liteutils.RecyclerAdapterUtil
import java.io.Serializable
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


fun String.capitalizeWords(): String = split(" ").joinToString(" ") { it.lowercase().capitalize() }

fun delayFor(millseconds: Long, action: () -> Unit){
    Handler().postDelayed({
        action()
    }, millseconds)
}

fun RecyclerView.updateRecycler(context: Context, listOfItems:List<Any>, layout:Int, layoutManager: GridLayoutManager, listOfLayout:List<Int>, binder: (Map<Int, View>, Int) -> Unit, onClickPosition:(Int) -> Unit): RecyclerView.Adapter<*>? {
    this.layoutManager =layoutManager
    val reyclerAdaptor = RecyclerAdapterUtil(context,listOfItems,layout)
    reyclerAdaptor.addViewsList(listOfLayout)
    reyclerAdaptor.addOnDataBindListener{itemView, item, position, innerViews ->
        binder(innerViews,position)
    }
    reyclerAdaptor.addOnClickListener { item, position -> onClickPosition(position) }

    this.adapter = reyclerAdaptor

    return adapter
}

fun View.show(){this.visibility = View.VISIBLE}
fun View.hide(){this.visibility = View.INVISIBLE}

abstract class GenericViewHolder <in T> (itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind (item:T)
}

fun getPatternFromDate(date: String, outputPattern: String = "dd MMM yyyy", inputPattern:String = "MMMM dd, yyyy h:mm a") : String{
    val originalFormat: DateFormat = SimpleDateFormat(inputPattern, Locale.ENGLISH)
    val targetFormat = SimpleDateFormat(outputPattern)
    val formattedDate = originalFormat.parse(date)
    return targetFormat.format(formattedDate)
}

fun Fragment.hideKeyboard(){
    val imm: InputMethodManager =
        context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.view?.windowToken, 0)
}

fun DatePicker.setToTodayDate(){
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val  month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)
    this.updateDate(year, month, day)
}

fun DatePicker.getDate(dateFormat: String):String{
    val day = this.dayOfMonth
    val month: Int = this.month
    val year: Int = this.year
    val calendar = Calendar.getInstance()
    calendar[year, month] = day
    val date =  calendar.time
    return date.toDateString(dateFormat)
}

fun Date.toDateString(targetFormart: String): String {
    val sdf = SimpleDateFormat(targetFormart)
    val calendar = Calendar.getInstance()
    calendar.time = this
    return sdf.format(calendar.time)
}

fun addNYearsToDate(currentDate: Date, noOfYears: Int): Date {
    val cal = Calendar.getInstance()
    cal.time = currentDate
    cal.add(Calendar.YEAR, noOfYears)
    return cal.time
}

object DefaultDateConfiguration{
    val defaultDateFormat:String = "dd MMM yyyy"
    val defaultTitle:String = "Choose end date"
    val dateType: DateType = DateType.MIN_DATE
}
