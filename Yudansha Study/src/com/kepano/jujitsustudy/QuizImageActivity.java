package com.kepano.jujitsustudy;

import quizHandler.Quiz;
import database.handler.JujitsuStudyDBAdapter;
import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class QuizImageActivity extends Activity{
	private ImageView displayImage;
	private Button answer1, answer2, answer3, answer4;
	private TextView displayText;
	private Quiz quizer;
	private String rank;
	private String category;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_quiz_layout);
		JujitsuStudyDBAdapter db;
		
		rank = getIntent().getExtras().getString("rank");
		category = getIntent().getExtras().getString("category");
		quizer = new Quiz(rank, category, new JujitsuStudyDBAdapter(this));
		loadObjects();
		loadImage();
		loadText();
	}
	

	private void loadObjects() {
		displayImage = (ImageView) findViewById(R.id.quizDisplayImage);
		answer1 = (Button) findViewById(R.id.quizAnswer1);
		answer2 = (Button) findViewById(R.id.quizAnswer2);
		answer3 = (Button) findViewById(R.id.quizAnswer3);
		answer4 = (Button) findViewById(R.id.quizAnswer4);
		displayText = (TextView) findViewById(R.id.quizDisplayText);
	}
	
	public void onClick(View target){
		switch(target.getId()){
		case R.id.quizAnswer1:
			nextImage();
			break;
		case R.id.quizAnswer2:
			nextQuestion();
			break;
		case R.id.quizAnswer3:
			prevImage();
			break;
		case R.id.quizAnswer4:
			break;
		}
	}
	public void nextQuestion(){
		quizer.nextQuestion();
		loadText();
		loadImage();
	}
	
	public void nextImage(){
		quizer.nextImage();
		loadImage();
	}
	
	public void prevImage(){
		quizer.prevImage();
		loadImage();
	}
	
	public void loadImage(){
		displayImage.setImageBitmap(quizer.getImage());
	}
	
	public void loadText(){
		displayText.setText(quizer.getText());
	}
}
