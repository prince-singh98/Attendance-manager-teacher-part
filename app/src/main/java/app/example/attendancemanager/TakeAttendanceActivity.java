package app.example.attendancemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import app.example.attendancemanager.adapter.TakeAttendanceAdapter;
import app.example.attendancemanager.model.StudentModel;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

public class TakeAttendanceActivity extends AppCompatActivity {

    TextView selectedDateTvTakeAttendance;
    Button submit;

    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    FirebaseUser currentUser;

    public static List<StudentModel> myStudentList;
    RecyclerView takeAttendanceRecycler;
    TakeAttendanceAdapter takeAttendanceAdapter;

    public static String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance);

        Toolbar toolbar = findViewById(R.id.take_attendance_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Take Attendance.");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        selectedDate = getIntent().getExtras().getString("DATE");

        selectedDateTvTakeAttendance = findViewById(R.id.selectedDateTvTakeAttendance);
        selectedDateTvTakeAttendance.setText(selectedDate);

        takeAttendanceRecycler = findViewById(R.id.take_attendance_recycler);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("Students").child(classList.get(selectedClassIndex).getId());
        currentUser = mAuth.getCurrentUser();

        submit = findViewById(R.id.submitBtn);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TakeAttendanceActivity.this, "Done", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        takeAttendanceRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        myStudentList = new ArrayList<>();
        takeAttendanceAdapter = new TakeAttendanceAdapter(myStudentList);
        takeAttendanceRecycler.setAdapter(takeAttendanceAdapter);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                myStudentList.clear();

                for (DataSnapshot studentSnapshot : snapshot.getChildren()){

                    StudentModel studentModel = studentSnapshot.getValue(StudentModel.class);
                    myStudentList.add(studentModel);

                    takeAttendanceAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(TakeAttendanceActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

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