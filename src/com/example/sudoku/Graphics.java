package com.example.sudoku;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.os.Bundle;
import android.view.View;

public class Graphics extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new GraphicsView(this));
	}
	
	public static class GraphicsView extends View {

		private static final String QUOTE = "Now is the time for all good men to come to the aid of their country";
		
		public GraphicsView(Context context) {
			super(context);
		}
		
		@Override
		protected void onDraw(Canvas canvas) {
			setBackgroundResource(R.drawable.background);
			super.onDraw(canvas);
			int red = Color.RED;
			Path circle = new Path();
			Paint p = new Paint();
			p.setStyle(Style.STROKE);
			p.setColor(red);
			circle.addCircle(150, 150, 100, Direction.CW);
			canvas.drawPath(circle, p);
			p.setColor(Color.WHITE);
			p.setTextSize(20);
			p.setStyle(Style.FILL);
			canvas.drawTextOnPath(QUOTE, circle, 0, 20, p);
		}
	}
	
}
