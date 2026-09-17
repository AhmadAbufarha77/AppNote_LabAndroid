package birzeit.edu.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    { super(context, name, factory, version); }

    @Override // Creates tables when database is first created.
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
                        "FAVORITE INTEGER DEFAULT 0," +
                        "USER_EMAIL TEXT)"
        );

    }

    @Override // Updates tables when database version changes.
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL(
                    "CREATE TABLE NOTES(" +
                            "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "TITLE TEXT NOT NULL, " +
                            "CONTENT TEXT, " +
                            "TAG TEXT, " +
                            "CREATION_DATE TEXT, " +
                            "FAVORITE INTEGER DEFAULT 0," +
                            "USER_EMAIL TEXT)"
            );
        }
        if (oldVersion < 3) {
            db.execSQL(
                    "ALTER TABLE NOTES ADD COLUMN USER_EMAIL TEXT"
            );
        }
    }

    public boolean emailExists(String email) { // Checks email before Sign Up.

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT EMAIL FROM USERS WHERE EMAIL = ?",
                new String[]{email}
        );

        boolean exists = cursor.getCount() > 0;

        cursor.close();

        return exists;
    }

    public void insertUser(User user) { // Saves a new user during Sign Up.
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("EMAIL", user.getEmail());
        contentValues.put("FIRST_NAME", user.getFirstName());
        contentValues.put("LAST_NAME", user.getLastName());
        contentValues.put("PASSWORD", user.getPassword());
        sqLiteDatabase.insert("USERS", null, contentValues);
    }

    public boolean checkLogin(String email, String password) { // Checks email and password during Sign In.

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT EMAIL FROM USERS WHERE EMAIL = ? AND PASSWORD =?",
                new String[]{email,password}
        );

        boolean exists = cursor.getCount() > 0;

        cursor.close();

        return exists;
    }

    public User getUserByEmail(String email) {// Gets user data for Profile.

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM USERS WHERE EMAIL = ?",
                new String[]{email}
        );

        User user = null;

        if (cursor.moveToFirst()) {
            user = new User();

            user.setEmail(cursor.getString(0));
            user.setFirstName(cursor.getString(1));
            user.setLastName(cursor.getString(2));
            user.setPassword(cursor.getString(3));
        }

        cursor.close();

        return user;
    }

    public void updateUser(User user) { // Updates user data from Edit Profile.

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("FIRST_NAME", user.getFirstName());
        values.put("LAST_NAME", user.getLastName());
        values.put("PASSWORD", user.getPassword());

        db.update(
                "USERS",
                values,
                "EMAIL = ?",
                new String[]{user.getEmail()}
        );
    }
    public long insertNote(AddNote note) {// Saves a new note from Add Note.

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("TITLE", note.getTitle());
        values.put("CONTENT", note.getContent());
        values.put("TAG", note.getTag());
        values.put("CREATION_DATE", note.getCreationDate());
        values.put("FAVORITE", note.isFavorite() ? 1 : 0);
        values.put("USER_EMAIL", note.getUserEmail());

        return db.insert("NOTES", null, values);
    }

    public java.util.ArrayList<AddNote> getAllNotes(String email) {// Gets all notes for All Notes page.

        java.util.ArrayList<AddNote> notes =
                new java.util.ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM NOTES WHERE USER_EMAIL = ? ORDER BY ID DESC",
                new String[]{email}
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
    public void updateFavorite(int noteId, boolean favorite) {// Updates favorite status when star is clicked.

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("FAVORITE", favorite ? 1 : 0);

        db.update(
                "NOTES",
                values,
                "ID = ?",
                new String[]{String.valueOf(noteId)}
        );
    }
    public ArrayList<AddNote> getSortedNotes(String email, String sortOrder) {// Gets notes for Sorted page.

        ArrayList<AddNote> notes = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        String query;

        if (sortOrder.equals("alphabetical")) {

            query = "SELECT * FROM NOTES " +
                    "WHERE USER_EMAIL = ? " +
                    "ORDER BY TITLE ASC";

        } else {

            query = "SELECT * FROM NOTES " +
                    "WHERE USER_EMAIL = ? " +
                    "ORDER BY ID DESC";
        }

        Cursor cursor = db.rawQuery(
                query,
                new String[]{email}
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

                note.setUserEmail(
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("USER_EMAIL")
                        )
                );

                notes.add(note);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return notes;
    }
    public ArrayList<AddNote> getFavoriteNotes(String email) {// Gets favorite notes for Favorites page.

        ArrayList<AddNote> notes = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM NOTES WHERE USER_EMAIL = ? AND FAVORITE = 1 ORDER BY ID DESC",
                new String[]{email}
        );

        if (cursor.moveToFirst()) {

            do {

                AddNote note = new AddNote();

                note.setId(cursor.getInt(
                        cursor.getColumnIndexOrThrow("ID")
                ));

                note.setTitle(cursor.getString(
                        cursor.getColumnIndexOrThrow("TITLE")
                ));

                note.setContent(cursor.getString(
                        cursor.getColumnIndexOrThrow("CONTENT")
                ));

                note.setTag(cursor.getString(
                        cursor.getColumnIndexOrThrow("TAG")
                ));

                note.setCreationDate(cursor.getString(
                        cursor.getColumnIndexOrThrow("CREATION_DATE")
                ));

                note.setFavorite(
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow("FAVORITE")
                        ) == 1
                );

                note.setUserEmail(cursor.getString(
                        cursor.getColumnIndexOrThrow("USER_EMAIL")
                ));

                notes.add(note);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return notes;
    }
    public int updateNote(// Updates note from Edit Note.
            int noteId,
            String title,
            String content,
            String tag
    ) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("TITLE", title);
        values.put("CONTENT", content);
        values.put("TAG", tag);

        return db.update(
                "NOTES",
                values,
                "ID = ?",
                new String[]{String.valueOf(noteId)}
        );
    }


    public int deleteNote(int noteId) {// Deletes note from Note Details.

        SQLiteDatabase db = getWritableDatabase();

        return db.delete(
                "NOTES",
                "ID = ?",
                new String[]{String.valueOf(noteId)}
        );
    }


    public AddNote getNoteById(int noteId) {// Gets one note for Note Details/Edit.

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM NOTES WHERE ID = ?",
                new String[]{String.valueOf(noteId)}
        );

        AddNote note = null;

        if (cursor.moveToFirst()) {

            note = new AddNote();

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

            note.setUserEmail(
                    cursor.getString(
                            cursor.getColumnIndexOrThrow("USER_EMAIL")
                    )
            );
        }

        cursor.close();

        return note;
    }
    public ArrayList<AddNote> searchNotes(// Searches notes by title or content.
            String email,
            String searchText
    ) {

        ArrayList<AddNote> notes = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        String search = "%" + searchText + "%";

        Cursor cursor = db.rawQuery(
                "SELECT * FROM NOTES " +
                        "WHERE USER_EMAIL = ? " +
                        "AND (TITLE LIKE ? OR CONTENT LIKE ?) " +
                        "ORDER BY ID DESC",
                new String[]{
                        email,
                        search,
                        search
                }
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

                note.setUserEmail(
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("USER_EMAIL")
                        )
                );

                notes.add(note);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return notes;
    }
    public ArrayList<String> getUserTags(String email) {// Gets tags for the tag filter.

        ArrayList<String> tags = new ArrayList<>();

        tags.add("All Tags");

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT DISTINCT TAG FROM NOTES " +
                        "WHERE USER_EMAIL = ? " +
                        "AND TAG IS NOT NULL " +
                        "AND TRIM(TAG) != '' " +
                        "ORDER BY TAG COLLATE NOCASE ASC",
                new String[]{email}
        );

        if (cursor.moveToFirst()) {

            do {

                String tag = cursor.getString(0);

                tags.add(tag);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return tags;
    }
    public ArrayList<AddNote> getNotesByTag(// Gets notes for selected tag.
            String email,
            String tag
    ) {

        ArrayList<AddNote> notes =
                new ArrayList<>();

        SQLiteDatabase db =
                getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM NOTES " +
                        "WHERE USER_EMAIL = ? AND TAG = ? " +
                        "ORDER BY ID DESC",
                new String[]{
                        email,
                        tag
                }
        );

        if (cursor.moveToFirst()) {

            do {

                AddNote note =
                        new AddNote();

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

                note.setUserEmail(
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("USER_EMAIL")
                        )
                );

                notes.add(note);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return notes;
    }
}
