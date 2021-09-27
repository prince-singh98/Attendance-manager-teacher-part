package app.example.attendancemanager.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import app.example.attendancemanager.R;
import app.example.attendancemanager.model.AttendanceModel;
import app.example.attendancemanager.model.StudentModel;

import static app.example.attendancemanager.MainActivity.classList;
import static app.example.attendancemanager.MainActivity.selectedClassIndex;
import static app.example.attendancemanager.TakeAttendanceActivity.selectedDate;

public class TakeAttendanceAdapter extends RecyclerView.Adapter<TakeAttendanceAdapter.ViewHolder> {

    List<StudentModel> studentList;

    public TakeAttendanceAdapter(List<StudentModel> studentList) {
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public TakeAttendanceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance_list_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TakeAttendanceAdapter.ViewHolder viewHolder, int position) {

        int roll = studentList.get(position).getRoll();
        String name = studentList.get(position).getName();

        viewHolder.setData(roll, name, position, this);

    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView rollTv, nameTv;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rollTv = itemView.findViewById(R.id.rollTv);
            nameTv = itemView.findViewById(R.id.nameTv);
            checkBox = itemView.findViewById(R.id.check_box);
        }

        public void setData(final int roll, String name, int position, TakeAttendanceAdapter takeAttendanceAdapter) {

            rollTv.setText(String.valueOf(roll));
            nameTv.setText(name);

            final AttendanceModel[] attendanceModel = new AttendanceModel[1];
            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Attendance")
                    .child(classList.get(selectedClassIndex).getId());

            attendanceModel[0] = new AttendanceModel(selectedDate, roll, 0);
            reference.child(selectedDate).child(String.valueOf(roll)).setValue(attendanceModel[0]);

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    for (int i=0; i<studentList.size(); i++){

                        if (checkBox.isChecked()) {
                            attendanceModel[0] = new AttendanceModel(selectedDate, roll, 1);
                            reference.child(selectedDate).child(String.valueOf(roll)).setValue(attendanceModel[0]);
                        } else if (!checkBox.isChecked()) {
                            attendanceModel[0] = new AttendanceModel(selectedDate, roll, 0);
                            reference.child(selectedDate).child(String.valueOf(roll)).setValue(attendanceModel[0]);
                        }
                    }

                }
            });

        }
    }
}
