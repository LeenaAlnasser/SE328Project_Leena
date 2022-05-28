package com.example.myapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.StudentInformation;
import com.example.myapplication.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class SQLiteDataAdapter extends RecyclerView.Adapter<SQLiteDataAdapter.ViewHolder> {

    private ArrayList<StudentInformation> studentInformation;
    private Context mContext;
    OnViewClickListener onViewClickListener;

    public SQLiteDataAdapter(Context context, ArrayList<StudentInformation> studentInformation,
                             OnViewClickListener onViewClickListener) {
        this.mContext = context;
        this.studentInformation = studentInformation;
        this.onViewClickListener = onViewClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_information_item_list, parent, false);
        return new SQLiteDataAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StudentInformation object = studentInformation.get(position);
        holder.tvId.setText(object.getId());
        holder.tvName.setText(object.getName());
        holder.tvSurName.setText(object.getSurName());
        holder.tvFatherName.setText(object.getFatherName());
        holder.tvNationalId.setText(object.getNationalId().toString());
        holder.tvdob.setText(object.getDob());
        holder.tvGender.setText(object.getGender());

        holder.btnDeleteStudentInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onViewClickListener.onViewClick(view, holder.getAdapterPosition());
            }
        });

        holder.btnUpdateStudentInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onViewClickListener.onViewClick(view, holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return studentInformation.size();
    }

    public void notifyData(ArrayList<StudentInformation> studentInformation) {

        this.studentInformation = studentInformation;
        this.notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvId, tvName, tvSurName, tvFatherName, tvNationalId, tvdob,
                tvGender;
        MaterialButton btnUpdateStudentInformation, btnDeleteStudentInformation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvId);
            tvName = itemView.findViewById(R.id.tvName);
            tvSurName = itemView.findViewById(R.id.tvSurName);
            tvFatherName = itemView.findViewById(R.id.tvFatherName);
            tvNationalId = itemView.findViewById(R.id.tvNationalId);
            tvdob = itemView.findViewById(R.id.tvdob);
            tvGender = itemView.findViewById(R.id.tvGender);
            btnUpdateStudentInformation = itemView.findViewById(R.id.btnUpdateStudentInformation);
            btnDeleteStudentInformation = itemView.findViewById(R.id.btnDeleteStudentInformation);
        }
    }

    public interface OnViewClickListener {
        void onViewClick(View view, int position);
    }
}
