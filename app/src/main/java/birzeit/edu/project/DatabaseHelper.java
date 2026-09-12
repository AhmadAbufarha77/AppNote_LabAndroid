package birzeit.edu.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    { super(context, name, factory, version); }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE USERS(" +
                        "EMAIL TEXT PRIMARY KEY, " +
                        "FIRST_NAME TEXT, " +
                        "LAST_NAME TEXT, " +
                        "PASSWORD TEXT)"
        );
        db.execSQL(
                "CREATE TABLE NOTES(" +
                        "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "TITLE TEXT NOT NULL, " +
                        "CONTENT TEXT, " +
                        "TAG TEXT, " +
                        "CREATION_DATE TEXT, " +
                        "FAVORITE INTEGER DEFAULT 0)"
        );








    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean emailExists(String email) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT EMAIL FROM USERS WHERE EMAIL = ?",
                new String[]{email}
        );

        boolean exists = cursor.getCount() > 0;

        cursor.close();

        return exists;
    }

    public void insertUser(User user) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("EMAIL", user.getEmail());
        contentValues.put("FIRST_NAME", user.getFirstName());
        contentValues.put("LAST_NAME", user.getLastName());
        contentValues.put("PASSWORD", user.getPassword());
        sqLiteDatabase.insert("USERS", null, contentValues);
    }

    public boolean checkLogin(String email, String password) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT EMAIL FROM USERS WHERE EMAIL = ? AND PASSWORD =?",
                new String[]{email,password}
        );

        boolean exists = cursor.getCount() > 0;

        cursor.close();

        return exists;
    }




































    public long insertNote(AddNote note) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("TITLE", note.getTitle());
        values.put("CONTENT", note.getContent());
        values.put("TAG", note.getTag());
        values.put("CREATION_DATE", note.getCreationDate());
        values.put("FAVORITE", note.isFavorite() ? 1 : 0);

        return db.insert("NOTES", null, values);
    }

    public java.util.ArrayList<AddNote> getAllNotes() {

        java.util.ArrayList<AddNote> notes =
                new java.util.ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM NOTES ORDER BY ID DESC",
                null
        );

        if (cursor.moveToFirst()) {

            do {

                AddNote note = new AddNote();

                note.setId(
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow("ID")
                        )
                );

                note.setTitle(
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("TITLE")
                        )
                );

                note.setContent(
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("CONTENT")
                        )
                );

                note.setTag(
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("TAG")
                        )
                );

                note.setCreationDate(
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("CREATION_DATE")
                        )
                );

                note.setFavorite(
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow("FAVORITE")
                        ) == 1
                );

                notes.add(note);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return notes;
    }

}
