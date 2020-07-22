package ru.denfad.cheackheart;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class EcgFragment extends Fragment {

    private double[] points = {0,1,3,0,1,10,5,1,0,-2,-1,-5,0,1,1,0,3,-11,0,4,0,1,3,0,1,10,5,1,0,-2,-1,-5,0,1,1,0,3,-11,0,4,0,1,3,0,1,10,5,1,0,-2,-1,-5,0,1,1,0,3,-11,0,4,0,1,3,0,1,10,5,1,0,-2,-1,-5,0,1,1,0,3,-11,0,4,0,1,3,0,1,10,5,1,0,-2,-1,-5,0,1,1,0,3,-11,0,4,};

    public EcgFragment(){}

    public static EcgFragment newInstance(){
        return new EcgFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ecg_fragment, container, false);

        GraphView graph = (GraphView) rootView.findViewById(R.id.ecg_graph);
        graph.getLegendRenderer().setVisible(false);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(50);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(-15);
        graph.getViewport().setMaxY(15);
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);

        addGraph(graph,points);
        return rootView;
    }

    private void addGraph(GraphView graphView, double[] arr){
        DataPoint[] dataPoints = new DataPoint[arr.length];
        for(int i=0; i<arr.length;i++) {
            dataPoints[i] = new DataPoint(i + 1, arr[i]);
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
        series.setColor(getContext().getColor(R.color.colorAccent));
        series.setThickness(8);
        graphView.addSeries(series);
    }

}
