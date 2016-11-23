package goodcreed.locker.locker;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class TermsAndConditions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms_and_conditions_layout);
        Log.d("came","class");
        final ArrayList<String> tAndCond = new ArrayList<String>();
        tAndCond.add("1.This app can be used to store all your details like ATM pin's , bank password's etc.");
        tAndCond.add("2.This app is protected by two layers of security which consists of database details encryption and password protected database.");
        tAndCond.add("3.This app is not connected with internet and will give you full security about your details.");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tAndCond){
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
        ListView lv = (ListView) findViewById(R.id.tandcond);
        lv.setAdapter(arrayAdapter);


    }

}
