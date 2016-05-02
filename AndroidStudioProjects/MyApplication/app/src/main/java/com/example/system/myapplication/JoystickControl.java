package com.example.system.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.io.IOException;
import android.os.Handler;



/**
 * @authors Emanuel Mellblom, Tigistu Desta, John Sundling
 * This class handels the joystick control option
 */

public class JoystickControl extends AppCompatActivity {

    private JoystickView joystick;
    private TextView angleTextView;
    private TextView powerTextView;
    //private TextView directionTextView;
    boolean dialogShownFlag = false;
    TextView textView1, textView2, textView3, textView4, textView5;
    RelativeLayout layout_joystick;
    public static boolean obstacleAlertisShown;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_joystick);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (TextView) findViewById(R.id.textView4);
        textView5 = (TextView) findViewById(R.id.textView5);
        layout_joystick = (RelativeLayout) findViewById(R.id.layout_joystick);

        final JoyStickClass js = new JoyStickClass(getApplicationContext(), layout_joystick, R.drawable.lever);
        js.setStickSize(150, 150);
        js.setLayoutSize(750, 750);
        js.setLayoutAlpha(150);
        js.setStickAlpha(100);
        js.setOffset(90);
        js.setMinimumDistance(50);
        //MotionEvent hej;
       final BooleanC threadOpen = new BooleanC(false);

        layout_joystick.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View arg0, MotionEvent arg1) {
                js.drawStick(arg1);
                if(arg1.getAction() == MotionEvent.ACTION_UP) {
                    //threadOpen.setBool(true);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            String up = "c0|0";
                            try {
                                BtConnection.btOutputStream.write(up.getBytes());
                            } catch (IOException io) {
                                System.out.println(io);
                            } catch (NullPointerException nu) {
                                System.out.println(nu);
                            }
                        }
                    }, 250);
                }
                textView3.setText("Angle : " + String.valueOf(Math.round(js.getAngle())));
                textView4.setText("Distance : " + String.valueOf(Math.round(js.getDistance())));
                textView1.setText("X : " + String.valueOf(js.getX()));
                textView2.setText("Y : " + String.valueOf(js.getY()));

                if (arg1.getAction() == MotionEvent.ACTION_DOWN
                        || arg1.getAction() == MotionEvent.ACTION_MOVE) {
                    if(!threadOpen.getBool()) {
                        threadOpen.setBool(true);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("delayed");
                                threadOpen.setBool(false);
                                try {
                                    String send = "c" + Integer.toString(Math.round(js.getDistance() < 50 ?50:js.getDistance())%100) + "|" + Integer.toString(js.turnRight() ? Math.round(js.getAngle()) : -Math.round(js.getAngle()));
                                    BtConnection.btOutputStream.write(send.getBytes());
                                }catch (IOException io) {
                                    if (dialogShownFlag) {
                                    }else {
                                         dialogShownFlag = true;
                                         AlertBoxes.bluetoothAlert(JoystickControl.this);
                                    }
                                }catch (IllegalStateException il) {
                                    if (dialogShownFlag) {
                                    } else {
                                        dialogShownFlag = true;
                                        AlertBoxes.bluetoothAlert(JoystickControl.this);
                                    }
                                }catch (NullPointerException nu) {
                                    if (dialogShownFlag) {
                                    }else {
                                        dialogShownFlag = true;
                                        AlertBoxes.bluetoothAlert(JoystickControl.this);
                                    }
                                }
                            }
                        }, 250);
                    }
                    int direction = js.get8Direction();
                        if (direction == JoyStickClass.STICK_UP) {
                            textView5.setText("Direction : Up");
                        } else if (direction == JoyStickClass.STICK_UPRIGHT) {
                            textView5.setText("Direction : Up Right");
                        } else if (direction == JoyStickClass.STICK_RIGHT) {
                            textView5.setText("Direction : Right");
                        } else if (direction == JoyStickClass.STICK_DOWNRIGHT) {
                            textView5.setText("Direction : Down Right");
                        } else if (direction == JoyStickClass.STICK_DOWN) {
                            textView5.setText("Direction : Down");
                        } else if (direction == JoyStickClass.STICK_DOWNLEFT) {
                        textView5.setText("Direction : Down Left");
                        } else if (direction == JoyStickClass.STICK_LEFT) {
                            textView5.setText("Direction : Left");
                        } else if (direction == JoyStickClass.STICK_UPLEFT) {
                            textView5.setText("Direction : Up Left");
                        } else if (direction == JoyStickClass.STICK_NONE) {
                            textView5.setText("Direction : Center");
                        }
                }else if (arg1.getAction() == MotionEvent.ACTION_UP) {
                    textView3.setText("Angle :");
                    textView4.setText("Speed :");
                    textView5.setText("Direction :");
                }
                return true;
            }
        });
    }

    public void readSensorInput() {
        try {
            obstacleAlertisShown = false;
            //char in = (char)BtConnection.btInputStream.read();
            //if (in == '1') {
            if (BtConnection.btInputStream.read() == 49) {
                System.out.println("Obstacle detected");
                System.out.println("" + BtConnection.btInputStream.read());
                if(!obstacleAlertisShown){
                    AlertBoxes.obstacleDetected(this);
                    obstacleAlertisShown = true;
                }
            } else {
            }
        }catch (IOException io) {
            //System.out.println("Error reading input");
            AlertBoxes.bluetoothAlert(this);
        }catch (IllegalStateException il) {
            AlertBoxes.bluetoothAlert(this);
        }catch (NullPointerException nu) {
            AlertBoxes.bluetoothAlert(this);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.buttonmode) {
            Intent intent = new Intent(this, buttonControl.class);
            startActivity(intent);
            return true;
        }
        if(id == R.id.connectToBluetooth){
            BtConnection.setBluetoothData();
            BtConnection.blueTooth();
            return true;
        }
        if (id == R.id.action_video) {
            Intent intent = new Intent(this, VideoDisplay.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
