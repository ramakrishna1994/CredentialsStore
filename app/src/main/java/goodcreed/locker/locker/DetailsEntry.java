package goodcreed.locker.locker;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by Ramakrishna Saradhi on 15-Nov-16.
 */

public class DetailsEntry {

        public static String table = Properties.TABLE_NAME;
        public static String[] cols = Properties.COLUMN_NAMES;


        public boolean insertIntoDB(SQLiteDatabase db,String description,String passcode)
        {
            db.execSQL("INSERT INTO "+table+"("+cols[1]+","+cols[2]+") VALUES('"+description+"','"+passcode+"')");
            return true;
        }

        public boolean updateRecordInDB(SQLiteDatabase db,int id,String description,String passcode)
        {
            db.execSQL("UPDATE  "+table+" SET "+cols[1]+" = '"+description+"',"+cols[2]+"='"+passcode+"' where "+cols[0]+"="+id+";");
            return true;
        }

        public boolean deleteRecordFromDB(SQLiteDatabase db,int id)
        {
            db.execSQL("DELETE FROM "+table+" where "+cols[0]+"="+id+";");
            return true;
        }

        public Cursor selectAllFromDB(SQLiteDatabase db)
        {
            Cursor c = db.rawQuery("SELECT * FROM "+table+";",null);
            return c;
        }

        public ArrayList<String> selectSpecificRecordFromDB(SQLiteDatabase db, int id)
        {
            Log.d("id on return",String.valueOf(id));
            ArrayList<String> details = new ArrayList<String>();
            //Cursor c = db.rawQuery("SELECT * FROM "+table+" where "+cols[0]+"="+id+";",null);
            Cursor c = db.rawQuery("SELECT * FROM "+table+" ;",null);
            while(c.moveToNext()){

                details.add(c.getString(0).toString().trim());
                details.add(c.getString(1).toString().trim());
                details.add(c.getString(2).toString().trim());
            }
            return details;
        }

        public ArrayList<String> getAllElementsAsList(SQLiteDatabase db,int columnId)
        {
            int i=1;
            ArrayList<String> details = new ArrayList<String>();
            Cursor c = db.rawQuery("SELECT "+cols[columnId]+" FROM "+table,null);
            while(c.moveToNext()){
                Log.d("data",c.getString(0));
                details.add(c.getString(0).toString().trim());
                i++;
            }
            return details;
        }


}
