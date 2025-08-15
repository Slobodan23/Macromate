package com.example.macromate.baza;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.macromate.model.Korisnik;
import com.example.macromate.model.Obrok;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "macroMateDB";
    private static Database instance;

    public Database(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public static synchronized Database getInstance(Context context) {
        if (instance == null) {
            instance = new Database(context.getApplicationContext());
        }
        return instance;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // KORISNIK
        String SQL_KORISNIK = String.format(
                "CREATE TABLE IF NOT EXISTS %s (" +
                        "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s INTEGER, " +
                        "%s REAL, " +
                        "%s INTEGER);",
                Korisnik.TABLE_NAME,
                Korisnik.FIELD_ID,
                Korisnik.FIELD_EMAIL,
                Korisnik.FIELD_LOZINKA,
                Korisnik.FIELD_IME,
                Korisnik.FIELD_PREZIME,
                Korisnik.FIELD_GODINE,
                Korisnik.FIELD_KILAZA,
                Korisnik.FIELD_VISINA
        );
        db.execSQL(SQL_KORISNIK);

        // OBROK
        String SQL_OBROK = String.format(
                "CREATE TABLE IF NOT EXISTS %s (" +
                        "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s TEXT, " +
                        "%s REAL, " +
                        "%s REAL, " +
                        "%s REAL, " +
                        "%s REAL);",
                Obrok.TABLE_NAME,
                Obrok.FIELD_ID,
                Obrok.FIELD_IME,
                Obrok.FIELD_KCAL,
                Obrok.FIELD_FAT,
                Obrok.FIELD_CARB,
                Obrok.FIELD_PROTEIN
        );
        db.execSQL(SQL_OBROK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(String.format("DROP TABLE IF EXISTS %s;", Obrok.TABLE_NAME));
        db.execSQL(String.format("DROP TABLE IF EXISTS %s;", Korisnik.TABLE_NAME));
        onCreate(db);
    }

    // ------------------ KORISNIK ------------------ //
    public long addKorisnik(String email, String lozinka, String ime, String prezime, int godine, float kilaza, int visina) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(Korisnik.FIELD_EMAIL, email);
        cv.put(Korisnik.FIELD_LOZINKA, lozinka);
        cv.put(Korisnik.FIELD_IME, ime);
        cv.put(Korisnik.FIELD_PREZIME, prezime);
        cv.put(Korisnik.FIELD_GODINE, godine);
        cv.put(Korisnik.FIELD_KILAZA, kilaza);
        cv.put(Korisnik.FIELD_VISINA, visina);

        return db.insert(Korisnik.TABLE_NAME, null, cv);
    }

    public void editKorisnik(long id, String email, String lozinka, String ime, String prezime, int godine, float kilaza, int visina) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(Korisnik.FIELD_EMAIL, email);
        cv.put(Korisnik.FIELD_LOZINKA, lozinka);
        cv.put(Korisnik.FIELD_IME, ime);
        cv.put(Korisnik.FIELD_PREZIME, prezime);
        cv.put(Korisnik.FIELD_GODINE, godine);
        cv.put(Korisnik.FIELD_KILAZA, kilaza);
        cv.put(Korisnik.FIELD_VISINA, visina);

        db.update(Korisnik.TABLE_NAME, cv, Korisnik.FIELD_ID + "=?", new String[]{String.valueOf(id)});
    }

    public int deleteKorisnik(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(Korisnik.TABLE_NAME, Korisnik.FIELD_ID + "=?", new String[]{String.valueOf(id)});
    }

    public Korisnik getKorisnikById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = String.format("SELECT * FROM %s WHERE %s = ?", Korisnik.TABLE_NAME, Korisnik.FIELD_ID);
        Cursor result = db.rawQuery(query, new String[]{String.valueOf(id)});

        if (result.moveToFirst()) {
            Korisnik k = new Korisnik(
                    id,
                    result.getString(result.getColumnIndexOrThrow(Korisnik.FIELD_EMAIL)),
                    result.getString(result.getColumnIndexOrThrow(Korisnik.FIELD_LOZINKA)),
                    result.getString(result.getColumnIndexOrThrow(Korisnik.FIELD_IME)),
                    result.getString(result.getColumnIndexOrThrow(Korisnik.FIELD_PREZIME)),
                    result.getInt(result.getColumnIndexOrThrow(Korisnik.FIELD_GODINE)),
                    result.getFloat(result.getColumnIndexOrThrow(Korisnik.FIELD_KILAZA)),
                    result.getInt(result.getColumnIndexOrThrow(Korisnik.FIELD_VISINA))
            );
            result.close();
            return k;
        }
        result.close();
        return null;
    }

    public List<Korisnik> getAllKorisnici() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery(String.format("SELECT * FROM %s", Korisnik.TABLE_NAME), null);

        List<Korisnik> list = new ArrayList<>();
        if (result.moveToFirst()) {
            do {
                list.add(new Korisnik(
                        result.getLong(result.getColumnIndexOrThrow(Korisnik.FIELD_ID)),
                        result.getString(result.getColumnIndexOrThrow(Korisnik.FIELD_EMAIL)),
                        result.getString(result.getColumnIndexOrThrow(Korisnik.FIELD_LOZINKA)),
                        result.getString(result.getColumnIndexOrThrow(Korisnik.FIELD_IME)),
                        result.getString(result.getColumnIndexOrThrow(Korisnik.FIELD_PREZIME)),
                        result.getInt(result.getColumnIndexOrThrow(Korisnik.FIELD_GODINE)),
                        result.getFloat(result.getColumnIndexOrThrow(Korisnik.FIELD_KILAZA)),
                        result.getInt(result.getColumnIndexOrThrow(Korisnik.FIELD_VISINA))
                ));
            } while (result.moveToNext());
        }
        result.close();
        return list;
    }
    public Korisnik getKorisnikByEmailAndPassword(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = String.format("SELECT * FROM %s WHERE %s = ? AND %s = ?",
                Korisnik.TABLE_NAME, Korisnik.FIELD_EMAIL, Korisnik.FIELD_LOZINKA);
        Cursor result = db.rawQuery(query, new String[]{email, password});

        if (result.moveToFirst()) {
            Korisnik k = new Korisnik(
                    result.getLong(result.getColumnIndexOrThrow(Korisnik.FIELD_ID)),
                    result.getString(result.getColumnIndexOrThrow(Korisnik.FIELD_EMAIL)),
                    result.getString(result.getColumnIndexOrThrow(Korisnik.FIELD_LOZINKA)),
                    result.getString(result.getColumnIndexOrThrow(Korisnik.FIELD_IME)),
                    result.getString(result.getColumnIndexOrThrow(Korisnik.FIELD_PREZIME)),
                    result.getInt(result.getColumnIndexOrThrow(Korisnik.FIELD_GODINE)),
                    result.getFloat(result.getColumnIndexOrThrow(Korisnik.FIELD_KILAZA)),
                    result.getInt(result.getColumnIndexOrThrow(Korisnik.FIELD_VISINA))
            );
            result.close();
            return k;
        }
        result.close();
        return null;
    }

    // Check if email already exists in database
    public boolean emailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = String.format("SELECT 1 FROM %s WHERE %s = ?",
                Korisnik.TABLE_NAME, Korisnik.FIELD_EMAIL);
        Cursor result = db.rawQuery(query, new String[]{email});

        boolean exists = result.moveToFirst();
        result.close();
        return exists;
    }

    // ------------------ OBROK ------------------ //
    public void addObrok(String ime, float kcal, float fat, float carb, float protein) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(Obrok.FIELD_IME, ime);
        cv.put(Obrok.FIELD_KCAL, kcal);
        cv.put(Obrok.FIELD_FAT, fat);
        cv.put(Obrok.FIELD_CARB, carb);
        cv.put(Obrok.FIELD_PROTEIN, protein);

        db.insert(Obrok.TABLE_NAME, null, cv);
    }

    public void editObrok(long id, String ime, float kcal, float fat, float carb, float protein) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(Obrok.FIELD_IME, ime);
        cv.put(Obrok.FIELD_KCAL, kcal);
        cv.put(Obrok.FIELD_FAT, fat);
        cv.put(Obrok.FIELD_CARB, carb);
        cv.put(Obrok.FIELD_PROTEIN, protein);

        db.update(Obrok.TABLE_NAME, cv, Obrok.FIELD_ID + "=?", new String[]{String.valueOf(id)});
    }

    public int deleteObrok(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(Obrok.TABLE_NAME, Obrok.FIELD_ID + "=?", new String[]{String.valueOf(id)});
    }

    public Obrok getObrokById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = String.format("SELECT * FROM %s WHERE %s = ?", Obrok.TABLE_NAME, Obrok.FIELD_ID);
        Cursor result = db.rawQuery(query, new String[]{String.valueOf(id)});

        if (result.moveToFirst()) {
            Obrok o = new Obrok(
                    id,
                    result.getString(result.getColumnIndexOrThrow(Obrok.FIELD_IME)),
                    result.getFloat(result.getColumnIndexOrThrow(Obrok.FIELD_KCAL)),
                    result.getFloat(result.getColumnIndexOrThrow(Obrok.FIELD_FAT)),
                    result.getFloat(result.getColumnIndexOrThrow(Obrok.FIELD_CARB)),
                    result.getFloat(result.getColumnIndexOrThrow(Obrok.FIELD_PROTEIN))
            );
            result.close();
            return o;
        }
        result.close();
        return null;
    }

    public List<Obrok> getAllObroci() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery(String.format("SELECT * FROM %s", Obrok.TABLE_NAME), null);

        List<Obrok> list = new ArrayList<>();
        if (result.moveToFirst()) {
            do {
                list.add(new Obrok(
                        result.getLong(result.getColumnIndexOrThrow(Obrok.FIELD_ID)),
                        result.getString(result.getColumnIndexOrThrow(Obrok.FIELD_IME)),
                        result.getFloat(result.getColumnIndexOrThrow(Obrok.FIELD_KCAL)),
                        result.getFloat(result.getColumnIndexOrThrow(Obrok.FIELD_FAT)),
                        result.getFloat(result.getColumnIndexOrThrow(Obrok.FIELD_CARB)),
                        result.getFloat(result.getColumnIndexOrThrow(Obrok.FIELD_PROTEIN))
                ));
            } while (result.moveToNext());
        }
        result.close();
        return list;
    }
}
