package com.wagit.desktrack.ui.user.home.view

import android.os.Handler
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
import com.wagit.desktrack.ui.user.home.viewmodel.HomeViewModel
import com.wagit.desktrack.ui.user.viewmodel.SharedHomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val sharedViewModel: SharedHomeViewModel by activityViewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    //To call the updateTime() method every second, we’re going to use the Handler() class and repeat the handler every second (1000 milliseconds)
    //TODO: buscar alternativa del Handler() porque es deprecated
    private var handler: Handler? = Handler()
    private var tvDhour: TextView? = null
    private var tvDhoursWorked: TextView? = null
    private var startedTime: LocalDateTime? = null

    private fun startCountingHours(tvDAuxil: TextView) {
        tvDhour = tvDAuxil
        handler!!.post(run)
        println("llega al startCountingHours(tvDAuxil: TextView)")
    }

    private val run: Runnable = object : Runnable {
        override fun run() {
            println("llega al val run: Runnable = object : Runnable del primer Handler")
            handler!!.postDelayed(this, 1000)
            //Call the updateTime()
            tvDhour?.let { updateTime(it) }
        }
    }

    //To set the current hours worked every 2 seconds, we’re going to use the Handler() class and repeat the handler every  2 seconds (2000 milliseconds)
    private var handler2: Handler? = Handler()

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
            println("llega al val r: Runnable = object : Runnable del segundo Handler")
            handler2!!.postDelayed(this, 1000);
            //Call to setHoursWorked
            tvDhoursWorked?.let { setHoursWorked(it) }
        }
    };

    override fun FragmentHomeBinding.initialize() {
        println("HHOLA FROM HOME")
        this.sharedVM = sharedViewModel
        val treg = homeViewModel.getTodaysRegistry(sharedViewModel.employee.value!!.id)

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
            if (it.isEmpty()){
                println("Llega al IF: it.isEmpty()")
                Log.d("IFS","Llega al IF")
                this.buttonCheckOut.isEnabled = false
                this.buttonCheckIn.isEnabled = true
            }else{
                println("Llega al ELSE")
                if ((it.first().startedAt==null) and (it.first().endedAt==null)){
                    Log.d("IFS","Llega al primer IF dentro del ELSE")
                    this.buttonCheckOut.isEnabled = false
                    this.buttonCheckIn.isEnabled = true
                }else if ((it.first().startedAt!=null) and (it.first().endedAt!=null)){
                    Log.d("IFS","Llega al segundo IF dentro del ELSE")
                    this.buttonCheckOut.isEnabled = false
                    this.buttonCheckIn.isEnabled = false
                }
                else if ((it.first().startedAt!=null) and (it.first().endedAt==null)){
                    println("Llega al ELSE IF ULTIMO")
                    Log.d("IFS","Llega al cuarto IF dentro del ELSE")
                    this.buttonCheckOut.isEnabled = true
                    this.buttonCheckIn.isEnabled = false
                }

            }
        })

    }

    private fun onClickIn(v: Button,tvD: TextView){
        println("buttonCheckIn has been clicked")
        Toast.makeText(v.context, "buttonCheckIn has been clicked", Toast.LENGTH_SHORT).show()
        v.isSelected != v.isSelected

        //Create a new registry for today calling the checkIn method
        val reg= Registry(employeeId = sharedViewModel.employee.value!!.id, startedAt = LocalDateTime.now(), endedAt = null)
        homeViewModel.checkIn(reg)

        //Set the check in time and start counting the hours worked
        startCountingHoursWorked(reg.startedAt!!)
        setCheckInTime(tvD)

    }

    private fun onClickOut(v: Button,tvD: TextView){
        println("buttonCheckOut has been clicked")
        Toast.makeText(v.context, "buttonCheckOut has been clicked", Toast.LENGTH_SHORT).show()
        v.isSelected != v.isSelected
        //Update todays register calling homeViewModel.checkOut method
        //val reg= Registry(id= homeViewModel.tRegistry.value!!.first().id, employeeId = sharedViewModel.employee.value!!.id, startedAt = homeViewModel.tRegistry.value!!.first().startedAt, endedAt = LocalDateTime.now())
        //homeViewModel.checkOut(reg)
        homeViewModel.tRegistry.value!!.first().endedAt=LocalDateTime.now()
        homeViewModel.checkOut(homeViewModel.tRegistry.value!!.first())
        //Stop counting the hours worked and set the total hours worked
        stopCountingHoursWorked()
        setTotalHoursWorked(tvD, homeViewModel.tRegistry.value!!.first().endedAt!!)

        println("buttonCheckOut has been clicked and endedAt is ${homeViewModel.tRegistry.value!!.first().endedAt}")
        println("buttonCheckOut has been clicked and tRegistry is ${homeViewModel.tRegistry.value!!.first()}")
    }

    private fun setUserWelcomeMessage(tvN: TextView){
        Log.d("TVN","Llega a la función setUserWelcomeMessage el ${tvN.id}")
        tvN.setText("${sharedViewModel.employee.value!!.firstName} ${sharedViewModel.employee.value!!.lastName}, at your workplace")
    }

    private fun setMonthDay(tvD: TextView){
        Log.d("TVD","Llega a la función setMonthDay el ${tvD.id}")
        tvD.setText("${LocalDateTime.now().month.toString()} ${LocalDateTime.now().dayOfMonth.toString()}")
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
        Log.d("TVD","Llega a la función updateTime el ${tvD.id}")
        tvD.setText("Current time: ${LocalDateTime.now().hour.toString()}:${LocalDateTime.now().minute.toString()}:${LocalDateTime.now().second.toString()}")
    }

    private fun setCheckInTime(tvD: TextView){
        Log.d("TVD","Llega a la función setHoursWorked el ${tvD.id}")
        tvD.setText("You have started working at ${startedTime!!.hour.toString()} hours ${startedTime!!.minute.toString()} minutes and ${startedTime!!.second.toString()} seconds.")
    }

    private fun timeDifference(initialTime: LocalDateTime, finalTime: LocalDateTime): LocalTime {
        Log.d("DFS","Entra en la función timeDifference(initialTime: LocalDateTime, finalTime: LocalDateTime): LocalTime")
        var start = LocalDateTime.of(1971,1,1,initialTime.hour, initialTime.minute, initialTime.second)
        var stop = LocalDateTime.of(1971,1,1,finalTime.hour,finalTime.minute,finalTime.second)

        var fromTemp = LocalDateTime.from(start)
        val years = fromTemp.until(stop, ChronoUnit.YEARS)
        fromTemp = fromTemp.plusYears(years)

        val months = fromTemp.until(stop, ChronoUnit.MONTHS)
        fromTemp = fromTemp.plusMonths(months)

        val days = fromTemp.until(stop, ChronoUnit.DAYS)
        fromTemp = fromTemp.plusDays(days)

        val hours = fromTemp.until(stop, ChronoUnit.HOURS)
        fromTemp = fromTemp.plusHours(hours)

        val minutes = fromTemp.until(stop, ChronoUnit.MINUTES)
        fromTemp = fromTemp.plusMinutes(minutes)

        val seconds = fromTemp.until(stop, ChronoUnit.SECONDS)
        fromTemp = fromTemp.plusSeconds(seconds)

        val millis = fromTemp.until(stop, ChronoUnit.MILLIS)

        return LocalTime.of(hours.toInt(),minutes.toInt(),seconds.toInt())
    }

    private fun setHoursWorked(tvD: TextView){
        Log.d("TVD","Llega a la función setHoursWorked el ${tvD.id}")
        // startedTime -> contains the started time; TODO: udate with the hours worked
        var differenceTime = timeDifference(startedTime!!,LocalDateTime.now())
        tvD.setText("You have worked ${differenceTime.hour.toString()} hour ${differenceTime.minute.toString()} minutes and ${differenceTime.second.toString()} seconds so far.")
    }

    private fun setTotalHoursWorked(tvD: TextView, checkOutTime: LocalDateTime){
        Log.d("TVD","Llega a la función setTotalHoursWorked el ${tvD.id}")
        // startedTime -> contains the started time; TODO: udate with the hours worked
        var differenceTime = timeDifference(startedTime!!,checkOutTime!!)
        // You have worked today a total of 1 hour 8 minutes and 0 seconds.
        tvD.setText("You have worked today a total of ${differenceTime.hour.toString()} hour ${differenceTime.minute.toString()} minutes and ${differenceTime.second.toString()} seconds.")
    }

}