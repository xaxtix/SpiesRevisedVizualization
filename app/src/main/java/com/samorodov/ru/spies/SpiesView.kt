package com.samorodov.ru.spies

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.v4.content.ContextCompat.getColor
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * Created by xaxtix on 10.12.17.
 * ♪♫•*¨*•.¸¸❤¸¸.•*¨*•♫♪ﾟ+｡☆*゜+。.。:.*.ﾟ ﾟ¨ﾟﾟ･*:..｡o○☆ﾟ+｡
 */
class SpiesView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val N = 11

    private val linesPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val disabledPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val spiesPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var rectSize = 0
    private var step = 0
    private var topOffeset = 0f
    private var leftOffset = 0f

    private val matrix: Array<IntArray> = Array(N, { IntArray(N) })

    init {
        linesPaint.color = getColor(context, R.color.lineColor)
        disabledPaint.color = getColor(context, R.color.disabledColor)
        spiesPaint.textSize = dp(context, 24f)
        spiesPaint.color = getColor(context, R.color.green)
        spiesPaint.textAlign = Paint.Align.CENTER
        linesPaint.strokeWidth = dp(context, 2f)
        for (i in 0 until N)
            for (j in 0 until N)
                matrix[i][j] = 0
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        rectSize = if (measuredHeight > measuredWidth) measuredWidth else measuredHeight
        step = (rectSize.toFloat() / N.toFloat()).toInt()
        topOffeset = (measuredHeight - rectSize).toFloat() / 2f
        leftOffset = (measuredWidth - rectSize).toFloat() / 2f

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.translate(
                leftOffset,
                topOffeset + linesPaint.strokeWidth / 2

        )

        for (i in 0 until N)
            (0 until N)
                    .filter { matrix[i][it] != 0 }
                    .forEach {
                        when (matrix[i][it]) {
                            -1 -> canvas.drawRect(
                                    (i * step).toFloat(),
                                    (it * step).toFloat(),
                                    ((i + 1) * step).toFloat(),
                                    ((it + 1) * step).toFloat(),
                                    disabledPaint
                            )
                            1 -> canvas.drawText("S",
                                    (i * step).toFloat() + step / 2f,
                                    (it * step).toFloat() + step / 2f + spiesPaint.textSize / 2f,
                                    spiesPaint
                            )
                        }

                    }


        for (i in 0..N) {
            canvas.drawLine(
                    0f, (i * step).toFloat(),
                    rectSize.toFloat(), (i * step).toFloat(),
                    linesPaint
            )

            canvas.drawLine(
                    (i * step).toFloat(), 0f,
                    (i * step).toFloat(), rectSize.toFloat(),
                    linesPaint
            )
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            if (event.x > leftOffset && event.x < rectSize + leftOffset
                    && event.y > topOffeset && event.y < rectSize + topOffeset) {
                var x = event.x - leftOffset
                var y = event.y - topOffeset
                var i = (x / step).toInt()
                var j = (y / step).toInt()

                when (matrix[i][j]) {
                    0 -> setSpies(matrix, i, j)
                    1 -> removeSpies(matrix, i, j);
                }
                invalidate()
                return true
            }
        }
        return true
    }

    private fun removeSpies(matrix: Array<IntArray>, iS: Int, jS: Int) {
        matrix[iS][jS] = 0

        for (i in 0 until N)
            for (j in 0 until N)
                if (matrix[i][j] != 1) matrix[i][j] = 0

        for (i in 0 until N)
            for (j in 0 until N)
                if (matrix[i][j] == 1) setSpies(matrix, i, j)
    }

    private fun setSpies(matrix: Array<IntArray>, iS: Int, jS: Int) {
        for (i in 0 until N)
            for (j in 0 until N)
                if (matrix[i][j] == 1 && (iS != i && jS != j)) {
                    var iStep = i - iS
                    var jStep = j - jS
                    var gcd = gcd(iStep, jStep)

                    iStep /= gcd
                    jStep /= gcd


                    var currentI = iS - iStep
                    var currentJ = jS - jStep
                    while (currentI < N && currentJ < N && currentI >= 0 && currentJ >= 0) {
                        if (matrix[currentI][currentJ] != 1)
                            matrix[currentI][currentJ] = -1
                        currentI -= iStep
                        currentJ -= jStep
                    }

                    currentI = iS + iStep
                    currentJ = jS + jStep
                    while (currentI < N && currentJ < N && currentI >= 0 && currentJ >= 0) {
                        if (matrix[currentI][currentJ] != 1)
                            matrix[currentI][currentJ] = -1
                        currentI += iStep
                        currentJ += jStep
                    }

                }

        matrix[iS][jS] = 1

        (0 until N)
                .filter { it != iS }
                .forEach { matrix[it][jS] = -1 }

        (0 until N)
                .filter { it != jS }
                .forEach { matrix[iS][it] = -1 }

        var i = iS - 1
        var j = jS - 1
        while (i >= 0 && j >= 0) {
            matrix[i][j] = -1
            i--
            j--
        }

        i = iS + 1
        j = jS + 1
        while (i < N && j < N) {
            matrix[i][j] = -1
            i++
            j++
        }

        i = iS - 1
        j = jS + 1
        while (i >= 0 && j < N) {
            matrix[i][j] = -1
            i--
            j++
        }

        i = iS + 1
        j = jS - 1
        while (i < N && j >= 0) {
            matrix[i][j] = -1
            i++
            j--
        }


    }
}