package com.urrr4545.appusagetime.NewCashLogic

import android.content.Context
import android.content.pm.PackageManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.urrr4545.appusagetime.R
import kotlinx.android.synthetic.main.list_total_item.view.*
import java.text.SimpleDateFormat
import java.util.*


class TotalAdapter(mContext: Context): RecyclerView.Adapter<TotalAdapter.ViewHolder>() {

    var totalEventItems : ArrayList<UsageAppStateObj> = ArrayList<UsageAppStateObj>()
    var pm = mContext.getPackageManager()
    override fun onCreateViewHolder(vg: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(vg.context).inflate(R.layout.list_total_item, vg, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(p0: ViewHolder, position: Int) {
        p0.bindItems(totalEventItems.get(position),pm)
    }

    override fun getItemCount(): Int {
        return totalEventItems.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var format1 = SimpleDateFormat("HH시 mm분 ss초")
        fun bindItems(item: UsageAppStateObj,pm:PackageManager) {
            itemView?.run {

                var cal = Calendar.getInstance()
                cal.timeInMillis = item.appStartTime

                var startTime = String.format("%02d:%02d", cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE))


                var useHour = item.appUseTime /(60 * 60)
                var useMin = (item.appUseTime  % (60 * 60)) / 60
                var useSec = item.appUseTime  % 60
                var useTime = String.format("%02d:%02d:%02d", useHour,useMin,useSec)
                this.usageAppStartTime.text = startTime
                try{
                    this.usageAppIcon.setImageDrawable(pm.getApplicationIcon(item.appPackageName));

                } catch (e:Exception){

                }
                this.usageAppUseTime.text = useTime
                this.usageAppName.text = item.appName
//                this.event_class.text = item.className

//                Log.e("vv",""+this.event_type.text + " "+this.event_timestamp.text +" "+this.event_package.text +" "+item.className)
            }
        }
    }

    fun setEventItem(totalEventItems : ArrayList<UsageAppStateObj>){
        this.totalEventItems = totalEventItems
        notifyDataSetChanged()
    }

    fun setReverse(){
        this.totalEventItems.reverse()
        notifyDataSetChanged()
    }


}