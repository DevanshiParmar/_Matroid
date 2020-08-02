package com.example.smartindiahackathon;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.senab.photoview.PhotoViewAttacher;

import static android.content.ContentValues.TAG;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>{

    private List<Comments_Data> mCommentsList;
    Context context;
    public CommentsAdapter(Context mContext, List<Comments_Data> mCommentsList){
        this.context = mContext;
        this.mCommentsList = mCommentsList;
    }

    @Override
    public CommentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_single_layout, parent, false);

        return new CommentsViewHolder(v);
    }

    public class CommentsViewHolder extends RecyclerView.ViewHolder{
        public TextView commentText;
        public TextView name;
        public TextView timeStamp;
        public CircleImageView profileImage;
        public ImageView messageImage;
        public ScrollView scrollView;
        public CardView cardView;
        public CommentsViewHolder(View itemView) {
            super(itemView);

            commentText = itemView.findViewById(R.id.comment_text_layout);
            name = itemView.findViewById(R.id.nameId);
            profileImage = itemView.findViewById(R.id.profile_pic);
            timeStamp = itemView.findViewById(R.id.timeView);
            messageImage = itemView.findViewById(R.id.image_message);
            scrollView = itemView.findViewById(R.id.scrollMessages);
            cardView = itemView.findViewById(R.id.cardViewImage);
        }
    }

    @Override
    public void onBindViewHolder(final CommentsViewHolder holder, final int position) {
        final Comments_Data c = mCommentsList.get(position);
        String type = c.getType();

        if(type.equals("text")){
            holder.cardView.removeAllViews();
//            holder.messageImage.setVisibility(View.INVISIBLE);
            holder.commentText.setText(c.getComment());
            holder.name.setText(c.getName_commentor());
            holder.timeStamp.setText(c.getTime());
            Picasso.with(context).load(c.getThumbnail_image_url()).networkPolicy(NetworkPolicy.OFFLINE).noFade().into(holder.profileImage, new Callback() {
                @Override
                public void onSuccess() {
                    Picasso.with(context).load(c.getThumbnail_image_url()).into(holder.profileImage);
                }

                @Override
                public void onError() {
                    Picasso.with(context).load(c.getThumbnail_image_url()).into(holder.profileImage);
                }
            });
            Log.d(TAG, "onBindViewHolder: " + c.getThumbnail_image_url());
        }
        else if(type.equals("image")){
            holder.commentText.setVisibility(View.INVISIBLE);
            holder.name.setText(c.getName_commentor());
            holder.timeStamp.setText(c.getTime());
            Picasso.with(context).load(c.getThumbnail_image_url()).networkPolicy(NetworkPolicy.OFFLINE).noFade().into(holder.profileImage, new Callback() {
                @Override
                public void onSuccess() {
                    Picasso.with(context).load(c.getThumbnail_image_url()).into(holder.profileImage);
                }

                @Override
                public void onError() {
                    Picasso.with(context).load(c.getThumbnail_image_url()).into(holder.profileImage);
                }
            });
            Picasso.with(context).load(c.getComment()).networkPolicy(NetworkPolicy.OFFLINE)
                    .into(holder.messageImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            Picasso.with(context).load(c.getComment()).into(holder.messageImage);
                        }

                        @Override
                        public void onError() {
                            Picasso.with(context).load(c.getComment()).into(holder.messageImage);
                        }
                    });
            Log.d(TAG, "onBindViewHolder: " + c.getComment());
        }

        /*PhotoViewAttacher pAttacher;
        pAttacher = new PhotoViewAttacher(holder.messageImage);
        pAttacher.update();*/

        holder.messageImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "ImageView " + position + " Clicked!", Toast.LENGTH_LONG).show();
            }
        });

        Log.d(TAG, "onBindViewHolder: COUNT " + getItemCount());

    }

    @Override
    public int getItemCount() {
        return mCommentsList.size();
    }

}