package education.mostafa.projects.servicekt.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import education.mostafa.projects.servicekt.helpers.Constants

// https://www.techotopia.com/index.php/Android_Started_Service_%E2%80%93_A_Kotlin_Example
class MyIntentService : Service() {

    var result: Float = 0F


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("OperationService" , "Started")
        val first = intent!!.getFloatExtra("first_num", 0F)
        val second = intent!!.getFloatExtra("second_num", 0F)

        Log.i("first_num" , first.toString())
        Log.i("second_num" , second.toString())

        var Oper:String? = intent!!.getStringExtra("Oper")
        Log.i("Oper" , Oper)

        if (Oper == Constants.SUM) {
            result = first + second
        } else if (Oper == Constants.SUB) {
            result = first - second
        } else if (Oper == Constants.MUL) {
            result = first * second
        } else if (Oper == Constants.DIV) {
            result = first / second
        }


        LocalBroadcastManager.getInstance(this)
            .sendBroadcast(Intent(Constants.RES_INTENT_ACTION).putExtra("result", result))
        return START_NOT_STICKY

    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


}