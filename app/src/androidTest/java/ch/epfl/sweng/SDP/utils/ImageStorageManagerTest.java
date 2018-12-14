package ch.epfl.sweng.SDP.utils;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.GrantPermissionRule;
import android.support.v4.app.ActivityCompat;

import org.junit.Rule;
import org.junit.Test;

import java.io.File;

import static ch.epfl.sweng.SDP.game.drawing.DrawingOnlineTest.initializedBitmap;
import static ch.epfl.sweng.SDP.utils.ImageStorageManager.writeFileToStorage;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ImageStorageManagerTest {

    @Rule
    public GrantPermissionRule writeExternalStoragePermission =
            GrantPermissionRule.grant(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
    @Rule
    public GrantPermissionRule readExternalStoragePermission =
            GrantPermissionRule.grant(Manifest.permission.READ_EXTERNAL_STORAGE);

    @Test
    public void testIsPermissionsGranted() {
        boolean granted = ActivityCompat.checkSelfPermission(InstrumentationRegistry.getContext(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
        assertThat(granted, is(true));
    }

    @Test
    public void testGetFile() {
        String root = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM).toString() + "/Camera/Gyrodraw/";
        String fileName = root + "Image-testFile.png";

        File file = ImageStorageManager.getFile("testFile");
        writeFileToStorage(initializedBitmap(), file);
        assertThat(file.getPath(), is(equalTo(fileName)));
    }
}
