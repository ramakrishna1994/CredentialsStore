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
        public static String key = Properties.KEY;
        public static String iv = Properties.IV;
        public KeyManager km;
        private Context cntx;
        public Crypto crypto;

        public DetailsEntry(Crypto crypto,KeyManager km)
        {
            this.crypto = crypto;
            this.km = km;
        }

        public boolean insertIntoDB(SQLiteDatabase db,String description,String passcode)
        {

            try{
                db.execSQL("INSERT INTO "+table+"("+cols[1]+","+cols[2]+") VALUES('"+crypto.armorEncrypt(description.getBytes())+"','"+crypto.armorEncrypt(passcode.getBytes())+"')");
            }catch (Exception e){
                Log.d("error",e.getMessage());
            }
            return true;
        }

        public boolean updateRecordInDB(SQLiteDatabase db,int id,String description,String passcode)
        {
            try{
                db.execSQL("UPDATE  "+table+" SET "+cols[1]+" = '"+crypto.armorEncrypt(description.getBytes())+"',"+cols[2]+"='"+crypto.armorEncrypt(passcode.getBytes())+"' where "+cols[0]+"="+id+";");
            }catch (Exception e){
                Log.d("error",e.getMessage());
            }
            return true;
        }

        public boolean deleteRecordFromDB(SQLiteDatabase db,int id)
        {
            try{
                db.execSQL("DELETE FROM "+table+" where "+cols[0]+"="+id+";");
            }catch (Exception e){
                Log.d("error",e.getMessage());
            }

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
            Cursor c = db.rawQuery("SELECT * FROM "+table+" where "+cols[0]+"="+id+";",null);
            while(c.moveToNext()){
                try{
                    details.add(c.getString(0).toString().trim());
                    details.add(crypto.armorDecrypt(c.getString(1).toString().trim()));
                    details.add(crypto.armorDecrypt(c.getString(2).toString().trim()));
                }catch(Exception e) {
                    Log.d("Error",e.getMessage());
                }

            }
            return details;
        }

        public ArrayList<String> getAllElementsAsListEncrypted(SQLiteDatabase db,int columnId) throws Exception
        {
            int i=1;
            ArrayList<String> details = new ArrayList<String>();
            Cursor c = db.rawQuery("SELECT "+cols[columnId]+" FROM "+table,null);
            while(c.moveToNext()){
                Log.d("data",c.getString(0));
                details.add(crypto.armorDecrypt(c.getString(0)));
                i++;
            }
            return details;
        }

    public ArrayList<String> getAllElementsAsListUnEncrypted(SQLiteDatabase db,int columnId) throws Exception {
        int i=1;
        ArrayList<String> details = new ArrayList<String>();
        Cursor c = db.rawQuery("SELECT "+cols[columnId]+" FROM "+table,null);
        while(c.moveToNext()){
            Log.d("data",c.getString(0));
            details.add(c.getString(0));
            i++;
        }
        return details;
    }




}
