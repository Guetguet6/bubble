package ca.uqac.bubble

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import ca.uqac.bubble.Calendrier.CalendrierActivity
import ca.uqac.bubble.BottomNavItem
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import androidx.compose.ui.text.style.TextAlign
import ca.uqac.bubble.pomodoro.PomodoroActivity
import ca.uqac.bubble.Calendrier.NotificationScheduler
import ca.uqac.bubble.Calendrier.NotificationService
import ca.uqac.bubble.pomodoro.PomodoroSelectorActivity
import ca.uqac.bubble.profil.ProfileActivity
import ca.uqac.bubble.sante.SanteActivity
import ca.uqac.bubble.todolist.ToDoListActivity
import ca.uqac.bubble.ui.theme.BubbleAppTheme
import ca.uqac.bubble.ui.theme.Purple200
import ca.uqac.bubble.ui.theme.Topbar
import java.util.*

class MainActivity : ComponentActivity() {
    private val navItems = listOf(
        BottomNavItem("Tâches", Icons.Filled.Task),
        BottomNavItem("Calendrier", Icons.Filled.CalendarMonth),
        BottomNavItem("Accueil", Icons.Filled.Home),
        BottomNavItem("Pomodoro", Icons.Filled.Timer),
        BottomNavItem("Santé", Icons.Filled.MonitorHeart)
    )
    private var selectedIndex by mutableStateOf(2)

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BubbleAppTheme {
                // A surface container using the 'background' color from the theme
                Scaffold(
                    bottomBar = {
                        BottomNavigationBar(
                            items = navItems,
                            selectedIndex = selectedIndex,
                            onSelectedIndexChanged = { newIndex ->
                                selectedIndex = newIndex
                            }
                        )
                    }
                ) {
                    Column {
                        TopBar()
                        Surface(
                            modifier = Modifier
                                .fillMaxSize(),
                            color = MaterialTheme.colors.background,
                        ) {
                            buttons()
                        }
                    }
                }
            }

            // Démarrer le service
            val serviceIntent = Intent(this, NotificationService::class.java)
            startService(serviceIntent)


            /*
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
    }

    @Composable
    fun buttons() {
        val context = LocalContext.current

        Column(
            modifier = Modifier.padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Bienvenue sur Bubble, ton application de développement personnel, booste ta " +
                        "productivité personnelle, professionnelle ainsi que ton bien-être à partir " +
                        "d’une seule application !",
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Planifie tes tâches avec notre TodoList",
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Button(
                    onClick = { startActivity(Intent(context, ToDoListActivity::class.java)) },
                    modifier = Modifier.size(width = 150.dp, height = 50.dp)
                ) {
                    Text(text = "ToDoList")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Organise ton agenda avec notre calendrier",
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Button(
                    onClick = { startActivity(Intent(context, CalendrierActivity::class.java)) },
                    modifier = Modifier.size(width = 150.dp, height = 50.dp)
                ) {
                    Text(text = "Calendrier")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Fais des sessions de Pomodoro pour travailler",
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Button(
                    onClick = { startActivity(Intent(context, PomodoroSelectorActivity::class.java)) },
                    modifier = Modifier.size(width = 150.dp, height = 50.dp)
                ) {
                    Text(text = "Pomodoro")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Relaxe-toi après chaque session de travail",
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Button(
                    onClick = { startActivity(Intent(context, SanteActivity::class.java)) },
                    modifier = Modifier.size(width = 150.dp, height = 50.dp)
                ) {
                    Text(text = "Sante")
                }
            }
        }
    }

    @Composable
    @Preview
    fun Preview() {
        BubbleAppTheme {
            Surface(
                modifier = Modifier.fillMaxSize()
            ) {
                Column {
                    TopBar()
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
                .height(70.dp)
                .fillMaxWidth()
                .background(color = Purple200)
                .padding(horizontal = 16.dp),
            content = {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .height(50.dp)
                        .width(50.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = {
                            startActivity(
                                Intent(
                                    context,
                                    ProfileActivity::class.java
                                )
                            )
                        }),
                    content = {
                        Text(
                            text = "Profil",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = Color.White,
                            modifier = Modifier
                                .padding(start = 20.dp, end = 16.dp)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.default_profile_image),
                            contentDescription = "Profile image",
                            modifier = Modifier
                                .height(50.dp)
                                .width(50.dp)
                        )
                    }
                )
            }
        )
    }

    @Composable
    fun BottomNavigationBar(
        items: List<BottomNavItem>,
        selectedIndex: Int,
        onSelectedIndexChanged: (Int) -> Unit
    ) {
        val context = LocalContext.current
        BottomNavigation(
            backgroundColor = MaterialTheme.colors.primary,
            elevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp) // adjust the height as needed
        ) {
            items.forEachIndexed { index, item ->
                BottomNavigationItem(
                    icon = { Icon(item.icon, contentDescription = item.title) },
                    selected = selectedIndex == index,
                    onClick = {
                        onSelectedIndexChanged(index)
                        // Create an Intent to switch activities
                        when (index) {
                            0 -> context.startActivity(
                                Intent(
                                    context,
                                    ToDoListActivity::class.java
                                )
                            )

                            1 -> context.startActivity(
                                Intent(
                                    context,
                                    CalendrierActivity::class.java
                                )
                            )

                            2 -> context.startActivity(
                                Intent(
                                    context,
                                    MainActivity::class.java
                                )
                            )

                            3 -> context.startActivity(
                                Intent(
                                    context,
                                    PomodoroSelectorActivity::class.java
                                )
                            )

                            4 -> context.startActivity(
                                Intent(
                                    context,
                                    SanteActivity::class.java
                                )
                            )
                        }
                    },
                    selectedContentColor = MaterialTheme.colors.secondary,
                    unselectedContentColor = Color.White,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
