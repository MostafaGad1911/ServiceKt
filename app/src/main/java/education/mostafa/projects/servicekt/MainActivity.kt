package education.mostafa.projects.servicekt

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import education.mostafa.projects.servicekt.helpers.Constants
import education.mostafa.projects.servicekt.interfaces.main_view
import education.mostafa.projects.servicekt.services.MyIntentService

class MainActivity : AppCompatActivity(), main_view {

    lateinit var first_num_txt: EditText
    lateinit var second_num_txt: EditText
    lateinit var operations_spin: Spinner
    lateinit var result_txt:TextView
    var res: Int = 0

    private val opersResReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if(intent!!.hasExtra("result")){
                res = intent.getIntExtra("result" , 0)
                setRest(res)
                Log.i("ResultRecieved" , res.toString())
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        initOperations()
    }

    override fun initViews() {
        first_num_txt = findViewById(R.id.first_num_txt)
        second_num_txt = findViewById(R.id.second_num_txt)
        operations_spin = findViewById(R.id.operations_spin)
        result_txt = findViewById(R.id.result_txt)
    }


    override fun initOperations() {
        var math_operations = arrayOf(
            resources.getString(R.string.operations),
            resources.getString(R.string.sum),
            resources.getString(R.string.sub),
            resources.getString(R.string.mul),
            resources.getString(R.string.div)
        )
        operations_spin.adapter = ArrayAdapter<String>(
            this, android.R.layout.select_dialog_item
            , math_operations
        )
        operations_spin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                if (position != 0) {
                    if (validInputs()) {
                        var firstNum = first_num_txt.text.toString().toIntOrNull()
                        var secondNum = second_num_txt.text.toString().toIntOrNull()

                        val intent = Intent(applicationContext, MyIntentService::class.java)
                            .putExtra("first_num" , firstNum)
                            .putExtra("second_num" , secondNum)

                        if(position == 1){
                            intent.putExtra("Oper" , Constants.SUM)
                        }else if(position == 2){
                            intent.putExtra("Oper" , Constants.SUB)
                        }else if(position == 3){
                            intent.putExtra("Oper" , Constants.MUL)
                        }else if(position == 4){
                            intent.putExtra("Oper" , Constants.DIV)
                        }
                        startService(intent)
                    }

                }
            }
        }
    }

    override fun validInputs(): Boolean {
        if (first_num_txt.text.toString().isNullOrEmpty()) {
            first_num_txt.setError(resources.getString(R.string.num_error))
            operations_spin.setSelection(0)
            return false
        } else if (second_num_txt.text.toString().isNullOrEmpty()) {
            operations_spin.setSelection(0)
            second_num_txt.setError(resources.getString(R.string.num_error))
            return false
        } else
            return true
    }

    override fun setRest(res: Int) {
        if(res != null){
            result_txt.setText(res.toString())
        }
    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(applicationContext)
            .registerReceiver(opersResReceiver, IntentFilter(Constants.RES_INTENT_ACTION))
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(applicationContext)
            .registerReceiver(opersResReceiver, IntentFilter(Constants.RES_INTENT_ACTION))
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(applicationContext)
            .unregisterReceiver(opersResReceiver)

    }
}
