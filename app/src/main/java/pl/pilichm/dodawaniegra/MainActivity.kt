package pl.pilichm.dodawaniegra

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import pl.pilichm.dodawaniegra.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var mCurrentAmount: Int = 0
    private var mCorrectAnswerCount: Int = 0
    private var mIncorrectAnswerCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpListeners()
    }

    private fun setUpListeners(){
        /**
         * Start game, hide start button, start timer and display first question.
         * */
        binding.tvStart.setOnClickListener {
            binding.tvStart.visibility = View.INVISIBLE
            binding.glAnswers.visibility = View.VISIBLE
            startGame()
        }

        /**
         * Set up listeners to answers text views. Background of correct answer will be set
         * to green. If user selects incorrect answer its background will be set to red.
         * */
        for (position in 1..4) {
            val tvAnswer = binding.glAnswers.findViewWithTag<TextView>(position.toString())
            /**
             * Check and color correct and incorrect answer text view.
             * */
            tvAnswer.setOnClickListener {
                binding.tvShowResultDescription.visibility = View.VISIBLE
                if (tvAnswer.text.equals(mCurrentAmount.toString())){
                    mCorrectAnswerCount++
                    tvAnswer.setBackgroundColor(resources.getColor(R.color.correct_answer_background))
                    binding.tvShowResultDescription.text = resources.getString(R.string.answer_correct)
                } else {
                    mIncorrectAnswerCount++
                    tvAnswer.setBackgroundColor(resources.getColor(R.color.incorrect_answer_background))
                    binding.tvShowResultDescription.text = resources.getString(R.string.answer_incorrect)
                    for (position in 1..4) {
                        val answerView = binding.glAnswers.findViewWithTag<TextView>(position.toString())
                        if (answerView.text.equals(mCurrentAmount.toString())) {
                            answerView.setBackgroundColor(resources.getColor(R.color.correct_answer_background))
                        }
                    }
                }
                /**
                 * Make answer view un clickable.
                 * */
                for (position in 1..4) {
                    binding.glAnswers.isClickable = false
                    binding.glAnswers.isFocusable = false
                }

                binding.tvCorrectIncorrectRatio.text = "$mCorrectAnswerCount/$mIncorrectAnswerCount "
                Handler(Looper.getMainLooper()).postDelayed({
                    /**
                     * Reset background, make answers clickable and display next question.
                     * */
                    for (position in 1..4){
                        val answerView = binding.glAnswers.findViewWithTag<TextView>(position.toString())
                        answerView.isClickable = true
                        answerView.isFocusable = true
                        answerView.setBackgroundColor(resources.getColor(R.color.start_button_color))
                        setUpQuestion()
                    }
                }, NEXT_QUESTION_DELAY)
            }
        }
    }

    /**
     * Function for generating and displaying question with random values.
     * */
    private fun setUpQuestion(){
        val firstNumber = (Q_MIN_VALUE..Q_MAX_VALUE).random()
        val secondNumber = (Q_MIN_VALUE..Q_MAX_VALUE).random()
        val correctPosition = (1..4).random()

        mCurrentAmount = firstNumber + secondNumber

        binding.tvCurrentAddition.text = "$firstNumber + $secondNumber"
        val correctAnswerTextView =
            binding.glAnswers.findViewWithTag<TextView>(correctPosition.toString())
        correctAnswerTextView.text = (mCurrentAmount).toString()

        for (position in 1..4){
            if (position!=correctPosition){
                val tvAnswer = binding.glAnswers.findViewWithTag<TextView>(position.toString())
                tvAnswer.text = (0..2*mCurrentAmount).random().toString()
            }
        }
    }

    /**
     * Displays first question and the starts game timer.
     * */
    private fun startGame(){
        setUpQuestion()
        binding.tvTimeRemaining.text = "${TIMER_TIME/ TIMER_TICK} ${resources.getString(R.string.seconds_suffix)}"
        mCorrectAnswerCount = 0
        mIncorrectAnswerCount = 0
        binding.tvCorrectIncorrectRatio.text = "$mCorrectAnswerCount/$mIncorrectAnswerCount "

        object : CountDownTimer(TIMER_TIME, TIMER_TICK){
            override fun onTick(millisUntilFinished: Long) {
                binding.tvTimeRemaining.text =
                    "${millisUntilFinished/ TIMER_TICK} ${resources.getString(R.string.seconds_suffix)}"
            }

            override fun onFinish() {
                binding.tvStart.visibility = View.VISIBLE
                binding.glAnswers.visibility = View.INVISIBLE
                binding.tvStart.text = resources.getString(R.string.button_restart)
                binding.tvShowResultDescription.visibility = View.INVISIBLE
            }
        }.start()
    }

    companion object {
        const val Q_MIN_VALUE = 0
        const val Q_MAX_VALUE = 50
        const val TIMER_TIME = 10_000L
        const val TIMER_TICK = 1_000L
        const val NEXT_QUESTION_DELAY = 550L
    }
}