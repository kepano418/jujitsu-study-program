package com.kepano.jujitsustudy;

import java.util.ArrayList;

import database.handler.JujitsuStudyDBAdapter;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class YudanshaStudyActivity extends Activity {
	/** Called when the activity is first created. */
	final String FILE_LOC = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/Jujitsu/JujitsuData.xml";
	private ArrayAdapter<CharSequence> rankAdapter;
	private ArrayAdapter<CharSequence> sectionAapter;
	private Spinner rankSelect, catSelect;
	private ArrayList<String> list;
	private JujitsuStudyDBAdapter db;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		db = new JujitsuStudyDBAdapter(this);
		db.open();
		//db.addData(FILE_LOC);

		setRankSelectSpinner();
	}

	public void onClickStartButton(View target) {

		// regular
		Intent i = new Intent("com.kepano.jujitsustudy.QuizImageActivity");
		i.putExtra("rank", rankSelect.getSelectedItem().toString());
		if (catSelect.getSelectedItem().toString().equals("All Sections"))
			i.putExtra("category", "all");
		else
			i.putExtra("category", catSelect.getSelectedItem().toString());

		// admin needs to add images to DB
		// Intent i = new Intent("com.kepano.jujitsustudy.AdminPanel");

		startActivity(i);
	}

	private void setSectionSelectSpinner(String selection) {
		try {
			Cursor section = db.fetchSectionSelect(selection);
			list = new ArrayList<String>();
			list.add("All Sections");
			if (section.moveToFirst()) {
				do {
					list.add(section.getString(section
							.getColumnIndex(JujitsuStudyDBAdapter.COLUMN_VALUES[JujitsuStudyDBAdapter.CATEGORY_TABLE][1])));
				} while (section.moveToNext());
			}

			catSelect = (Spinner) findViewById(R.id.sectionselect);

			sectionAapter = new ArrayAdapter<CharSequence>(
					this,
					android.R.layout.simple_spinner_item,
					(CharSequence[]) list.toArray(new CharSequence[list.size()]));
			sectionAapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			catSelect.setAdapter(sectionAapter);
		} catch (Exception e) {
			Log.e("kepano", "sectionAdapter: ", e);

		}
	}

	private void setRankSelectSpinner() {
		Cursor belts = db.fetchAdultRanks();
		list = new ArrayList<String>();
		if (belts.moveToFirst()) {
			do {
				list.add(belts.getString(belts
						.getColumnIndex(JujitsuStudyDBAdapter.COLUMN_VALUES[JujitsuStudyDBAdapter.ADULT_RANK_TABLE][1])));
			} while (belts.moveToNext());
		}

		rankSelect = (Spinner) findViewById(R.id.rankselect);

		rankAdapter = new ArrayAdapter<CharSequence>(this,
				android.R.layout.simple_spinner_item,
				(CharSequence[]) list.toArray(new CharSequence[list.size()]));
		rankAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		rankSelect.setAdapter(rankAdapter);

		rankSelect.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {
				setSectionSelectSpinner(parentView.getItemAtPosition(position)
						.toString());

			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});

	}

}