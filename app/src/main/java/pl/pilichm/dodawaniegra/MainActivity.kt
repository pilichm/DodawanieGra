package pl.pilichm.dodawaniegra

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpListeners()
    }

    private fun setUpListeners(){
        /**
         * Start game, hide start button, start timer and display first question.
         * */
        tvStart.setOnClickListener {
            tvStart.visibility = View.INVISIBLE
            frAnswers.visibility = View.VISIBLE
        }
    }


}