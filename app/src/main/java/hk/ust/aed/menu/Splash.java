package hk.ust.aed.menu;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Splash extends AppCompatActivity {
    private static boolean splashLoaded = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!splashLoaded) {
            setContentView(R.layout.splash);
            final Button proceedBtn = (Button) findViewById(R.id.proceedBtn);
            proceedBtn.setVisibility(View.INVISIBLE);
            int secondsDelayed = 3;
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    proceedBtn.setVisibility(View.VISIBLE);
                    proceedBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(Splash.this, Login.class));
                            finish();
                        }
                    });
                }
            }, secondsDelayed * 1000);
            splashLoaded = true;
        }
        else {
            Intent goToLogin = new Intent(Splash.this, Login.class);
            goToLogin.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(goToLogin);
            finish();
        }
    }
}
