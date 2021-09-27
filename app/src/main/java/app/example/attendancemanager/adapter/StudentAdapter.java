package app.example.attendancemanager.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import app.example.attendancemanager.ClassFunctionsActivity;
import app.example.attendancemanager.R;
import app.example.attendancemanager.StudentsActivity;
import app.example.attendancemanager.ViewAttendanceActivity;
import app.example.attendancemanager.model.AttendanceModel;
import app.example.attendancemanager.model.StudentModel;

import static app.example.attendancemanager.MainActivity.classList;
import static app.example.attendancemanager.MainActivity.selectedClassIndex;
import static app.example.attendancemanager.StudentsActivity.selectedRollNo;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

    List<StudentModel> studentModelList;

    public StudentAdapter(StudentsActivity studentsActivity, List<StudentModel> studentModelList) {
        this.studentModelList = studentModelList;
    }

    @NonNull
    @Override
    public StudentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_list_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final StudentAdapter.ViewHolder viewHolder, final int position) {

        int roll = studentModelList.get(position).getRoll();
        String name = studentModelList.get(position).getName();

        viewHolder.setData(roll, name, position, this);

    }

    @Override
    public int getItemCount() {
        return studentModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView rollTv, nameTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rollTv = itemView.findViewById(R.id.rollTv);
            nameTv = itemView.findViewById(R.id.nameTv);

        }

        public void setData(final int roll, String name, final int position, StudentAdapter studentAdapter) {

            rollTv.setText(String.valueOf(roll));
            nameTv.setText(name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedRollNo = position;

                    Intent intent = new Intent(itemView.getContext(), ViewAttendanceActivity.class);
                    intent.putExtra("selectedRollNo", selectedRollNo);
                    itemView.getContext().startActivity(intent);
                }
            });

        }
    }
}