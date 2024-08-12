package mx.edu.utch.mdapp

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import mx.edu.utch.mdapp.databinding.ActivityMainBinding
import java.util.Collections

class MainActivity : AppCompatActivity() {
    private var turno: Boolean? = true
    private var puntosJugadorUno: Int? = 0
    private var puntosJugadorDos: Int = 0
    private var PrimeraCarta: ImageView? = null
    private var primeraImagen: Int? = 0
    private var clicked: Boolean? = true
    private var tiempo: Long? = 2000

    private var baraja = ArrayList<Int>(
        listOf(
            R.drawable.cloud,
            R.drawable.day,
            R.drawable.moon,
            R.drawable.night,
            R.drawable.rain,
            R.drawable.rainbow,
            R.drawable.cloud,
            R.drawable.day,
            R.drawable.moon,
            R.drawable.night,
            R.drawable.rain,
            R.drawable.rainbow
        )
    )
    private var imageView: ArrayList<ImageView>? = null
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        imageView = ArrayList(
            listOf(
                binding!!.lytCartas.im11, binding!!.lytCartas.im12,
                binding!!.lytCartas.im13, binding!!.lytCartas.im21,
                binding!!.lytCartas.im22, binding!!.lytCartas.im23,
                binding!!.lytCartas.im31, binding!!.lytCartas.im32,
                binding!!.lytCartas.im33, binding!!.lytCartas.im41, binding!!.lytCartas.im42,
                binding!!.lytCartas.im43
            )
        )

        binding!!.fabPrincipal.setOnClickListener {
    val dialogBuilder = AlertDialog.Builder(this)
    dialogBuilder.setTitle("Terminar juego")
    dialogBuilder.setMessage("¿Desea terminar el juego actual y comenzar uno nuevo o salir de la aplicación?")
    dialogBuilder.setPositiveButton("Nuevo Juego") { _, _ ->
        reiniciarJuego()
    }
    dialogBuilder.setNeutralButton("Salir") { _, _ ->
        finishAffinity()
    }
    dialogBuilder.setNegativeButton("Cancelar", null)
    dialogBuilder.show()
}

        setSupportActionBar(binding!!.mainBottomAppBar)
        Collections.shuffle(baraja)
        StartOn()
        ClickOn()
    }

    private fun ClickOn() {
        for (i in (0 until imageView!!.size)) {
            imageView!![i].setOnClickListener {
                imageView!![i].setImageResource(baraja[i])
                guardaClick(imageView!![i], baraja[i])
            }
        }
    }


    private fun guardaClick(imageView: ImageView, i: Int) {
        if (clicked!!) {
            PrimeraCarta = imageView
            primeraImagen = i
            PrimeraCarta!!.isEnabled = false
            clicked = !clicked!!
        } else {
            xctivar(false)
            var handler = Handler(Looper.getMainLooper())

            handler.postDelayed({
                if (primeraImagen == i) {
                    PrimeraCarta!!.visibility = View.INVISIBLE
                    imageView.visibility = View.INVISIBLE
                    if (turno!!) {
                        puntosJugadorUno = puntosJugadorUno!! + 1
                    } else {
                        puntosJugadorDos++
                    }
                    actualizarMarcador()
                    verificarGanador()
                } else {
                    PrimeraCarta!!.setImageResource(R.drawable.reverso)
                    imageView.setImageResource(R.drawable.reverso)
                    turno = !turno!!
                    PrimeraCarta!!.isEnabled = true
                    StartOn()
                }

                xctivar(true)
            }, 2000)

            clicked = !clicked!!
        }
    }

    private fun xctivar(b: Boolean) {
        for (i in (0 until imageView!!.size - 1)) {
            imageView!![i].isEnabled = b
        }
    }

    private fun StartOn() {
        if (turno!!) {
            binding!!.lytMarcador.mainActivityTvPlayer1.setTextColor(Color.MAGENTA)
            binding!!.lytMarcador.mainActivityTvPlayer2.setTextColor(Color.GREEN)
        } else {
            binding!!.lytMarcador.mainActivityTvPlayer1.setTextColor(Color.GREEN)
            binding!!.lytMarcador.mainActivityTvPlayer2.setTextColor(Color.MAGENTA)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.option_reiniciar -> {
                reiniciarJuego()
                true
            }

            R.id.option_terminar -> {
                mostrarDialogoTerminar()
                true
            }

            R.id.option_salir -> {
                finishAffinity()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_main_activity, menu)
        return true
    }

    private fun actualizarMarcador() {
        binding?.lytMarcador?.mainActivityTvPlayer1?.text = "Jugador 1: $puntosJugadorUno"
        binding?.lytMarcador?.mainActivityTvPlayer2?.text = "Jugador 2: $puntosJugadorDos"
    }

    private fun verificarGanador() {
    val totalCartas = baraja.size / 2
    if ((puntosJugadorUno?.plus(puntosJugadorDos)) ?: 0 == totalCartas) {
        if (puntosJugadorUno!! > puntosJugadorDos) {
            mostrarDialogoGanador("Jugador 1")
        } else if (puntosJugadorDos > puntosJugadorUno!!) {
            mostrarDialogoGanador("Jugador 2")
        } else {
            mostrarDialogoEmpate()
        }
    }
}

    private fun mostrarDialogoGanador(ganador: String) {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("¡Ganador!")
        dialogBuilder.setMessage("$ganador ha ganado la partida. ¿Desea iniciar una nueva partida?")
        dialogBuilder.setPositiveButton("Sí") { _, _ ->
            reiniciarJuego()
        }
        dialogBuilder.setNegativeButton("No", null)
        dialogBuilder.show()
    }

    private fun mostrarDialogoEmpate() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Empate")
        dialogBuilder.setMessage("La partida ha terminado en empate. ¿Desea iniciar una nueva partida?")
        dialogBuilder.setPositiveButton("Sí") { _, _ ->
            reiniciarJuego()
        }
        dialogBuilder.setNegativeButton("No", null)
        dialogBuilder.show()
    }

    private fun reiniciarJuego() {
        puntosJugadorUno = 0
        puntosJugadorDos = 0
        turno = true
        Collections.shuffle(baraja)
        for (imageView in imageView!!) {
            imageView.setImageResource(R.drawable.reverso)
            imageView.isEnabled = true
            imageView.visibility = View.VISIBLE
        }
        actualizarMarcador()
        StartOn()
    }

    private fun mostrarDialogoTerminar() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Terminar juego")
        dialogBuilder.setMessage("Puntaje final:\nJugador 1: $puntosJugadorUno\nJugador 2: $puntosJugadorDos\n\n¿Desea salir del juego?")
        dialogBuilder.setPositiveButton("Sí") { _, _ ->
            finishAffinity()
        }
        dialogBuilder.setNegativeButton("No", null)
        dialogBuilder.show()
    }

}