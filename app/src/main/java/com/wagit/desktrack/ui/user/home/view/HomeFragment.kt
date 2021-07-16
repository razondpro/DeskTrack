package com.wagit.desktrack.ui.user.home.view

import android.R.attr.button
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.wagit.desktrack.R
import com.wagit.desktrack.databinding.FragmentHomeBinding
import com.wagit.desktrack.ui.BaseFragment
import com.wagit.desktrack.ui.user.home.viewmodel.HomeViewModel
import com.wagit.desktrack.ui.user.viewmodel.SharedHomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.Observer
import com.wagit.desktrack.data.entities.Registry
import java.time.LocalDateTime


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val sharedViewModel: SharedHomeViewModel by activityViewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    override fun FragmentHomeBinding.initialize() {
        println("HHOLA FROM HOME")
        this.sharedVM = sharedViewModel
        //CHECK IN/OUT HABILITADO/DESHABILITADO SI STARTED_AT Y ENDED_AT ES IGUAL A NULL
        // EN CASO CONTRARIO CHECK IN/OUT HABILITADO/DESHABILITADO
        //val treg = homeViewModel.getTodaysRegistry(sharedViewModel.employee.value!!.id)
        /*
        println(treg)
        homeViewModel.tRegistry.observe(viewLifecycleOwner, Observer {
            println("--------")
            println(it.first())
            println(treg)
            println("--------")
        })
         */
        //this.textView.setText("CAMBIO")

        this.buttonCheckIn.setOnClickListener(View.OnClickListener {
            Log.d("Onclick","Llega al setonclick")
            // Code here executes on main thread after user presses button
            //println("buttonCheckIn has been clicked")
            //Toast.makeText(it.context, "buttonCheckIn has been clicked", Toast.LENGTH_SHORT).show()
            //this.buttonCheckOut.isEnabled = true
            //onClick(it)
            onClickIn(this.buttonCheckIn)
            this.tvRegister.text = "buttonCheckIn has been clicked"
            //Crear un registry de hoy llamando al checkIn
            //val reg= Registry(employeeId = sharedViewModel.employee.value!!.id, startedAt = LocalDateTime.now(), endedAt = null)
            //homeViewModel.checkIn(reg)
        })

        this.buttonCheckOut.setOnClickListener(View.OnClickListener {
            // Code here executes on main thread after user presses button
            //println("buttonCheckOut has been clicked")
            //Toast.makeText(it.context, "buttonCheckOut has been clicked", Toast.LENGTH_SHORT).show()
            //onClick(it)
            onClickOut(this.buttonCheckOut)
            //this.buttonCheckIn.isEnabled = true
            this.tvRegister.text = "buttonCheckOut has been clicked"
        })
        val treg = homeViewModel.getTodaysRegistry(sharedViewModel.employee.value!!.id)
        //Detectar si hay cambio en el livedata, si hay un elemento en el it.first activar el boton checkIn y no en caso contrario
        homeViewModel.tRegistry.observe(viewLifecycleOwner, Observer {
            println("El primer item si existe: ${it.first()}")
            println("El treg: ${treg.value}")
            println("¿No Hay ningun registro? ${it.isEmpty()}")
            //HABILITAR/DESHABILITAR -> LOS DOS BOTONES = ESTO SE EJECUTA AL CAMBIAR EL REGISTRO

            if (it.isEmpty()){
                println("Llega al IF: it.isEmpty()")
                Log.d("IFS","Llega al IF")
                this.buttonCheckOut.isEnabled = false
                this.buttonCheckIn.isEnabled = true
                this.buttonCheckOut.isSelected != this.buttonCheckOut.isSelected
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

        println("Todays registry is ${treg.value}")
        println("Todays first registry is ${treg.value?.first()}")


    }

//TODO: añadir la funcionalidad de CHECK IN/OUT si se hace clic o no
/*
override fun onCreateView(
inflater: LayoutInflater, container: ViewGroup?,
savedInstanceState: Bundle?
): View? {
    val view = inflater.inflate(R.layout.fragment_home, container,false)
    //onClick(view);
    Log.d("HomeFagment", "HHOLA FROM HOME FRAGMENT");
    println("The id of the object is ${view}")

    return view
}
 */

private fun onClickIn(v: Button){
    println("buttonCheckIn has been clicked")
    Toast.makeText(v.context, "buttonCheckIn has been clicked", Toast.LENGTH_SHORT).show()
    v.isSelected != v.isSelected
    //v.isEnabled = false
    //Crear un registry de hoy llamando al checkIn
    val reg= Registry(employeeId = sharedViewModel.employee.value!!.id, startedAt = LocalDateTime.now(), endedAt = null)
    homeViewModel.checkIn(reg)
    println("buttonCheckIn has been clicked and tRegistry is ${homeViewModel.tRegistry.value!!.first()}")
}
private fun onClickOut(v: Button){
    println("buttonCheckOut has been clicked")
    Toast.makeText(v.context, "buttonCheckOut has been clicked", Toast.LENGTH_SHORT).show()
    v.isSelected != v.isSelected
    //v.isEnabled = false
    //Actualizar el registro de hoy llamando al checkOut
    homeViewModel.tRegistry.value!!.first().endedAt=LocalDateTime.now()
    homeViewModel.checkOut(homeViewModel.tRegistry.value!!.first())
    println("buttonCheckOut has been clicked and tRegistry is ${homeViewModel.tRegistry.value!!.first()}")
}
/*
private fun onClick(v: View,b: Button) {
    println("Llega al onclick")
    when (v.id) {
        R.id.buttonCheckOut -> {
            println("buttonCheckIn has been clicked")
            Toast.makeText(v.context, "buttonCheckIn has been clicked", Toast.LENGTH_SHORT).show()
            v.isSelected != v.isSelected
            //v.isEnabled = false
            //Crear un registry de hoy llamando al checkIn
            val reg= Registry(employeeId = sharedViewModel.employee.value!!.id, startedAt = LocalDateTime.now(), endedAt = null)
            homeViewModel.checkIn(reg)
            println("buttonCheckIn has been clicked and tRegistry is ${homeViewModel.tRegistry.value!!.first()}")
        }
        R.id.buttonCheckOut -> {
            println("buttonCheckOut has been clicked")
            Toast.makeText(v.context, "buttonCheckOut has been clicked", Toast.LENGTH_SHORT).show()
            v.isSelected != v.isSelected
            //v.isEnabled = false
            //Actualizar el registro de hoy llamando al checkOut
            homeViewModel.tRegistry.value!!.first().endedAt=LocalDateTime.now()
            homeViewModel.checkOut(homeViewModel.tRegistry.value!!.first())
            println("buttonCheckOut has been clicked and tRegistry is ${homeViewModel.tRegistry.value!!.first()}")
        }
    }
}
 */


}