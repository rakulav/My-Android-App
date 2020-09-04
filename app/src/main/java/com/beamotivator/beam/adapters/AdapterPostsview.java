package com.beamotivator.beam.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.beamotivator.beam.Imagepopup.PhotoFullPopupWindow;
import com.beamotivator.beam.PostDetailActivity;
import com.beamotivator.beam.PostLikedByActivity;
import com.beamotivator.beam.R;
import com.beamotivator.beam.ThierProfile;
import com.beamotivator.beam.ThierProfile2;
import com.beamotivator.beam.Variables;
import com.beamotivator.beam.View_Post;
import com.beamotivator.beam.models.ModelPost;
import com.bumptech.glide.Glide;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.downloader.request.DownloadRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;


public class AdapterPostsview extends RecyclerView.Adapter<AdapterPostsview.MyHolder> {

    Context context;
    List<ModelPost> postList;
    FirebaseAuth firebaseAuth;
    LinearLayout bottomSheet;
    CircleImageView bottomProfileImage;
    TextView bottomSheetName;

    String myUid;

    private  DatabaseReference likesRef; //for likes database node
    private  DatabaseReference postsRef; //for posts database node
    private  DatabaseReference totalLikesRef;

    boolean mProcessLike = false;
    boolean mSaved = false;
    public AdapterPostsview(Context context, List<ModelPost> postList) {
        this.context = context;
        this.postList = postList;
        firebaseAuth = FirebaseAuth.getInstance();
        myUid = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        likesRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        postsRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        totalLikesRef = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //inflate layout row_post.xml
        View view = LayoutInflater.from(context).inflate(R.layout.row_posts1, viewGroup, false);

        bottomSheet = view.findViewById(R.id.bottomSheetContainer);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, final int i) {
        //get data
        final String uid = postList.get(i).getUid();
        final String uName = postList.get(i).getuName();
        String uDp = postList.get(i).getuDp();
        final String pId = postList.get(i).getpId();
        final String pDescription = postList.get(i).getpDescr();
        final String pImage = postList.get(i).getpImage();
        String pTimeStamp = postList.get(i).getpTime();
        String pComments = postList.get(i).getpComments(); //total number of comments for a post
        String timestamp = postList.get(i).getStamp();
        final String email = postList.get(i).getuEmail();

        //to convert it date time
        try {
            Glide.with(context)
                    .load(pImage)
                    .centerInside()
                    .into(myHolder.image_furniture);
        }
        catch (Exception e) {
            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        Query query = likesRef.orderByChild("pId").equalTo(pId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren())
                {
                    String pLikes = ""+ Objects.requireNonNull(ds.child("pLikes").getValue()).toString();
                    myHolder.tv_likeCount.setText(pLikes + " Helpful");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        myHolder.image_furniture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(context, View_Post.class);
                intent.putExtra("uid",uid);
                intent.putExtra("position",i);
                SharedPreferences sh=context.getSharedPreferences("posts",MODE_PRIVATE);
                SharedPreferences.Editor ee=sh.edit();
                ee.putString("uid",uid);
                ee.putInt("choice",1);
                ee.putInt("position",i);
                ee.commit();

                context.startActivity(intent);


            }
        });

    }

    private void savePost(final String myUid, final String pId) {
        mSaved = true;
        String timestamp = ""+System.currentTimeMillis();
        final DatabaseReference savedRef = FirebaseDatabase.getInstance().getReference("Extras");

        savedRef.child(myUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(mSaved){
                    if(snapshot.child("Saved").hasChild(pId)){
                        //already like so remove like
                        savedRef.child(myUid).child("Saved").child(pId).removeValue();

                    }
                    else
                    {
                        savedRef.child(myUid).child("Saved").child(pId).setValue("Saved");

                    }
                    mSaved = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }












    @Override
    public int getItemCount() {
        return postList.size();
    }

    //view holder class
    static class MyHolder extends RecyclerView.ViewHolder {

        //views from row_post.xml
        ImageView image_furniture;

        TextView tv_likeCount;

        public MyHolder(@NonNull View itemView) {
            super(itemView);


            //init views
            image_furniture = itemView.findViewById(R.id.image_furniture);
            tv_likeCount = itemView.findViewById(R.id.tv_likeCount);



        }
    }
    public static Date currentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    private static int getTimeDistanceInMinutes(long time) {
        long timeDistance = currentDate().getTime() - time;
        return Math.round((Math.abs(timeDistance) / 1000) / 60);
    }


}
