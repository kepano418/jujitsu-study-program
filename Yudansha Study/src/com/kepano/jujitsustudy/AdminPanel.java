package com.kepano.jujitsustudy;

import java.io.File;
import java.util.Arrays;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import database.handler.JujitsuStudyDBAdapter;

public class AdminPanel extends Activity {

	protected final String folderLoc = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/Jujitsu/";
	protected final String[] folders = { "Gokyu", "Ikkyu", "Nikyu", "Sankyu",
			"Yonkyu" };
	protected JujitsuStudyDBAdapter db = new JujitsuStudyDBAdapter(this);
	protected String[] files;
	protected File folder;
	protected int folderSpot = 0, fileSpot = -1, fileCounter = 0;

	protected ImageView imageDisplay;
	protected TextView textDisplay;
	protected EditText input;

	private boolean raiseFlag = false, complete = false;
	private String moveName, mvID, picNum;

	private Thread loadTheData;
	private Handler mHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.massimageadd);

		getViews();

		db.open();
		db.clearImageTable();

		folder = new File(folderLoc + folders[folderSpot]);
		files = folder.list();

		Arrays.sort(files);
		
		
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				updateGUI();
			}
		};

		loadTheData = new Thread() {
			@Override
			public void run() {
				while (!complete)
					while (!raiseFlag) {
						runAll();
					}
				stop();
			}
			
			public void runAll() {
				while (true) {
					fileSpot++;

					if (fileSpot == files.length) {
						folderSpot++;
						fileSpot = 0;
						if(folderSpot == folders.length - 1){
							complete = true;
							break;
						}
						folder = new File(folderLoc + folders[folderSpot]);
						Arrays.sort(files);
						files = folder.list();
					}
					Log.e("kepano", "folderSpot:  " + folderSpot + "/" + (folders.length - 1) + "   fileSpot: " + fileSpot + "/"
							+ (files.length - 1));
					
					moveName = parseFileName(files[fileSpot]);
					getInsertData();
					if (!raiseFlag) {
						putImage();
					}
					Message msg = new Message(); 
			        String textTochange = "";
			        msg.obj = textTochange; 
			        mHandler.sendMessage(msg);
			        fileCounter++;
				}
			}
		};


		loadTheData.start();
	}

	@Override
	protected void onDestroy() {
		loadTheData.stop();
		super.onDestroy();
	}

	public void updateGUI(){
		loadImage();
		textDisplay.setText(moveName + "   mvID: " + mvID + "    picID: "
				+ picNum + "   Files Done: " + fileCounter);
	}

	public void getViews() {
		textDisplay = (TextView) findViewById(R.id.adminAddDisplay);

		imageDisplay = (ImageView) findViewById(R.id.adminAddImageView);

		input = (EditText) findViewById(R.id.adminInput);
	}

	public String parseFileName(String fileName) {
		String[] jpgHolder = fileName.split(".jpg");
		int x = 0;

		for (x = jpgHolder[0].length() - 1; jpgHolder[0].charAt(x) != ' '; x--) {
		}

		return jpgHolder[0].substring(0, x);
	}

	private void getInsertData() {
		Cursor c = db.getMoveID(moveName);
		mvID = "";
		picNum = "";
		if (c.getCount() == 1) {
			c.moveToFirst();
			mvID = c.getString(c
					.getColumnIndex(JujitsuStudyDBAdapter.COLUMN_VALUES[JujitsuStudyDBAdapter.MOVES_TABLE][0]));

			c = db.getPictureNum(mvID);
			if (c.getCount() < 1) {
				picNum = "1";
			} else {
				c.moveToLast();
				picNum = Integer
						.parseInt(c.getString(c
								.getColumnIndex((JujitsuStudyDBAdapter.COLUMN_VALUES[JujitsuStudyDBAdapter.IMAGE_TABLE][2]))))
						+ 1 + "";
			}
			if (mvID == "" && picNum == "") {
				raiseFlag = true;
			}

		}

	}

	private void putImage() {
		ContentValues c = new ContentValues();
		c.put(JujitsuStudyDBAdapter.COLUMN_VALUES[JujitsuStudyDBAdapter.IMAGE_TABLE][3],
				folders[folderSpot] + "/" + files[fileSpot]);
		c.put(JujitsuStudyDBAdapter.COLUMN_VALUES[JujitsuStudyDBAdapter.IMAGE_TABLE][2],
				picNum);
		c.put(JujitsuStudyDBAdapter.COLUMN_VALUES[JujitsuStudyDBAdapter.IMAGE_TABLE][1],
				mvID);
		db.updateImage(c);
	}

	public void submitClick(View target) {
		putImage();
		//runAll();
	}
	
	public void skipClick(View target) {
		raiseFlag = false;
	}


	public void loadImage() {
		imageDisplay.setImageBitmap(BitmapFactory.decodeFile(folder + "/"
				+ files[fileSpot]));
	}

}
