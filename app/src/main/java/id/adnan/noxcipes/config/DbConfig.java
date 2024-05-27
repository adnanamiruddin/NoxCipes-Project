package id.adnan.noxcipes.config;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DbConfig extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "db_noxcipes";
    private static final int DATABASE_VERSION = 1;
    public static final String USERS_TABLE_NAME = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String RECIPES_TABLE_NAME = "favorites";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_RECIPE_ID = "recipe_id";

    public DbConfig(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + USERS_TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_EMAIL + " TEXT UNIQUE,"
                + COLUMN_PASSWORD + " TEXT)");

        db.execSQL("CREATE TABLE " + RECIPES_TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_ID + " INTEGER,"
                + COLUMN_RECIPE_ID + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public Cursor insertUser(String name, String email, String password) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO " + USERS_TABLE_NAME + " (" + COLUMN_NAME + ", " + COLUMN_EMAIL + ", " + COLUMN_PASSWORD + ") VALUES ('" + name + "', '" + email + "', '" + password + "')");
        return db.rawQuery("SELECT * FROM " + USERS_TABLE_NAME + " WHERE " + COLUMN_EMAIL + " = '" + email + "'", null);
    }

    public Cursor login(String email, String password) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + USERS_TABLE_NAME + " WHERE " + COLUMN_EMAIL + " = '" + email + "' AND " + COLUMN_PASSWORD + " = '" + password + "'", null);
    }

    public Cursor getUserDataById(int userId) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + USERS_TABLE_NAME + " WHERE " + COLUMN_ID + " = " + userId, null);
    }

    public void updateProfile(int userId, String name, String email, String password) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE " + USERS_TABLE_NAME + " SET " + COLUMN_NAME + " = '" + name + "', " + COLUMN_EMAIL + " = '" + email + "', " + COLUMN_PASSWORD + " = '" + password + "' WHERE " + COLUMN_ID + " = " + userId);
        db.close();
    }

    public void insertFavorite(int userId, int recipeId) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO " + RECIPES_TABLE_NAME + " (" + COLUMN_USER_ID + ", " + COLUMN_RECIPE_ID + ") VALUES (" + userId + ", " + recipeId + ")");
        db.close();
    }

    public Cursor getFavoritesByUserId(int userId) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + RECIPES_TABLE_NAME + " WHERE " + COLUMN_USER_ID + " = " + userId, null);
    }

    public boolean isFavorite(int userId, int recipeId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + RECIPES_TABLE_NAME + " WHERE " + COLUMN_USER_ID + " = " + userId + " AND " + COLUMN_RECIPE_ID + " = " + recipeId, null);
        return cursor.getCount() > 0;
    }

    public void deleteFavorite(int userId, int recipeId) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + RECIPES_TABLE_NAME + " WHERE " + COLUMN_USER_ID + " = " + userId + " AND " + COLUMN_RECIPE_ID + " = " + recipeId);
        db.close();
    }
}
