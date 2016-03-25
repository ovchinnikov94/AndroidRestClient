package com.example.dmitry.androidrestclient.asynctask;

import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.example.dmitry.androidrestclient.MainActivity;
import com.example.dmitry.androidrestclient.R;
import com.example.dmitry.androidrestclient.RestService;
import com.example.dmitry.androidrestclient.data.Credit;
import com.example.dmitry.androidrestclient.db.DbService;

import java.io.IOException;

/**
 * Created by dmitry on 24.02.16.
 */
public class DeleteCreditTask extends AsyncTask<Object, Void, Boolean> {
    private View frameView;
    private RestService service;
    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (aBoolean.booleanValue()) {
            Snackbar.make(frameView, "Deleted successfully!", Snackbar.LENGTH_SHORT)
                    .show();
        }
        else {
            Snackbar.make(frameView, "Error occurred while deleting!", Snackbar.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    protected Boolean doInBackground(Object... params) {
        Credit credit = (Credit)params[0];
        frameView = (View)params[1];
        service = (RestService)params[2];
        DbService.deleteCredit(credit);
        try {
            return new Boolean(service.deleteCredit(credit.get_id()).execute().isSuccess());
        }
        catch (IOException e) {
            return new Boolean(false);
        }
    }
}