package com.example.dmitry.androidrestclient;

import android.os.AsyncTask;
import android.support.design.widget.Snackbar;

import com.example.dmitry.androidrestclient.data.Credit;

import java.io.IOException;

/**
 * Created by dmitry on 24.02.16.
 */
public class DeleteCreditTask extends AsyncTask<Object, Void, Boolean> {
    private MainActivity activity;
    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (aBoolean.booleanValue()) {
            Snackbar.make(activity.findViewById(R.id.swipe_container), "Deleted successfully!", Snackbar.LENGTH_SHORT)
                    .show();
        }
        else {
            Snackbar.make(activity.findViewById(R.id.swipe_container), "Error occurred while deleting!", Snackbar.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    protected Boolean doInBackground(Object... params) {
        Credit credit = (Credit)params[0];
        activity = (MainActivity)params[1];
        activity.dbService.deleteCredit(credit);
        try {
            return new Boolean(activity.service.deleteCredit(credit.get_id()).execute().isSuccess());
        }
        catch (IOException e) {
            return new Boolean(false);
        }
    }
}