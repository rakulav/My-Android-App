package com.beamotivator.beam.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.beamotivator.beam.R;
import com.beamotivator.beam.models.ModelSuggestions;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Objects;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterSuggestions extends RecyclerView.Adapter<AdapterSuggestions.HolderSuggestion>{

    Context context;
    FirebaseAuth firebaseAuth;
    List<ModelSuggestions> suggestionsList;

    public AdapterSuggestions(Context context, List<ModelSuggestions> suggestionsList) {
        this.context = context;
        this.suggestionsList = suggestionsList;
    }

    @NonNull
    @Override
    public HolderSuggestion onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_suggestions,viewGroup,false);

        return new HolderSuggestion(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderSuggestion holder, int i) {

        String name = suggestionsList.get(i).getName();
        final String suggestion = suggestionsList.get(i).getSuggestion();
        String uDp = suggestionsList.get(i).getuDp();

        final String suggestionId = suggestionsList.get(i).getSugId();

        holder.sugName.setText(name);
        holder.sugDesc.setText(suggestion);

        try{
            Glide.with(context)
                    .load(uDp)
                    .into(holder.sugDp);
        }
        catch (Exception ignored)
        {

        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder =  new AlertDialog.Builder(context);
                builder.setTitle("Delete Suggestion");
                builder.setMessage("Are you sure to delete this suggestion? ");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        DatabaseReference sugRef = FirebaseDatabase.getInstance().getReference("Suggestions");
                        sugRef.child(suggestionId)
                                .removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(context, "Suggestion deleted", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.create().show();


                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return suggestionsList.size();
    }

    static class HolderSuggestion extends RecyclerView.ViewHolder {

        CircleImageView sugDp;
        TextView sugName, sugDesc;

        public HolderSuggestion(@NonNull View itemView) {
            super(itemView);

            sugDp = itemView.findViewById(R.id.sugPictureIv);
            sugName = itemView.findViewById(R.id.sugNameTv);
            sugDesc = itemView.findViewById(R.id.sugDescriptionTv);
        }
    }
}
