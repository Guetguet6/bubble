package ca.uqac.bubble


import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import ca.uqac.bubble.sante.SanteAdapter
import ca.uqac.bubble.ui.theme.BubbleAppTheme



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BubbleAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    buttons()
                }
            }
            /* setContentView(R.layout.activity_main)

        val calendrierbutton = findViewById<Button>(R.id.calendrier_btn)
        calendrierbutton.setOnClickListener{
            val Intent = Intent(this,Calendrier::class.java)
            startActivity(Intent)
        }*/
        }
    }
}

@Composable
fun buttons(){
    val context = LocalContext.current;


    Column {
        Button(onClick = {
            val intent = Intent(context, Calendrier::class.java)
            context.startActivity(intent)
        }) {
            Text(text = "Calendrier")
        }

        Button(onClick = {
            val intent = Intent(context, SanteAdapter::class.java)
            context.startActivity(intent)
        }) {
            Text(text = "Santé")
        }



    }
}