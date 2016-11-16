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
import android.widget.Toast;

public class InitialLock extends AppCompatActivity {

    public static SQLiteDatabase db;
    public static String database = Properties.DATABASE;
    public static String table = Properties.TABLE_NAME;
    public static String[] cols = Properties.COLUMN_NAMES;
    public static String[] colTypes = Properties.COLUMN_TYPES;
    public String query = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_lock);
        DBAndTableCreation();
    }

    public void DBAndTableCreation(){
        db=openOrCreateDatabase(database, Context.MODE_PRIVATE, null);
        for(int i=0;i<cols.length;i++)
            query += " "+cols[i]+" "+colTypes[i]+",";
        Log.d("query",query);
        db.execSQL("CREATE TABLE IF NOT EXISTS "+table+"("+query.substring(0,query.length()-1)+");");
        db.execSQL("CREATE TABLE IF NOT EXISTS secrettable(id integer primary key,password text)");
        try{
            db.execSQL("INSERT INTO secrettable values(1,'1234')");
        }catch(Exception e) {
            Log.d("ERROR","Row already exists");
        }

    }


    public void checkForPassword(View v) {
        String secretCode = ((EditText)findViewById(R.id.password)).getText().toString().trim();
        Intent i = null;
        Cursor c = db.rawQuery("SELECT * FROM secrettable;",null);
        while(c.moveToNext()) {
            if(c.getString(1).equals(secretCode)){
                i = new Intent(InitialLock.this, MainActivity.class);
                startActivity(i);
                finish();
            }
            else{
                Toast.makeText(getApplicationContext(),"Password is incorrect",Toast.LENGTH_SHORT).show();
            }

        }
    }

}
