package ca.uqac.bubble.pomodoro

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ca.uqac.bubble.R
import ca.uqac.bubble.ui.theme.BubbleAppTheme

class PomodoroActivity : ComponentActivity() {

    private lateinit var timeDisplay: TextView
    private lateinit var pauseResumeButton: Button
    private lateinit var stopButton: Button
    private var timer: CountDownTimer? = null
    private var isRunning = false
    private var isPaused = false
    private var remainingTime = 120_000L // 120 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pomodoro)

        timeDisplay = findViewById(R.id.time_display)
        pauseResumeButton = findViewById(R.id.pause_resume_button)
        stopButton = findViewById(R.id.stop_button)

        updateTimerDisplay()

        pauseResumeButton.setOnClickListener {
            if (isRunning && !isPaused) {
                pauseTimer()
                pauseResumeButton.text = "Resume"
                stopButton.visibility = View.VISIBLE
                isPaused = true
            } else if (isRunning && isPaused) {
                resumeTimer()
                pauseResumeButton.text = "Pause"
                stopButton.visibility = View.GONE
                isPaused = false
            }
        }

        stopButton.setOnClickListener {
            stopTimer()
            isRunning = false
            isPaused = false
            finish()
        }

        pauseResumeButton.visibility = View.VISIBLE
        stopButton.visibility = View.GONE

        resumeTimer()
    }

    private fun resumeTimer() {
        timer = object : CountDownTimer(remainingTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTime = millisUntilFinished
                updateTimerDisplay()
            }

            override fun onFinish() {
                stopTimer()
                isRunning = false
                isPaused = false
                finish()
            }
        }
        timer?.start()
        isRunning = true
    }

    private fun updateTimerDisplay(timeDisplay: TextView, remainingTime: Long) {
        val seconds = (remainingTime / 1000) % 60
        val minutes = (remainingTime / (1000 * 60)) % 60
        val hours = remainingTime / (1000 * 60 * 60)
        timeDisplay.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    private fun pauseTimer() {
        timer?.cancel()
    }


    private fun stopTimer() {
        timer?.cancel()
        timer = null
        remainingTime = 0
        updateTimerDisplay()
    }

    private fun updateTimerDisplay() {
        val seconds = (remainingTime / 1000) % 60
        val minutes = (remainingTime / (1000 * 60)) % 60
        val hours = remainingTime / (1000 * 60 * 60)
        timeDisplay.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

}
