package ca.uqac.bubble.sante

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import ca.uqac.bubble.R
import ca.uqac.bubble.sante.liste.CoherenceCardiaqueActivity
import ca.uqac.bubble.ui.theme.BubbleAppTheme

class SanteActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BubbleAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        Text(text = "Santé")
                        Content(null)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Content(cont : List<String>?){
    val context = LocalContext.current
    LazyColumn {
        try {
            item {
                Card(
                    onClick = {
                        val intent = Intent(context, CoherenceCardiaqueActivity::class.java)
                        context.startActivity(intent)
                    }
                ) {
                    Image(
                        painterResource(id = R.drawable.plage),
                        contentDescription = "",
                        modifier = Modifier.size(height = 10.dp, width = 20.dp)
                    )
                    Log.d("Info", "Je fonctionne")
                }
            }
        } catch (e:Exception){
            e.printStackTrace()
        }
        items(5) { index -> Text(text = "Hello World")}
    }
}