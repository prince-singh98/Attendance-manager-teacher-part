package app.example.attendancemanager.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import app.example.attendancemanager.R;
import app.example.attendancemanager.model.AttendanceModel;

import static app.example.attendancemanager.CheckAttendanceActivity.checkDate;
import static app.example.attendancemanager.MainActivity.classList;
import static app.example.attendancemanager.MainActivity.selectedClassIndex;

public class CheckAttendanceAdapter extends ArrayAdapter<AttendanceModel> {

    private Activity context;
    private List<AttendanceModel> attendanceModelList;

    public CheckAttendanceAdapter(Activity context, List<AttendanceModel> attendanceModelList) {
        super(context, R.layout.check_attendance_list_layout, attendanceModelList);
        this.context = context;
        this.attendanceModelList = attendanceModelList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

        View view = inflater.inflate(R.layout.check_attendance_list_layout,null,true);

        TextView rollTvCA = view.findViewById(R.id.rollTvCA);
        final TextView attendanceValueCA = view.findViewById(R.id.attendanceValueTvCA);

        final Dialog updateAttendanceDialog;


        updateAttendanceDialog = new Dialog(view.getContext());
        updateAttendanceDialog.setContentView(R.layout.update_attendance_dialog_layout);
        updateAttendanceDialog.setCancelable(true);
        updateAttendanceDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);


        final EditText attendance_value_Et_dialog = updateAttendanceDialog.findViewById(R.id.attendance_value_Et_dialog);
        Button update_attendance_btn_dialog = updateAttendanceDialog.findViewById(R.id.update_attendance_btn_dialog);

        final AttendanceModel attendanceModel = attendanceModelList.get(position);

        rollTvCA.setText(String.valueOf(attendanceModel.getRoll()));
        attendanceValueCA.setText(String.valueOf(attendanceModel.getValue()));


        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                attendance_value_Et_dialog.setText(String.valueOf(attendanceModel.getValue()));
                updateAttendanceDialog.show();


                return false;
            }
        });

        update_attendance_btn_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String value = attendance_value_Et_dialog.getText().toString();

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Attendance")
                        .child(classList.get(selectedClassIndex).getId()).child(checkDate);

                //myRef.child(userID).child("which field do you want to update").setValue(ValueVariable);
                reference.child(String.valueOf(attendanceModel.getRoll())).child("value").setValue(Integer.valueOf(value));
            }
        });


        return view;
    }


}
