package com.example.clockmachine.mainActivity.CalendarActivity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cas.igsnrr.clocktool.R
import com.example.clockmachine.mainActivity.com.example.clockmachine.mainActivity.RecordClockState
import com.example.clockmachine.mainActivity.com.example.clockmachine.mainActivity.RecordClockTimeLocation
import com.example.clockmachine.mainActivity.com.example.clockmachine.mainActivity.TimeUtile
import cas.igsnrr.clocktool.CalendarActivity.Adapter.CalendarAdapter
import kotlinx.android.synthetic.main.activity_calendar.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.collections.ArrayList

class CalendarActivity : AppCompatActivity() {

    val myAdapter = MyAdapter()
    val cAdapter = CalendarAdapter()
    val mData: MutableList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
        initGird()
        myAdapter.setData(mData)
        Gird1.adapter = myAdapter
        cAdapter.setContext(this)
        Grid2.adapter = cAdapter
        feedUI()
    }

    fun feedUI(){
        date_month.setText(TimeUtile.getRealMonthDate())
        when(RecordClockState.clockState){
            //显示签到信息
            2 -> {
                alocation2.visibility = View.GONE
                aarrive2.visibility = View.GONE
                apoint2.setImageResource(R.mipmap.point_dark)
                aarrive1.setText("签到: ${RecordClockTimeLocation.signInTime}")
                alocation1.setText(RecordClockTimeLocation.signInLocation)
            }
            //显示签退信息
            3 -> {
                aarrive1.setText("签到: ${RecordClockTimeLocation.signInTime}")
                aarrive2.setText("签到: ${RecordClockTimeLocation.signOutTime}")
                alocation1.setText(RecordClockTimeLocation.signInLocation)
                alocation2.setText(RecordClockTimeLocation.signOutLocation)
            }
            //还没有签到签退信息
            else -> {
                ablock1.visibility = View.GONE
                bline.visibility = View.GONE
            }
        }
    }

    fun initGird(){
        mData.apply {
            add("日")
            add("一")
            add("二")
            add("三")
            add("四")
            add("五")
            add("六")
        }
    }

    class MyAdapter: BaseAdapter() {

        var mData: MutableList<String> = ArrayList()

        fun setData(mData : MutableList<String>){
            this.mData = mData
        }

        inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val weekday: TextView = itemView.findViewById(R.id.weekday)
        }

        override fun getCount(): Int = mData.size

        override fun getItem(p0: Int): Any {
            return mData[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        @SuppressLint("ViewHolder")
        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            val view =  LayoutInflater.from(p2?.context).inflate(R.layout.activity_calendar_item, p2, false)
            val weekday : TextView = view.findViewById(R.id.weekday)
            weekday.setText(mData[p0])
            return view
        }


    }
}