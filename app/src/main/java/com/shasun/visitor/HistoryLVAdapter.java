package com.shasun.visitor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class HistoryLVAdapter extends RecyclerView.Adapter<HistoryLVAdapter.ViewHolder> {
    private static ArrayList<String> leavestatus_list = new ArrayList<String>();
    private final int itemLayout;

    public HistoryLVAdapter(ArrayList<String> leavestatus_list, int itemLayout) {
        HistoryLVAdapter.leavestatus_list = leavestatus_list;
        this.itemLayout = itemLayout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String item = leavestatus_list.get(position);
        String[] strColumns = item.split("##");

        holder.txtVName.setText(strColumns[0]);
        holder.txtMobileNo.setText(strColumns[4]);
        holder.txtAddress.setText(strColumns[1]);
        holder.txtPurpose.setText(strColumns[2]);
        holder.txtInTime.setText(strColumns[5]);
        holder.txtlsOutTime.setText(strColumns[6]);
        holder.txtWhom.setText(strColumns[3]);
        holder.btnPrint.setTag(strColumns[8]);
        String base64 = strColumns[7];
        holder.btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CheckNetwork.isInternetAvailable(holder.btnPrint.getContext())) {
                    Toast.makeText(holder.btnPrint.getContext(), holder.btnPrint.getContext().getResources().getString(R.string.loginNoInterNet), Toast.LENGTH_LONG).show();
                    return;
                } else {

                    Context context = holder.btnPrint.getContext();
                    Intent intent;
                    intent = new Intent(context, TempActivity.class);
                    intent.putExtra("visitorData", item);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    context.startActivity(intent);
                   /*
                    ViewPassFragment fragment = new ViewPassFragment( v.getTag().toString());
                    FragmentTransaction transaction =  ((AppCompatActivity)
                            holder.btnPrint.getContext()).getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.nav_host_fragment_content_main_activity_navigation, fragment).addToBackStack(null).commit();
 */
                }

            }
        });

        byte[] byteArrPhoto = Base64.decode(base64, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArrPhoto, 0, byteArrPhoto.length);
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(holder.txtWhom.getContext().getResources(), bitmap);
        roundedBitmapDrawable.setCornerRadius(10);
        holder.lsimageView.setImageDrawable(roundedBitmapDrawable);

    }

    @Override
    public int getItemCount() {
        return leavestatus_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextInputEditText txtVName;
        private final TextInputEditText txtMobileNo;
        private final TextInputEditText txtAddress;
        private final TextInputEditText txtPurpose;
        private final TextInputEditText txtInTime;
        private final TextInputEditText txtlsOutTime;
        private final TextInputEditText txtWhom;
        private final ImageView lsimageView;
        private final ImageButton btnPrint;

        private long lngLeaveApplnId = 0;

        public ViewHolder(View itemView) {
            super(itemView);
            lsimageView = itemView.findViewById(R.id.lsimageView);
            txtVName = itemView.findViewById(R.id.txtVName);
            txtMobileNo = itemView.findViewById(R.id.txtMobileNo);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            txtPurpose = itemView.findViewById(R.id.txtPurpose);
            txtInTime = itemView.findViewById(R.id.txtInTime);
            txtlsOutTime = itemView.findViewById(R.id.txtlsOutTime);
            txtWhom = itemView.findViewById(R.id.txtWhom);
            btnPrint = itemView.findViewById(R.id.btnPrint);


        }
    }

    public void removeAt(int position) {
        leavestatus_list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, leavestatus_list.size());
    }
}
