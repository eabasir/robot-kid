package com.ansari.smartplug.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import com.ansari.smartplug.R;
import com.ansari.smartplug.App;
import com.ansari.smartplug.interfaces.OnSwitchReadListener;
import com.ansari.smartplug.network.AsyncClient;
import com.ansari.smartplug.network.NetConfig;


public class DemoFragment extends Fragment implements OnSwitchReadListener, View.OnTouchListener {


    View view;


    private NetConfig.TYPE type;
    AsyncClient client;

    ToggleButton tglRight;
    ToggleButton tglLeft;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.fragment_demo, container, false);


        ((ImageButton) view.findViewById(R.id.btnUp)).setOnTouchListener(this);
        ((ImageButton) view.findViewById(R.id.btnDown)).setOnTouchListener(this);
        ((ImageButton) view.findViewById(R.id.btnLeft)).setOnTouchListener(this);
        ((ImageButton) view.findViewById(R.id.btnRight)).setOnTouchListener(this);

        tglLeft = (ToggleButton) view.findViewById(R.id.tglLeft);
        tglRight = (ToggleButton) view.findViewById(R.id.tglRight);

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        App.getInstance().addUIListener(OnSwitchReadListener.class, this);

        client = new AsyncClient(NetConfig.DEVICE_IP
                , NetConfig.DEVICE_PORT);

        handler.post(readSwitchRunnable);


    }

    @Override
    public void onPause() {
        super.onPause();
        App.getInstance().removeUIListener(OnSwitchReadListener.class, this);
        client.finish();
        handler.removeCallbacks(readSwitchRunnable);
        handler.removeCallbacks(actionRunnable);
    }

    @Override
    public void onSwitchRead(boolean left, boolean right) {

        tglRight.setChecked(right);
        tglLeft.setChecked(left);


    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {

        switch (view.getId()) {

            case R.id.btnUp:
                this.type = NetConfig.TYPE.Forward;
                break;
            case R.id.btnDown:
                this.type = NetConfig.TYPE.Backward;
                break;
            case R.id.btnRight:
                this.type = NetConfig.TYPE.Right;
                break;
            case R.id.btnLeft:
                this.type = NetConfig.TYPE.Left;
                break;
            default:
                this.type = null;
                break;

        }


        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            handler.post(actionRunnable);

        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            handler.removeCallbacks(actionRunnable);

            Log.v("TAG", "Stopped!!!");
        }


        return false;
    }


    Handler handler = new Handler();

    private Runnable readSwitchRunnable = new Runnable() {
        @Override
        public void run() {

            client.startSendAction(NetConfig.TYPE.ReadSwitch);
            handler.postDelayed(readSwitchRunnable, 1000);
        }
    };

    private Runnable actionRunnable = new Runnable() {
        @Override
        public void run() {

            client.startSendAction(DemoFragment.this.type);
            handler.postDelayed(actionRunnable, 150);
        }
    };


}
