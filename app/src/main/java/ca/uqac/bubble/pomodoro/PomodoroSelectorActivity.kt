package ca.uqac.bubble.pomodoro

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ca.uqac.bubble.ui.theme.BubbleAppTheme
import androidx.compose.material3.*
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.unit.sp


class PomodoroSelectorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BubbleAppTheme {
                Column (
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {

                    var numberCycles by remember { mutableStateOf("4") }
                    var activeCycle by remember { mutableStateOf("25") }
                    var breakCycle by remember { mutableStateOf("5") }

                    PomodoroActivityLabel()
                    Column {
                        ActivityCycleSelector(duration = activeCycle, changeDuration = { newDuration -> activeCycle = newDuration })
                        Spacer(modifier = Modifier.height(16.dp))
                        BreakCycleSelector(duration = breakCycle, changeDuration = { newDuration -> breakCycle = newDuration })
                        Spacer(modifier = Modifier.height(16.dp))
                        NumberCyclesSelector(duration = numberCycles, changeDuration = { newNumber -> numberCycles = newNumber })
                    }
                    StartButton(ActiveCycle = activeCycle, BreakCycle = breakCycle, NumberCycles = "") {
                        val intent = Intent(this@PomodoroSelectorActivity, PomodoroActivity::class.java)
                        intent.putExtra("activeCycle", activeCycle)
                        intent.putExtra("breakCycle", breakCycle)
                        intent.putExtra("numberCycles", numberCycles)
                        startActivity(intent)
                    }
                }
            }
        }
    }


    @Composable
    fun PomodoroActivityLabel() {
        Text(
            text = "Pomodoro Selector",
            textAlign = TextAlign.Center,
            fontSize = 32.sp
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ActivityCycleSelector(duration: String, changeDuration: (String) -> Unit) {
        Row (verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Activity duration: ", fontSize = 20.sp)
            TextField(
                value = duration,
                onValueChange = changeDuration,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle = LocalTextStyle.current.copy(fontSize = 16.sp, textAlign = TextAlign.Center),
                modifier = Modifier.width(70.dp)

            )
            Text(text = " min.", fontSize = 20.sp)

        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun BreakCycleSelector(duration: String, changeDuration: (String) -> Unit) {
        Row (verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Break duration: ", fontSize = 20.sp)
            TextField(
                value = duration,
                onValueChange = changeDuration,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle = LocalTextStyle.current.copy(fontSize = 16.sp, textAlign = TextAlign.Center),
                modifier = Modifier.width(70.dp)

            )
            Text(text = " min.", fontSize = 20.sp)

        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun NumberCyclesSelector(duration: String, changeDuration: (String) -> Unit) {
        Row (verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Number of cycles: ", fontSize = 20.sp)
            TextField(
                value = duration,
                onValueChange = changeDuration,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle = LocalTextStyle.current.copy(fontSize = 16.sp, textAlign = TextAlign.Center),
                modifier = Modifier.width(70.dp)

            )

        }
    }

    @Composable
    fun StartButton(ActiveCycle: String, BreakCycle: String, NumberCycles: String, onclick: () -> Unit) {
        Button(
            onClick = onclick
        ) {
            Text(text = "Start Pomodoro", fontSize = 16.sp)
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        BubbleAppTheme {
            Column (
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                PomodoroActivityLabel()
                Column {
                    ActivityCycleSelector(duration = "25", changeDuration = {})
                    Spacer(modifier = Modifier.height(16.dp))
                    BreakCycleSelector(duration = "5", changeDuration = {})
                    Spacer(modifier = Modifier.height(16.dp))
                    NumberCyclesSelector(duration = "4", changeDuration = {})
                }
                StartButton(ActiveCycle = "", BreakCycle = "", NumberCycles = "") {}
            }
        }
    }
}
