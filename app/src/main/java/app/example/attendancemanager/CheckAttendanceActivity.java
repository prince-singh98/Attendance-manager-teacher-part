package app.example.attendancemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import app.example.attendancemanager.adapter.CheckAttendanceAdapter;
import app.example.attendancemanager.model.AttendanceModel;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static app.example.attendancemanager.MainActivity.classList;
import static app.example.attendancemanager.MainActivity.selectedClassIndex;

public class CheckAttendanceActivity extends AppCompatActivity {

    public static String checkDate;

    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    FirebaseUser currentUser;

    TextView classNameTvCA, dateTvCA;
    ListView listViewCA;
    Button submitBtnCA;

    List<AttendanceModel> listCA;
    CheckAttendanceAdapter checkAttendanceAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_attendance);

        Toolbar toolbar = findViewById(R.id.check_attendance_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Check Attendance.");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        classNameTvCA = findViewById(R.id.classNameTvVA);
        dateTvCA = findViewById(R.id.selectedDateTvCA);
        listViewCA = findViewById(R.id.listViewVA);
        submitBtnCA = findViewById(R.id.doneBtnCA);
        submitBtnCA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CheckAttendanceActivity.this, "Done", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        listCA = new ArrayList<>();


        checkDate = getIntent().getExtras().getString("DATE");
        dateTvCA.setText(checkDate);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("Attendance").child(classList.get(selectedClassIndex).getId()).child(checkDate);
        currentUser = mAuth.getCurrentUser();


        classNameTvCA.setText(classList.get(selectedClassIndex).getClassTitle());

    }

    @Override
    protected void onStart() {
        super.onStart();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listCA.clear();

                if (snapshot.exists()){
                    for (DataSnapshot ds : snapshot.getChildren()){
                        AttendanceModel attendanceModel = ds.getValue(AttendanceModel.class);
                        listCA.add(attendanceModel);
                    }

                    checkAttendanceAdapter = new CheckAttendanceAdapter(CheckAttendanceActivity.this, listCA);
                    listViewCA.setAdapter(checkAttendanceAdapter);
                }else {
                    Toast.makeText(CheckAttendanceActivity.this, "No record found.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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