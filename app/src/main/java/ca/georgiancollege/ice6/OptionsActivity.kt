package ca.georgiancollege.ice6

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ca.georgiancollege.ice6.databinding.ActivityOptionsBinding

class OptionsActivity : AppCompatActivity()
{
   private lateinit var binding: ActivityOptionsBinding
   override fun onCreate(savedInstanceState: Bundle?)
   {
      super.onCreate(savedInstanceState)
      binding = ActivityOptionsBinding.inflate(layoutInflater)
      enableEdgeToEdge()
      setContentView(binding.root)
      ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
         val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
         v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
         insets
      }
      binding.ThemeToggleButton.setOnCheckedChangeListener {_, isChecked ->
         if (isChecked)
         {
            Toast.makeText(this, "Dark Theme Selected", Toast.LENGTH_SHORT).show()
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
         }
         else
         {
            Toast.makeText(this, "Light Theme Selected", Toast.LENGTH_SHORT).show()
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
         }
      }
   }
}

