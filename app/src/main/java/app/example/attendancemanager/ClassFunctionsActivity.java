package app.example.attendancemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import static app.example.attendancemanager.MainActivity.classList;
import static app.example.attendancemanager.MainActivity.selectedClassIndex;

public class ClassFunctionsActivity extends AppCompatActivity {

    Button studentsBtn, takeAttendanceBtn, checkAttendanceBtn, applicationBtn;
    TextView setClassNameTv, selectDateTv;

    //for date picker
    DatePickerDialog.OnDateSetListener mDateSetListener;
    public static String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_functions);

        studentsBtn = findViewById(R.id.studentsBtn);
        studentsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassFunctionsActivity.this, StudentsActivity.class);
                startActivity(intent);
            }
        });

        takeAttendanceBtn = findViewById(R.id.takeAttendanceBtn);
        takeAttendanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ClassFunctionsActivity.this, TakeAttendanceActivity.class);
                intent1.putExtra("DATE", date);
                startActivity(intent1);
            }
        });


        checkAttendanceBtn = findViewById(R.id.checkAttendanceBtn);
        checkAttendanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(ClassFunctionsActivity.this, CheckAttendanceActivity.class);
                intent2.putExtra("DATE", date);
                startActivity(intent2);
            }
        });

        applicationBtn = findViewById(R.id.applicationBtn);
        applicationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(ClassFunctionsActivity.this, ApplicationActivity.class);
                startActivity(intent3);
            }
        });

        setClassNameTv = findViewById(R.id.setClassNameTv);
        setClassNameTv.setText(classList.get(selectedClassIndex).getClassTitle());

        selectDateTv = findViewById(R.id.selectDateTv);
        selectDateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        ClassFunctionsActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;

                Log.d("DatePicker",  + day + "-" + month + "-" + year);

                date = day + "-" + month + "-" + year;
                selectDateTv.setText(date);

            }
        };


    }
}