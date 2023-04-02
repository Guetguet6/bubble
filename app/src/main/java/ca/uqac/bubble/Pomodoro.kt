package ca.uqac.bubble

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity

class Pomodoro : ComponentActivity() {

    private lateinit var timeDisplay: TextView
    private lateinit var startButton: Button
    private lateinit var pauseResumeButton: Button
    private lateinit var resetButton: Button
    private var timer: CountDownTimer? = null
    private var isRunning = false
    private var isPaused = false
    private var elapsedTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pomodoro)

        timeDisplay = findViewById(R.id.time_display)
        startButton = findViewById(R.id.start_button)
        pauseResumeButton = findViewById(R.id.pause_resume_button)
        resetButton = findViewById(R.id.reset_button)

        startButton.setOnClickListener {
            if (!isRunning) {
                startTimer()
                startButton.visibility = View.GONE
                pauseResumeButton.visibility = View.VISIBLE
            }
        }

        pauseResumeButton.setOnClickListener {
            if (isRunning && !isPaused) {
                pauseTimer()
                pauseResumeButton.text = "Resume"
                resetButton.visibility = View.VISIBLE
                isPaused = true
            } else if (isRunning && isPaused) {
                resumeTimer()
                pauseResumeButton.text = "Pause"
                resetButton.visibility = View.GONE
                isPaused = false
            }
        }

        resetButton.setOnClickListener {
            stopTimer()
            startButton.visibility = View.VISIBLE
            pauseResumeButton.visibility = View.GONE
            resetButton.visibility = View.GONE
            isRunning = false
            isPaused = false
        }

        timeDisplay.text = "00:00:00"
        pauseResumeButton.visibility = View.GONE
        resetButton.visibility = View.GONE
    }

    private fun startTimer() {
        timer = object : CountDownTimer(Long.MAX_VALUE, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                elapsedTime += 1000
                updateTimerDisplay()
            }

            override fun onFinish() {
                stopTimer()
                startButton.visibility = View.VISIBLE
                pauseResumeButton.visibility = View.GONE
                resetButton.visibility = View.GONE
                isRunning = false
                isPaused = false
            }
        }
        timer?.start()
        isRunning = true
    }

    private fun pauseTimer() {
        timer?.cancel()
    }

    private fun resumeTimer() {
        startTimer()
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