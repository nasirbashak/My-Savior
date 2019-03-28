package e.mysavior;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    StringBuffer pattern;
    int counter=0;
    int pattern2[];
    boolean count=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pattern = new StringBuffer();
        pattern2 = new int[3];

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP)){
            Toast.makeText(getApplicationContext(),"Volume up pressed",Toast.LENGTH_SHORT).show();
            if(count)
                pattern2[counter++]=1;
            if(counter>=3){
                count=false;
                if(pattern2[0]==1 && pattern2[1]==0 && pattern2[2]==1){
                    Toast.makeText(getApplicationContext(),"Triggered",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this,CurrentLocationActivity.class));

                }
            }

        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)){
            Toast.makeText(getApplicationContext(),"Volume down pressed",Toast.LENGTH_SHORT).show();

            if(count)
                pattern2[counter++]=0;
            if(counter>=3){
                count=false;
                if(pattern2[0]==1 && pattern2[1]==0 && pattern2[2]==1){
                    Toast.makeText(getApplicationContext(),"Triggered",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this,CurrentLocationActivity.class));
                }
            }


        }
        return true;
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        super.onBackPressed();
    }


}
