package quizHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import database.handler.JujitsuStudyDBAdapter;

public class Quiz {
	private ArrayList<Question> moves = new ArrayList<Question>();
	private ArrayList<Bitmap> images = new ArrayList<Bitmap>();
	private JujitsuStudyDBAdapter db;
	private int imageNumber = 0;
	private int questionNumber = 0;
	private final String fileLoc = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/Jujitsu/";
	
	
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
		randomQuestions();
	}

	public void LoadImages() {
		Cursor c = db.fetchImages(moves.get(questionNumber).getMoveID());
		if (c.getCount() > 1) {
			c.moveToFirst();
			while (!c.isAfterLast()) {
				images.add(BitmapFactory.decodeFile(fileLoc + c.getString(c.getColumnIndex(JujitsuStudyDBAdapter.COLUMN_VALUES[JujitsuStudyDBAdapter.IMAGE_TABLE][3]))));
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
		if (imageNumber == images.size()-1){}
		else
			imageNumber += 1;
	}
	
	public void prevImage() {
		if (imageNumber == 0){}
		else
			imageNumber -= 1;
	}

	public void nextQuestion() {
		questionNumber += 1;
		imageNumber = 0;
		images.clear();
	}

	public String getText() {
		return moves.get(questionNumber).getWhatIs();
	}
	
	public String[] getAnswerSet(){
		String[] answers = null;
		Random randomAnswerSlot = new Random();
		
		if(moves.size() > 4){
			answers = new String[4];
			answers[0] = moves.get(questionNumber).getWhatIs();
			for(int x = 1; x < answers.length; x++){
				int spot = randomAnswerSlot.nextInt(moves.size());
				while(Arrays.asList(answers).contains(moves.get(spot).getWhatIs())){
					spot = randomAnswerSlot.nextInt(moves.size());
				}
				answers[x] = moves.get(spot).getWhatIs();
			}
		}
		
		for(int x = 0; x < answers.length; x++){
			String temp = answers[x];
			int spot = randomAnswerSlot.nextInt(answers.length);
			answers[x] = answers[spot];
			answers[spot] = temp;
		}
		return answers;
		
	}

	public void randomQuestions(){
		Random randomAnswerSlot = new Random();
		for(int x = 0; x < moves.size(); x++){
			Question temp = moves.get(x);
			int spot = randomAnswerSlot.nextInt(moves.size());
			moves.set(x, moves.get(spot));
			moves.set(spot, temp);
		}
	}
	
	public boolean checkAnswer(String s){
		if(moves.get(questionNumber).checkAnswer(s))
			return true;
		else
			return false;
	}
}
