package com.example.android.mobideastats;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.mobideastats.data.BusStation;
import com.example.android.mobideastats.data.CheckNetworkState;
import com.example.android.mobideastats.data.DailyDataItem;
import com.example.android.mobideastats.data.DailyListAdapter;
import com.example.android.mobideastats.data.DataItem;
import com.example.android.mobideastats.data.XMLParser;
import com.squareup.otto.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class MonthlyActivity extends AppCompatActivity {

    double total;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<DataItem> dataItems = null;

    public String url;
    private int year, month, day;
    ArrayList<DailyDataItem> dailyDataItems = new ArrayList<>();
    ArrayList<DailyDataItem> sortedDailyDataItems = new ArrayList<>();

    int numberOfDays = 7;
    int i = 0;

    TextView tvTotalMonthly;
    Double totalMonthly = 0.00;
    Spinner spinnerIntervalChooser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly);
        BusStation.getBus().register(this);
        isThereActiveNetworkConnection();
        tvTotalMonthly = findViewById(R.id.total);
        spinnerIntervalChooser = findViewById(R.id.spinner1);

        startProcess();


       /* spinnerIntervalChooser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                switch (String.valueOf(spinnerIntervalChooser.getSelectedItemId())) {
                    case "0": numberOfDays = 7;
                        startProcess();
                        break;
                    case "1": numberOfDays = 30;
                        startProcess();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
*/

    }

    private void startProcess() {
        for (int i = 0; i < numberOfDays; i++) {
            String queryUrl = createUrl(i);
            getData(queryUrl, createDate(i));
        }

    }


    ///////////////// Get date today \\\\\\\\\\\\
    public Calendar todayIs() {

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        return calendar;
    }

//////////////// Check is there active network connection \\\\\\\\\\

    public void isThereActiveNetworkConnection() {
        if (!CheckNetworkState.isNetworkAvailable(this)) {
            Toast.makeText(this, "No network connection!", Toast.LENGTH_SHORT).show();
        }
    }

///////////// Create queried URL \\\\\\\\\\\\\\\
    /*
    * arg i -> number of days back from today
    **/

    private String createUrl(int i) {

        String urlDate = createDate(i);
        String BASE_URL = "https://affiliates.mobidea.com/api/export/stats/http-xml?";
        String login = "260147900";
        String password = "b7487c967a25aba9ec078d164268197f";

        url = BASE_URL + "login=" + login + "&password=" + password +
                "&date=" + urlDate + "&currency=USD&format=xml";

        return url;

    }

    private String createDate(int i) {
        Calendar calendar = todayIs();
        calendar.add(calendar.DAY_OF_YEAR, -i);
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String urlDate = sdf.format(date);
        return urlDate;
    }

    ///////////// Show data in RecyclerView \\\\\\\\\\\\
    /*
    * args ArrayList dailyDataItems
    * */
    private void runRecyclerView(ArrayList dailyDataItems) {

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        if (dailyDataItems != null) {
            recyclerView.setVisibility(View.VISIBLE);
            mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            mAdapter = new DailyListAdapter(this, dailyDataItems);
            recyclerView.setAdapter(mAdapter);
            recyclerView.scrollToPosition(dailyDataItems.size() - 1);
        } else {
            recyclerView.setVisibility(View.INVISIBLE);
        }
    }


    ///////////////


    ////////////// Volley side thread request \\\\\\\\\\\\\
    private void getData(String url, final String conversionDate) {

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dataItems = XMLParser.parseFeed(response);
                if (dataItems != null) {
                    for (DataItem item : dataItems) {
                        total += item.getmRevenue();
                    }
                } else {
                    total = 0;
                }

                dailyDataItems.add(new DailyDataItem(conversionDate, total));


                BusStation.getBus().post("");

                total = 0;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);


    }

    ///// Otto Bus receiver \\\\\\\\\\\\\\
    @Subscribe
    public void getMessage(String message) {
        if (dailyDataItems.size() == numberOfDays) {
            sortedDailyDataItems = sortAList(dailyDataItems);
            runRecyclerView(dailyDataItems);
            for (DailyDataItem item : dailyDataItems) {
                totalMonthly += item.getRevenue();
            }
            tvTotalMonthly.setText("$" + String.format("%.2f", totalMonthly));
        }
    }

    ////////// Sort ArrayList of objects DailyDataItem \\\\\\\\\\\\\\\\\\\\\\q
    private ArrayList<DailyDataItem> sortAList(ArrayList<DailyDataItem> dailyDataItems) {
        Collections.sort(dailyDataItems, new Comparator<DailyDataItem>() {
            public int compare(DailyDataItem o1, DailyDataItem o2) {
                if (o1.getmDate() == null || o2.getmDate() == null)
                    return 0;
                return o1.getmDate().compareTo(o2.getmDate());
            }
        });
        return dailyDataItems;
    }

    public void btnRefresh(View view) {
        startProcess();
        Toast.makeText(this, "Refreshing", Toast.LENGTH_SHORT).show();
    }
}
