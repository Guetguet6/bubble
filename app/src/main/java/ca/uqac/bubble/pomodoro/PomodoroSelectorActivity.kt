package ca.uqac.bubble.pomodoro

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ca.uqac.bubble.ui.theme.BubbleAppTheme

class PomodoroSelectorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BubbleAppTheme {
                PomodoroSelectorScreen()
            }
        }
    }

    @Composable
    fun PomodoroSelectorScreen() {
        Box(
            modifier = Modifier.padding(16.dp).fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Text(
                        text = "Pomodoro Selector",
                        modifier = Modifier.padding(bottom = 16.dp),
                        style = MaterialTheme.typography.h5,
                        textAlign = TextAlign.Center
                    )
                }
                Button(
                    onClick = { startPomodoro() },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text(text = "Start Pomodoro")
                }
            }
        }
    }
    private fun startPomodoro() {
        val intent = Intent(this, PomodoroActivity::class.java)
        startActivity(intent)
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        BubbleAppTheme {
            Surface (
                modifier = Modifier.fillMaxSize()
            ) {
                PomodoroSelectorScreen()
            }
        }
    }
}
