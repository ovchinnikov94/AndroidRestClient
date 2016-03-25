package com.example.dmitry.androidrestclient.adapter;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.dmitry.androidrestclient.fragment.CreditFragment;
import com.example.dmitry.androidrestclient.asynctask.DeleteCreditTask;
import com.example.dmitry.androidrestclient.MainActivity;
import com.example.dmitry.androidrestclient.R;
import com.example.dmitry.androidrestclient.data.Credit;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by dmitry on 15.02.16.
 */
public class CreditListAdapter extends ArrayAdapter<Credit> {
    private final Context context;
    private final List<Credit> credits;
    private MainActivity activity;
    private CreditFragment fragment;
    public CreditListAdapter(MainActivity activity, Context context, CreditFragment fragment, List<Credit> objects) {
        super(context, R.layout.my_list_item, objects);
        this.context = context;
        this.credits = objects;
        this.activity = activity;
        this.fragment = fragment;
    }

    static class ViewHolder{
        protected TextView textViewCreditType;
        protected TextView textViewCreditSum;
        protected TextView textViewCreditDate;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.my_list_item, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.textViewCreditType = (TextView)view.findViewById(R.id.CreditType);
            viewHolder.textViewCreditSum = (TextView)view.findViewById(R.id.CreditSum);
            viewHolder.textViewCreditDate = (TextView)view.findViewById(R.id.CreditDate);
            view.setTag(viewHolder);
        }
        ViewHolder viewHolder = (ViewHolder)view.getTag();
        final Credit credit = credits.get(position);
        viewHolder.textViewCreditType.setText(credit.getName());
        viewHolder.textViewCreditSum.setText(String.valueOf(credit.getSum()) + " руб.");
        String date = "";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(credit.getDate());
        date += calendar.get(Calendar.HOUR_OF_DAY) + ":" +
                String.format("%02d", calendar.get(Calendar.MINUTE)) + "   " +
                calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())+ " " +
                calendar.get(Calendar.DAY_OF_MONTH) + " " +
                calendar.get(Calendar.YEAR);
        viewHolder.textViewCreditDate.setText(date);
        view.setLongClickable(true);
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder adb = new AlertDialog.Builder(activity);
                adb.setTitle("Deleting");
                adb.setMessage("Are you sure you wand to delete " + credit.getName());
                adb.setNegativeButton("Cancel", null);
                final int pos = position;
                adb.setPositiveButton("OK", new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DeleteCreditTask task = new DeleteCreditTask();
                        if (!task.execute(credits.get(pos), fragment.getFrameView(), fragment.getService()).isCancelled()) {
                            credits.remove(pos);
                            notifyDataSetChanged();
                        }
                    }
                });
                adb.create().show();
                return false;
            }
        });
        return view;
    }
}
