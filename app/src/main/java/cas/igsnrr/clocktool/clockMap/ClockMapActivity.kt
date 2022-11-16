package cas.igsnrr.clocktool.clockMap

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import cas.igsnrr.clocktool.R
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.example.clockmachine.mainActivity.com.example.clockmachine.mainActivity.RecordClockState
import com.example.clockmachine.mainActivity.com.example.clockmachine.mainActivity.RecordClockTimeLocation
import com.example.clockmachine.mainActivity.com.example.clockmachine.mainActivity.TimeUtile
import kotlinx.android.synthetic.main.activity_clock_map.*
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import kotlin.concurrent.thread


class ClockMapActivity : AppCompatActivity() {

    var mMapView: MapView? = null
    var mBaiduMap : BaiduMap? = null
    var mLocationClient : LocationClient? = null
    val myListener = MyLocationListener()
    var isFirstLoc = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        SDKInitializer.initialize(getApplicationContext())//初始化地图SDK，一定要放在setContentView之前
        setContentView(R.layout.activity_clock_map)
        mLocationClient = LocationClient(applicationContext)
        mLocationClient!!.registerLocationListener(myListener)
        initBauduMap()
        feedUI()
//        initLocation()
    }


//    fun initLocation(){
//        val option = LocationClientOption().apply {
//            isOpenGps = true // 打开gps
//            setCoorType("bd09ll") // 设置坐标类型
//            setScanSpan(1000)
//        }
//        mLocationClient?.setLocOption(option)
//    }

    val handler = object : Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when(msg.what){
                2 ->  {
                    //在这里更新UI
                    clock.setText(TimeUtile.getRealtime()+" 打卡")
                }
            }
        }
    }

    fun feedUI(){
        thread {
            while (true){
                try {
                    val msg = Message()
                    msg.what = 2
                    handler.sendMessage(msg)
                    Thread.sleep(1000)
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }

//        clock.setOnClickListener(){
//            when(RecordClockState.mapClockState){
//                0 ->{
//                    RecordClockState.clockState = 1
//                    RecordClockState.test2 = RecordClockState.test1
//                }
//                2 ->{
//                    RecordClockState.clockState = 3
//                }
//                else ->{
//
//                }
//            }
////            finish()
//        }
//        iback3.setOnClickListener(){
//            Log.d("2222222222222222", RecordClockState.test2)
//        }
    }

    fun initBauduMap(){
        mMapView = bmapView
        mBaiduMap = mMapView?.map!!
        mBaiduMap?.isMyLocationEnabled = true
        //定位初始化
        mLocationClient = LocationClient(this)
//通过LocationClientOption设置LocationClient相关参数
        val option = LocationClientOption().apply {
            isOpenGps = true // 打开gps
            setCoorType("bd09ll") // 设置坐标类型
            setScanSpan(1000)
            setNeedDeviceDirect(true)
            setIsNeedAddress(true)
        }
//设置locationClientOption
        mLocationClient?.setLocOption(option)
//注册LocationListener监听器
        val myLocationListener = MyLocationListener()
        mLocationClient?.registerLocationListener(myLocationListener)
//开启地图定位图层
        mLocationClient?.start()
    }



    override fun onResume() {
        super.onResume()
        mMapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mMapView?.onPause()
    }

    override fun onDestroy() {
        mLocationClient?.stop()
        mBaiduMap?.isMyLocationEnabled = false
        mMapView?.onDestroy()
        mMapView = null
        super.onDestroy()
    }

    inner class MyLocationListener : BDAbstractLocationListener() {
        override fun onReceiveLocation(location: BDLocation) {
            //mapView 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return
            }
            val locData = MyLocationData.Builder()
                .accuracy(location.radius) // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(location.direction).latitude(location.latitude)
                .longitude(location.longitude).build()
            mBaiduMap?.setMyLocationData(locData)

            mylocation.setText(location.addrStr)

            clock.setOnClickListener(){
                when(RecordClockState.mapClockState){
                    0 ->{
                        RecordClockState.clockState = 2
                        RecordClockState.mapClockState = 1
                        RecordClockTimeLocation.signInLocation = location.addrStr
                    }
                    1 ->{
                        RecordClockTimeLocation.signOutLocation = location.addrStr
                        RecordClockState.clockState = 3
                        RecordClockState.mapClockState = 2
                    }
                    2 -> {
                        RecordClockTimeLocation.signOutLocation = location.addrStr
                        RecordClockState.clockState = 3
                        RecordClockState.mapClockState = 3
                    }
                    else ->{

                    }
                }
                finish()
            }

            iback3.setOnClickListener(){
                Log.d("2222222222222222", RecordClockState.test2)
            }

            if (isFirstLoc){
                val ll = LatLng(location.latitude, location.longitude)
                val builder = MapStatus.Builder()
                builder.target(ll).zoom(18.0f)
                val u = MapStatusUpdateFactory.newMapStatus(builder.build())
                mBaiduMap?.animateMapStatus(u)
                isFirstLoc = false
            }
        }
    }
}