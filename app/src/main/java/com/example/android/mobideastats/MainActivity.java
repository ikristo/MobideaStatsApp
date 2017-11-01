package com.example.android.mobideastats;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class MainActivity extends AppCompatActivity {


    double total;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

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

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        createUrl();


//        Check is there active network connection
        if (CheckNetworkState.isNetworkAvailable(this)) {
            Toast.makeText(this, "Downloading data!", Toast.LENGTH_SHORT).show();
            //        Get Data
            getData(url);
        } else {
            Toast.makeText(this, "No network connection!", Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.refresh:
                getData(url);
                return true;
            case R.id.calendar:
                showDialog(999);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
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
    }


    private void runRecyclerView() {

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

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
                            Toast.makeText(MainActivity.this, "No conversions!", Toast.LENGTH_SHORT).show();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Unable to fetch data!", Toast.LENGTH_SHORT).show();
            }
        });
// Add the request to the RequestQueue.

        queue.add(stringRequest);

    }

    private void calculateTotal() {
        for (DataItem item : dataItems) {
            total += item.getmRevenue();
        }
        TextView tvTotal = (TextView) findViewById(R.id.total);
        tvTotal.setText("Total: " + String.format("%.2f", total));
        total = 0;
    }


}
