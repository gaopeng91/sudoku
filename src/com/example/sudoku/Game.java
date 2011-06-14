package com.example.sudoku;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class Game extends Activity {

	private static final String TAG = "Sudoku";
	public static final String KEY_DIFFICULTY = "org.example.sudoku.difficulty";
	public static final int DIFFICULTY_EASY = 0;
	public static final int DIFFICULTY_MEDIUM = 1;
	public static final int DIFFICULTY_HARD = 2;

	private int[] puzzle = new int[9 * 9];

	private PuzzleView puzzleView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		int diff = getIntent().getIntExtra(KEY_DIFFICULTY, DIFFICULTY_EASY);
		puzzle = getPuzzle(diff);
		calculateUsedTiles();

		puzzleView = new PuzzleView(this);
		setContentView(puzzleView);
		puzzleView.requestFocus();
	}

	private void calculateUsedTiles() {
		// TODO Auto-generated method stub
	}

	private int[] getPuzzle(int diff) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTileString(int i, int j) {
		return String.valueOf((i + j) % 10);
	}

	public void showKeyPadOrError(int selX, int selY) {
		int[] tiles = getUsedTiles(selX, selY);
		if (tiles.length == 9) {
			Toast toast = Toast.makeText(this, R.string.no_moves_label,
					Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		} else {
			Dialog v = new Keypad(this, tiles, puzzleView);
			v.show();
		}

	}

	public boolean setTileIfValid(int selX, int selY, int tile) {
		// TODO Auto-generated method stub
		return false;
	}

	public int[] getUsedTiles(int i, int j) {
		// TODO Auto-generated method stub
		return new int[]{0};
	}
}
