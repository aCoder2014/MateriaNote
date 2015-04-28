package data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Song on 2015/4/27.
 */
public class NoteDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "noteDatabaseHelper";

    public static final String DATABASE_NAME = "personal_note";

    public static final String TABLE_NOTE = "note";

    public static final int DB_VERSION = 5;

    private static NoteDatabaseHelper mNoteDatabaseHelper;

    private static final String CREATE_TABLE_NOTE = "create table "
            + TABLE_NOTE + "(" + " _id integer primary key autoincrement,"
            + "content text," + "_time text" + ")";

    private NoteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    public static synchronized NoteDatabaseHelper getInstance(Context context) {
        if (mNoteDatabaseHelper == null) {
            mNoteDatabaseHelper = new NoteDatabaseHelper(context);
        }
        return mNoteDatabaseHelper;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_NOTE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table "+TABLE_NOTE);
        db.execSQL(CREATE_TABLE_NOTE);
    }

}
