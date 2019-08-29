package com.urrr4545.appusagetime.NewCashLogic

import android.app.AppOpsManager
import android.app.usage.UsageEvents
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.util.Log
import android.widget.LinearLayout
import com.urrr4545.appusagetime.R
import kotlinx.android.synthetic.main.activity_total.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class TotalEventActivity : AppCompatActivity() {

    var totalEventItems : ArrayList<UsageEvents.Event>? = null
    var allAdapter : TotalAdapter? = null
    var pm : PackageManager? = null
    var calendar : Calendar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_total)

        pm = packageManager
        calendar = Calendar.getInstance()
        permission_Status.text = "권한 : "+if (checkPermission()) "적용중" else "적용안됨"

        permission_Button.setOnClickListener{
            if(checkPermission()){
                Log.e("vv","#### 권한 적용중 ####")
            } else {
                val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                startActivity(intent)
            }
        }//

        reversBtn.setOnClickListener {
            reverseItem()
        }

        dataload_Button.setOnClickListener {
            if(checkPermission()) {
                loadData()
            }
        }

        setCurrentDayText()
        if(checkPermission()){
            loadData()
        }

        next_day.setOnClickListener {
            calendar!!.add(Calendar.DAY_OF_MONTH,1)
            setCurrentDayText()
            if(checkPermission()) {
                loadData()
            }
        }

        prev_day.setOnClickListener {
            calendar!!.add(Calendar.DAY_OF_MONTH,-1)
            setCurrentDayText()
            if(checkPermission()) {
                loadData()
            }
        }
    }

    fun setCurrentDayText(){
        var format1 = SimpleDateFormat("yyyy-MM-dd")
        current_day.text = format1.format(calendar!!.timeInMillis)
    }

    fun loadData(){
        totalEventItems = CashPaymentUtils.getTotalTodayDate(this,calendar!!)
        recycler_all?.let{
            it.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
            allAdapter = TotalAdapter(this)
            it.adapter = allAdapter
            it.isNestedScrollingEnabled = false
        }

        var usageAppList : ArrayList<UsageAppStateObj> = ArrayList<UsageAppStateObj>()
        var currentAppPackage : UsageAppStateObj? = null //현재 탐색중인앱.
        var tempAppPackage : UsageAppStateObj? = null // (ex, 게임을하면서 카톡팝업같은 앱에대한 처리를 위해 임시보관)  value = 게임중 카톡팝업이라면 게임에대한 상태값

        totalEventItems?.forEachIndexed { index, item ->
            if(currentAppPackage != null){ //현재 시작값을 같고 탐색중인 앱이 있다면.

                if(tempAppPackage != null){ //현재 다른앱위에 현재앱이 실행되고있는상태
                    if(currentAppPackage!!.appPackageName.equals(item.packageName)){
                        if(item.eventType == 1){ //현재앱이라면 무시.

                        } else if(item.eventType == 2){
                            var isContinuity = false
                            if(index < totalEventItems!!.lastIndex){
                                if(item.packageName.equals(totalEventItems!!.get(index+1).packageName)){
                                    if(totalEventItems!!.get(index+1).timeStamp - item.timeStamp < 100){
                                        isContinuity = true
                                    }
                                }
                            }

                            if(!isContinuity){
                                tempAppPackage!!.appEndTime = currentAppPackage!!.appStartTime - 100
                                usageAppList.add(tempAppPackage!!)

                                currentAppPackage!!.appEndTime = item.timeStamp
                                usageAppList.add(currentAppPackage!!)


                                var newItem = UsageAppStateObj()
                                newItem.appPackageName = tempAppPackage!!.appPackageName
                                newItem.appName = getAppNameFromPackageName(tempAppPackage!!.appPackageName)
                                newItem.appStartTime = item.timeStamp + 100
//                                newItem.appIcon =
//                                newItem.appCategroy =

                                currentAppPackage = newItem
                                tempAppPackage = null
                            }
                        }
                    } else {

                    }
                } else { //다른앱위에 실행되고있지않다면.
                    if(currentAppPackage!!.appPackageName.equals(item.packageName)){
                        if(item.eventType == 1){ //현재앱이라면 무시.

                        } else if(item.eventType == 2){
                            var isContinuity = false
                            if(index < totalEventItems!!.lastIndex){
                                if(item.packageName.equals(totalEventItems!!.get(index+1).packageName)){
                                    if(totalEventItems!!.get(index+1).timeStamp - item.timeStamp < 100){
                                        isContinuity = true
                                    }
                                }
                            }
                            if(!isContinuity) {
                                currentAppPackage!!.appEndTime = item.timeStamp
                                usageAppList.add(currentAppPackage!!)
                                currentAppPackage = null
                            }
                        }
                    } else { // 탐색된 앱이 이전에 찾고있던 앱이랑 다르다면
                        if(item.eventType == 1){
                            //해당앱을 임시보관.
                            tempAppPackage = currentAppPackage
                            var newItem = UsageAppStateObj()
                            newItem.appPackageName = item.packageName
                            newItem.appName = getAppNameFromPackageName(item.packageName)
                            newItem.appStartTime = item.timeStamp
//                    newItem.appIcon =
//                    newItem.appCategroy =

                            currentAppPackage = newItem

                        }
                    }
                }


            } else {
                if(item.eventType == 1){
                    var newItem = UsageAppStateObj()
                    newItem.appPackageName = item.packageName
                    newItem.appName = getAppNameFromPackageName(item.packageName)
                    newItem.appStartTime = item.timeStamp
//                    newItem.appIcon =
//                    newItem.appCategroy =

                    currentAppPackage = newItem

                } else if(item.eventType == 2){
                    if(index == 0){
                        var sTime : Long
                        val cal = Calendar.getInstance()
                        cal.set(Calendar.HOUR_OF_DAY,0)
                        cal.set(Calendar.MINUTE,0)
                        cal.set(Calendar.SECOND,0)
                        cal.set(Calendar.MILLISECOND,0)
                        sTime = cal.timeInMillis

                        var newItem = UsageAppStateObj()
                        newItem.appName = getAppNameFromPackageName(item.packageName)
                        newItem.appPackageName = item.packageName
                        newItem.appStartTime = sTime
                        newItem.appEndTime = item.timeStamp
//                    newItem.appIcon =
//                    newItem.appCategroy =
                        usageAppList.add(newItem)
                    }
                }
            }
        }
        var format1 = SimpleDateFormat("HH시 mm분 ss초")

        usageAppList.filter { it.appUseTime > 0 }.forEach {usageAppStateObj ->
            //            Log.e("usageAppList","앱이름 : "+usageAppStateObj.appName + " 시작시간 : "+format1.format(usageAppStateObj.appStartTime) + " 끝시간 : "+format1.format(usageAppStateObj.appEndTime)+ " 사용시간 : " +usageAppStateObj.appUseTime+ "초 패키지이름 : " +usageAppStateObj.appPackageName)
            var hour = usageAppStateObj.appUseTime /(60 * 60)
            var min = (usageAppStateObj.appUseTime % (60 * 60)) / 60
            var sec = usageAppStateObj.appUseTime % 60
            var time = String.format("%02d:%02d:%02d", hour,min,sec)
            Log.e("dst data"," 시작시간 : "+format1.format(usageAppStateObj.appStartTime) + " 앱이름 : "+usageAppStateObj.appName +  " 사용시간 : " +time)
        }

        var totalUseTime = usageAppList.filter{it.appUseTime>0 && !it.appPackageName.equals("com.sec.android.app.launcher")}.sumBy { it.appUseTime }

        var hour = totalUseTime /(60 * 60)
        var min = (totalUseTime % (60 * 60)) / 60
        var sec = totalUseTime % 60
        var time = String.format("%02d:%02d:%02d", hour,min,sec)
        totalTime.text = time

        allAdapter?.setEventItem(ArrayList(usageAppList.filter { it.appUseTime > 0 }))
    }

    fun reverseItem(){
        allAdapter?.setReverse()
    }

    fun getAppNameFromPackageName(packageName : String) : String{
        var appName = ""
        try {
            appName = pm?.getApplicationLabel(
                pm?.getApplicationInfo(
                    packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES
                )
            ).toString()
        } catch (e:Exception){

        }

        if(TextUtils.isEmpty(appName)){
            return "알수없음"
        } else {
            return appName
        }
    }

    fun checkPermission():Boolean {
        var granted = false;
        var appOps : AppOpsManager = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        var mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,android.os.Process.myUid(), packageName)

        if (mode == AppOpsManager.MODE_DEFAULT) {
            granted = (checkCallingOrSelfPermission(android.Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED)
        } else {
            granted = (mode == AppOpsManager.MODE_ALLOWED)
        }
        return granted
    }
}
