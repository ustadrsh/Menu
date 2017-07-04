package hk.ust.aed.menu;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class Splash extends AppCompatActivity {
    private static boolean splashLoaded = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("There will be no title");
        if (!splashLoaded) {
            setContentView(R.layout.activity_splash);
            int secondsDelayed = 3;
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    startActivity(new Intent(Splash.this, MainActivity.class));
                    finish();
                }
            }, secondsDelayed * 1000);
            splashLoaded = true;
        }
        else {
            Intent goToMainActivity = new Intent(Splash.this, MainActivity.class);
            goToMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(goToMainActivity);
            finish();
        }
    }
}
