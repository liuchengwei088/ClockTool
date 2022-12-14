package cas.igsnrr.clocktool.clockMap;

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import com.baidu.location.BDLocation
import com.baidu.location.BDLocationListener
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.model.LatLng
import com.example.clockmachine.mainActivity.com.example.clockmachine.mainActivity.RecordClockState
import com.example.clockmachine.mainActivity.com.example.clockmachine.mainActivity.RecordClockTimeLocation
import java.util.*


class LocationServices : Service() {
    //定位点信息
    var latlng: LatLng? = null
    private var strLocationProvince //定位点的省份
            : String? = null
    private var strLocationCity //定位点的城市
            : String? = null
    private var strLocationDistrict //定位点的区县
            : String? = null
    private var strLocationStreet //定位点的街道信息
            : String? = null
    private var strLocationStreetNumber //定位点的街道号码
            : String? = null
    private var strLocationAddrStr //定位点的详细地址(包括国家和以上省市区等信息)
            : String? = null
    private var mLocationClient: LocationClient? = null //定位客户端
    var mMyLocationListener = MyLocationListener()
    private var mTimer: Timer? = null
    private var mTimerTask: TimerTask? = null
    private var isStop = false
    override fun onCreate() {
        // TODO Auto-generated method stub
        super.onCreate()
        mLocationClient = LocationClient(applicationContext)
        mLocationClient!!.locOption = setLocationClientOption()
        mLocationClient!!.registerLocationListener(mMyLocationListener)
        Log.d("MyService", "start service")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // 触发定时器
        if (!isStop) {
            Log.i("tag", "定时器启动")
            startTimer()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        // TODO Auto-generated method stub
        if (mLocationClient != null) {
            mLocationClient!!.stop()
        }
        super.onDestroy()
        // 停止定时器
        if (isStop) {
            Log.i("tag", "定时器服务停止")
            stopTimer()
        }
    }

    /**
     * 定时器 每隔一段时间执行一次
     */
    private fun startTimer() {
        isStop = true //定时器启动后，修改标识，关闭定时器的开关
        if (mTimer == null) {
            mTimer = Timer()
        }
        if (mTimerTask == null) {
            mTimerTask = object : TimerTask() {
                override fun run() {
                    do {
                        try {
                            Log.d("tag", "isStop=$isStop")
                            Log.d("tag", "mMyLocationListener=$mMyLocationListener")
                            mLocationClient!!.start()
                            Log.d("tag", "mLocationClient.start()")
                            Log.d("tag", "mLocationClient==$mLocationClient")
                            Thread.sleep((1000 * 3).toLong()) //3秒后再次执行
                        } catch (e: InterruptedException) {
                            // TODO Auto-generated catch block
                            e.printStackTrace()
                        }
                    } while (isStop)
                }
            }
        }
        if (mTimer != null && mTimerTask != null) {
            Log.d("tag", "mTimer.schedule(mTimerTask, delay)")
            mTimer!!.schedule(mTimerTask, 0) //执行定时器中的任务
        }
    }

    /**
     * 停止定时器，初始化定时器开关
     */
    private fun stopTimer() {
        if (mTimer != null) {
            mTimer!!.cancel()
            mTimer = null
        }
        if (mTimerTask != null) {
            mTimerTask!!.cancel()
            mTimerTask = null
        }
        isStop = false //重新打开定时器开关
        Log.d("tag", "isStop=$isStop")
    }

    override fun onBind(intent: Intent?): IBinder? {
        // TODO Auto-generated method stub
        return null
    }

    /**
     * 定位客户端参数设定，更多参数设置，查看百度官方文档
     * @return
     */
    private fun setLocationClientOption(): LocationClientOption {
        val option = LocationClientOption()
        option.locationMode =
            LocationClientOption.LocationMode.Hight_Accuracy // 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setScanSpan(500) //每隔1秒发起一次定位
        option.setCoorType("bd09ll") // 可选，默认gcj02，设置返回的定位结果坐标系
        option.isOpenGps = true //是否打开gps
        option.setIsNeedLocationDescribe(true) //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到该描述，不设置则在4G情况下会默认定位到“天安门广场”
        option.setIsNeedAddress(true) //可选，设置是否需要地址信息，默认不需要，不设置则拿不到定位点的省市区信息
        option.setIsNeedLocationPoiList(true) //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.isLocationNotify = true //可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        /*可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        该参数若不设置，则在4G状态下，会出现定位失败，将直接定位到天安门广场
         */return option
    }

    /**
     * 定位监听器
     * @author User
     */
    inner class MyLocationListener : BDLocationListener {
        override fun onReceiveLocation(location: BDLocation) {
            if (location == null) {
                return
            }
            val lat = location.latitude
            val lng = location.longitude
            latlng = LatLng(lat, lng)
            //定位点地址信息做非空判断
            strLocationProvince = if ("" == location.province) {
                "未知省"
            } else {
                location.province
            }
            strLocationCity = if ("" == location.city) {
                "未知市"
            } else {
                location.city
            }
            strLocationDistrict = if ("" == location.district) {
                "未知区"
            } else {
                location.district
            }
            strLocationStreet = if ("" == location.street) {
                "未知街道"
            } else {
                location.street
            }
            strLocationStreetNumber = if ("" == location.streetNumber) {
                ""
            } else {
                location.streetNumber
            }
            strLocationAddrStr = if ("" == location.addrStr) {
                ""
            } else {
                location.addrStr
            }
            if (RecordClockTimeLocation.signInLocation==""){
                RecordClockTimeLocation.signInLocation = location.addrStr
            }
            if (RecordClockTimeLocation.signInLocation!==""&&RecordClockTimeLocation.signOutLocation==""){
                RecordClockTimeLocation.signOutLocation = location.addrStr
            }
            if (RecordClockState.restartSate == 1){
                RecordClockTimeLocation.signOutLocation = location.addrStr
            }

            //定位成功后对获取的数据依据需求自定义处理，这里只做log显示
            Log.d("tag", location.addrStr)

            // 到此定位成功，没有必要反复定位
            // 应该停止客户端再发送定位请求
            if (mLocationClient!!.isStarted) {
                Log.d("tag", "mLocationClient.isStarted()==>mLocationClient.stop()")
                mLocationClient!!.stop()
            }
        }
    }
}