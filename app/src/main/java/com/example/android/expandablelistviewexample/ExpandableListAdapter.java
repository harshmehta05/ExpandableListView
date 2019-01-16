package com.example.android.expandablelistviewexample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ExpandableListAdapter extends BaseExpandableListAdapter  {

    private Context _context;
    TextView editTextAnswer;
    private List<String> _listDataHeader; // header titles
    private List<QuestionAnswerModel> questionAnswerModels;
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    String[] ansArray;
    String[] questionArray;

    String deptName;
    String companyName;
    String alumniName;


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference deptRef;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData,String deptName,String companyName,String alumniName) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        questionAnswerModels = new ArrayList<>();
        this.deptName = deptName;
        this.companyName = companyName;
        this.alumniName = alumniName;
        ansArray = new String[100];
        questionArray = new String[100];
    }


    public void setAnswerFirebase(final String currentQuestion, final String currentAnswer)
    {

        final String alumniParKey;
        //Dept Name
        String str1 = "Computers";

        //Company name
        String str2 = "Google";

        //Alumni Name
        final String str3 = "Yash HEY";

        deptRef = database.getReference("Departments").child(str1);
        DatabaseReference companyRef = deptRef.child("Companies");
        DatabaseReference companyRef2=companyRef.child(str2);
        final DatabaseReference alumniRef=companyRef2.child("Alumnis");
        //DatabaseReference alumniRef2=alumniRef.child(str3);*/


        alumniRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                String alumniParKey = "";
                String questionParKey = "";

                for (DataSnapshot alumniSnapshot : dataSnapshot.getChildren())
                {
                    String alumniName = alumniSnapshot.child("Name").getValue().toString().trim();
                    if (str3.equals(alumniName))
                    {

                        alumniParKey = alumniSnapshot.getKey();

                        for (DataSnapshot qSnapshot: alumniSnapshot.child("Questions").getChildren())
                        {

                            String question = qSnapshot.child("question").getValue().toString();
                            if (question.equals(currentQuestion))
                            {
                                 questionParKey = qSnapshot.getKey();
                            }

                        }
                    }

                }

                DatabaseReference alumniRef2 = alumniRef.child(alumniParKey);
                DatabaseReference questionsRef = alumniRef2.child("Questions");
                DatabaseReference questionsRef2 = questionsRef.child(questionParKey);


                QuestionAnswerModel model = new QuestionAnswerModel(currentQuestion,currentAnswer);
                questionsRef2.setValue(model);
                Toast.makeText(_context,"Updated Databaswe",Toast.LENGTH_SHORT).show();




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }


    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }



        editTextAnswer = (TextView) convertView.findViewById(R.id.ansEditText);

        Button saveBtn = (Button) convertView.findViewById(R.id.saveItemAnsButton);

        Button ansBtn = (Button) convertView.findViewById(R.id.answerItemAnsButton);

        ansBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater li = LayoutInflater.from(_context);
                View promptsView = li.inflate(R.layout.prompts_answer, null);



                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        _context);

                // set prompts_answer.xmler.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // get user input and set it to result
                                        // edit text
                                        //result.setText(userInput.getText());
                                        //prepareListData(listDataHeader.get(gP),userInput.getText().toString());
                                        editTextAnswer.setText(userInput.getText().toString());

                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ans = editTextAnswer.getText().toString();
                if (ans!=null)
                {
                    String question = getGroup(groupPosition).toString();
                    ansArray[groupPosition] = ans;
                    questionArray[groupPosition] = question;



                }
                setAnswerFirebase(questionArray[groupPosition],ansArray[groupPosition]);
                notifyDataSetChanged();
                Toast.makeText(_context,"SAVED YOUR RESPONSE",Toast.LENGTH_SHORT).show();

                editTextAnswer.setText("");


            }
        });


        return convertView;
    }

    public  List<QuestionAnswerModel> getQuestionAnswerModels() {
        return questionAnswerModels;
    }

    public String[] getAnsArray() {
        return ansArray;
    }

    public String[] getQuestionArray() {
        return questionArray;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}
