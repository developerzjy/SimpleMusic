package com.example.simplemusic.tools;

public class StateControl {

    private static StateControl mInstance = null;
    private int mCurrentState;

    private StateControl() {
        mCurrentState = Constants.TitleState.MUSIC;
    }

    public static StateControl getInstance() {
        if (mInstance == null) {
            mInstance = new StateControl();
        }
        return mInstance;
    }

    public int getCurrentState() {
        return mCurrentState;
    }

    public void setCurrentState(int state) {
        mCurrentState = state;
    }
}
