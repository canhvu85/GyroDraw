package ch.epfl.sweng.SDP.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import ch.epfl.sweng.SDP.Activity;
import ch.epfl.sweng.SDP.auth.Account;
import ch.epfl.sweng.SDP.firebase.FbStorage;

public class ImageSharer {

    private static ImageSharer instance = null;

    private Activity activity;
    Context context;

    ImageSharer(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    /**
     * Get this ImageSharer instance.
     *
     * @param context context calling this method
     * @return ImageSharer instance
     */
    public static ImageSharer getInstance(Context context, Activity activity) {
        if (instance == null) {
              instance = new ImageSharer(context,activity);
        }
        return instance;
    }

    /**
     * Get the ImageSharer instance.
     */
    public static ImageSharer getInstance() {
        return instance;
    }

    /**
     * Use this method to set the activity. This method should also be used to prevent
     * reference cycle by setting the activity to null when it's not used anymore.
     * @param activity the activity
     */
    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    /**
     * Share an image to to facebook.
     * @param image the image to share.
     */
    public void shareImageToFacebook(Bitmap image) {
        // Check if Facebook app is installed.
        if (ShareDialog.canShow(SharePhotoContent.class)) {
            SharePhoto photo = new SharePhoto.Builder()
                    .setBitmap(image)
                    .build();
            SharePhotoContent content = new SharePhotoContent.Builder()
                    .addPhoto(photo)
                    .build();
            ShareDialog.show(activity,content);
        } else {
            // Facebook app not install, use web instead.
            uploadImageToFireBase(image);
        }
    }

    private void uploadImageToFireBase(Bitmap image) {
        Account account = Account.getInstance(context);
        String imageName = "DRAWING_" + account.getTotalMatches()
                + "_" + account.getUsername() + ".jpg";
        final StorageReference ref = FirebaseStorage.getInstance().getReference().child(imageName);
        FbStorage.sendBitmapToFirebaseStorage(image, ref,
                new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                getUrl(ref);
            }
        });
    }

    private void getUrl(StorageReference ref) {
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(final Uri uri) {
                // Share image to facebook after getting url
                shareDrawingToFacebook(uri);
                Log.d("SUCESS", "Successfully uploaded a task");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("ERROR", "Error uploading task");
            }
        });
    }

    private void shareDrawingToFacebook(Uri uri) {
        ShareLinkContent linkContent = new ShareLinkContent.Builder().setContentUrl(uri)
                .build();
        ShareDialog.show(activity,linkContent);
    }


}
