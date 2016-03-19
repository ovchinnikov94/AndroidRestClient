package com.example.dmitry.androidrestclient;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dmitry.androidrestclient.data.Credit;
import com.example.dmitry.androidrestclient.db.DbService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private static final String BASE_URL = "http://mighty-depths-10490.herokuapp.com/";
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private Gson gson;
    private ListView lvMain;
    private List<Credit> credits;
    private TextView textView;
    private CreditListAdapter adapter;
    private SwipeRefreshLayout swipeLayout;
    public RestService service;
    private FloatingActionButton fab;
    public DbService dbService;

    class GetCreditsTask extends AsyncTask <RestService, Void, List<Credit>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<Credit> creditList) {
            super.onPostExecute(creditList);
            if (creditList != null) {
                credits.clear();
                dbService.saveCredit(creditList);
                credits.addAll(dbService.selectCredits());
                Calendar calendar = Calendar.getInstance();
                textView.setText("Updated: " + calendar.get(Calendar.HOUR_OF_DAY) + ":"
                        + String.format("%02d", calendar.get(Calendar.MINUTE)));
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected List<Credit> doInBackground(RestService... params) {
            RestService service = params[0];
            try {
                return service.getCredits().execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    class SaveCreditTask extends AsyncTask<Object, Void, Boolean>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean.booleanValue()) {
                Snackbar.make(findViewById(R.id.fab), "Saved successfully!", Snackbar.LENGTH_SHORT)
                        .show();
            }
            else {
                Snackbar.make(findViewById(R.id.fab), "Some error occurred while saving!", Snackbar.LENGTH_SHORT)
                        .show();
            }
        }

        @Override
        protected Boolean doInBackground(Object... params) {
            RestService service = (RestService)params[0];
            Credit credit = (Credit)params[1];
            try {
                return new Boolean(service.saveCredit(credit).execute().isSuccess());
            } catch (IOException e) {
                return new Boolean(false);
            }
        }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbService = new DbService(getApplicationContext());

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
                adb.setTitle("Enter info:");
                final LinearLayout linearLayout = new LinearLayout(MainActivity.this);
                final EditText nameInput = new EditText(MainActivity.this);
                final EditText sumInput = new EditText(MainActivity.this);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.addView(nameInput);
                linearLayout.addView(sumInput);
                nameInput.setInputType(InputType.TYPE_CLASS_TEXT);
                nameInput.setWidth(30);
                sumInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                sumInput.setWidth(30);
                adb.setView(linearLayout);
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("OK", new AlertDialog.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Credit credit = new Credit(nameInput.getText().toString(),  Integer.valueOf(sumInput.getText().toString()));
                        SaveCreditTask task = new SaveCreditTask();
                        task.execute(service, credit);
                        new GetCreditsTask().execute(service);
                    }
                });

                adb.show();

            }
        });
        lvMain = (ListView)findViewById(R.id.lvMain);
        credits = dbService.selectCredits();
        textView = (TextView)findViewById(R.id.text1);
        swipeLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_container);

        gson = new GsonBuilder()
                .setDateFormat(DATE_FORMAT)
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        service = retrofit.create(RestService.class);

        lvMain.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (lvMain != null && lvMain.getChildCount() > 0)
                    if (lvMain.getFirstVisiblePosition() == 0 && lvMain.getChildAt(0).getTop() == 0)
                        swipeLayout.setEnabled(true);
                    else swipeLayout.setEnabled(false);
                else
                    swipeLayout.setEnabled(false);
            }
        });
        swipeLayout.setEnabled(true);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeLayout.setRefreshing(true);
                GetCreditsTask getCreditsTask = new GetCreditsTask();
                getCreditsTask.execute(service);
                swipeLayout.setRefreshing(false);
            }
        });
        adapter = new CreditListAdapter(this, this, credits);
        adapter.notifyDataSetChanged();
        lvMain.setAdapter(adapter);

        //GetCreditsTask task = new GetCreditsTask();
        //task.execute(service);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.select_year:

        }

        return super.onOptionsItemSelected(item);
    }
}
