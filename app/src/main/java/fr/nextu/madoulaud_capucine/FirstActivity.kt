package fr.nextu.madoulaud_capucine

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import fr.nextu.madoulaud_capucine.databinding.ActivityFirstBinding

class FirstActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFirstBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_first)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding = ActivityFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonViewCompleteList.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
            intent.putExtra("listType", "Complete")
            startActivity(intent)
        }

        binding.buttonViewAlcoholicList.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
            intent.putExtra("listType", "Alcoholic")
            startActivity(intent)
        }

        binding.buttonViewNonAlcoholicList.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
            intent.putExtra("listType", "Non_Alcoholic")
            startActivity(intent)
        }
    }
}