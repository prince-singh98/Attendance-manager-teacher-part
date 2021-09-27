package app.example.attendancemanager.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import app.example.attendancemanager.ClassFunctionsActivity;
import app.example.attendancemanager.MainActivity;
import app.example.attendancemanager.R;
import app.example.attendancemanager.model.CreateClassModel;

import static app.example.attendancemanager.MainActivity.classList;
import static app.example.attendancemanager.MainActivity.selectedClassIndex;

public class ClassListAdapter extends RecyclerView.Adapter<ClassListAdapter.ViewHolder> {

    List<CreateClassModel> createClassModelList;

    public ClassListAdapter(MainActivity mainActivity, List<CreateClassModel> createClassModelList) {
        this.createClassModelList = createClassModelList;
    }

    @NonNull
    @Override
    public ClassListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_list_layout,parent,false);

        //for random colors
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255));
        view.setBackgroundColor(color);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassListAdapter.ViewHolder viewHolder, int position) {

        String title = createClassModelList.get(position).getClassTitle();
        String code = createClassModelList.get(position).getId();

        viewHolder.setData(title, code, position, this);

    }

    @Override
    public int getItemCount() {
        return createClassModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView className, classCode;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            className = itemView.findViewById(R.id.className);
            classCode = itemView.findViewById(R.id.classCode);

        }

        public void setData(String title, String code, final int position, final ClassListAdapter classListAdapter) {

            className.setText(title);
            classCode.setText(code);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivity.selectedClassIndex = position;

                    Intent intent = new Intent(itemView.getContext(), ClassFunctionsActivity.class);
                    intent.putExtra("selectedClass", selectedClassIndex);
                    itemView.getContext().startActivity(intent);
                }
            });

        }

    }
}
