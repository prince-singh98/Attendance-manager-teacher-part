package app.example.attendancemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import app.example.attendancemanager.adapter.StudentAdapter;
import app.example.attendancemanager.model.StudentModel;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static app.example.attendancemanager.MainActivity.classList;
import static app.example.attendancemanager.MainActivity.selectedClassIndex;

public class StudentsActivity extends AppCompatActivity {

    public static int selectedRollNo = 0;

    private Dialog loadingDialog, addStudentDialog;
    private TextView classNameTv;
    private EditText roll, name, email;
    private Button addStudentBtn;

    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    FirebaseUser currentUser;

    public static List<StudentModel> studentList;
    RecyclerView studentRecycler;
    StudentAdapter studentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);

        Toolbar toolbar = findViewById(R.id.student_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Students");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("Students").child(classList.get(selectedClassIndex).getId());
        currentUser = mAuth.getCurrentUser();


        loadingDialog = new Dialog(StudentsActivity.this);
        loadingDialog.setContentView(R.layout.loading_progressbar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.progress_background);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        addStudentDialog = new Dialog(StudentsActivity.this);
        addStudentDialog.setContentView(R.layout.student_details_dialog_layout);
        addStudentDialog.setCancelable(true);
        addStudentDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        classNameTv = addStudentDialog.findViewById(R.id.studentDetailsFormTvDialog);
        roll = addStudentDialog.findViewById(R.id.studentRollDialog);
        name = addStudentDialog.findViewById(R.id.studentNameDialog);
        email = addStudentDialog.findViewById(R.id.studentEmailDialog);
        addStudentBtn = addStudentDialog.findViewById(R.id.addBtnDialog);

        studentRecycler = findViewById(R.id.student_recycler);

        String className = classList.get(selectedClassIndex).getClassTitle();
        classNameTv.setText(className);

        addStudentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Query query1 = firebaseDatabase.getReference().child("Students")
                        .child(classList.get(selectedClassIndex).getId())
                        .orderByChild("roll").equalTo(Integer.parseInt(roll.getText().toString()));
                query1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()){

                            Query query2 = firebaseDatabase.getReference().child("Students")
                                    .child(classList.get(selectedClassIndex).getId())
                                    .orderByChild("id").equalTo(email.getText().toString());

                            query2.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                        Toast.makeText(StudentsActivity.this, "This email is already added", Toast.LENGTH_SHORT).show();


                                    }else {
                                        addNewStudent(Integer.parseInt(roll.getText().toString()),
                                                name.getText().toString(),
                                                email.getText().toString());

                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(StudentsActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });


                        }else {
                            Toast.makeText(StudentsActivity.this, "Roll is already taken ", Toast.LENGTH_SHORT).show();


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Toast.makeText(StudentsActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });


            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        studentRecycler.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onStart() {
        super.onStart();

        studentList = new ArrayList<>();
        studentAdapter = new StudentAdapter(StudentsActivity.this, studentList);
        studentRecycler.setAdapter(studentAdapter);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                studentList.clear();

                for (DataSnapshot studentSnapshot : snapshot.getChildren()){

                    StudentModel studentModel = studentSnapshot.getValue(StudentModel.class);
                    studentList.add(studentModel);

                    studentAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addNewStudent(int i, String s, String toString){
        String studentRoll = roll.getText().toString();
        String studentName = name.getText().toString();
        String studentEmail = email.getText().toString();

        if (!TextUtils.isEmpty(studentRoll) && !TextUtils.isEmpty(studentName) && !TextUtils.isEmpty(studentEmail)){

            String className = classList.get(selectedClassIndex).getClassTitle();
            String classCode = classList.get(selectedClassIndex).getId();

            StudentModel studentModel = new StudentModel(studentEmail, Integer.valueOf(studentRoll), studentName, className,classCode);

            reference.child(String.valueOf(studentRoll)).setValue(studentModel);


            Toast.makeText(this, "Student Added Successfully.", Toast.LENGTH_SHORT).show();

        }else {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        }

        addStudentDialog.dismiss();
    }

    public void addStudentForm(View view) {
        roll.getText().clear();
        name.getText().clear();
        email.getText().clear();
        addStudentDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();

        }
        return super.onOptionsItemSelected(item);
    }
}