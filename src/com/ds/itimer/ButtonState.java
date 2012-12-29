package com.ds.itimer;

import java.util.Timer;
import java.util.TimerTask;

import java.util.Observable;

/**
 * 
 * @author zkhan
 *
 */
public class ButtonState extends Observable {

    private int mState;
    
    private String mInputString;
    
    private CountDownTimer mCountDownTimer;
    
    private Timer mTimer;
    
    private int mSecs = 1;

    
    /**
     * 
     * @param button
     * @return
     */
    private boolean isNumberButton(int button) {
        switch(button) {
            case ButtonState.BUTTON_0:
            case ButtonState.BUTTON_1:
            case ButtonState.BUTTON_2:
            case ButtonState.BUTTON_3:
            case ButtonState.BUTTON_4:
            case ButtonState.BUTTON_5:
            case ButtonState.BUTTON_6:
            case ButtonState.BUTTON_7:
            case ButtonState.BUTTON_8:
            case ButtonState.BUTTON_9:
                return true;
        }
        
        return false;
    }
    
    /**
     * 
     */
    private void formatInput(int button) {
        mInputString = mInputString + Integer.toString(button);
        int start = mInputString.length() - 4;
        if(start < 0) {
            start = 0;
        }
        String format = mInputString.substring(start, mInputString.length());
        mInputString = format;
    }

    /**
     * 
     * @param secs
     */
    private void startTimer() {
        mTimer = new Timer();
        mCountDownTimer = new CountDownTimer(Integer.parseInt(mInputString));
        mTimer.schedule(mCountDownTimer, 0, 1000 / mSecs);        
    }

    /**
     * 
     * @param secs
     */
    private void stopTimer() {
        mTimer.cancel();
        mTimer = null;
    }

    /**
     * 
     */
    public ButtonState() {
        mState = STATE_STOPPED;
        mInputString = "0000";
    }
    
    /**
     * 
     * @param secs
     */
    public void setSecs(boolean sr) {
        if(sr) {
            mSecs = 3;
        }
        else {
            mSecs = 1;
        }
    }
    

    /**
     * 
     * @return
     */
    public String getTimeString() {
        return mInputString;
    }

    /**
     * 
     * @param button
     */
    public void runState(int button) {
        
        switch(mState) {
            case STATE_RUNNING:
                if(isNumberButton(button)) {
                }
                else if(BUTTON_STOP == button) {
                    mState = STATE_STOPPED;
                    stopTimer();
                }
                break;
            case STATE_STOPPED:
                if(isNumberButton(button)) {
                    formatInput(button);                    
                }
                else if(BUTTON_START == button) {
                    mState = STATE_RUNNING;
                    startTimer();
                }
                break;
        }   
    }
    
    /**
     * Buttons
     */
    public static final int BUTTON_0 = 0;
    public static final int BUTTON_1 = 1;
    public static final int BUTTON_2 = 2;
    public static final int BUTTON_3 = 3;
    public static final int BUTTON_4 = 4;
    public static final int BUTTON_5 = 5;
    public static final int BUTTON_6 = 6;
    public static final int BUTTON_7 = 7;
    public static final int BUTTON_8 = 8;
    public static final int BUTTON_9 = 9;
    public static final int BUTTON_STOP = 10;
    public static final int BUTTON_START = 11;
    public static final int BUTTON_TOTAL = 12;

    private static final int STATE_STOPPED = 0;
    private static final int STATE_RUNNING = 1;
    
    
    /**
     * @author zkhan
     *
     */
    private class CountDownTimer extends TimerTask {

        private int mSecsLeft;
        
        /**
         * 
         */
        public CountDownTimer(int secsLeft) {
            mSecsLeft = secsLeft;
        }

        /* (non-Javadoc)
         * @see java.util.TimerTask#run()
         */
        @Override
        public void run() {
            mSecsLeft--;
            if(mSecsLeft < 0) {
                mSecsLeft = -1;
            }
            ButtonState.this.setChanged();
            ButtonState.this.notifyObservers(mSecsLeft); 
        }
    }
}
