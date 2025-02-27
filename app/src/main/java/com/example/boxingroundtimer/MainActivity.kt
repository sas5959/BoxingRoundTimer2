package com.example.boxingroundtimer

import android.os.Bundle
import android.os.CountDownTimer
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContent {
            BoxingTimerApp()
        }
    }
}

@Composable
fun BoxingTimerApp() {
    var roundTime by remember { mutableStateOf(180000L) } // 3 minutes
    var restTime by remember { mutableStateOf(60000L) } // 1 minute
    var warningTime by remember { mutableStateOf(10000L) } // 10 seconds warning
    var isRunning by remember { mutableStateOf(false) }
    var currentTime by remember { mutableStateOf(roundTime) }
    var isRest by remember { mutableStateOf(false) }
    var timer: CountDownTimer? by remember { mutableStateOf(null) }

    fun startTimer() {
        isRunning = true
        timer?.cancel()
        timer = object : CountDownTimer(currentTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                currentTime = millisUntilFinished
            }
            override fun onFinish() {
                if (isRest) {
                    isRest = false
                    currentTime = roundTime
                } else {
                    isRest = true
                    currentTime = restTime
                }
                startTimer()
            }
        }.start()
    }

    fun stopTimer() {
        isRunning = false
        timer?.cancel()
        currentTime = roundTime
        isRest = false
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "${currentTime / 1000}s", fontSize = 48.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Button(onClick = { if (!isRunning) startTimer() }) { Text("Start") }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = { stopTimer() }) { Text("Stop") }
        }
    }
}
