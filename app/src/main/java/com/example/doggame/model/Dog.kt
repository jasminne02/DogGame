package com.example.doggame.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import com.example.doggame.R

class Dog(context: Context, screenX: Int, screenY: Int)
    : BaseObject(screenX / 3, screenY / 6, 8) {

        private var walkLeft = Bitmap.createScaledBitmap(
            BitmapFactory.decodeResource(context.resources, R.drawable.dog_walk_left),
            getScreenX(),
            getScreenY(),
            true
        )

        private var walkRight = Bitmap.createScaledBitmap(
            BitmapFactory.decodeResource(context.resources, R.drawable.dog_walk_right),
            getScreenX(),
            getScreenY(),
            true
        )

        private var direction = "left"

        override var bitmap: Bitmap = walkLeft

        override var collisionDetection: Rect = Rect(
            getX(),
            getY(),
            getX() + bitmap.width,
            getY() + bitmap.height
        )

        init {
            setX(screenX / 2 - bitmap.width / 2)
            setY(screenY / 2 - bitmap.height / 4 + getScreenY())
        }

        fun update() {
            when (direction) {
                "left" -> bitmap = walkLeft
                "right" -> bitmap = walkRight
            }

            collisionDetection.top = getY() + 50
            collisionDetection.left = getX() + 50
            collisionDetection.right = getX() + bitmap.width - 50
        }

        fun setDirection(direction: String) {this.direction = direction}
}
