package com.example.clockmachine.mainActivity.com.example.clockmachine.mainActivity

import android.text.format.DateFormat
import java.util.*
import kotlin.collections.ArrayList


object TimeUtile {
    //"HH:mm:ss"
    fun getRealtime(): String {
        val realTime: Long = System.currentTimeMillis()
        val realTimeString = DateFormat.format("HH:mm:ss", realTime)
        return realTimeString.toString()
    }

    //"yyyy-MM-dd HH:mm:ss"
    fun getRealDateTime(): String{
        val realTime: Long = System.currentTimeMillis()
        val realTimeDateString = DateFormat.format("yyyy-MM-dd HH:mm:ss", realTime)
        return realTimeDateString.toString()
    }

    //"yyyy-MM-dd"
    fun getRealDate(): String{
        val realTime: Long = System.currentTimeMillis()
        val realDateString = DateFormat.format("yyyy-MM-dd", realTime)
        return realDateString.toString()
    }

    fun getRealMonthDate(): String{
        val realTime: Long = System.currentTimeMillis()
        val realDateString = DateFormat.format("yyyy-MM", realTime)
        return realDateString.toString()
    }

    //今天星期几
    fun getWeekDay(): String{
        val realTime: Long = System.currentTimeMillis()
        val realDateString = DateFormat.format("EEEE", realTime)
        return realDateString.toString()
    }

    //获得本月日历的数组
    fun getCalendarList(): MutableList<Int>{
        var a = 1
        var b = 1
        //获得这个月、上个月、下个月多少天
        val cal = Calendar.getInstance()
        val thisMonth = cal.get(Calendar.MONTH)
        val thisYear = cal.get(Calendar.YEAR)
        cal.set(Calendar.MONTH, thisMonth)
        val thisMonthDayCount =cal.getActualMaximum(Calendar.DATE)
        cal.set(Calendar.MONTH, thisMonth-1)
        var lastMonthDayCount =cal.getActualMaximum(Calendar.DATE)
        cal.set(Calendar.MONTH, thisMonth+1)
        val nextMonthDayCount = cal.getActualMaximum(Calendar.DATE)
        cal.set(thisYear,thisMonth,1)
        val firstDayWeek = cal.get(Calendar.DAY_OF_WEEK)-1//每月的第一天是星期几，同时-1也是前面有几个数字
        //将上个月的日期加入数组
        val monthList : MutableList<Int> = ArrayList()
        for (i in 1..firstDayWeek){
            monthList.add(lastMonthDayCount-firstDayWeek+1)
            lastMonthDayCount++
        }
        for (i in 1..thisMonthDayCount){
            monthList.add(a)
            a++
        }
        for (i in 1..(35-firstDayWeek-thisMonthDayCount)){
            monthList.add(b)
            b++
        }
        return monthList
    }

    //获得在日历中有几天是上个月的
    fun getLastMonthDayInThisMonth(): Int{
        val cal = Calendar.getInstance()
        val thisMonth = cal.get(Calendar.MONTH)
        val thisYear = cal.get(Calendar.YEAR)
        cal.set(thisYear,thisMonth,1)
        val firstDayWeek = cal.get(Calendar.DAY_OF_WEEK)-1//每月的第一天是星期几，同时-1也是前面有几个数字
        val count = firstDayWeek
        return count
    }

    //获取在日历中有几天是下个月的
    fun getNextMonthDayInThisMonth():Int{
        val cal = Calendar.getInstance()
        val thisMonth = cal.get(Calendar.MONTH)
        val thisYear = cal.get(Calendar.YEAR)
        cal.set(thisYear,thisMonth,1)
        val firstDayWeek = cal.get(Calendar.DAY_OF_WEEK)-1//每月的第一天是星期几，同时-1也是前面有几个数字
        cal.set(Calendar.MONTH, thisMonth)
        val thisMonthDayCount =cal.getActualMaximum(Calendar.DATE)
        val count = 35-firstDayWeek-thisMonthDayCount
        return count
    }

    //获取今天在list里是第几个
    fun getTodayNumInList() : Int{
        val cal = Calendar.getInstance()
        val todayInMonth = cal.get(Calendar.DAY_OF_MONTH)
        return todayInMonth+ getLastMonthDayInThisMonth()
    }
}