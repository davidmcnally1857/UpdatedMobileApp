package android.example.caproject;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splash extends AppCompatActivity {

    public AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        database = AppDatabase.getDatabase(getApplicationContext());

        if (database.userDAO().getAllUsers().isEmpty()) {

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(Splash.this, LoginActivity.class);
                    Splash.this.startActivity(intent);
                    Splash.this.finish();
                }
            }, 1000);
        }

        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    Intent intent = new Intent(Splash.this, MainActivity.class);
                    Splash.this.startActivity(intent);
                    Splash.this.finish();

                }
            },1000);

        }
    }
}