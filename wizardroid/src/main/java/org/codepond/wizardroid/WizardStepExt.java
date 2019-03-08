package org.codepond.wizardroid;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Simplify implementation of wizard steps
 */
public abstract class WizardStepExt extends WizardStep {
    private static final String PARAM_ENTERED = "wizard_step_entered";
    private boolean mEntered = false;   // true when the user is in this wizard

    private boolean mInitialized = false;

    public WizardStepExt() { }

    @Override
    final public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null != savedInstanceState) {
            mEntered = savedInstanceState.getBoolean(PARAM_ENTERED, false);
        }
        return doOnCreateView(inflater, container, savedInstanceState);
    }

    public boolean isInitialized() {
        return mInitialized;
    }

    @Override
    public void onEnter() {
        mEntered = true;
        if (!mInitialized) {
            initialize();
            mInitialized = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mEntered && !mInitialized) {
            initialize();
            mInitialized = true;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(PARAM_ENTERED, mEntered);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mInitialized) {
            deinitialize();
            mInitialized = false;
        }
    }

    @Override
    public void onExitStep() {
        if (mInitialized) {
            deinitialize();
            mInitialized = false;
        }
        mEntered = false;
    }

    protected abstract View doOnCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                           Bundle savedInstanceState);

    protected abstract void initialize();

    protected abstract void deinitialize();
}
