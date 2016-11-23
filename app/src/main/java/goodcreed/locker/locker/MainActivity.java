package goodcreed.locker.locker;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

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
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        km = new KeyManager(getApplicationContext());
        km.setIv(iv.getBytes());
        km.setId(key.getBytes());
        this.cntx = getApplicationContext();
        crypto = new Crypto(cntx);
        setContentView(R.layout.activity_main);
        db = openOrCreateDatabase(database, Context.MODE_PRIVATE, null);

    }


    public void showAllResults(View v) throws Exception {
        final DetailsEntry de = new DetailsEntry(crypto, km);
        Cursor c = de.selectAllFromDB(db);
        setContentView(R.layout.all_details_layout);
        final ArrayList<String> detailNames = de.getAllElementsAsListEncrypted(db, 1);
        final ArrayList<String> detailIDs = de.getAllElementsAsListUnEncrypted(db, 0);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, detailNames){
            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view =super.getView(position, convertView, parent);
                TextView textView=(TextView) view.findViewById(android.R.id.text1);

                /*YOUR CHOICE OF COLOR*/
                textView.setTextColor(Color.WHITE);

                return view;
            }
        };
        ListView lv = (ListView) findViewById(R.id.lv);
        lv.setAdapter(arrayAdapter);
        showIndividualRecord(lv, detailIDs, detailNames);

    }

    public void showIndividualRecord(ListView lv, final ArrayList<String> detailIDs, final ArrayList<String> detailNames) throws Exception {
        final DetailsEntry de = new DetailsEntry(crypto, km);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("DEBUG", detailIDs.get(position) + "-------" + detailNames.get(position));
                setContentView(R.layout.individual_details_layout);
                TextView detailID = (TextView) findViewById(R.id.detailID);
                EditText description = (EditText) findViewById(R.id.description);
                EditText passcode = (EditText) findViewById(R.id.passcode);

                ArrayList<String> indDetails = de.selectSpecificRecordFromDB(db, Integer.parseInt(detailIDs.get(position)));

                detailID.setText(indDetails.get(0));
                description.setText(indDetails.get(1));
                passcode.setText(indDetails.get(2));
            }
        });
    }

    public void deleteRecordFromDB(View v) {
        final DetailsEntry de = new DetailsEntry(crypto, km);
        TextView id = (TextView) findViewById(R.id.detailID);
        int detailID = Integer.parseInt(id.getText().toString());
        de.deleteRecordFromDB(db, detailID);
        Toast.makeText(getApplicationContext(), "Deleted the record!!", Toast.LENGTH_SHORT).show();
        closeView(v);

    }

    public void showNewRecordToUser(View v) {
        setContentView(R.layout.add_details_layout);

    }

    public void addRecordToDB(View v) {
        EditText description = (EditText) findViewById(R.id.descriptionValue);
        EditText passcode = (EditText) findViewById(R.id.passcodeValue);

        String des = description.getText().toString().trim();
        String pass = passcode.getText().toString().trim();

        if (des.equals("")) {
            Toast.makeText(getApplicationContext(), "Please Enter Description", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pass.equals("")) {
            Toast.makeText(getApplicationContext(), "Please Enter Passcode", Toast.LENGTH_SHORT).show();
            return;
        }

        final DetailsEntry de = new DetailsEntry(crypto, km);
        de.insertIntoDB(db, des, pass);
        Toast.makeText(getApplicationContext(), "Saved Successfully", Toast.LENGTH_SHORT).show();
        closeView(v);
    }

    public void updateRecordToDB(View v) {
        EditText description = (EditText) findViewById(R.id.description);
        EditText passcode = (EditText) findViewById(R.id.passcode);
        TextView detailID = (TextView) findViewById(R.id.detailID);

        String des = description.getText().toString().trim();
        String pass = passcode.getText().toString().trim();
        int id = Integer.parseInt(detailID.getText().toString().trim());

        if (des.equals("")) {
            Toast.makeText(getApplicationContext(), "Please Enter Description", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pass.equals("")) {
            Toast.makeText(getApplicationContext(), "Please Enter Passcode", Toast.LENGTH_SHORT).show();
            return;
        }

        final DetailsEntry de = new DetailsEntry(crypto, km);
        de.updateRecordInDB(db, id, des, pass);

        Toast.makeText(getApplicationContext(), "Updated the record", Toast.LENGTH_SHORT).show();
        closeView(v);
    }


    public void closeView(View v) {
        setContentView(R.layout.activity_main);
    }

    public void showSecretCodeChangeLayout(View v) {
        setContentView(R.layout.change_secret_password_layout);
    }

    public void updateSecretCode(View v) throws Exception {
        String oldPass = ((EditText) findViewById(R.id.currentPassword)).getText().toString().trim();
        String newPass = ((EditText) findViewById(R.id.newPassword)).getText().toString().trim();
        if (oldPass.equals("")) {
            Toast.makeText(getApplicationContext(), "Please Enter Current Password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (newPass.equals("")) {
            Toast.makeText(getApplicationContext(), "Please Enter New Password", Toast.LENGTH_SHORT).show();
            return;
        }
        Cursor c = db.rawQuery("SELECT * FROM secrettable;", null);
        while (c.moveToNext()) {
            if ((crypto.armorDecrypt(c.getString(1))).equals(oldPass)) {
                db.execSQL("update secrettable set password='" + crypto.armorEncrypt(newPass.getBytes()) + "' where id=1;");
                Toast.makeText(getApplicationContext(), "Updated Successfully!!", Toast.LENGTH_SHORT).show();
                closeView(v);
            } else {
                Toast.makeText(getApplicationContext(), "Incorrect Password!!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        db.close();
        Log.d("Entered","E");
        finish();
    }




}
