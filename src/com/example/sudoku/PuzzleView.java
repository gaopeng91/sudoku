package com.example.sudoku;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.text.method.MovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;

public class PuzzleView extends View {

	private float width;
	private float height;
	private int selX;
	private int selY;
	private Rect selRect = new Rect();

	private static final String TAG = "Sudoku";
	private final Game game;

	public PuzzleView(Context context) {
		super(context);
		this.game = (Game) context;
		setFocusable(true);
		setFocusableInTouchMode(true);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		width = w / 9f;
		height = h / 9f;
		getRect(selX, selY, selRect);
		Log.d(TAG, "onSizeChanged width:" + w + " height:" + h);
	}

	private void getRect(int x, int y, Rect rect) {
		rect.set((int) (selX * width), (int) (selY * height), (int) (selX
				* width + width), (int) (selY * height + height));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// draw the background
		Paint background = new Paint();
		background.setColor(getResources().getColor(R.color.puzzle_background));
		canvas.drawRect(0, 0, getWidth(), getHeight(), background);

		// draw the board
		Paint light = new Paint();
		light.setColor(getResources().getColor(R.color.puzzle_light));

		Paint hilite = new Paint();
		hilite.setColor(getResources().getColor(R.color.puzzle_hilite));

		Paint dark = new Paint();
		dark.setColor(getResources().getColor(R.color.puzzle_dark));

		// Draw the minor grid lines
		for (int i = 0; i < 9; i++) {
			canvas.drawLine(0, i * height, getWidth(), i * height, light);
			canvas.drawLine(0, i * height + 1, getWidth(), i * height + 1,
					hilite);

			canvas.drawLine(i * width, 0, i * width, getHeight(), light);
			canvas.drawLine(i * width + 1, 0, i * width + 1, getHeight(),
					hilite);
		}

		// Draw the major grid lines
		for (int i = 0; i < 9; i++) {
			if (i % 3 != 0)
				continue;
			canvas.drawLine(0, i * height, getWidth(), i * height, dark);
			canvas.drawLine(0, i * height + 1, getWidth(), i * height + 1,
					hilite);

			canvas.drawLine(i * width, 0, i * width, getHeight(), dark);
			canvas.drawLine(i * width + 1, 0, i * width + 1, getHeight(),
					hilite);
		}

		// Draw the numbers
		Paint foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
		foreground.setColor(getResources().getColor(R.color.puzzle_foreground));
		foreground.setStyle(Style.FILL);
		foreground.setTextSize(height * 0.75f);
		// foreground.setTextScaleX(width / height);
		foreground.setTextAlign(Align.CENTER);
		FontMetrics metrics = foreground.getFontMetrics();
		float x = width / 2;
		float y = (height - metrics.ascent - metrics.descent) / 2;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				canvas.drawText(game.getTileString(i, j), i * width + x, j
						* height + y, foreground);
			}
		}

		// Draw the hint
		Paint hint = new Paint();
		Rect r = new Rect();
		int[] c = { getResources().getColor(R.color.puzzle_hint_0),
				getResources().getColor(R.color.puzzle_hint_1),
				getResources().getColor(R.color.puzzle_hint_2) };
		int movesLeft;
		for (int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				movesLeft = 9 - game.getUsedTiles(i, j).length;
				if(movesLeft < c.length) {
					Log.d(TAG, i + ":" + j + "  movesleft:" + movesLeft);
					getRect(i, j, r);
					hint.setColor(c[movesLeft]);
					canvas.drawRect(r, hint);
				}
			}
		}

		// Draw the selection
		Paint selection = new Paint();
		selection.setColor(getResources().getColor(R.color.puzzle_selected));
		canvas.drawRect(selRect, selection);
	}

	/**
	 * 响应移动方块事件
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d(TAG, "onKeyDown:" + keyCode);
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_DOWN:
			select(selX, selY + 1);
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			select(selX - 1, selY);
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			select(selX + 1, selY);
			break;
		case KeyEvent.KEYCODE_DPAD_UP:
			select(selX, selY - 1);
			break;
		case KeyEvent.KEYCODE_ENTER:
		case KeyEvent.KEYCODE_DPAD_CENTER:
			game.showKeyPadOrError(selX, selY);
		default:
			return super.onKeyDown(keyCode, event);
		}
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() != MotionEvent.ACTION_DOWN)
			return super.onTouchEvent(event);
		select((int) (event.getX() / width), (int) (event.getY() / height));
		game.showKeyPadOrError(selX, selY);
		Log.d(TAG, "onTouchEvent:" + event.getX() + ":" + event.getY());
		return true;
	}

	/**
	 * 选取方块
	 * 
	 * @param x
	 * @param y
	 */
	private void select(int x, int y) {
		invalidate(selRect);
		// selX = Math.min(Math.max(0, x), 8);
		// selY = Math.min(Math.max(0, y), 8);
		selX = (x + 9) % 9;
		selY = (y + 9) % 9;
		getRect(selX, selY, selRect);
		invalidate(selRect);
		Log.d(TAG, "select:" + selX + " " + selY);
	}

	public void setSelectedTile(int tile) {
		if (game.setTileIfValid(selX, selY, tile)) {
			invalidate();
		} else {
			Log.d(TAG, "setSelectedTile: invalid: " + tile);
			startAnimation(AnimationUtils.loadAnimation(game, R.anim.shake));
		}
	}
}
