package com.example.notesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class Database  extends SQLiteOpenHelper {

    Context context;
    private static final String DatabaseName = "MyNotes";
    private static final int DatabaseVersion = 1;

    private static final String tableName = "mynotes";
    private static final String columnId = "id";
    private static final String columnTitle = "title";
    private static final String columnDescription = "description";



    public Database(@Nullable Context context) {
        super(context, DatabaseName,null,DatabaseVersion);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE "+tableName+
                " ("+columnId+ " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                columnTitle+ " TEXT, "+
                columnDescription +" Text);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ tableName);
        onCreate(db);
    }

    public void  addNotes(String title, String desc){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(columnTitle,title);
        cv.put(columnDescription,desc);

        long resultValue = db.insert(tableName,null,cv);

        if (resultValue == -1){
            Toast.makeText(context, "Datos no añadidos.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Datos añadidos correctamente", Toast.LENGTH_SHORT).show();
        }

    }

    Cursor readNotes(){
        String query = "SELECT * FROM "+  tableName;
        SQLiteDatabase database= this.getReadableDatabase();

        Cursor cursor= null;
        if (database!= null){
            cursor = database.rawQuery(query,null);
        }
        return  cursor;
    }

    void deleteAllNotes(){
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "DELETE FROM "+ tableName;
        database.execSQL(query);
    }


    void updateNotes(String title,String desc , String id){
        SQLiteDatabase database =  this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(columnTitle,title);
        contentValues.put(columnDescription,desc);

        long resut  = database.update(tableName,contentValues,"id=?",new String[]{id});
        if (resut == -1){
            Toast.makeText(context, "Fallido", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Exitoso!", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteSingleItem(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(tableName,"id=?", new String[]{id});
        if (result == -1){
            Toast.makeText(context, "No se pudo borrar", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Borrado exitosamente", Toast.LENGTH_SHORT).show();
        }
    }
}
