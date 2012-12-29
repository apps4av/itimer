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
    
    private int mSecs = Helper.SEC;

    private static final int PERIOD_MS = 1000;
    
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
     * @param secs
     */
    private void startTimer() {
        mTimer = new Timer();
        
        if(!Helper.isStandardRate(mSecs)) {
            mCountDownTimer = new CountDownTimer(Helper.fourToTime(mInputString));
        }
        else {
            mCountDownTimer = new CountDownTimer(Helper.fourToDegrees(mInputString));            
        }
        mTimer.schedule(mCountDownTimer, 0, PERIOD_MS);
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
    public void setStandardRate(boolean sr) {
        if(sr) {
            mSecs = Helper.DEGREES_PER_SEC;
        }
        else {
            mSecs = Helper.SEC;
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
     * @return
     */
    public boolean isStopped() {
        return mState == STATE_STOPPED;
    }
    
    /**
     * 
     * @param button
     * @return 
     */
    public synchronized void runState(int button) {
        
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
                    mInputString = Helper.formatInput(mInputString, button);                    
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
            mSecsLeft -= mSecs;
            if(mSecsLeft <= 0) {
                runState(ButtonState.BUTTON_STOP);
            }
            ButtonState.this.setChanged();
            ButtonState.this.notifyObservers(mSecsLeft); 
        }
    }
}
