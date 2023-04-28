package ca.uqac.bubble

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import ca.uqac.bubble.Calendrier.CalendrierActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.res.painterResource
import ca.uqac.bubble.pomodoro.PomodoroSelectorActivity
import ca.uqac.bubble.profil.ProfileActivity
import ca.uqac.bubble.sante.SanteActivity
import ca.uqac.bubble.todolist.ToDoListActivity
import ca.uqac.bubble.ui.theme.BubbleAppTheme
import ca.uqac.bubble.ui.theme.Topbar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BubbleAppTheme {
                // A surface container using the 'background' color from the theme
                Surface (
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colors.background,

                ) {
                    Column {
                        TopBar()
                        Text(
                            text = "Bubble",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                        buttons()
                    }
                }
            }
        }/*
        setContentView(R.layout.activity_main)

        val calendrierbutton = findViewById<Button>(R.id.calendrier_btn)
        calendrierbutton.setOnClickListener {
            val Intent = Intent(
                this,
                CalendrierActivity::class.java
            )
            startActivity(Intent)
        }

        val profilbutton = findViewById<Button>(R.id.profil_btn)
        profilbutton.setOnClickListener {
            val Intent = Intent(this, ProfileActivity::class.java)
            startActivity(Intent)
        }

        val toDoListButton = findViewById<Button>(R.id.toDoList_btn)
        toDoListButton.setOnClickListener {
            val intent = Intent(this, ToDoListActivity::class.java)
            startActivity(intent)
        }

        val pomodoroButton = findViewById<Button>(R.id.pomodoro_btn)
        pomodoroButton.setOnClickListener {
            val intent = Intent(this, Pomodoro::class.java)
            startActivity(intent)
        }*/
    }

    @Composable
    fun buttons() {
        val context = LocalContext.current;


        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(space = 20.dp, alignment = Alignment.CenterVertically),
        ) {
            Button(
                onClick = { startActivity(Intent(context, ToDoListActivity::class.java)) },
                modifier = Modifier.size(width = 150.dp, height = 50.dp)
            ) {
                Text(text = "ToDoList")
            }

            Button(
                onClick = { startActivity(Intent(context, CalendrierActivity::class.java)) },
                modifier = Modifier.size(width = 150.dp, height = 50.dp)
            ) {
                Text(text = "Calendrier")
            }

            Button(
                onClick = {startActivity(Intent(context, SanteActivity::class.java))},
                modifier = Modifier.size(width = 150.dp, height = 50.dp)
            ) {
                Text(text = "Santé")
            }

            Button(
                onClick = {startActivity(Intent(context, PomodoroSelectorActivity::class.java))},
                modifier = Modifier.size(width = 150.dp, height = 50.dp)
            ) {
                Text(text = "Pomodoro")
            }


        }
    }

    @Composable
    @Preview
    fun Preview() {
        BubbleAppTheme {
            Surface (
                modifier = Modifier.fillMaxSize()
            ) {
                Column {
                    TopBar()
                    Text(
                        text = "Bubble",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                    buttons()
                }
            }
        }
    }

    @Composable
    fun TopBar() {
        val context = LocalContext.current;
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(92.dp)
                .fillMaxWidth()
                .background(color = Topbar ,)
                .padding(horizontal = 16.dp),
            content = {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .height(72.dp)
                        .width(72.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                    content = {
                        Text(
                            text = "Profile",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = MaterialTheme.colors.primary,
                            modifier = Modifier
                                .padding(start = 20.dp, end = 16.dp)
                                .clickable(onClick = { startActivity(Intent(context, ProfileActivity::class.java)) })
                        )
                        Image(
                            painter = painterResource(id = R.drawable.default_profile_image),
                            contentDescription = "Profile image",
                            modifier = Modifier
                                .height(72.dp)
                                .width(72.dp)
                                .clickable(onClick = { startActivity(Intent(context, ProfileActivity::class.java)) })
                        )
                    }
                )
            }
        )
    }

}