package com.websarva.wings.android.mapsecond;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.maps.android.clustering.Cluster;

import java.util.ArrayList;
import java.util.List;

public class CommentListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_comment_list);

        RecyclerView rv = findViewById(R.id.myCommentListRecyclerView);

        RecycleViewAdapter adapter = new RecycleViewAdapter(this.createDataSet(DisplayComment.mCluster));

        LinearLayoutManager llm = new LinearLayoutManager(this);

        rv.setHasFixedSize(true);

        rv.setLayoutManager(llm);

        rv.setAdapter(adapter);

    }





//Copied a part of HomeFragment.java and paste here.
    private List<RowData> createDataSet(Cluster<Person> cluster) {

        List<RowData> dataset = new ArrayList<>();
        for (Person p : cluster.getItems()) {

            RowData data = new RowData();

            data.setTitle(p.getComment());

            data.setFav(p.getFav());

            data.setUsername(p.getUsername());

            data.setIcon(p.getIcon());

            dataset.add(data);

            //here, List of RowData is created.
        }
        return dataset;


    }

}
