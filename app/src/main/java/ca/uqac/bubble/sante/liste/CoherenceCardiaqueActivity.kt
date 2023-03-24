package ca.uqac.bubble.sante.liste

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import ca.uqac.bubble.buttons
import ca.uqac.bubble.ui.theme.BubbleAppTheme

class CoherenceCardiaqueActivity : ComponentActivity() {
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
                        Text(text = "Je suis une Cohérence Cardiaque")
                    }
                }
            }
        } catch (e: Exception){
            e.printStackTrace();
        }
    }
}