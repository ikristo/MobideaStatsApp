package com.example.android.mobideastats;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.mobideastats.data.CheckNetworkState;
import com.example.android.mobideastats.data.DataItem;
import com.example.android.mobideastats.data.ConversionListAdapter;
import com.example.android.mobideastats.data.XMLParser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DailyActivity extends AppCompatActivity {


    double total;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView mDatePicker;

    ArrayList<DataItem> dataItems = null;
    public String url;
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatePicker = findViewById(R.id.tvDatePicker);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        Intent intent = getIntent();
        createUrl(intent.getStringExtra("date"));
        getData(url);
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }


    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            year = arg1;
            month = arg2;
            day = arg3;


            createUrl();
            getData(url);
        }
    };

    private void createUrl() {

        calendar.set(year, month, day);
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String urlDate = sdf.format(date);


        url = "https://affiliates.mobidea.com/api/export/stats/http-xml?login=260147900&password=b7487c967a25aba9ec078d164268197f" +
                "&date=" + urlDate + "&currency=USD&format=xml";

        mDatePicker.setText(urlDate);
    }

    private void createUrl(String date) {

                url = "https://affiliates.mobidea.com/api/export/stats/http-xml?login=260147900&password=b7487c967a25aba9ec078d164268197f" +
                "&date=" + date + "&currency=USD&format=xml";

        mDatePicker.setText(date);
    }


    private void runRecyclerView() {

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setVisibility(View.VISIBLE);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ConversionListAdapter(this, dataItems);
        recyclerView.setAdapter(mAdapter);


        recyclerView.scrollToPosition(dataItems.size() - 1);

    }

    private void getData(String url) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dataItems = XMLParser.parseFeed(response);
                        if (dataItems != null) {
                            runRecyclerView();
                            calculateTotal();
                        } else {
                            hideRecyclerView();
                            Toast.makeText(DailyActivity.this, "No conversions!", Toast.LENGTH_SHORT).show();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DailyActivity.this, "Unable to fetch data!", Toast.LENGTH_SHORT).show();
            }
        });
// Add the request to the RequestQueue.

        queue.add(stringRequest);

    }

    private void hideRecyclerView() {
        recyclerView.setVisibility(View.INVISIBLE);

    }

    private void calculateTotal() {
        for (DataItem item : dataItems) {
            total += item.getmRevenue();
        }
        TextView tvTotal = (TextView) findViewById(R.id.total);
        tvTotal.setText("Total: " + String.format("%.2f", total));
        total = 0;
    }


    public void datePicker(View view) {
        showDialog(999);

    }
}
