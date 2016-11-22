package goodcreed.locker.locker;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class InitialLock extends AppCompatActivity {

    public static SQLiteDatabase db;
    public static String database = Properties.DATABASE;
    public static String table = Properties.TABLE_NAME;
    public static String[] cols = Properties.COLUMN_NAMES;
    public static String[] colTypes = Properties.COLUMN_TYPES;
    public String query = "";
    public static String key = Properties.KEY;
    public static String iv = Properties.IV;
    public KeyManager km;
    private Context cntx;
    public Crypto crypto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        km = new KeyManager(getApplicationContext());
        km.setIv(iv.getBytes());
        km.setId(key.getBytes());
        this.cntx = getApplicationContext();
        crypto = new Crypto(cntx);
        setContentView(R.layout.activity_initial_lock);
        DBAndTableCreation();
        Cursor c = db.rawQuery("SELECT * FROM secrettable;",null);
        TextView alertText = (TextView)findViewById(R.id.textView3);
        try{
            while(c.moveToNext()) {

                if(!(crypto.armorDecrypt(c.getString(1))).equals("1234")){
                    alertText.setText("");
                }
                else{
                    alertText.setText("As You are accessing for the first time , please enter 1234 as password. Please change it as soon as log in!!");
                }

            }
        }catch (InvalidKeyException e) {
            Log.e("SE3", "Exception in StoreData: " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            Log.e("SE3", "Exception in StoreData: " + e.getMessage());
        } catch (NoSuchPaddingException e) {
            Log.e("SE3", "Exception in StoreData: " + e.getMessage());
        } catch (IllegalBlockSizeException e) {
            Log.e("SE3", "Exception in StoreData: " + e.getMessage());
        } catch (BadPaddingException e) {
            Log.e("SE3", "Exception in StoreData: " + e.getMessage());
        } catch (InvalidAlgorithmParameterException e) {
            Log.e("SE3", "Exception in StoreData: " + e.getMessage());
        }


    }

    public void DBAndTableCreation(){
        db=openOrCreateDatabase(database, Context.MODE_PRIVATE, null);
        for(int i=0;i<cols.length;i++)
            query += " "+cols[i]+" "+colTypes[i]+",";

        db.execSQL("CREATE TABLE IF NOT EXISTS "+table+"("+query.substring(0,query.length()-1)+");");
        db.execSQL("CREATE TABLE IF NOT EXISTS secrettable(id integer primary key,password text)");
        try{
            String encryptedPasscode = crypto.armorEncrypt("1234".getBytes());
            Log.d("Encrypetd one : ",encryptedPasscode);
            db.execSQL("INSERT INTO secrettable values(1,'"+encryptedPasscode+"')");
        }catch(Exception e) {
            Log.d("ERROR","Row already exists");
        }

    }


    public boolean checkForPassword(View v) throws Exception{
        String secretCode = ((EditText)findViewById(R.id.password)).getText().toString().trim();
        if(secretCode.equals("")){
            Toast.makeText(getApplicationContext(),"Please Enter Password",Toast.LENGTH_SHORT).show();
            return false;
        }
        Intent i = null;
        Cursor c = db.rawQuery("SELECT * FROM secrettable;",null);
        while(c.moveToNext()) {
            if((crypto.armorDecrypt(c.getString(1))).equals(secretCode)){
                i = new Intent(InitialLock.this, MainActivity.class);
                startActivity(i);
                finish();
            }
            else{
                Toast.makeText(getApplicationContext(),"Incorrect Password",Toast.LENGTH_SHORT).show();
            }

        }
        return true;
    }

    public void showDB(View v) {
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
