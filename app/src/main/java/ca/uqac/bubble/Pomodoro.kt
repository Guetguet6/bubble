package ca.uqac.bubble

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity

class Pomodoro : ComponentActivity() {

    private lateinit var timeDisplay: TextView
    private lateinit var startStopButton: Button
    private var timer: CountDownTimer? = null
    private var isRunning = false
    private var elapsedTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pomodoro)

        timeDisplay = findViewById(R.id.time_display)
        startStopButton = findViewById(R.id.start_stop_button)

        startStopButton.setOnClickListener {
            if (!isRunning) {
                startTimer()
                startStopButton.text = "Stop"
            } else {
                stopTimer()
                startStopButton.text = "Start"
            }
            isRunning = !isRunning
        }
    }

    private fun startTimer() {
        timer = object : CountDownTimer(Long.MAX_VALUE, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                elapsedTime += 1000
                updateTimerDisplay()
            }

            override fun onFinish() {
                // do nothing
            }
        }
        timer?.start()
    }

    private fun stopTimer() {
        timer?.cancel()
        timer = null
        elapsedTime = 0L
        updateTimerDisplay()
    }

    private fun updateTimerDisplay() {
        val seconds = (elapsedTime / 1000) % 60
        val minutes = (elapsedTime / (1000 * 60)) % 60
        val hours = elapsedTime / (1000 * 60 * 60)
        timeDisplay.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

}
