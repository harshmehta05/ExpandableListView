package com.example.android.expandablelistviewexample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends Activity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference deptRef;
    Context context;
    ArrayList<QuestionAnswerModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        context = this;
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);






        getUnansweredQuestionList();


        // preparing list data
       // prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            int previousItem = -1;
            @Override
            public void onGroupExpand(int i) {

                if(i != previousItem )

                    expListView.collapseGroup(previousItem );
                previousItem = i;

            }
        });

        // setting list adapter
        expListView.setAdapter(listAdapter);


        Button save = findViewById(R.id.saveAnsButton);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String[] stringArray = listAdapter.getAnsArray();
                String[] questionArray = listAdapter.getQuestionArray();

                Intent intent = new Intent(MainActivity.this, AnswerActivity.class);
                intent.putExtra("ansArray", stringArray);
                intent.putExtra("questionArray",questionArray);
                startActivity(intent);

            }
        });
    }

    /*
     * Preparing the list data
     */


    private void getUnansweredQuestionList()
    {

        list = new ArrayList<>();
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        //Dept Name
        String str1 = "Computers";

        //Company name
        String str2 = "Google";

        //Alumni Name
        final String str3 = "Yash HEY";

        deptRef = database.getReference("Departments").child(str1);
        DatabaseReference companyRef = deptRef.child("Companies");
        DatabaseReference companyRef2=companyRef.child(str2);
        DatabaseReference alumniRef=companyRef2.child("Alumnis");
        //DatabaseReference alumniRef2=alumniRef.child(str3);


        alumniRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                for (DataSnapshot alumniSnapshot : dataSnapshot.getChildren())
                {
                    String alumniName = alumniSnapshot.child("Name").getValue().toString().trim();
                    if (str3.equals(alumniName))
                    {


                        for (DataSnapshot qSnapshot: alumniSnapshot.child("Questions").getChildren())
                        {
                            /*if ((qSnaposhot.child("answer").getValue().toString().trim()).equals(""))
                            {
                                //String question = qSnaposhot.child("question").getValue().toString().trim();
                                //listDataHeader.add(question);

                                list.add(qSnapshot.getValue(QuestionsModel.class));
                            }*/
                            String question = qSnapshot.child("question").getValue().toString();
                            String answer = qSnapshot.child("answer").getValue().toString();

                            list.add(new QuestionAnswerModel(question,answer));

                            
                        }
                    }
                }


                for (int i=0;i<list.size();i++)
                {
                    if (list.get(i).getAnswer().equals(""))
                    {
                        listDataHeader.add(list.get(i).getQuestion());
                    }
                }

                //String q1="Which year Did You start preparing for GRE";
                // String q2="Why did you select such a position in the respective company";
                //String q3="What was your average pointer in btech?";


                // Adding child data
                //listDataHeader.add("Which year Did You start preparing for GRE");
                //listDataHeader.add("Why did you select such a position in the respective company");
                // listDataHeader.add("What was your average pointer in btech?");


                // Adding child data
                List<String> top250 = new ArrayList<String>();
                top250.add("The Shawshank Redemption");

                List<String> nowShowing = new ArrayList<String>();
                nowShowing.add("The Conjuring");


                List<String> comingSoon = new ArrayList<String>();
                comingSoon.add("2 Guns");

                Log.d("harsh :",listDataHeader.get(0));
                Log.d("harsh :",listDataHeader.get(1));
                Log.d("harsh :",listDataHeader.get(2));

                listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
                listDataChild.put(listDataHeader.get(1), nowShowing);
                listDataChild.put(listDataHeader.get(2), comingSoon);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //listDataHeader = new ArrayList<String>();



    }


    private void prepareListData() {




       // listDataHeader = new ArrayList<String>();

        for (int i=0;i<list.size();i++)
        {
            if (list.get(i).getAnswer().equals(""))
            {
                listDataHeader.add(list.get(i).getQuestion());
            }
        }

        //String q1="Which year Did You start preparing for GRE";
        // String q2="Why did you select such a position in the respective company";
        //String q3="What was your average pointer in btech?";


        // Adding child data
        //listDataHeader.add("Which year Did You start preparing for GRE");
        //listDataHeader.add("Why did you select such a position in the respective company");
        // listDataHeader.add("What was your average pointer in btech?");


        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("The Shawshank Redemption");

        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("The Conjuring");


        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("2 Guns");



         //listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        //listDataChild.put(listDataHeader.get(1), nowShowing);
        //listDataChild.put(listDataHeader.get(2), comingSoon);

    }

}