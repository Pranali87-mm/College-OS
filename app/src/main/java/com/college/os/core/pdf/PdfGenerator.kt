package com.college.os.core.pdf

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import java.io.OutputStream

data class ResumeData(
    val name: String,
    val email: String,
    val phone: String,
    val education: String,
    val experience: String,
    val skills: String
)

class PdfGenerator(private val context: Context) {

    fun generatePdf(data: ResumeData, outputStream: OutputStream) {
        val pdfDocument = PdfDocument()

        // Create a standard A4 page (595 x 842 points)
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas
        val paint = Paint()

        // 1. Draw Name (Header)
        paint.color = Color.BLACK
        paint.textSize = 24f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText(data.name, 40f, 60f, paint)

        // 2. Draw Contact Info
        paint.textSize = 12f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        paint.color = Color.DKGRAY
        canvas.drawText("${data.email} | ${data.phone}", 40f, 85f, paint)

        // Draw Divider Line
        paint.color = Color.LTGRAY
        paint.strokeWidth = 1f
        canvas.drawLine(40f, 100f, 555f, 100f, paint)

        // Helper to draw multiline sections
        var currentY = 140f

        fun drawSection(title: String, content: String) {
            // Title
            paint.color = Color.BLACK
            paint.textSize = 16f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText(title, 40f, currentY, paint)
            currentY += 25f

            // Content
            paint.color = Color.BLACK
            paint.textSize = 12f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)

            // Simple text wrapping (very basic)
            val lines = content.split("\n")
            lines.forEach { line ->
                // Basic check to prevent writing off page
                if (currentY > 800) return@forEach

                canvas.drawText(line, 40f, currentY, paint)
                currentY += 20f
            }
            currentY += 30f // Space after section
        }

        drawSection("EDUCATION", data.education)
        drawSection("SKILLS", data.skills)
        drawSection("EXPERIENCE / PROJECTS", data.experience)

        pdfDocument.finishPage(page)

        try {
            pdfDocument.writeTo(outputStream)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            pdfDocument.close()
        }
    }
}