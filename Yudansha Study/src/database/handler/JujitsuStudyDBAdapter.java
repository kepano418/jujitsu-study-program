package database.handler;

import java.util.ArrayList;
import java.util.Map;

import parser.Parser;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class JujitsuStudyDBAdapter {
	public static final String DB_TABLE = "YudanshaStudy";
	private Parser parse;

	public static final int ADULT_RANK_TABLE = 0;
	public static final int CATEGORY_TABLE = 1;
	public static final int MOVES_TABLE = 2;
	public static final int IMAGE_TABLE = 3;
	public static final int VERSION_TABLE = 4;

	public static final String[][] COLUMN_VALUES = { 
		    { "ID", "Belt" }, // Adult Rank Table
			{ "ID", "AreaMoveIsIn" }, // Category Table
			{ "ID", "Moves", "Category", "ARank" }, // Moves Table
			{ "PrimaryKey", "PicID", "PicNum", "Picture" }, // Image Table
			{ "ID", "desc"} //version
	};

	private Context context;
	private SQLiteDatabase db;
	private DataHandler dbHandler;

	public JujitsuStudyDBAdapter(Context context) {
		this.context = context;
	}

	public JujitsuStudyDBAdapter open() throws SQLException {
		dbHandler = new DataHandler(context);
		db = dbHandler.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHandler.close();
	}

	public boolean insertValues(int tableValue, String[] values) {
		String table = DataHandler.tableNames[tableValue];
		ContentValues c_values = createValues(values, tableValue);

		insert(table, c_values);
		return true;
	}

	private void insert(String table, ContentValues values) {
		db.insert(table, null, values);
	}

	public boolean updateValues() {
		return true;
	}

	public boolean deleteValues() {
		return true;
	}

	private ContentValues createValues(String[] holder, int table) {
		ContentValues values = new ContentValues();

		for (int x = 0; x < holder.length; x++) {
			values.put(COLUMN_VALUES[table][x], holder[x]);
		}

		return values;
	}

	public void addData(String FileLoc) {
		final String[] nodesNames = { "ARanks", "Category", "MoveData" };

		parse = new Parser(FileLoc);
		Map<String, ArrayList<Map<String, String>>> data = parse.parse();

		for (int x = 0; x < nodesNames.length; x++) {
			ArrayList<Map<String, String>> listData = data.get(nodesNames[x]);
			String[] dataValues = new String[COLUMN_VALUES[x].length];
			for (int y = 0; y < listData.size(); y++) {
				Map<String, String> innerData = listData.get(y);
				for (int z = 0; z < dataValues.length; z++) {
					dataValues[z] = innerData.get(COLUMN_VALUES[x][z]);
				}
				insertValues(x, dataValues);
			}

		}

	}

	public Cursor fetchAdultRanks() {
		return db.query(DataHandler.tableNames[ADULT_RANK_TABLE],
				new String[] { COLUMN_VALUES[ADULT_RANK_TABLE][1] }, null,
				null, null, null, COLUMN_VALUES[ADULT_RANK_TABLE][0]);
	}

	public Cursor fetchSectionSelect(String selection) {
		return db.query(DataHandler.QUERY_SECTIONS_BASED_ON_BELT,
				new String[] { COLUMN_VALUES[CATEGORY_TABLE][1] },
				COLUMN_VALUES[ADULT_RANK_TABLE][1] + "='" + selection + "'",
				null, null, null, null);
	}

	public Cursor fetchImage() {
		return db.query(DataHandler.tableNames[IMAGE_TABLE], new String[] {
				COLUMN_VALUES[IMAGE_TABLE][1], COLUMN_VALUES[IMAGE_TABLE][3] },
				COLUMN_VALUES[IMAGE_TABLE][0] + "=1", null, null, null, null);
	}

	public Cursor fetchImages(String i) {
		return db
				.query(DataHandler.tableNames[IMAGE_TABLE], new String[] {
						COLUMN_VALUES[IMAGE_TABLE][2],
						COLUMN_VALUES[IMAGE_TABLE][3] },
						COLUMN_VALUES[IMAGE_TABLE][1] + "=" + i, null, null,
						null, null);
	}

	public void updateImage(ContentValues c) {
		db.insert(DataHandler.tableNames[JujitsuStudyDBAdapter.IMAGE_TABLE],
				null, c);
	}

	public void clearImageTable() {
		db.execSQL("DELETE FROM images;");

	}

	public Cursor getMoveID(String moveName) {
		return db.query(DataHandler.tableNames[MOVES_TABLE],
				new String[] { COLUMN_VALUES[MOVES_TABLE][0] },
				COLUMN_VALUES[MOVES_TABLE][1] + "='" + moveName + "'", null,
				null, null, null);
	}

	public Cursor getPictureNum(String moveID) {
		return db.query(DataHandler.tableNames[IMAGE_TABLE],
				new String[] { COLUMN_VALUES[IMAGE_TABLE][2] },
				COLUMN_VALUES[IMAGE_TABLE][1] + "=" + moveID, null, null, null,
				COLUMN_VALUES[IMAGE_TABLE][2]);
	}

	public Cursor getMoves(String rank) {
		return db.query(DataHandler.tableNames[MOVES_TABLE], new String[] {
				COLUMN_VALUES[MOVES_TABLE][0], COLUMN_VALUES[MOVES_TABLE][1],
				COLUMN_VALUES[MOVES_TABLE][2] }, COLUMN_VALUES[MOVES_TABLE][3]
				+ "='" + rank + "'", null, null, null, null);
	}

	public Cursor getMoves(String rank, String category) {
		return db.query(DataHandler.tableNames[MOVES_TABLE], new String[] {
				COLUMN_VALUES[MOVES_TABLE][0], COLUMN_VALUES[MOVES_TABLE][1],
				COLUMN_VALUES[MOVES_TABLE][2] }, COLUMN_VALUES[MOVES_TABLE][3]
				+ "='" + rank + "' AND " + COLUMN_VALUES[MOVES_TABLE][2] + "='"
				+ category + "'", null, null, null, null);
	}

	public String getAdultRankID(String rank) {
		Cursor c = db.query(DataHandler.tableNames[ADULT_RANK_TABLE],
				new String[] { COLUMN_VALUES[ADULT_RANK_TABLE][0] },
				COLUMN_VALUES[ADULT_RANK_TABLE][1] + "='" + rank + "'", null,
				null, null, null);
		if(c.getCount() == 1){
			c.moveToFirst();
			return c.getString(c.getColumnIndex(COLUMN_VALUES[ADULT_RANK_TABLE][0]));
		}
		else
			return "";
	}
	
	public String getCategoryID(String category) {
		Cursor c = db.query(DataHandler.tableNames[CATEGORY_TABLE],
				new String[] { COLUMN_VALUES[CATEGORY_TABLE][0] },
				COLUMN_VALUES[CATEGORY_TABLE][1] + "='" + category + "'", null,
				null, null, null);
		if(c.getCount() == 1){
			c.moveToFirst();
			return c.getString(c.getColumnIndex(COLUMN_VALUES[CATEGORY_TABLE][0]));
		}
		else
			return "";
	}
	
	public Cursor fetchVersion() {
		return db.query(DataHandler.QUERY_VERSION,
				null, null,
				null, null, null, null);
	}
	
}
