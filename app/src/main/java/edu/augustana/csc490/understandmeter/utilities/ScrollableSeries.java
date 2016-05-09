package edu.augustana.csc490.understandmeter.utilities;

import com.androidplot.xy.XYSeries;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kelseyself14 on 5/9/2016.
 */
public class ScrollableSeries implements XYSeries {
    private ArrayList<Number> numberList=new ArrayList<Number>();
    private int seriesIndex;
    private String title;

    public ScrollableSeries(List<Number> startingNums, int seriesIndex, String title) {
        this.numberList.addAll(startingNums);
        this.seriesIndex = seriesIndex;
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public int size() {
        return numberList.size();
    }

    @Override
    public Number getX(int index) {
        return index;
    }

    @Override
    public Number getY(int index) {
        return numberList.get(index);
    }

    public void add(Number value) {
        numberList.add(value);

    }
}
