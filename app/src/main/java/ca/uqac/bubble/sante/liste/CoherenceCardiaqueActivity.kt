package ca.uqac.bubble.sante.liste

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Popup
import ca.uqac.bubble.ui.theme.BubbleAppTheme
import android.graphics.Canvas
import android.media.MediaPlayer
import android.os.CountDownTimer
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.animation.*
import androidx.compose.animation.core.AnimationVector
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateSizeAsState
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import java.lang.reflect.TypeVariable
import androidx.compose.animation.core.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.compose.material.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.BrokenImage
import androidx.compose.material.icons.outlined.Image
import androidx.compose.ui.platform.LocalContext
import ca.uqac.bubble.R


class CoherenceCardiaqueActivity(
    var tutorial: Boolean = true
) : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContent {
                BubbleAppTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        Column {
                            TopApp()
                            Content()
                        }
                    }
                }
            }
        } catch (e: Exception){
            e.printStackTrace();
        }
    }

    @Composable
    fun Info(t: Unit) {
        Surface (
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = "Je suis une info")
        }
    }

    @Composable
    fun Content(){
        Surface (
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircleUI()
                sond()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    @Composable
    fun CircleUI(){
        var isStarted by remember {
            mutableStateOf(false)
        }
        val StateSeance : String = if (!isStarted) "Commencer la Séance !" else "Arrêter la Séance"

        val width : Float = LocalConfiguration.current.screenWidthDp.toFloat()
        val transition = rememberInfiniteTransition()
        val sizeTransition by transition.animateFloat(
            initialValue = width,
            targetValue = if (isStarted) 300f else width,
            animationSpec = infiniteRepeatable(
                repeatMode = RepeatMode.Reverse,
                animation = tween(5000)
            )
        )

        var StateRespi by remember { mutableStateOf("Inspire") }
        val countDownTimer = remember {
            object : CountDownTimer(5000, 1000) {
                override fun onFinish() {
                    StateRespi = if (StateRespi == "Inspire") "Expire" else "Inspire"
                    this.start()
                }

                override fun onTick(millisUntilFinished: Long) {}
            }
        }

        countDownTimer.start()

        Column (
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(bottom = 50.dp)
        ){
            Text(text = if (isStarted) StateRespi else "")
            Canvas(
                modifier = Modifier
                    .size(size = width.dp)
            ) {
                drawCircle(
                    color = Color.Blue,
                    radius = sizeTransition
                )
            }

            Button(
                onClick = { isStarted = !isStarted }
            ) {
                Text(text = StateSeance)
            }
        }
    }


    @Composable
    fun TopApp(){
        TopAppBar (
            title = { Text(text = "Cohérence Cardiaque")},
            navigationIcon = {
                IconButton(onClick = {
                    Toast.makeText(this,"Back",Toast.LENGTH_SHORT).show()
                }) {
                    Icon(Icons.Filled.ArrowBack, "Back")
                }
            },
            actions = {
                IconButton(onClick = {  }) {
                    Icon(Icons.Filled.Info,"Info")
                }
            }
        )
    }

    @Composable
    fun sond() {
        var isPlaying by remember { mutableStateOf(false) }
        val mediaPlayer = remember { MediaPlayer.create(this, R.raw.meditation).apply {
            isLooping = true
        } }
        Button(onClick = {
            isPlaying = !isPlaying
            if (isPlaying) {
                mediaPlayer.start()
            } else {
                mediaPlayer.pause()
            }
        }) {
            if (isPlaying) {
                Icon(Icons.Outlined.Image, "Mute" )
            } else {
                Icon(Icons.Outlined.BrokenImage, "Unmute")
            }
        }
    }

    @Preview
    @Composable
    fun Preview(){
        BubbleAppTheme {
            Surface (
                modifier = Modifier.fillMaxSize()
            ) {
                Column(){
                    TopApp()
                    Content()
                }
            }
        }
    }

}