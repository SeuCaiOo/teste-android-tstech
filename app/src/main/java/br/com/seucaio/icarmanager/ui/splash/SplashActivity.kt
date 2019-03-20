package br.com.seucaio.icarmanager.ui.splash

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import br.com.seucaio.icarmanager.R
import br.com.seucaio.icarmanager.ui.main.MainActivity
import org.jetbrains.anko.startActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        initApp()
    }

    private fun initApp() {
        Handler().postDelayed({ startActivity<MainActivity>() }, 2000)
    }
}
