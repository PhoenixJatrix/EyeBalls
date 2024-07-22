package com.example.kotlintest

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //this is a comment
            val eyeWidth = 100
            val eyeHeight = 60

            val eyeBallSize = 35

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

            var isAnimating by remember {
                mutableStateOf(false)
            }

            var doneAnim by remember {
                mutableStateOf(false)
            }

            val mediaPlayerManager by remember {
                mutableStateOf(MediaPlayerManager(this))
            }

            val animState by animateIntAsState(
                targetValue = animRowHeight.value.toInt(),
                animationSpec = tween(Random.nextInt(100, 200))
            ) {
                doneAnim = true
            }

            var percentageOfWidthCovered by remember {
                mutableStateOf(0)
            }

            var percentageOfHeightCovered by remember {
                mutableStateOf(0)
            }

            var moneyCentered by remember {
                mutableStateOf(false)
            }

            LaunchedEffect(key1 = time, block = {
                delay(700)

                if (Random.nextInt(0, 15) == 7) {
                    animRowHeight = 100.dp
                    mediaPlayerManager.play {
                        isAnimating = true
                        if (doneAnim) {
                            doneAnim = false
                            animRowHeight = 0.dp
                            isAnimating = false
                        }
                    }
                }

                time += 1
            })

            var visible by remember {
                mutableStateOf(false)
            }

            val density = LocalDensity.current

            val screenWidth = LocalConfiguration.current.screenWidthDp
            val screenHeight = LocalConfiguration.current.screenHeightDp

            val percentageWidthAnim by animateIntAsState(targetValue = percentageOfWidthCovered)
            val percentageHeightAnim by animateIntAsState(targetValue = percentageOfHeightCovered)

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
                            with(density) {
                                percentageOfWidthCovered = getPercent(
                                    screenWidth,
                                    change.position.x.toDp().value.toInt()
                                )

                                percentageOfHeightCovered = getPercent(
                                    screenHeight,
                                    change.position.y.toDp().value.toInt()
                                )

                                Log.d(
                                    "",
                                    "$percentageOfHeightCovered"
                                )

                                moneyCentered =
                                    percentageOfWidthCovered in 48..52 && percentageOfHeightCovered in 48..52

                                moneyX = change.position.x.toDp()
                                moneyY = change.position.y.toDp()
                            }
                        }
                    }
            ) {

                Box {
                    Canvas(modifier = Modifier
                        .height(eyeHeight.dp)
                        .width(eyeWidth.dp), onDraw = {
                        drawOval(color = Color.White)
                    })

                    Canvas(modifier = Modifier
                        .size(eyeBallSize.dp)
                        .offset(
                            x = if (moneyCentered) eyeWidth.dp - (eyeBallSize / 2).dp else getPercentage(
                                eyeWidth,
                                percentageWidthAnim
                            ).dp - (eyeBallSize / 2).dp,
                            y = if (moneyCentered) (eyeHeight / 2).dp - (eyeBallSize / 2).dp else getPercentage(
                                eyeHeight,
                                percentageHeightAnim
                            ).dp - (eyeBallSize / 2).dp
                        ), onDraw = {
                        drawCircle(color = Color.Black)
                        drawOval(
                            color = Green,
                            style = Fill,
                            size = Size(height = 30f, width = 30f),
                            topLeft = Offset(
                                x = with(density) {
                                    ((eyeBallSize / 2)).dp.toPx() - 15f
                                },
                                y = with(density) {
                                    ((eyeBallSize / 2)).dp.toPx() - 15f
                                }
                            )
                        )
                    })
                }

                Box {
                    Canvas(modifier = Modifier
                        .height(eyeHeight.dp)
                        .width(eyeWidth.dp), onDraw = {
                        drawOval(color = Color.White)
                    })

                    Canvas(modifier = Modifier
                        .size(eyeBallSize.dp)
                        .offset(
                            x = if (moneyCentered) -(eyeBallSize / 2).dp else getPercentage(
                                eyeWidth,
                                percentageWidthAnim
                            ).dp - (eyeBallSize / 2).dp,
                            y = if (moneyCentered) (eyeHeight / 2).dp - (eyeBallSize / 2).dp else getPercentage(
                                eyeHeight,
                                percentageHeightAnim
                            ).dp - (eyeBallSize / 2).dp
                        ), onDraw = {
                        drawCircle(color = Color.Black)
                        drawOval(
                            color = Green,
                            style = Fill,
                            size = Size(height = 30f, width = 30f),
                            topLeft = Offset(
                                x = with(density) {
                                    ((eyeBallSize / 2)).dp.toPx() - 15f
                                },
                                y = with(density) {
                                    ((eyeBallSize / 2)).dp.toPx() - 15f
                                })
                        )
                    })
                }
            }

            if (visible) {
                Image(
                    painter = painterResource(id = R.drawable.money),
                    contentDescription = "red",
                    colorFilter = ColorFilter.tint(Green),
                    modifier = Modifier
                        .size(25.dp)
                        .offset(x = moneyX - 12.5.dp, y = moneyY - 12.5.dp)
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

fun getPercentage(totalAmount: Int, percentage: Int): Int {
    return (totalAmount * percentage) / 100
}

fun getPercent(totalAmount: Int, partAmount: Int): Int {
    return (partAmount * 100) / totalAmount
}