package hk.ust.aed.menu;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;

import java.io.File;

/**
 * Created by Administrator on 20/07/2017.
 */

public abstract class DownloadFile {
    public abstract void onDownloaded(File file);

    public void downloadFileAndAct(StorageReference storageRef, final File outFile){
        storageRef.getFile(outFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                //Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                onDownloaded(outFile);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                exception.printStackTrace();
            }
        });
    }

}
