package com.example.dmitry.androidrestclient.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dmitry.androidrestclient.MainActivity;
import com.example.dmitry.androidrestclient.R;
import com.example.dmitry.androidrestclient.RestService;
import com.example.dmitry.androidrestclient.adapter.CreditListAdapter;
import com.example.dmitry.androidrestclient.data.Credit;
import com.example.dmitry.androidrestclient.db.DbService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by dmitry on 23.03.16.
 */
public class CreditFragment extends Fragment {

    private View frameView;
    private ListView listView;
    private CreditListAdapter adapter;
    private SwipeRefreshLayout swipeLayout;
    private List<Credit> credits;
    private Gson gson;
    private Retrofit retrofit;
    private RestService service;
    private FloatingActionButton fab;

    class GetCreditsTask extends AsyncTask<RestService, Void, List<Credit>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<Credit> creditList) {
            TextView textView = (TextView)frameView.findViewById(R.id.updated_text);
            super.onPostExecute(creditList);
            if (creditList != null) {
                credits.clear();
                DbService.saveCredit(creditList);
                credits.addAll(DbService.selectCredits());
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
                Snackbar.make(frameView, "Saved successfully!", Snackbar.LENGTH_SHORT)
                        .show();
            }
            else {
                Snackbar.make(frameView, "Some error occurred while saving!", Snackbar.LENGTH_SHORT)
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        frameView = inflater.inflate(R.layout.credit_fragment, container, false);
        initFloatingActionButton();
        initSwipeLayout();
        initRetrofit();
        initListView();

        return frameView;
    }

    private void initSwipeLayout() {
        swipeLayout = (SwipeRefreshLayout) frameView.findViewById(R.id.swipe_credit_container);
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
    }

    private void initListView() {
        credits = DbService.selectCredits();
        listView = (ListView) frameView.findViewById(R.id.listView_credit);
        listView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (listView != null && listView.getChildCount() > 0)
                    if (listView.getFirstVisiblePosition() == 0 && listView.getChildAt(0).getTop() == 0)
                        swipeLayout.setEnabled(true);
                    else swipeLayout.setEnabled(false);
                else
                    swipeLayout.setEnabled(false);
            }
        });
        adapter = new CreditListAdapter((MainActivity)getActivity(),
                getActivity().getApplicationContext(),
                this,
                credits);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
    }

    private void initRetrofit() {
        gson = new GsonBuilder()
                .setDateFormat(MainActivity.DATE_FORMAT)
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        service = retrofit.create(RestService.class);
    }

    private void initFloatingActionButton() {
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                adb.setTitle("Enter info:");
                final LinearLayout linearLayout = new LinearLayout(getActivity());
                final EditText nameInput = new EditText(getActivity());
                final EditText sumInput = new EditText(getActivity());
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
                    }
                });

                adb.create().show();

            }
        });
        fab.show();
    }

    public View getFrameView() {
        return frameView;
    }

    public RestService getService() {
        return service;
    }
}
