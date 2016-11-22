package goodcreed.locker.locker;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import static goodcreed.locker.locker.InitialLock.database;
import static goodcreed.locker.locker.InitialLock.table;

/**
 * Created by rs186098 on 18-Nov-16.
 */

public class DbResultsCheck extends AppCompatActivity {
    public static SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db=openOrCreateDatabase(database, Context.MODE_PRIVATE, null);
        Cursor c = db.rawQuery("SELECT * FROM "+table+" ;",null);
        while(c.moveToNext()){
            try{
                Log.d("id",c.getString(0));
                Log.d("Des",c.getString(1));
                Log.d("Pass",c.getString(2));

            }catch(Exception e) {
                Log.d("Error",e.getMessage());
            }

        }
    }

}
