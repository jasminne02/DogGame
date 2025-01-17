package com.example.doggame.model

import android.graphics.Bitmap
import android.graphics.Rect

abstract class BaseObject(
    private var screenX: Int, private var screenY: Int,
    private var speed: Int
) {

    private var x = 0
    private var y = 0
    abstract var bitmap: Bitmap
    abstract var collisionDetection: Rect

    fun getX(): Int {return this.x}

    fun getY(): Int {return this.y}

    fun getScreenX(): Int {return this.screenX}

    fun getScreenY(): Int {return this.screenY}

    fun getSpeed(): Int {return this.speed}

    fun setX(x: Int) {this.x = x}

    fun setY(y: Int) {this.y = y}
}
