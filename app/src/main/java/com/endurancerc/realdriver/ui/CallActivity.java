package com.endurancerc.realdriver.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.endurancerc.realdriver.R;
import com.endurancerc.realdriver.databinding.ActivityCallBinding;
import com.endurancerc.realdriver.repository.MainRepository;
import com.endurancerc.realdriver.utils.DataModelType;
import com.google.firebase.database.FirebaseDatabase;
import android.widget.SeekBar;
import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import android.widget.TextView;

import android.content.Intent;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.EventBusException;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import com.endurancerc.realdriver.core.Consts;
import com.endurancerc.realdriver.core.events.DeviceAttachedEvent;
import com.endurancerc.realdriver.core.events.DeviceDetachedEvent;
import com.endurancerc.realdriver.core.events.LogMessageEvent;
import com.endurancerc.realdriver.core.events.PrepareDevicesListEvent;
import com.endurancerc.realdriver.core.events.SelectDeviceEvent;
import com.endurancerc.realdriver.core.events.ShowDevicesListEvent;
import com.endurancerc.realdriver.core.events.USBDataReceiveEvent;
import com.endurancerc.realdriver.core.events.USBDataSendEvent;
import com.endurancerc.realdriver.core.services.USBHIDService;


public class CallActivity extends AppCompatActivity implements MainRepository.Listener {

    private ActivityCallBinding views;
    private MainRepository mainRepository;
    private Boolean isCameraMuted = false;
    private Boolean isMicrophoneMuted = false;
    private SeekBar steeringBar;
    private DatabaseReference steeringBarRef;
    private TextView steeringProgress;
    private SeekBar throttleBar;
    private DatabaseReference throttleBarRef;
    private TextView throttleProgress;

    private SharedPreferences sharedPreferences;
    private Intent usbService;
    private String settingsDelimiter;
    private String receiveDataFormat;
    private String delimiter;

    private int sb1_val = 1;
    private int sb2_val = 1;
    private int sb3_val = 1;
    private int sb4_val = 1;
    private int sb5_val = 1;
    private int sb6_val = 1;
    private int sb7_val = 1;
    private int sb8_val = 1;

    byte[] arr = new byte[8];
    private Boolean connected = false;
    protected EventBus eventBus;

    private void prepareServices() {
        usbService = new Intent(this, USBHIDService.class);
        startService(usbService);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        views = ActivityCallBinding.inflate(getLayoutInflater());

        try {
            eventBus = EventBus.builder().logNoSubscriberMessages(false).sendNoSubscriberEvent(false).installDefaultEventBus();
        } catch (EventBusException e) {
            eventBus = EventBus.getDefault();
        }
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        setContentView(views.getRoot());
        init();
    }


    public void update() {

        if(connected) {
            sb1_val = steeringBar.getProgress();
            sb2_val = throttleBar.getProgress();

            arr[0] = (byte)sb1_val;
            arr[1] = (byte)sb2_val;
            arr[2] = (byte)sb3_val;
            arr[3] = (byte)sb4_val;
            arr[4] = (byte)sb5_val;
            arr[5] = (byte)sb6_val;
            arr[6] = (byte)sb7_val;
            arr[7] = (byte)sb8_val;

            eventBus.post(new USBDataSendEvent(arr));
        }
    }


    private void init(){
        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        steeringBarRef = database.getReference("steeringBarProgress");
        throttleBarRef = database.getReference("throttleBarProgress");

        // Initialize Views
        steeringBar = findViewById(R.id.steeringBar);
        throttleBar = findViewById(R.id.throttleBar);
        steeringProgress = findViewById(R.id.steeringText);
        throttleProgress = findViewById(R.id.throttleText);

        // Listen for seek bar changes from Firebase
        steeringBarRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int progress = dataSnapshot.getValue(Integer.class);
                    steeringBar.setProgress(progress);
                    steeringProgress.setText(Integer.toString(progress));

                    update();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });

        // Listen to local SeekBar changes and update Firebase
        steeringBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    steeringBarRef.setValue(progress); // Update Firebase
                    steeringProgress.setText(Integer.toString(progress));

                    update();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Not needed
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Not needed
            }
        });

        // Listen for seek bar changes from Firebase
        throttleBarRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int progress = dataSnapshot.getValue(Integer.class);
                    throttleBar.setProgress(progress);
                    throttleProgress.setText(Integer.toString(progress));

                    update();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });

        // Listen to local SeekBar changes and update Firebase
        throttleBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    throttleBarRef.setValue(progress); // Update Firebase
                    throttleProgress.setText(Integer.toString(progress));

                    update();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Not needed
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Not needed
            }
        });


        mainRepository = MainRepository.getInstance();
        views.callBtn.setOnClickListener(v->{
            //start a call request here
            mainRepository.sendCallRequest(views.targetUserNameEt.getText().toString(),()->{
                Toast.makeText(this, "couldnt find the target", Toast.LENGTH_SHORT).show();
            });

        });
        mainRepository.initLocalView(views.localView);
        mainRepository.initRemoteView(views.remoteView);
        mainRepository.listener = this;

        mainRepository.subscribeForLatestEvent(data->{
            if (data.getType()== DataModelType.StartCall){
                runOnUiThread(()->{
                    views.incomingNameTV.setText("Accept connection from: " + data.getSender());
                    views.incomingCallLayout.setVisibility(View.VISIBLE);
                    views.acceptButton.setOnClickListener(v->{
                        //star the call here
                        mainRepository.startCall(data.getSender());
                        views.incomingCallLayout.setVisibility(View.GONE);
                    });
                    views.rejectButton.setOnClickListener(v->{
                        views.incomingCallLayout.setVisibility(View.GONE);
                    });
                });
            }
        });

        views.switchCameraButton.setOnClickListener(v->{
            mainRepository.switchCamera();
        });

        views.micButton.setOnClickListener(v->{
            if (isMicrophoneMuted){
                views.micButton.setImageResource(R.drawable.ic_baseline_mic_off_24);
            }else {
                views.micButton.setImageResource(R.drawable.ic_baseline_mic_24);
            }
            mainRepository.toggleAudio(isMicrophoneMuted);
            isMicrophoneMuted=!isMicrophoneMuted;
        });

        views.videoButton.setOnClickListener(v->{
            if (isCameraMuted){
                views.videoButton.setImageResource(R.drawable.ic_baseline_videocam_off_24);
            }else {
                views.videoButton.setImageResource(R.drawable.ic_baseline_videocam_24);
            }
            mainRepository.toggleVideo(isCameraMuted);
            isCameraMuted=!isCameraMuted;
        });

        views.endCallButton.setOnClickListener(v->{
            mainRepository.endCall();
            finish();
        });


        views.testButton.setOnClickListener(v->{
            eventBus.post(new PrepareDevicesListEvent());
        });


        views.testButton.setOnClickListener(v->{
            eventBus.post(new PrepareDevicesListEvent());
        });
    }

    @Override
    public void webrtcConnected() {
        runOnUiThread(()->{
            views.incomingCallLayout.setVisibility(View.GONE);
            views.whoToCallLayout.setVisibility(View.GONE);
            views.callLayout.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public void webrtcClosed() {
        runOnUiThread(this::finish);
    }

    void showListOfDevices(CharSequence devicesName[]) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (devicesName.length == 0) {
            builder.setTitle(Consts.MESSAGE_CONNECT_YOUR_USB_HID_DEVICE);
        } else {
            builder.setTitle(Consts.MESSAGE_SELECT_YOUR_USB_HID_DEVICE);
        }

        builder.setItems(devicesName, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                eventBus.post(new SelectDeviceEvent(which));
            }
        });
        builder.setCancelable(true);
        builder.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(USBDataReceiveEvent event) {
//		mLog(event.getData() + " \nReceived " + event.getBytesCount() + " bytes", true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LogMessageEvent event) {
//		mLog(event.getData(), true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ShowDevicesListEvent event) {
        showListOfDevices(event.getCharSequenceArray());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DeviceAttachedEvent event) {
        connected = true;
        //textView9.setText("Servo Controller Connected");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DeviceDetachedEvent event) {
        connected = false;
        //textView9.setText("Servo Controller Not Connected");
    }

    @Override
    protected void onStart() {
        super.onStart();
        receiveDataFormat = sharedPreferences.getString(Consts.RECEIVE_DATA_FORMAT, Consts.TEXT);
        prepareServices();
        setDelimiter();
        eventBus.register(this);
    }

    @Override
    protected void onStop() {
        eventBus.unregister(this);
        super.onStop();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String action = intent.getAction();
        if (action == null) {
            return;
        }
        switch (action) {
            case Consts.WEB_SERVER_CLOSE_ACTION:
                break;
            case Consts.USB_HID_TERMINAL_CLOSE_ACTION:
                stopService(new Intent(this, USBHIDService.class));
                ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancel(Consts.USB_HID_TERMINAL_NOTIFICATION);
                finish();
                break;
            case Consts.SOCKET_SERVER_CLOSE_ACTION:
                sharedPreferences.edit().putBoolean("enable_socket_server", false).apply();
                break;
        }
    }

    private void setDelimiter() {
        settingsDelimiter = sharedPreferences.getString(Consts.DELIMITER, Consts.DELIMITER_NEW_LINE);
        if (settingsDelimiter != null) {
            if (settingsDelimiter.equals(Consts.DELIMITER_NONE)) {
                delimiter = "";
            } else if (settingsDelimiter.equals(Consts.DELIMITER_NEW_LINE)) {
                delimiter = Consts.NEW_LINE;
            } else if (settingsDelimiter.equals(Consts.DELIMITER_SPACE)) {
                delimiter = Consts.SPACE;
            }
        }
        usbService.setAction(Consts.RECEIVE_DATA_FORMAT);
        usbService.putExtra(Consts.RECEIVE_DATA_FORMAT, receiveDataFormat);
        usbService.putExtra(Consts.DELIMITER, delimiter);
        startService(usbService);
    }
}
