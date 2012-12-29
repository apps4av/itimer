package com.ds.itimer;

import java.util.Observable;
import java.util.Observer;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements View.OnClickListener, Observer {

    
    private Button mButton[];
    private ImageButton mImageButton[];
    private ButtonState mState;
    private TextView mInput;
    private TextView mOutput;
    private ToggleButton mToggle;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        mState = new ButtonState();
        mState.addObserver(this);
        mButton = new Button[ButtonState.BUTTON_TOTAL];
        mImageButton = new ImageButton[ButtonState.BUTTON_TOTAL];
        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.activity_main, null);
        mButton[ButtonState.BUTTON_0] = (Button)view.findViewById(R.id.button0);
        mButton[ButtonState.BUTTON_1] = (Button)view.findViewById(R.id.button1);
        mButton[ButtonState.BUTTON_2] = (Button)view.findViewById(R.id.button2);
        mButton[ButtonState.BUTTON_3] = (Button)view.findViewById(R.id.button3);
        mButton[ButtonState.BUTTON_4] = (Button)view.findViewById(R.id.button4);
        mButton[ButtonState.BUTTON_5] = (Button)view.findViewById(R.id.button5);
        mButton[ButtonState.BUTTON_6] = (Button)view.findViewById(R.id.button6);
        mButton[ButtonState.BUTTON_7] = (Button)view.findViewById(R.id.button7);
        mButton[ButtonState.BUTTON_8] = (Button)view.findViewById(R.id.button8);
        mButton[ButtonState.BUTTON_9] = (Button)view.findViewById(R.id.button9);
        mImageButton[ButtonState.BUTTON_STOP]  = (ImageButton)view.findViewById(R.id.buttonStop);
        mImageButton[ButtonState.BUTTON_START] = (ImageButton)view.findViewById(R.id.buttonStart);
        mInput = (TextView)view.findViewById(R.id.textView2);
        mOutput = (TextView)view.findViewById(R.id.textView4);
        mToggle = (ToggleButton)view.findViewById(R.id.toggleButton1);
        mToggle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((ToggleButton) v).isChecked()) {
                    mState.setSecs(true);
                }
                else {
                    mState.setSecs(false);                    
                }
            }
       });
        
        for(int button = ButtonState.BUTTON_0; button <= ButtonState.BUTTON_9; button++) {
            mButton[button].setOnClickListener(this);
        }
        for(int button = ButtonState.BUTTON_STOP; button < ButtonState.BUTTON_TOTAL; button++) {
            mImageButton[button].setOnClickListener(this);
        }
        
        setContentView(view);
    }
    
    /* (non-Javadoc)
     * @see android.app.Activity#onResume()
     */
    @Override
    public void onResume() {
        super.onResume();
        Helper.setOrientationAndOn(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()) {
        
            case R.id.menu_settings:
                /*
                 * Bring up preferences
                 */
                startActivity(new Intent(this, PrefActivity.class));
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        int event = -1;
        switch(v.getId()) {
            case R.id.button0:
                event = ButtonState.BUTTON_0;
                break;
            case R.id.button1:
                event = ButtonState.BUTTON_1;
                break;
            case R.id.button2:
                event = ButtonState.BUTTON_2;
                break;
            case R.id.button3:
                event = ButtonState.BUTTON_3;
                break;
            case R.id.button4:
                event = ButtonState.BUTTON_4;
                break;
            case R.id.button5:
                event = ButtonState.BUTTON_5;
                break;
            case R.id.button6:
                event = ButtonState.BUTTON_6;
                break;
            case R.id.button7:
                event = ButtonState.BUTTON_7;
                break;
            case R.id.button8:
                event = ButtonState.BUTTON_8;
                break;
            case R.id.button9:
                event = ButtonState.BUTTON_9;
                break;
            case R.id.buttonStart:
                event = ButtonState.BUTTON_START;
                break;
            case R.id.buttonStop:
                event = ButtonState.BUTTON_STOP;
                break;
        }
        
        mState.runState(event);
        mInput.setText(mState.getTimeString());
    }

    /**
     * Use default notification
     */
    @Override
    public void update(Observable arg0, Object arg1) {
        int timeLeft = (Integer)arg1;
        
        Message msg = mHandler.obtainMessage();
        
        msg.what = (Integer)timeLeft;
        mHandler.sendMessage(msg);
    }
    
    /**
     * This leak warning is not an issue if we do not post delayed messages, which is true here.
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int timeLeft = msg.what;
            if(timeLeft >= 0) {
                mOutput.setText(Integer.toString(timeLeft));
            }
            if(0 == timeLeft) {
                /*
                 * Feedback to stop timer after a run
                 */
                mState.runState(ButtonState.BUTTON_STOP);
                try {
                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                    r.play();
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(1000);
                }
                catch (Exception e) {
                    
                }
            }
        }
    };

}

