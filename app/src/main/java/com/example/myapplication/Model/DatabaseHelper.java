package com.example.myapplication.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TAG = "DatabaseHelper";

    public static final String TABLE_NAME = "student_table";

    public static final String COL1 = "studentid";
    public static final String COL2 = "name";
    public static final String COL3 = "surname";
    public static final String COL4 = "fathername";
    public static final String COL5 = "nationalid";
    public static final String COL6 = "dob";
    public static final String COL7 = "gender";


    public DatabaseHelper(@Nullable Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String table = "CREATE TABLE " + TABLE_NAME + " (" +
                COL1 + " TEXT PRIMARY KEY, " +
                COL2 + " TEXT NOT NULL, " +
                COL3 + " TEXT NOT NULL, " +
                COL4 + " TEXT NOT NULL, " +
                COL5 + " INTEGER NOT NULL, " +
                COL6 + " TEXT NOT NULL, " +
                COL7 + " TEXT NOT NULL);";
        db.execSQL(table);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String id, String name, String surname, String fathername,
                           String nationalid, String dob, String gender) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, id);
        contentValues.put(COL2, name);
        contentValues.put(COL3, surname);
        contentValues.put(COL4, fathername);
        contentValues.put(COL5, nationalid);
        contentValues.put(COL6, dob);
        contentValues.put(COL7, gender);

        Cursor c = null;
        boolean tableExists = false;
        /* get cursor on it */
        try {
            c = db.query(TABLE_NAME, null,
                    null, null, null, null, null);
            System.out.println("----------------Exists-------------------");
            tableExists = true;
        } catch (Exception e) {
            System.out.println("----------------Not Exists-------------------");
        }
        if (tableExists) {
            db.insert("student_table", null, contentValues);
            Cursor cu = db.rawQuery("SELECT * FROM student_table ", null);
            if (cu.moveToFirst()) {
                do {
                    // Passing values
                    String column1 = cu.getColumnName(0);
                    String column2 = cu.getColumnName(1);
                    String column3 = cu.getColumnName(2);
                    String column4 = cu.getColumnName(3);
                    String column5 = cu.getColumnName(4);
                    String column6 = cu.getColumnName(5);
                    String column7 = cu.getColumnName(6);
                    String value1 = cu.getString(0);
                    String value2 = cu.getString(1);
                    String value3 = cu.getString(2);
                    String value4 = cu.getString(3);
                    int value5 = cu.getInt(4);
                    String value6 = cu.getString(5);
                    String value7 = cu.getString(6);
                    System.out.println("One: " + column1);
                    System.out.println("Two: " + column2);
                    System.out.println("Three: " + column3);
                    System.out.println("Four: " + column4);
                    System.out.println("Five: " + column5);
                    System.out.println("Six: " + column6);
                    System.out.println("Seven: " + column7);
                    System.out.println("------------------------------------------------");
                    System.out.println("Value One: " + value1);
                    System.out.println("Value Two: " + value2);
                    System.out.println("Value Three: " + value3);
                    System.out.println("Value Four: " + value4);
                    System.out.println("Value Five: " + value5);
                    System.out.println("Value Six: " + value6);
                    System.out.println("Value Seven: " + value7);
                    // Do something Here with values
                } while (cu.moveToNext());
            }
            cu.close();
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<StudentInformation> getAllDataFromSQlite() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cu = db.rawQuery("SELECT * FROM student_table ", null);
        ArrayList<StudentInformation> studentInformations = new ArrayList<>();
        if (cu.moveToFirst()) {
            do {

                StudentInformation information = new StudentInformation(null, null, null,
                        null, null, null, null);
                information.setId(cu.getString(0));
                information.setName(cu.getString(1));
                information.setSurName(cu.getString(2));
                information.setFatherName(cu.getString(3));
                information.setNationalId(cu.getString(4));
                information.setDob(cu.getString(5));
                information.setGender(cu.getString(6));

                studentInformations.add(information);
            } while (cu.moveToNext());
        }
        cu.close();

        return studentInformations;
    }

    public void deleteDataFromSQlite(String id) {

        String deleteQueury = "DELETE FROM " + TABLE_NAME + " WHERE studentid =" + "'" + id + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(deleteQueury);
    }

    public void updateDataInSQlite(String id, String name, String surname, String fathername,
                                   String nationalid, String dob, String gender) {
        SQLiteDatabase db = this.getWritableDatabase();
        /*ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, name);
        contentValues.put(COL3, surname);
        contentValues.put(COL4, fathername);
        contentValues.put(COL5, nationalid);
        contentValues.put(COL6, dob);
        contentValues.put(COL7, gender);*/


//        db.update(TABLE_NAME, contentValues, "studentid = " + "'" + id + "'", new String[]{id});
        String updateQueury = "UPDATE student_table SET name= " + "'" + name + "'," + "surname= " + "'" + surname + "',"
                + "fathername= " + "'" + fathername + "',"
                + "nationalid= " + "'" + nationalid + "',"
                + "dob= " + "'" + dob + "',"
                + "gender= " + "'" + gender + "'"
                + " WHERE studentid= " + "'" + id + "'";
        db.execSQL(updateQueury);

    }
}
