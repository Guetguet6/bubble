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
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.animation.*
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateSizeAsState
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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

class CoherenceCardiaqueActivity(
    var tutorial: Boolean = true
) : ComponentActivity() {
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
                        Content()
                    }
                }
            }
        } catch (e: Exception){
            e.printStackTrace();
        }
    }

    @Composable
    fun Info() {
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
                verticalArrangement = Arrangement.spacedBy(100.dp),


            ) {
                Button(
                    onClick = {  },
                    modifier = Modifier
                        .align(Alignment.End),
                    shape = CircleShape,
                    border = BorderStroke(1.dp, Color.Transparent),
                ) {
                    Image(
                        painter = painterResource(id = android.R.drawable.ic_menu_info_details),
                        contentDescription = null,
                    )
                }
                CircleUI()
                
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
        val text : String = if (!isStarted) "Commencer la Séance !" else "Arrêter la Séance"

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
        Column (
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(bottom = 50.dp)
        ){
            Canvas(
                modifier = Modifier
                    .size(size = (LocalConfiguration.current.screenWidthDp/2).dp)
            ) {
                drawCircle(
                    color = Color.Blue,
                    radius = sizeTransition
                )
            }

            Button(
                onClick = { isStarted = !isStarted }
            ) {
                Text(text = text)
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
                Content()
            }
        }
    }
}