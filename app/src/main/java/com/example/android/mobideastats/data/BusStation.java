package com.example.android.mobideastats.data;

import com.squareup.otto.Bus;

/**
 * Created by W567 on 09.11.2017..
 */

public class BusStation {
    private static Bus bus = new Bus();

    public static Bus getBus() {
        return bus;
    }
}
