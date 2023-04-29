package ca.uqac.bubble.pomodoro

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import ca.uqac.bubble.R

class PomodoroActivity : ComponentActivity() {

    private lateinit var timeDisplay: TextView
    private lateinit var phaseDisplay: TextView
    private lateinit var cyclesDisplay: TextView
    private lateinit var pauseResumeButton: Button
    private lateinit var stopButton: Button
    private var timer: CountDownTimer? = null
    private var isRunning = false
    private var isPaused = false
    private var isPomodoroPhase = true
    private var remainingTime = 120_000L // 120 seconds
    private var pomodoroPhase = 1500000L // 25 minutes in milliseconds
    private var breakPhase = 300000L // 5 minutes in milliseconds
    private var numberCycles = 4 // 4 cycles per pomodoro session by default

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pomodoro)

        val extras = intent.extras
        if (extras != null) {
            pomodoroPhase = (extras.getString("activeCycle")?.toLong() ?: pomodoroPhase) * 60000
            breakPhase = (extras.getString("breakCycle")?.toLong() ?: breakPhase) * 60000
            numberCycles = extras.getString("numberCycles")?.toInt() ?: numberCycles
        }

        remainingTime = pomodoroPhase

        timeDisplay = findViewById(R.id.time_display)
        pauseResumeButton = findViewById(R.id.pause_resume_button)
        stopButton = findViewById(R.id.stop_button)
        phaseDisplay = findViewById(R.id.phase_display)
        cyclesDisplay = findViewById(R.id.cycles_display)

        updateTimerDisplay()
        updatePhaseDisplay()
        updateCyclesDisplay()

        pauseResumeButton.setOnClickListener {
            if (isRunning) {
                pauseTimer()
                pauseResumeButton.text = "Resume"
                stopButton.visibility = View.VISIBLE
                isRunning = false
            } else if (!isRunning) {
                resumeTimer()
                pauseResumeButton.text = "Pause"
                stopButton.visibility = View.GONE
                isRunning = true
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
                if (isPomodoroPhase && numberCycles > 0) {
                    isPomodoroPhase = false
                    remainingTime = breakPhase
                    updatePhaseDisplay()
                    resumeTimer()
                } else if (!isPomodoroPhase && numberCycles > 0) {
                    isPomodoroPhase = true
                    remainingTime = pomodoroPhase
                    numberCycles--
                    updatePhaseDisplay()
                    updateCyclesDisplay()
                    resumeTimer()
                } else {
                    stopTimer()
                    isRunning = false
                    isPaused = false
                    finish()
                }
            }
        }
        timer?.start()
        isRunning = true
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

    private fun updatePhaseDisplay() {
        if (isPomodoroPhase) {
            phaseDisplay.text = "Pomodoro phase"
        } else {
            phaseDisplay.text = "Break phase"
        }
    }

    private fun updateCyclesDisplay() {
        cyclesDisplay.text = "Cycles left: $numberCycles"
    }



}
