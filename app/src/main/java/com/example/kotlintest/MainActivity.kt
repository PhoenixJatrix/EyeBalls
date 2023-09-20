package com.example.kotlintest

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var xRight by remember {
                mutableStateOf(0.dp)
            }

            var yRight by remember {
                mutableStateOf(0.dp)
            }

            var xLeft by remember {
                mutableStateOf(0.dp)
            }

            var yLeft by remember {
                mutableStateOf(0.dp)
            }

            var moneyX by remember {
                mutableStateOf(0.dp)
            }

            var moneyY by remember {
                mutableStateOf(0.dp)
            }

            var animRowHeight by remember {
                mutableStateOf(0.dp)
            }

            var time by remember {
                mutableStateOf(0)
            }

            var isAnimating = false

            var doneAnim by remember {
                mutableStateOf(false)
            }

            val player = MediaPlayer()

            val animState by animateIntAsState(
                targetValue = animRowHeight.value.toInt(),
                animationSpec = tween(Random.nextInt(100, 200))
            ) {
                doneAnim = true
            }

            LaunchedEffect(key1 = time, block = {
                delay(700)
                if (!isAnimating) {
                    time += 1
                }

                if (Random.nextInt(0, 15) == 7) {
                    animRowHeight = (100).dp
                    player.setDataSource(applicationContext, Uri.parse("android.resource://" + packageName + "/" + R.raw.blink))
                    player.prepare()
                    player.start()

                    player.setOnCompletionListener {
                        player.reset()
                        isAnimating = true
                        if (doneAnim) {
                            doneAnim = false
                            animRowHeight = 0.dp
                            isAnimating = false
                        }
                    }
                }
            })

            var visible by remember {
                mutableStateOf(false)
            }

            Row(horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .pointerInput(key1 = null) {
                        detectDragGestures(
                            onDragStart = {
                                visible = true
                            },

                            onDragEnd = {
                                visible = false
                            })

                        { change, _ ->
                            if(change.position.x.toInt() in 600..900 && change.position.y.toInt() in 1400..1500){
                                xLeft = (50).dp
                                xRight = (-50).dp

                                yLeft = (0).toDp()
                                yRight = (0).toDp()

                            }else if(change.position.x.toInt() in 600..750 && change.position.y.toInt() in 1300..1450){
                                xLeft = (0).toDp()
                                xRight = (0).toDp()
                            }

                            else{
                                if (change.position.x.toInt() in 0..600) {
                                    xLeft = (-110).toDp()
                                    xRight = (-110).toDp()
                                } else if (change.position.x.toInt() in 600..900) {
                                    xLeft = (0).toDp()
                                    xRight = (0).toDp()
                                } else if (change.position.x.toInt() in 900..Int.MAX_VALUE) {
                                    xLeft = (110).toDp()
                                    xRight = (110).toDp()
                                }

                                if (change.position.y.toInt() in 0..1300) {
                                    yLeft = (-50).toDp()
                                    yRight = (-50).toDp()
                                } else if (change.position.y.toInt() in 1300..1600) {
                                    yLeft = (0).toDp()
                                    yRight = (0).toDp()
                                } else if (change.position.y.toInt() in 1600..Int.MAX_VALUE) {
                                    yLeft = (50).toDp()
                                    yRight = (50).toDp()
                                }
                            }

                            moneyX = change.position.x.dp
                            moneyY = change.position.y.dp

                        }
                    }
            ) {

                Box(contentAlignment = Alignment.Center) {
                    Canvas(modifier = Modifier
                        .height(65.dp)
                        .width(100.dp), onDraw = {
                        drawOval(color = Color.White)
                    })

                    Canvas(modifier = Modifier
                        .size(40.dp)
                        .offset(x = xLeft, y = yLeft), onDraw = {
                        drawCircle(color = Color.Black)
                        drawOval(
                            color = Color.Green,
                            style = Fill,
                            size = Size(height = 30f, width = 30f),
                            topLeft = Offset(20f, 40f)
                        )
                    })
                }

                Box(contentAlignment = Alignment.Center) {
                    Canvas(modifier = Modifier
                        .height(65.dp)
                        .width(100.dp), onDraw = {
                        drawOval(color = Color.White)
                    })

                    Canvas(modifier = Modifier
                        .size(40.dp)
                        .offset(x = xRight, y = yRight), onDraw = {
                        drawCircle(color = Color.Black)
                        drawOval(
                            color = Color.Green,
                            style = Fill,
                            size = Size(height = 30f, width = 30f),
                            topLeft = Offset(85f, 40f)
                        )
                    })
                }
            }

            if (visible) {
                Image(
                    painter = painterResource(id = R.drawable.money),
                    contentDescription = null,
                    modifier = Modifier
                        .size(35.dp)
                        .offset(x = (moneyX.value / 3.7).dp, y = (moneyY.value / 3.7).dp),
                    colorFilter = ColorFilter.tint(Color.Green)
                )
            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(animState.dp)
                        .background(Color.Black)
                ) {
                }
            }
        }
    }
}