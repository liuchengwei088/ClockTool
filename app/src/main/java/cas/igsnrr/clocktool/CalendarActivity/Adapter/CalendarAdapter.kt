package cas.igsnrr.clocktool.CalendarActivity.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import cas.igsnrr.clocktool.R
import com.example.clockmachine.mainActivity.com.example.clockmachine.mainActivity.RecordClockState
import com.example.clockmachine.mainActivity.com.example.clockmachine.mainActivity.TimeUtile

class CalendarAdapter: BaseAdapter() {

    val cData: MutableList<Int> = TimeUtile.getCalendarList()
    var mContext: Context? = null
//    val cData: MutableList<Int> = ArrayList()

    fun setContext(context:Context){
        this.mContext = context
    }

    override fun getCount(): Int = cData.size

    override fun getItem(p0: Int): Any {
        return cData[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val view =  LayoutInflater.from(p2?.context).inflate(R.layout.activity_calendar_count_item, p2, false)
        val num : TextView = view.findViewById(R.id.num)
        val state : ImageView = view.findViewById(R.id.state)
        num.setText(cData[p0].toString())
        if (p0< TimeUtile.getLastMonthDayInThisMonth()||p0>=cData.size- TimeUtile.getNextMonthDayInThisMonth()){
            num.setTextColor(mContext!!.resources.getColor(R.color.grey))
        }else{
            if (!((p0==0)||(p0==6)||(p0==7)||(p0==13)||(p0==14)||(p0==20)||(p0==21)||(p0==27)||(p0==28)||(p0==35))
                &&(p0< TimeUtile.getTodayNumInList())){
                state.visibility = View.VISIBLE
            }
        }
        if (RecordClockState.clockState == 3 && p0== TimeUtile.getTodayNumInList()-1){
            state.setImageResource(R.drawable.point_blue)
        }
        return view
    }
}