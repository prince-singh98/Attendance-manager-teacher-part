package app.example.attendancemanager;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static app.example.attendancemanager.ApplicationActivity.selectedApplicationIndex;

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.ViewHolder> {

    List<ApplicationModel> applicationModelList;

    public ApplicationAdapter(ApplicationActivity applicationActivity, List<ApplicationModel> applicationModelList) {
        this.applicationModelList = applicationModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.application_list_layout,parent,false);


        return new ApplicationAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        String from = applicationModelList.get(position).getFrom();
        String subject = applicationModelList.get(position).getSubject();

        viewHolder.setData(from,subject,position);


    }

    @Override
    public int getItemCount() {
        return applicationModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView fromTvAl, subjectTvAl;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            fromTvAl = itemView.findViewById(R.id.fromTvAl);
            subjectTvAl = itemView.findViewById(R.id.subjectTvAl);
        }

        public void setData(String from, String subject, final int position) {
            fromTvAl.setText(from);
            subjectTvAl.setText(subject);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            selectedApplicationIndex = position;

                            Intent intent = new Intent(itemView.getContext(), ViewApplicationActivity.class);
                            intent.putExtra("selectedClass", selectedApplicationIndex);
                            itemView.getContext().startActivity(intent);
                        }
                    });

                }
            });
        }
    }
}
