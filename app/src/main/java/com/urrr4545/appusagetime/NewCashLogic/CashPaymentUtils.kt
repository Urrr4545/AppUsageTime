package com.urrr4545.appusagetime.NewCashLogic

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import com.google.gson.GsonBuilder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object CashPaymentUtils {

    fun getTotalTodayDate(context: Context,currentCal : Calendar) : ArrayList<UsageEvents.Event>{
        var listItem: ArrayList<UsageEvents.Event>? = null

//        val format1 = SimpleDateFormat("yyyy년 MM월dd일 HH시 mm분 ss초")
        var sTime : Long
        var eTime : Long
        val cal = Calendar.getInstance()
        cal.timeInMillis = currentCal.timeInMillis
        cal.set(Calendar.HOUR_OF_DAY,0)
        cal.set(Calendar.MINUTE,0)
        cal.set(Calendar.SECOND,0)
        cal.set(Calendar.MILLISECOND,0)
        sTime = cal.timeInMillis
        cal.set(Calendar.HOUR_OF_DAY,23)
        cal.set(Calendar.MINUTE,59)
        cal.set(Calendar.SECOND,59)
        eTime = cal.timeInMillis

        val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

        if(listItem == null){
            listItem = ArrayList<UsageEvents.Event>()
        }
        listItem!!.clear()

        val usageEvents = usageStatsManager.queryEvents(sTime, eTime)
        while (usageEvents.hasNextEvent()) {
            val event = UsageEvents.Event()
            usageEvents.getNextEvent(event)
            if((event.className != null)){
                listItem!!.add(event)
            }
        }

//        listItem!!.sortBy { it.timeStamp }
        return listItem
    }

    fun getUserDataLastIndex(mContext:Context) : Int{
//        val gson = GsonBuilder().create()
//        var str = SharedPreferencesUtil.getSharedPreference(
//            mContext,
//            SHARED_KEY_USERDATA
//        )
//        val objs = gson.fromJson(str, Array<UserLSUsageObj>::class.java).toList()
//        if(objs.size == 0){
//            return 0
//        }
//        return objs.get(objs.lastIndex).index
        return 0
    }
}