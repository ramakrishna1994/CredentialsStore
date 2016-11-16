package goodcreed.locker.locker;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

    public static SQLiteDatabase db;
    public static String database = Properties.DATABASE;
    public static String table = Properties.TABLE_NAME;
    public static String[] cols = Properties.COLUMN_NAMES;
    public static String[] colTypes = Properties.COLUMN_TYPES;
    public String query = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db=openOrCreateDatabase(database, Context.MODE_PRIVATE, null);
    }



    public void showAllResults(View v){
        final DetailsEntry de = new DetailsEntry();
        Cursor c = de.selectAllFromDB(db);
        setContentView(R.layout.all_details_layout);
        final ArrayList<String> detailNames = de.getAllElementsAsList(db,1);
        final ArrayList<String> detailIDs = de.getAllElementsAsList(db,0);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,detailNames);
        ListView lv = (ListView)findViewById(R.id.lv);
        lv.setAdapter(arrayAdapter);
        showIndividualRecord(lv,detailIDs,detailNames);

    }

    public void showIndividualRecord(ListView lv, final ArrayList<String> detailIDs, final ArrayList<String> detailNames)
    {
        final DetailsEntry de = new DetailsEntry();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("DEBUG",detailIDs.get(position) + "-------"+detailNames.get(position));
                setContentView(R.layout.individual_details_layout);
                TextView detailID = (TextView)findViewById(R.id.detailID);
                EditText description = (EditText)findViewById(R.id.description);
                EditText passcode = (EditText)findViewById(R.id.passcode);

                ArrayList<String> indDetails = de.selectSpecificRecordFromDB(db,Integer.parseInt(detailIDs.get(position)));

                detailID.setText(indDetails.get(0));
                description.setText(indDetails.get(1));
                passcode.setText(indDetails.get(2));
            }
        });
    }

    public void deleteRecordFromDB(View v)
    {
        DetailsEntry de = new DetailsEntry();
        TextView id = (TextView)findViewById(R.id.detailID);
        int detailID = Integer.parseInt(id.getText().toString());
        de.deleteRecordFromDB(db,detailID);
        Toast.makeText(getApplicationContext(),"Deleted the record",Toast.LENGTH_SHORT).show();
        closeView(v);

    }

    public void showNewRecordToUser(View v)
    {
        setContentView(R.layout.add_details_layout);

    }

    public void addRecordToDB(View v){
        EditText description = (EditText)findViewById(R.id.descriptionValue);
        EditText passcode = (EditText)findViewById(R.id.passcodeValue);
        DetailsEntry de = new DetailsEntry();
        de.insertIntoDB(db,description.getText().toString().trim(),passcode.getText().toString().trim());
        Toast.makeText(getApplicationContext(),"Saved Successfully",Toast.LENGTH_SHORT).show();
        closeView(v);
    }

    public void updateRecordToDB(View v){
        EditText description = (EditText)findViewById(R.id.description);
        EditText passcode = (EditText)findViewById(R.id.passcode);
        TextView detailID = (TextView) findViewById(R.id.detailID);

        String des = description.getText().toString().trim();
        String pass = passcode.getText().toString().trim();
        int id = Integer.parseInt(detailID.getText().toString().trim());

        DetailsEntry de = new DetailsEntry();
        de.updateRecordInDB(db,id,des,pass);

        Toast.makeText(getApplicationContext(),"Updated the record",Toast.LENGTH_SHORT).show();
        closeView(v);
    }


    public void closeView(View v)
    {
        setContentView(R.layout.activity_main);
    }

    public void showSecretCodeChangeLayout(View v)
    {
        setContentView(R.layout.change_secret_password_layout);
    }

    public void updateSecretCode(View v)
    {

        String oldPass = ((EditText)findViewById(R.id.currentPassword)).getText().toString().trim();
        String newPass = ((EditText)findViewById(R.id.newPassword)).getText().toString().trim();
        Cursor c = db.rawQuery("SELECT * FROM secrettable;",null);
        while(c.moveToNext()) {
            if(c.getString(1).equals(oldPass)){
                db.execSQL("update secrettable set password='"+newPass+"' where id=1;");
                Toast.makeText(getApplicationContext(),"Updated Successfully",Toast.LENGTH_SHORT).show();
                closeView(v);
            }
            else{
                Toast.makeText(getApplicationContext(),"Incorrect details",Toast.LENGTH_SHORT).show();
            }

        }
    }
}
