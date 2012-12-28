package database.handler;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataHandler extends SQLiteOpenHelper {
	// database creation

	private static final String DATABASE_CREATE_VERSION = "CREATE TABLE version (ID float primary key, desc text);";
	private static final String DATABASE_CREATE_ADULT_RANKS = "CREATE TABLE adultRanks (ID integer primary key, Belt text not null);";
	private static final String DATABASE_CREATE_CATEGORY = "CREATE TABLE category (ID integer primary key, AreaMoveIsIn text not null);";
	private static final String DATABASE_CREATE_MOVES = "CREATE TABLE moves (ID integer primary key, Moves text not null, Category integer not null, "
			+ "ARank integer, FOREIGN KEY(Category) REFERENCES category(ID), FOREIGN KEY(ARank) REFERENCES adultRanks(ID));";
	private static final String DATABASE_CREATE_IMAGES = "CREATE TABLE images (PrimaryKey integer primary key autoincrement, PicID integer not null, PicNum integer not null, Picture string not null unique," +
			" FOREIGN KEY(PicID) REFERENCES moves(ID));";

	private static final String DATABASE_CREATE_QUERY_VERSION = "CREATE VIEW get_version AS SELECT Max(ID) as ID, desc FROM version";
	private static final String DATABASE_CREATE_QUERY_SECTIONS_BASED_ON_BELT = "CREATE VIEW sections_for_belt AS SELECT DISTINCT Belt, AreaMoveIsIn FROM category, adultRanks, moves " +
			"WHERE adultRanks.ID=moves.ARank AND category.ID=moves.Category";
	public static final String QUERY_SECTIONS_BASED_ON_BELT = "sections_for_belt";
	public static final String QUERY_VERSION = "get_version";

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "YudanshaStudy";

	public static final String[] tableNames = { "adultRanks", "category",
			"moves", "images", "version"};

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE_ADULT_RANKS);  //no foreign key
		database.execSQL(DATABASE_CREATE_CATEGORY);		//no foreign key
		database.execSQL(DATABASE_CREATE_MOVES);		//rank and category FK
		database.execSQL(DATABASE_CREATE_IMAGES);		//moves FK
		database.execSQL(DATABASE_CREATE_VERSION);      //version
		database.execSQL(DATABASE_CREATE_QUERY_VERSION); //version query
		database.execSQL(DATABASE_CREATE_QUERY_SECTIONS_BASED_ON_BELT); //easy access to moves
	}

	public DataHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.e("kepano", "Upgrading data base from " + oldVersion + " to "
				+ newVersion + "\nWARNING this drops the table");
		// probably never use this

	}

}
