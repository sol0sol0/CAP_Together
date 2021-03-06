package com.example.together;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Monthly#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Monthly extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    PieChart pieChart;
    int[] colorArray=new int[]{Color.rgb(252,197,238),Color.rgb(250,243,175), Color.rgb(175,222,250),Color.rgb(148,229,220)};

    public Monthly() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Monthly.
     */
    // TODO: Rename and change types and number of parameters
    public static Monthly newInstance(String param1, String param2) {
        Monthly fragment = new Monthly();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        //Fragment??? ???????????? ?????? ????????? inflater ?????? ?????? findViewById ????????? ????????? ?????? ??? ??????.
        //onCreateView?????? View ????????? ????????? ?????? findViewById??? ?????? xml??? ????????? ????????? ????????????.
        View v=inflater.inflate(R.layout.fragment_monthly,container,false);

        //PieChart ??????
        pieChart=v.findViewById(R.id.monthlyPicChart);

        //PieChart ??????
        PieDataSet pieDataSet=new PieDataSet(MonthData(),"?????? ??????");

        //????????? ??????
        pieDataSet.setColors(colorArray);

        PieData pieData=new PieData(pieDataSet);
        pieChart.setDrawEntryLabels(true);
        pieChart.setUsePercentValues(true);

        //?????? ?????? ??????
        pieData.setValueTextSize(25);

        //????????? ?????? ??????
        pieChart.setCenterText("????????????");
        pieChart.setCenterTextSize(25);
        pieChart.setData(pieData);
        pieChart.invalidate();
        return v;
    }

    private ArrayList<PieEntry> MonthData(){
        ArrayList<PieEntry> devalues;
        devalues = new ArrayList<>();

        //PieChart ?????? ?????? ?????? PieEntry(?????????,?????????)
        devalues.add(new PieEntry(40,"?????????"));
        devalues.add(new PieEntry(30,"??????"));
        devalues.add(new PieEntry(20,"C??????"));
        devalues.add(new PieEntry(10,"??????"));

        return devalues;
    }

}