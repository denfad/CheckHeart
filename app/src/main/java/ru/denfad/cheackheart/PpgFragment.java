package ru.denfad.cheackheart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class PpgFragment extends Fragment {

    private double[] points = {0,20,90,0,-15,-20,0,5,9,0,-10,-2,0,0,20,90,0,-15,-20,0,5,9,0,-10,-2,0,0,20,90,0,-15,-20,0,5,9,0,-10,-2,0,0,20,90,0,-15,-20,0,5,9,0,-10,-2,0,0,20,90,0,-15,-20,0,5,9,0,-10,-2,0};

    public PpgFragment(){}

    public static PpgFragment newInstance(){
        return new PpgFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ppg_fragment, container, false);

        GraphView graph = (GraphView) rootView.findViewById(R.id.ppg_graph);
        graph.getLegendRenderer().setVisible(false);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(50);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(-60);
        graph.getViewport().setMaxY(100);
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);

        addGraph(graph,points);
        return rootView;
    }

    private void addGraph(GraphView graphView, double[] arr){
        DataPoint[] dataPoints = new DataPoint[60];
        for(int i=0; i<60;i++) {
            dataPoints[i] = new DataPoint(i + 1, arr[i]);
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
        series.setColor(getContext().getColor(R.color.colorAccent));
        series.setAnimated(true);
        series.setThickness(8);
        graphView.addSeries(series);
    }

}
