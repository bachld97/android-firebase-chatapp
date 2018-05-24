package com.example.cpu02351_local.firebasechatapp.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.cpu02351_local.firebasechatapp.R


class ConversationListDivider(context: Context) : RecyclerView.ItemDecoration() {

    private val divider = ContextCompat.getDrawable(context, R.drawable.divider)!!
    private val leftOffset = context.resources.getDimension(R.dimen.avaMargin)

    override fun onDraw(c: Canvas?, parent: RecyclerView?, state: RecyclerView.State?) {
        super.onDraw(c, parent, state)
        for (i in 0 until (parent?.childCount  ?: 1) - 1) {
            drawDivider(c!!, parent!!, parent.getChildAt(i), divider)
        }
    }

    private fun drawDivider(c: Canvas, parent: RecyclerView, child: View, divider: Drawable) {
        val left = parent.paddingLeft + leftOffset
        val right = parent.width - parent.paddingRight
        val params = child.layoutParams as RecyclerView.LayoutParams
        val top = child.bottom + params.bottomMargin
        val bottom = top + divider.intrinsicHeight
        divider.setBounds(left.toInt(), top, right, bottom)
        divider.draw(c)
    }

    /*
    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        outRect?.set(0, 0, 0, divider.intrinsicHeight)
    }
    */
}