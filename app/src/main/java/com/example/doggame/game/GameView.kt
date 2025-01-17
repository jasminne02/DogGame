package com.example.doggame.game

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.VelocityTracker
import android.view.WindowInsets
import android.view.WindowManager
import com.example.doggame.GameOverActivity
import com.example.doggame.R
import com.example.doggame.datamanager.LoggedUser
import com.example.doggame.model.BaseObject
import com.example.doggame.model.Bee
import com.example.doggame.model.Biscuit
import com.example.doggame.model.Dog
import com.example.doggame.model.GiftBone
import kotlinx.coroutines.Runnable
import kotlin.math.abs

class GameView(context: Context)
    : SurfaceView(context), SurfaceHolder.Callback, Runnable {

    private val screenX: Int
    private val screenY: Int
    private var thread: Thread? = null
    private var velocityTracker: VelocityTracker
    private var isPlaying: Boolean = false

    private var background: Bitmap
    private var paint: Paint = Paint()
    private lateinit var canvas: Canvas

    private val dog: Dog
    private var biscuitList: MutableList<Biscuit> = mutableListOf()
    private var giftBoneList: MutableList<GiftBone> = mutableListOf()
    private var beeList: MutableList<Bee> = mutableListOf()

    private var score: Int = 0
    private var missedGiftBone: Int = 0

    private var biscuitSleepCounter: Int = 0
    private var biscuitSleepNumber: Int = 70
    private var giftBoneSleepCounter: Int = 0
    private var giftBoneSleepNumber: Int = 250
    private var beeSleepCounter: Int = 0
    private var beeSleepNumber: Int = 300

    init {
        val point = getScreenSize()
        screenX = point.x
        screenY = point.y
        dog = Dog(context, screenX, screenY)

        this.background = Bitmap.createScaledBitmap(
            BitmapFactory.decodeResource(context.resources, R.drawable.background),
            screenX, screenY,
            true
        )

        velocityTracker = VelocityTracker.obtain()

        holder.addCallback(this)
    }

    override fun run() {
        while (isPlaying) {
            if (!holder.surface.isValid) continue

            draw()
            update()
            refreshRate()
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        startThread()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        stopThread()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    velocityTracker.clear()
                    velocityTracker.addMovement(event)
                }

                MotionEvent.ACTION_MOVE -> {
                    velocityTracker.addMovement(event)
                    velocityTracker.computeCurrentVelocity(1000)

                    if (abs(velocityTracker.xVelocity) > abs(velocityTracker.yVelocity)) {
                        if(velocityTracker.xVelocity > 0) {
                            dog.setDirection("right")
                            dog.setX(dog.getX() + dog.getSpeed())
                            if (dog.getX() >= background.width - dog.getSpeed()) {
                                dog.setX(-dog.bitmap.width)
                            }
                        } else {
                            dog.setDirection("left")
                            dog.setX(dog.getX() - dog.getSpeed())
                            if (dog.getX() <= - dog.bitmap.width / 2) {
                                dog.setX(background.width - dog.getSpeed() * 2)
                            }
                        }
                    }
                }
            }
        }

        return true
    }

    private fun draw() {
        if (holder.surface.isValid) {
            canvas = holder.lockCanvas()
            canvas.drawBitmap(background, 0f, 0f, null)
            canvas.drawBitmap(dog.bitmap, dog.getX().toFloat(), dog.getY().toFloat(), paint)
            paint.color = Color.WHITE
            paint.textSize = 55F
            canvas.drawText("${context.getString(R.string.score)}: $score", 50F, 60F, paint)
            canvas.drawText("${context.getString(R.string.missed_gifts)}: $missedGiftBone/5",
                50F, 120F, paint)

            createBiscuit()
            createGiftBone()
            createBee()

            drawObject(biscuitList)
            drawObject(giftBoneList)
            drawObject(beeList)

            holder.unlockCanvasAndPost(canvas)
        }
    }

    private fun update() {
        dog.update()
        biscuitSleepCounter += 1
        giftBoneSleepCounter += 1
        beeSleepCounter += 1

        if (missedGiftBone == 5) {
            endGame()
        }

        updateDifficulty()
        catchBiscuit()
        catchGiftBone()
        touchBee()
    }

    private fun refreshRate() {
        try {
            Thread.sleep(42)
        } catch (e: Exception) {
            Log.e("RefreshRateException", "Exception occurred: ${e.message}", e)
        }
    }

    fun startThread() {
        if (thread == null) {
            isPlaying = true
            thread = Thread(this)
            thread?.start()
        }
    }

    private fun stopThread() {
        isPlaying = false
        thread?.join()
        thread = null
    }

    private fun endGame() {
        LoggedUser.setLastScore(score)
        isPlaying = false
        val intent = Intent(context, GameOverActivity::class.java)
        context.startActivity(intent)
        (context as Activity).finish()
    }

    private fun getScreenSize(): Point {
        val point = Point()
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = windowManager.currentWindowMetrics
            val insets = windowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            val bounds = windowMetrics.bounds
            point.x = bounds.width() - insets.left - insets.right
            point.y = bounds.height() - insets.top - insets.bottom
        } else {
            val displayMetrics = DisplayMetrics()
            @Suppress("DEPRECATION")
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            point.x = displayMetrics.widthPixels
            point.y = displayMetrics.heightPixels
        }
        return point
    }

    private fun drawObject(objectsList: List<BaseObject>) {
        for (objectEntity in objectsList) {
            canvas.drawBitmap(
                objectEntity.bitmap, objectEntity.getX().toFloat(),
                objectEntity.getY().toFloat(), paint
            )

            objectEntity.setY(objectEntity.getY() + objectEntity.getSpeed())
            objectEntity.collisionDetection.bottom =
                objectEntity.getY() + objectEntity.bitmap.height - 50

            objectEntity.collisionDetection.left = objectEntity.getX() + 50
            objectEntity.collisionDetection.right = objectEntity.getX() +
                    objectEntity.bitmap.width - 50
        }
    }

    private fun updateDifficulty() {
        if (score > 1000) {
            beeSleepNumber = 50
        } else if (score > 500) {
            giftBoneSleepNumber = 100
            beeSleepNumber = 100
        } else if (score > 200) {
            giftBoneSleepNumber = 150
            beeSleepNumber = 200
        }
    }

    private fun createBiscuit() {
        if (biscuitSleepCounter == biscuitSleepNumber) {
            val biscuit = Biscuit(context, screenX, screenY)
            biscuitList.add(biscuit)
            canvas.drawBitmap(
                biscuit.bitmap, biscuit.getX().toFloat(), biscuit.getY().toFloat(), paint
            )
            biscuitSleepCounter = 0
        }
    }

    private fun createGiftBone() {
        if (giftBoneSleepCounter == giftBoneSleepNumber) {
            val giftBone = GiftBone(context, screenX, screenY)
            giftBoneList.add(giftBone)
            canvas.drawBitmap(
                giftBone.bitmap, giftBone.getX().toFloat(), giftBone.getY().toFloat(), paint
            )
            giftBoneSleepCounter = 0
        }
    }

    private fun createBee() {
        if (beeSleepCounter == beeSleepNumber) {
            val bee = Bee(context, screenX, screenY)
            beeList.add(bee)
            canvas.drawBitmap(
                bee.bitmap, bee.getX().toFloat(), bee.getY().toFloat(), paint
            )
            beeSleepCounter = 0
        }
    }

    private fun catchBiscuit() {
        for ((index, biscuit) in biscuitList.withIndex()) {
            if (biscuit.collisionDetection.intersect(dog.collisionDetection)) {
                score += biscuit.getScore()
                biscuitList.removeAt(index)
                break
            }
        }
    }

    private fun catchGiftBone() {
        for ((index, giftBone) in giftBoneList.withIndex()) {
            if (giftBone.collisionDetection.intersect(dog.collisionDetection)) {
                score += giftBone.getScore()
                giftBoneList.removeAt(index)
                break
            } else if (giftBone.getY() + giftBone.bitmap.height >= screenY) {
                missedGiftBone += 1
                giftBoneList.removeAt(index)
            }
        }
    }

    private fun touchBee() {
        for ((index, bee) in beeList.withIndex()) {
            if (bee.collisionDetection.intersect(dog.collisionDetection)) {
                endGame()
            } else if (bee.getY() >= screenY) {
                beeList.removeAt(index)
            }
        }
    }
}
