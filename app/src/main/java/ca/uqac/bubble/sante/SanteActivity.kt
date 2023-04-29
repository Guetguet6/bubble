package ca.uqac.bubble.sante

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ca.uqac.bubble.sante.liste.Categorie
import ca.uqac.bubble.sante.liste.DataProvider
import ca.uqac.bubble.ui.theme.BubbleAppTheme
import ca.uqac.bubble.R
import ca.uqac.bubble.sante.liste.CoherenceCardiaqueActivity

class SanteActivity : ComponentActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BubbleAppTheme(darkTheme = false) {
                Surface{
                    Column {
                    Text(text = "Sante")
                    Content()
                    }
                }
            }
        }
    }

    @Composable
    fun Content(){
        val liste = remember {
            DataProvider.liste
        }
        LazyColumn {
            items(
                items = liste,
                itemContent = {
                    SanteItem(Sante = it)
                })
        }
    }


    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
    @Composable
    fun SanteItem(Sante : Categorie) {
        Card (
            onClick = {
                if (Sante.classPath != null) {
                    startActivity(Intent(this, Sante.classPath))
                }
            },
            enabled = Sante.classPath != null,
            modifier = Modifier
                .padding(20.dp)
        ){
            Column (
                modifier = Modifier.padding(10.dp)
            ){
                Image(
                    painter = painterResource(id = Sante.imgagePath),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.height(100.dp),
                    alignment = Alignment.Center
                )
                Text(text = Sante.title,
                    style = typography.h5,
                    textAlign = TextAlign.Center)
                Text(text = Sante.description,
                    style = typography.caption,
                    textAlign = TextAlign.Justify)
            }
        }
    }

    @Preview
    @Composable
    fun previewInt() {
        BubbleAppTheme {
            Content()
        }
    }
}