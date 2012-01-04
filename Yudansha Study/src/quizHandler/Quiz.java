package quizHandler;

import java.util.ArrayList;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import database.handler.JujitsuStudyDBAdapter;

public class Quiz {
	private ArrayList<Question> moves = new ArrayList<Question>();
	private ArrayList<Bitmap> images = new ArrayList<Bitmap>();
	private JujitsuStudyDBAdapter db;
	private int imageNumber = 0;
	private int questionNumber = 0;

	public Quiz(String rank, String category, JujitsuStudyDBAdapter db) {
		this.db = db;
		this.db.open();
		Cursor c;
		String rankID = db.getAdultRankID(rank);
		String catID = db.getCategoryID(category);
		if (category.equals("all"))
			c = db.getMoves(rankID);
		else
			c = db.getMoves(rankID, catID);
		if (c.getCount() > 0) {
			c.moveToFirst();
			while (!c.isAfterLast()) {
				Question q = new Question(
						c.getString(c
								.getColumnIndex(JujitsuStudyDBAdapter.COLUMN_VALUES[JujitsuStudyDBAdapter.MOVES_TABLE][0])),
						c.getString(c
								.getColumnIndex(JujitsuStudyDBAdapter.COLUMN_VALUES[JujitsuStudyDBAdapter.MOVES_TABLE][1])));
				moves.add(q);
				c.moveToNext();
			}
		}

	}

	public void LoadImages() {
		Cursor c = db.fetchImages(moves.get(questionNumber).getMoveID());
		if (c.getCount() > 1) {
			c.moveToFirst();
			while (!c.isAfterLast()) {
				byte[] blob = c.getBlob(c.getColumnIndex("Picture"));
				Log.e("kepano", "BLOB: " + blob.length);
				images.add(BitmapFactory.decodeByteArray(blob, 0, blob.length));
				c.moveToNext();
			}
		}
	}

	public Bitmap getImage() {
		if(images.size()==0)
			LoadImages();
		if (imageNumber == -1 || imageNumber == images.size())
			return null;
		else
			return images.get(imageNumber);
	}

	public void nextImage() {
		imageNumber += 1;
	}

	public void nextQuestion() {
		questionNumber += 1;
		imageNumber = 0;
		images.clear();
	}

	public String getText() {
		return moves.get(questionNumber).getWhatIs();
	}
}
