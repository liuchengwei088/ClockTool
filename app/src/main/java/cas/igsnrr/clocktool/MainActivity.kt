package com.example.clockmachine.mainActivity


import android.annotation.SuppressLint
import android.content.Intent
import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import cas.igsnrr.clocktool.R
import cas.igsnrr.clocktool.clockMap.ClockMapActivity
import cas.igsnrr.clocktool.clockMap.LocationServices
import com.example.clockmachine.mainActivity.CalendarActivity.CalendarActivity
import com.example.clockmachine.mainActivity.com.example.clockmachine.mainActivity.RecordClockState
import com.example.clockmachine.mainActivity.com.example.clockmachine.mainActivity.RecordClockTimeLocation
import com.example.clockmachine.mainActivity.com.example.clockmachine.mainActivity.TimeUtile
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

//    private var clockState = 1 //1为签到，2为签退
    private var serviceState = 0 //0为关，1为开

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val startIntent = Intent(this, LocationServices::class.java)
        startService(startIntent)
        onClick()
        feedUI()
    }

    val handler = object : Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when(msg.what){
                1 ->  {
                    //在这里更新UI
                    time.setText(TimeUtile.getRealtime())
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun feedUI(){
        //开启异步线程，每隔一秒更新一次时间
        thread {
            while (true){
                try {
                    val msg = Message()
                    msg.what = 1
                    handler.sendMessage(msg)
                    Thread.sleep(1000)
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }
        date.setText(TimeUtile.getRealDate()+" "+ TimeUtile.getWeekDay())

        if (RecordClockTimeLocation.signInTime == ""){
            RecordClockTimeLocation.signInTime = TimeUtile.getRealDateTime()
        }else{
            RecordClockTimeLocation.signOutTime = TimeUtile.getRealDateTime()
        }
        when(RecordClockState.mapClockState){
            1 -> {
                block1.visibility = View.VISIBLE
                arrive2.visibility = View.GONE
                location2.visibility = View.GONE
                restart.visibility = View.GONE
                range.visibility = View.GONE
                point2.setImageResource(R.mipmap.point_dark)
                RecordClockState.clockState = 2
                arrive1.setText("签到：${RecordClockTimeLocation.signInTime}")
                location1.setText(RecordClockTimeLocation.signInLocation)
            }
            2 -> {
                arrive2.visibility = View.VISIBLE
                location2.visibility = View.VISIBLE
                restart.visibility = View.VISIBLE
                range.visibility = View.VISIBLE
                point2.setImageResource(R.mipmap.point)
                button.visibility = View.GONE
                block2.visibility = View.GONE
                RecordClockState.clockState = 3
                arrive2.setText("签到：${RecordClockTimeLocation.signOutTime}")
                location2.setText(RecordClockTimeLocation.signOutLocation)
            }
            3 -> {
                block1.visibility = View.VISIBLE
                button.visibility = View.GONE
                block2.visibility = View.GONE
                arrive2.setText("签到：${TimeUtile.getRealDateTime()}")
                RecordClockTimeLocation.signOutTime = arrive2.text.toString()
                location2.setText(RecordClockTimeLocation.signOutLocation)
            }
        }
    }

    fun onClick(){
        button.setOnClickListener(){

            if (RecordClockTimeLocation.signInTime == ""){
                RecordClockTimeLocation.signInTime = TimeUtile.getRealDateTime()
            }else{
                RecordClockTimeLocation.signOutTime = TimeUtile.getRealDateTime()
            }

            when(RecordClockState.clockState){
                1 -> {
                    block1.visibility = View.VISIBLE
                    arrive2.visibility = View.GONE
                    location2.visibility = View.GONE
                    restart.visibility = View.GONE
                    range.visibility = View.GONE
                    point2.setImageResource(R.mipmap.point_dark)
                    RecordClockState.clockState = 2
                    arrive1.setText("签到：${RecordClockTimeLocation.signInTime}")
                    location1.setText(RecordClockTimeLocation.signInLocation)
                    RecordClockState.mapClockState = 1
                }
                2 -> {
                    arrive2.visibility = View.VISIBLE
                    location2.visibility = View.VISIBLE
                    restart.visibility = View.VISIBLE
                    range.visibility = View.VISIBLE
                    point2.setImageResource(R.mipmap.point)
                    button.visibility = View.GONE
                    block2.visibility = View.GONE
                    RecordClockState.clockState = 3
                    arrive2.setText("签到：${RecordClockTimeLocation.signOutTime}")
                    location2.setText(RecordClockTimeLocation.signOutLocation)
                    RecordClockState.mapClockState = 2
                }
            }
        }
        restart.setOnClickListener(){
            RecordClockState.restartSate = 1
            Thread.sleep(500)
            arrive2.setText("签到：${TimeUtile.getRealDateTime()}")
            RecordClockTimeLocation.signOutTime = arrive2.text.toString()
            location2.setText(RecordClockTimeLocation.signOutLocation)
            RecordClockState.restartSate = 0
        }
        calendar.setOnClickListener(){
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
        }
        kaoqin.setOnClickListener {
            val intent = Intent(this, ClockMapActivity::class.java)
            startActivity(intent)
//            finish()
        }
        iback.setOnClickListener(){
            Log.d("11111111111111111111111", RecordClockTimeLocation.signInTime)
        }
        range.setOnClickListener {
            val intent = Intent(this, ClockMapActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        feedUI()
    }

    override fun onDestroy() {
        super.onDestroy()
        val stopService = Intent(this, LocationServices::class.java)
        stopService(stopService)

    }
}