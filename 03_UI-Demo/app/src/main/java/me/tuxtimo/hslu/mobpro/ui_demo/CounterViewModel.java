package me.tuxtimo.hslu.mobpro.ui_demo;

import androidx.lifecycle.ViewModel;

public class CounterViewModel extends ViewModel {
    private int counter = 0;
    public int incrementCounter() { return ++counter; }
    public int getCounter() { return counter; }
}
