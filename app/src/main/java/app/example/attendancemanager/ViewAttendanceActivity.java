package app.example.attendancemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import app.example.attendancemanager.model.AttendanceModel;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static app.example.attendancemanager.MainActivity.classList;
import static app.example.attendancemanager.MainActivity.selectedClassIndex;
import static app.example.attendancemanager.StudentsActivity.selectedRollNo;
import static app.example.attendancemanager.StudentsActivity.studentList;

public class ViewAttendanceActivity extends AppCompatActivity {

    DatabaseReference presentRef;
    TextView rollTvVa, presentTvVa, totalTvVa, percentTvVa, absentTvVa, classNameTvVa, classCodeTvVa;

    private List<String> presentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendance);

        Toolbar toolbar = findViewById(R.id.view_attendance_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("View Attendance");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rollTvVa = findViewById(R.id.tv_roll_va);
        final String roll = String.valueOf(studentList.get(selectedRollNo).getRoll());
        rollTvVa.setText(roll);


        totalTvVa = findViewById(R.id.tv_total_va);
        presentTvVa = findViewById(R.id.tv_present_va);
        percentTvVa = findViewById(R.id.tv_percent_va);
        absentTvVa = findViewById(R.id.tv_absent_va);
        classNameTvVa = findViewById(R.id.tv_className_va);
        classCodeTvVa = findViewById(R.id.tv_classCode_va);

        String className = classList.get(selectedClassIndex).getClassTitle();
        String classCode = classList.get(selectedClassIndex).getId();

        classNameTvVa.setText(className);
        classCodeTvVa.setText(classCode);

        presentRef = FirebaseDatabase.getInstance().getReference("Attendance")
                .child(classList.get(selectedClassIndex).getId());


        presentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                presentList.clear();

                if (dataSnapshot.exists()){

                    int totalClass = 0;

                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){

                        totalClass++;

                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()){

                            String key = dataSnapshot2.getKey();
                            Log.d("keyVal",key);

                            AttendanceModel attendanceModel = dataSnapshot2.getValue(AttendanceModel.class);

                            if (Integer.parseInt(String.valueOf(attendanceModel.getRoll())) == Integer.parseInt(roll) &&
                            attendanceModel.getValue() == 1){
                                presentList.add(roll);
                            }

                        }
                    }
                    Log.d("keySIZE3", String.valueOf(presentList.size()));

                    presentTvVa.setText(Integer.toString(presentList.size()));
                    totalTvVa.setText(Integer.toString(totalClass));

                    int presentClass = presentList.size();

                    double value = ((double)presentClass / totalClass);
                    double percent = (value * 100);

                    int absent = totalClass - presentClass;
                    absentTvVa.setText(Integer.toString(absent));

                    DecimalFormat numberFormat = new DecimalFormat("#.00");

                    percentTvVa.setText(String.valueOf(numberFormat.format(percent)) + "%");
                }
                else {
                    presentTvVa.setText("0");
                    totalTvVa.setText("0");
                    percentTvVa.setText("0.00%");
                    Toast.makeText(ViewAttendanceActivity.this, "No records found", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(ViewAttendanceActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

}