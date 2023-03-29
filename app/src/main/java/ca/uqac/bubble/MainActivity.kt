package ca.uqac.bubble

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import ca.uqac.bubble.Calendrier.CalendrierActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val calendrierbutton = findViewById<Button>(R.id.calendrier_btn)
        calendrierbutton.setOnClickListener{
            val Intent = Intent(this,
                CalendrierActivity::class.java)
            startActivity(Intent)
        }


    }
}