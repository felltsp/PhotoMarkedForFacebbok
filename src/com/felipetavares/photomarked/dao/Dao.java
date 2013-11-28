package com.felipetavares.photomarked.dao;

import com.felipetavares.photomarked.util.PhotosTableKeys;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Dao {
	
	String NOME_BANCO = "dba";
	int VERSAO_BANCO = 1;
	String SQL_CREATE_TABLE = "create table " + PhotosTableKeys.TABLE_NAME.getKey() + " " +
			"("+PhotosTableKeys.COLUMN_ID.getKey() +" integer primary key autoincrement, "
			+ PhotosTableKeys.COLUMN_ID_PHOTO.getKey() + " text not null);";
	
	final protected Context context;
	OpenHelper openHelper;
	SQLiteDatabase db;

	public Dao(Context ctx) {
		this.context = ctx;
		openHelper = new OpenHelper(context);
	}

	private class OpenHelper extends SQLiteOpenHelper {
		OpenHelper(Context context) {
			super(context, NOME_BANCO, null, VERSAO_BANCO);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			try {
				db.execSQL(SQL_CREATE_TABLE);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS contacts");
			onCreate(db);
		}
	}

	public void open() throws SQLException {
		db = openHelper.getWritableDatabase();
	}

	public void close() {
		openHelper.close();
	}
}
