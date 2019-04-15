package au.sjowl.lib.view.charts.telegram.chart.base

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View

class RenderThread(val hostView: View, val render: ((canvas: Canvas) -> Unit)) : Thread() {

    var hasBitmap = false

    var renderNew = false

    private var w = 1

    private var h = 1

    private val renderingCanvas = Canvas()

    private val drawingCanvas = Canvas()

    private val renderBitmapsSize = 2

    private val renderingBitmaps = mutableListOf<Bitmap>().apply {
        repeat(renderBitmapsSize) { add(newBitmap()) }
    }

    private var currentRenderIndex = 0

    private var drawingBitmap: Bitmap = newBitmap()

    override fun run() {
        while (!isInterrupted) {
            if (renderNew) {
                renderNewFrame()
                swapBitmaps()
                hasBitmap = true
                renderNew = false
                hostView.postInvalidate()
            } else {
                Thread.sleep(1)
            }
        }
    }

    fun createNewBitmap(w: Int, h: Int) {
        this.w = w
        this.h = h
        renderingBitmaps.clear()
        repeat(renderBitmapsSize) { renderingBitmaps.add(newBitmap()) }
        drawingBitmap = newBitmap()
        renderingCanvas.setBitmap(renderingBitmaps[currentRenderIndex])
        drawingCanvas.setBitmap(drawingBitmap)
    }

    fun renderNew() {
        renderNew = true
    }

    fun drawFrame(canvas: Canvas) {
        canvas.drawBitmap(drawingBitmap, 0f, 0f, null)
    }

    private fun newBitmap() = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444)

    private fun renderNewFrame() {
        render(renderingCanvas)
    }

    private fun swapBitmaps() {
        drawingBitmap = renderingBitmaps[currentRenderIndex]
        currentRenderIndex = (currentRenderIndex + 1) % renderingBitmaps.size
        renderingCanvas.setBitmap(renderingBitmaps[currentRenderIndex])
    }

    init {
        isDaemon = true
    }
}