package hk.ust.aed.menu.adapters;

/**
 * Created by pratap.kesaboyina on 24-12-2014.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import hk.ust.aed.menu.ParseRequest;
import hk.ust.aed.menu.R;
import hk.ust.aed.menu.SendGetRequest;
import hk.ust.aed.menu.models.SingleItemModel;

public class SectionListDataAdapter extends RecyclerView.Adapter<SectionListDataAdapter.SingleItemRowHolder> {

    private ArrayList<SingleItemModel> itemsList;
    private Context mContext;
    Bitmap image;

    public SectionListDataAdapter(Context context, ArrayList<SingleItemModel> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_single_card, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int i) {

        SingleItemModel singleItem = itemsList.get(i);

        holder.tvTitle.setText(singleItem.getName());

        File outFile = new File(mContext.getExternalFilesDir(null), "trump.png");
        updateAndSetHolderImage(holder, outFile);

       /* Glide.with(mContext)
                .load(feedItem.getImageURL())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.bg)
                .into(feedListRowHolder.thumbView);*/
    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView tvTitle;

        protected ImageView itemImage;

        public SingleItemRowHolder(View view) {
            super(view);

            this.tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            this.itemImage = (ImageView) view.findViewById(R.id.itemImage);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
    }

    void updateAndSetHolderImage(final SingleItemRowHolder holder, final File outFile){
        final boolean download = !outFile.exists() || (outFile.length() <= 0);
        ParseRequest downloadAndSet = new ParseRequest() {
            @Override
            public void execute(String response) {
                try {
                    FileOutputStream fos = new FileOutputStream(outFile.getAbsolutePath());
                    if (download) {
                        if (outFile.exists()){
                            outFile.delete();
                        }
                        try {
                            Log.e("Downloading", outFile.getAbsolutePath());
                            byte[] decodedString = Base64.decode(response, Base64.DEFAULT);
                            image = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            Log.e(outFile.getAbsolutePath(), Boolean.toString(image.compress(Bitmap.CompressFormat.PNG, 100, fos))); // successful
                            fos.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.e("SZ", Long.toString(outFile.length()));
                    }
                    else {
                        Log.e("Reusing", outFile.getAbsolutePath()); // never happens
                        image = BitmapFactory.decodeFile(outFile.getAbsolutePath());
                    }
                    holder.itemImage.setImageBitmap(image);
                } catch(FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };
        if(download){
            new SendGetRequest(mContext, "https://ustadrsh-cf116.firebaseio.com/trump.json", downloadAndSet);
        }
        else {
            downloadAndSet.execute(null);
        }

        /*byte[] byteArray;
        try {
            byteArray = fullyReadFileToBytes(outFile);
            image = BitmapFactory.decodeByteArray(byteArray, 0, (int) outFile.length());
            holder.itemImage.setImageBitmap(image);
        } catch (IOException e){
            e.printStackTrace();
        }*/
    }

    byte[] fullyReadFileToBytes(File f) throws IOException {
        int size = (int) f.length();
        byte bytes[] = new byte[size];
        byte tmpBuff[] = new byte[size];
        FileInputStream fis= new FileInputStream(f);;
        try {

            int read = fis.read(bytes, 0, size);
            if (read < size) {
                int remain = size - read;
                while (remain > 0) {
                    read = fis.read(tmpBuff, 0, remain);
                    System.arraycopy(tmpBuff, 0, bytes, size - remain, read);
                    remain -= read;
                }
            }
        }  catch (IOException e){
            throw e;
        } finally {
            fis.close();
        }

        return bytes;
    }

}