package com.kepano.jujitsustudy;

import quizHandler.Quiz;
import database.handler.JujitsuStudyDBAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class QuizImageActivity extends Activity {
	private ImageView displayImage;
	private Button answer1, answer2, answer3, answer4;
	private TextView displayText;
	private Quiz quizer;
	private String rank;
	private String category;

	private float imageTouchX = -1;
	private final float TOUCH_PADDING = 50;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_quiz_layout);
		
		rank = getIntent().getExtras().getString("rank");
		category = getIntent().getExtras().getString("category");
		quizer = new Quiz(rank, category, new JujitsuStudyDBAdapter(this));
		loadObjects();
		loadImage();
		loadText();
		setAnswerButtonText();
	}

	private void loadObjects() {		
		answer1 = (Button) findViewById(R.id.quizAnswer1);
		answer2 = (Button) findViewById(R.id.quizAnswer2);
		answer3 = (Button) findViewById(R.id.quizAnswer3);
		answer4 = (Button) findViewById(R.id.quizAnswer4);
		displayText = (TextView) findViewById(R.id.quizDisplayText);
		displayImage = (ImageView) findViewById(R.id.quizDisplayImage);
		
		displayImage.setOnTouchListener(new OnTouchListener() 
        { 
 
            public boolean onTouch(View v, MotionEvent event) 
            {  
            	if(event.getPointerCount() == 1){
            	if(event.getAction() == MotionEvent.ACTION_DOWN){
            		imageTouchX = event.getX();
            	}
            	else if(event.getAction() == MotionEvent.ACTION_UP){
            		float delta = event.getX() - imageTouchX;
            		if(delta < TOUCH_PADDING)
            			nextImage();
            		else if(delta > -TOUCH_PADDING){
            			prevImage();
            		}
            	}
            	}
                return true; 
            }

       }); 

		
	}

	public void onClick(View target) {
		switch (target.getId()) {
		case R.id.quizAnswer1:
			CheckAnswer(answer1.getText() + "");
			break;
		case R.id.quizAnswer2:
			CheckAnswer(answer2.getText() + "");
			break;
		case R.id.quizAnswer3:
			CheckAnswer(answer3.getText() + "");
			break;
		case R.id.quizAnswer4:
			CheckAnswer(answer4.getText() + "");
			break;
		}
	}
	
	private void CheckAnswer(String answer){
		if(quizer.checkAnswer(answer)){
			Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();
			nextQuestion();
		}
	}

	private void nextQuestion() {
		quizer.nextQuestion();
		loadText();
		loadImage();
		setAnswerButtonText();
	}

	private void nextImage() {
		quizer.nextImage();
		loadImage();
	}

	private void prevImage() {
		quizer.prevImage();
		loadImage();
	}

	private void loadImage() {
		displayImage.setImageBitmap(quizer.getImage());
	}

	private void loadText() {
		displayText.setText(quizer.getText());
	}
	
	private void setAnswerButtonText(){
		String[] answerSet = quizer.getAnswerSet();
		
		if(answerSet.length == 4){
			answer1.setText(answerSet[0]);
			answer2.setText(answerSet[1]);
			answer3.setText(answerSet[2]);
			answer4.setText(answerSet[3]);
		}
	}
}
