package com.wagit.desktrack.ui.user.home.view

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.wagit.desktrack.R
import com.wagit.desktrack.data.entities.Registry
import com.wagit.desktrack.databinding.FragmentHomeBinding
import com.wagit.desktrack.ui.BaseFragment
import com.wagit.desktrack.ui.helpers.TimeHelper
import com.wagit.desktrack.ui.user.home.viewmodel.HomeViewModel
import com.wagit.desktrack.ui.user.viewmodel.SharedHomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val sharedViewModel: SharedHomeViewModel by activityViewModels()
    private val homeViewModel: HomeViewModel by viewModels()

    //private var handler: Handler? = Handler()
    private var handler: Handler? = Looper.myLooper()?.let { Handler(it) }
    private var tvDhour: TextView? = null
    private var tvDhoursWorked: TextView? = null
    private var startedTime: LocalDateTime? = null

    val runnable = Runnable { doJob() }

    fun doJob() {
        //EventManager.post(BoutiqueRefreshTimerEvent())
        handler!!.postDelayed(runnable, 1000)
    }


    private fun startCountingHours(tvDAuxil: TextView) {
        tvDhour = tvDAuxil
        handler!!.post(run)
        //println("llega al startCountingHours(tvDAuxil: TextView)")
    }

    private val run: Runnable = object : Runnable {
        override fun run() {
            //println("llega al val run: Runnable = object : Runnable del primer Handler")
            handler!!.postDelayed(this, 1000)
            //Call the updateTime()
            sharedViewModel
            tvDhour?.let { updateTime(it) }
        }
    }

    //private var handler2: Handler? = Handler()
    private var handler2: Handler?  = Looper.myLooper()?.let { Handler(it) }

    private fun startCountingHoursWorked(startedTimeAux: LocalDateTime) {
        startedTime = startedTimeAux
        handler2!!.postDelayed(r, 1000);
        println("startCountingHoursWorked(tvDAuxil: TextView)")
    }

    private fun stopCountingHoursWorked() {
        handler2!!.removeCallbacksAndMessages(null);
        println("stopCountingHoursWorked()")
    }

    private val r: Runnable = object : Runnable {
        override fun run() {
            //println("llega al val r: Runnable = object : Runnable del segundo Handler")
            handler2!!.postDelayed(this, 1000);
            //Call to setHoursWorked
            tvDhoursWorked?.let { setHoursWorked(it) }
        }
    };

    override fun FragmentHomeBinding.initialize() {
        println("HHOLA FROM HOME")
        this.sharedVM = sharedViewModel

        //Set the user welcome message
        setUserWelcomeMessage(this.tvName)

        //Set todays date and hour
        setMonthDay(this.tvMonthDay)
        setYear(this.tvYear)
        setWeekDay(this.tvWeekDay)
        startCountingHours(this.tvHour)
        tvDhoursWorked = this.tvHoursWorked

        this.buttonCheckIn.setOnClickListener(View.OnClickListener {
            // Code here executes on main thread after user presses button (buttonCheckIn)
            onClickIn(this.buttonCheckIn,this.tvRegister)
        })

        this.buttonCheckOut.setOnClickListener(View.OnClickListener {
            // Code here executes on main thread after user presses button (buttonCheckOut)
            onClickOut(this.buttonCheckOut,this.tvHoursWorked)
        })
        

        //Detectar si hay cambio en los registros del livedata, si hay un elemento en el it.first activar el boton checkIn y no en caso contrario
        homeViewModel.tRegistry.observe(viewLifecycleOwner, Observer {
            //HABILITAR/DESHABILITAR -> LOS DOS BOTONES = ESTO SE EJECUTA AL CAMBIAR EL REGISTRO
            // Añadir que se ponga el startedAt y endedAt en los 2 textView
            if (it.isEmpty()){
                println("Llega al IF: it.isEmpty()")
                Log.d("IFS","Llega al IF")
                this.buttonCheckOut.isEnabled = false
                this.buttonCheckIn.isEnabled = true
                this.tvRegister.setText("You haven't started working yet.")
                this.tvHoursWorked.setText("")
            }else{
                println("Llega al ELSE")
                if ((it.first().startedAt==null) and (it.first().endedAt==null)){
                    Log.d("IFS","Llega al primer IF dentro del ELSE")
                    this.buttonCheckOut.isEnabled = false
                    this.buttonCheckIn.isEnabled = true
                    this.tvRegister.setText("You haven't started working yet.")
                    this.tvHoursWorked.setText("")
                }else if ((it.first().startedAt!=null) and (it.first().endedAt!=null)){
                    Log.d("IFS","Llega al segundo IF dentro del ELSE")
                    this.buttonCheckOut.isEnabled = false
                    this.buttonCheckIn.isEnabled = false

                    //Stop counting the hours worked and set the total hours worked
                    startedTime = it.first().startedAt!!
                    setCheckInTime(this.tvRegister)
                    stopCountingHoursWorked()
                    setTotalHoursWorked(this.tvHoursWorked, it.first().endedAt!!)
                }
                else if ((it.first().startedAt!=null) and (it.first().endedAt==null)){
                    println("Llega al ELSE IF ULTIMO")
                    Log.d("IFS","Llega al cuarto IF dentro del ELSE")
                    this.buttonCheckOut.isEnabled = true
                    this.buttonCheckIn.isEnabled = false

                    //Set the check in time and start counting the hours worked
                    startCountingHoursWorked(it.first().startedAt!!)
                    setCheckInTime(this.tvRegister)
                }

            }
        })

        val treg = homeViewModel.getTodaysRegistry(sharedViewModel.employee.value!!.id)

    }

    private fun onClickIn(v: Button,tvD: TextView){
        println("buttonCheckIn has been clicked")
        Toast.makeText(v.context, "buttonCheckIn has been clicked", Toast.LENGTH_SHORT).show()
        v.isSelected != v.isSelected

        //Create a new registry for today calling the checkIn method
        val reg= Registry(employeeId = sharedViewModel.employee.value!!.id,
            startedAt = LocalDateTime.now(), endedAt = null)
        homeViewModel.checkIn(reg)

        //Set the check in time and start counting the hours worked
        //startCountingHoursWorked(reg.startedAt!!)
        //setCheckInTime(tvD)

    }

    private fun onClickOut(v: Button,tvD: TextView){
        println("buttonCheckOut has been clicked")
        Toast.makeText(v.context, "buttonCheckOut has been clicked", Toast.LENGTH_SHORT).show()
        v.isSelected != v.isSelected
        homeViewModel.tRegistry.value!!.first().endedAt=LocalDateTime.now()
        homeViewModel.checkOut(homeViewModel.tRegistry.value!!.first())

        //Stop counting the hours worked and set the total hours worked
        //stopCountingHoursWorked()
        //setTotalHoursWorked(tvD, homeViewModel.tRegistry.value!!.first().endedAt!!)

        println("buttonCheckOut has been clicked and endedAt is " +
                "${homeViewModel.tRegistry.value!!.first().endedAt}")
        println("buttonCheckOut has been clicked and tRegistry is " +
                "${homeViewModel.tRegistry.value!!.first()}")
    }

    private fun setUserWelcomeMessage(tvN: TextView){
        Log.d("TVN","Llega a la función setUserWelcomeMessage el ${tvN.id}")
        tvN.setText("${sharedViewModel.employee.value!!.firstName}" +
                " ${sharedViewModel.employee.value!!.lastName}, at your workplace")
    }

    private fun setMonthDay(tvD: TextView){
        Log.d("TVD","Llega a la función setMonthDay el ${tvD.id}")
        tvD.setText("${LocalDateTime.now().month.toString()} " +
                "${LocalDateTime.now().dayOfMonth.toString()}")
    }

    private fun setYear(tvD: TextView){
        Log.d("TVD","Llega a la función setYear el ${tvD.id}")
        tvD.setText("${LocalDateTime.now().year.toString()}")
    }

    private fun setWeekDay(tvD: TextView){
        Log.d("TVD","Llega a la función setWeekDay el ${tvD.id}")
        tvD.setText("${LocalDateTime.now().dayOfWeek.toString()}")
    }

    private fun updateTime(tvD: TextView){
        //Log.d("TVD","Llega a la función updateTime el ${tvD.id}")
        tvD.setText("Current time: ${LocalDateTime.now().hour.toString()}:" +
                "${LocalDateTime.now().minute.toString()}:${LocalDateTime.now().second.toString()}")
    }

    private fun setCheckInTime(tvD: TextView){
        Log.d("TVD","Llega a la función setHoursWorked el ${tvD.id}")
        tvD.setText("You have started working at " +
                "${startedTime!!.hour.toString()}:${startedTime!!.minute.toString()}:" +
                "${startedTime!!.second.toString()} hours.")
    }


    private fun setHoursWorked(tvD: TextView){
        Log.d("TVD","Llega a la función setHoursWorked el ${tvD.id}")
        // startedTime -> contains the started time; TODO: udate with the hours worked
        Log.d("Time","Started time in setHoursWorked is ${startedTime!!}")
        var differenceTime = TimeHelper.timeDifference(startedTime!!,LocalDateTime.now()!!)
        tvD.setText("You have worked " +
                "${differenceTime.hour.toString()} hour ${differenceTime.minute.toString()} " +
                "minutes and ${differenceTime.second.toString()} seconds so far.")
    }

    private fun setTotalHoursWorked(tvD: TextView, checkOutTime: LocalDateTime){
        Log.d("TVD","Llega a la función setTotalHoursWorked el ${tvD.id}")
        Log.d("Time","Started time in setTotalHoursWorked is ${startedTime!!}")
        // startedTime -> contains the started time; TODO: udate with the hours worked
        var differenceTime = TimeHelper.timeDifference(startedTime!!,checkOutTime!!)
        // You have worked today a total of 1 hour 8 minutes and 0 seconds.
        tvD.setText("You have worked today a total of ${differenceTime.hour.toString()} " +
                "hour ${differenceTime.minute.toString()} " +
                "minutes and ${differenceTime.second.toString()} seconds.")
    }

}