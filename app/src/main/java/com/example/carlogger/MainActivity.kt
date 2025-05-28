package com.example.carlogger

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.carlogger.databinding.ActivityMainBinding

// Estas dos importaciones son clave:
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ① Inicializa Firebase:
        FirebaseApp.initializeApp(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ② Prueba rápida de escritura en Firestore:
        val db = Firebase.firestore
        db.collection("test")
            .document("ping")
            .set(mapOf("check" to true))
            .addOnSuccessListener {
                Log.d("FBTest", "✅ Escritura OK en Firestore")
            }
            .addOnFailureListener { e ->
                Log.e("FBTest", "❌ Error al escribir", e)
            }

        // ③ Prueba rápida de lectura de ese mismo doc:
        db.collection("test")
            .document("ping")
            .get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    Log.d("FBTest", "✅ Lectura OK: ${doc.data}")
                } else {
                    Log.w("FBTest", "⚠️ Doc ‘ping’ no existe todavía")
                }
            }
            .addOnFailureListener { e ->
                Log.e("FBTest", "❌ Error al leer", e)
            }

        // Tu código de navegación habitual:
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}
