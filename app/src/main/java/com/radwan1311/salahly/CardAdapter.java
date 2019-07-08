package com.radwan1311.salahly;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter {

    public Context mcontext;
    public List<DatabaseData> databaseData ;
    TextView cViewName, cViewJob, cViewPhone, cViewCity;
    Button cViewProfileButton;
    ImageView cViewProfileImage;


    class CardAdapterViewHolder extends RecyclerView.ViewHolder{
        TextView cViewName, cViewJob, cViewPhone, cViewCity;
        Button cViewProfileButton;
        ImageView cViewProfileImage;

        public CardAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            cViewName = (TextView) itemView.findViewById(R.id.ScardViewName);
            cViewCity = (TextView)itemView.findViewById(R.id.ScardViewCity);
            cViewJob = (TextView)itemView.findViewById(R.id.ScardViewJob);
            cViewPhone = (TextView)itemView.findViewById(R.id.ScardViewPhone);
            cViewProfileButton = (Button) itemView.findViewById(R.id.ScardViewProfileButton);
            cViewProfileImage = (ImageView)itemView.findViewById(R.id.ScardViewProfile_image);
        }
    }
    public CardAdapter(Context context , List<DatabaseData>list){
        mcontext = context ;
        databaseData = list ;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mcontext).inflate(R.layout.content_main_screen,viewGroup,false);
        return new CardAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        cViewName.setText("" + databaseData.get(i).getName());
        cViewCity.setText("" + databaseData.get(i).getCity());
        cViewJob.setText("" + databaseData.get(i).getJob());
        cViewPhone.setText("" + databaseData.get(i).getPhone());
        Picasso.get().load(databaseData.get(i).getImageUrl()).fit().centerCrop().into(cViewProfileImage);
    }





    @Override
    public int getItemCount() {
        return databaseData.size();
    }



}
