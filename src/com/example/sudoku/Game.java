package com.example.sudoku;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class Game extends Activity {

	private static final String TAG = "Game";
	public static final String KEY_DIFFICULTY = "org.example.sudoku.difficulty";
	public static final int DIFFICULTY_EASY = 0;
	public static final int DIFFICULTY_MEDIUM = 1;
	public static final int DIFFICULTY_HARD = 2;

	private int[] puzzle;
	private final int[][][] used = new int[9][9][];

	private PuzzleView puzzleView;

	private final String easyPuzzle = "360000000004230800000004200"
			+ "070460003820000014500013020" + "001900000007048300000000045";
	private final String mediumPuzzle = "650000070000506000014000005"
			+ "007009000002314700000700800" + "500000630000201000030000097";
	private final String hardPuzzle = "009000000080605020501078000"
			+ "000000700706040102004000000" + "000720903090301080000000600";

	private static final String PREF_PUZZLE = "puzzle";
	protected static final int DIFFICULTY_CONTINUE = -1;

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		Log.d(TAG, "onRestoreInstanceState");
	}
	
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

	private int[] getPuzzle(int diff) {
		String puz;
		switch (diff) {
		case DIFFICULTY_CONTINUE:
			puz = getPreferences(MODE_PRIVATE).getString(PREF_PUZZLE,
					easyPuzzle);
			break;
		case DIFFICULTY_MEDIUM:
			puz = mediumPuzzle;
			break;
		case DIFFICULTY_HARD:
			puz = hardPuzzle;
			break;
		case DIFFICULTY_EASY:
		default:
			puz = easyPuzzle;
			break;
		}
		return fromPuzzleString(puz);
	}

	private static String toPuzzleString(int[] puz) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < puz.length; i++) {
			result.append(puz[i]);
		}
		return result.toString();
	}

	protected static int[] fromPuzzleString(String puz) {
		int[] result = new int[puz.length()];
		int index = 0;
		for (char ch : puz.toCharArray()) {
			result[index++] = ch - '0';
		}
		return result;
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
		int[] tiles = getUsedTiles(selX, selY);
		if (tile != 0) {
			for (int i = 0; i < tiles.length; i++) {
				if (tile == tiles[i])
					return false;
			}
		}
		setTile(selX, selY, tile);
		calculateUsedTiles();
		return true;
	}

	public int[] getUsedTiles(int x, int y) {
		return used[x][y];
	}

	/** Compute the two dimensional array of used tiles */
	private void calculateUsedTiles() {
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				used[x][y] = calculateUsedTiles(x, y);
				// Log.d(TAG, "used[" + x + "][" + y + "] = "
				// + toPuzzleString(used[x][y]));
			}
		}
	}

	/** Compute the used tiles visible from this position */
	private int[] calculateUsedTiles(int x, int y) {
		int c[] = new int[9];
		// horizontal
		for (int i = 0; i < 9; i++) {
			if (i == x)
				continue;
			int t = getTile(i, y);
			if (t != 0)
				c[t - 1] = t;
		}
		// vertical
		for (int i = 0; i < 9; i++) {
			if (i == y)
				continue;
			int t = getTile(x, i);
			if (t != 0)
				c[t - 1] = t;
		}
		// same cell block
		int startx = (x / 3) * 3;
		int starty = (y / 3) * 3;
		for (int i = startx; i < startx + 3; i++) {
			for (int j = starty; j < starty + 3; j++) {
				if (i == x && j == y)
					continue;
				int t = getTile(i, j);
				if (t != 0)
					c[t - 1] = t;
			}
		}
		// compress
		int nused = 0;
		for (int t : c) {
			if (t != 0)
				nused++;
		}
		int c1[] = new int[nused];
		nused = 0;
		for (int t : c) {
			if (t != 0)
				c1[nused++] = t;
		}
		return c1;
	}

	private int getTile(int x, int y) {
		return puzzle[y * 9 + x];
	}

	private void setTile(int x, int y, int tile) {
		puzzle[y * 9 + x] = tile;
	}

	protected String getTileString(int x, int y) {
		int tile = getTile(x, y);
		if (tile == 0)
			return "";
		else
			return String.valueOf(tile);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (Prefs.getMusic(this))
			Music.play(this, R.raw.fresh_power);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (Prefs.getMusic(this))
			Music.stop(this);

		// save current puzzle
		getPreferences(MODE_PRIVATE).edit()
				.putString(PREF_PUZZLE, toPuzzleString(puzzle)).commit();
	}
}
