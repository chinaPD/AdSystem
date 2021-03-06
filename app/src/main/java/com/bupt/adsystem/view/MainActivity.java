package com.bupt.adsystem.view;

import android.app.Activity;
import android.content.Context;
import android.hardware.usb.UsbManager;
import android.media.MediaCodec;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.internal.telephony.ITelephony;
import com.bupt.adsystem.Camera.CameraApp;
import com.bupt.adsystem.Camera.UVCCameraEnumerator;
import com.bupt.adsystem.R;
import com.bupt.adsystem.RemoteServer.ServerRequest;
import com.bupt.adsystem.Utils.AdImageCtrl;
import com.bupt.adsystem.Utils.AdSystemConfig;
import com.bupt.adsystem.Utils.AdVideoCtrl;
import com.bupt.adsystem.Utils.NewImageMgr;
import com.bupt.adsystem.Utils.NewVideoMgr;
import com.bupt.adsystem.downloadtask.DownloadManager;
import com.serenegiant.usb.USBMonitor;
import com.serenegiant.usb.UVCCamera;

import org.anyrtc.core.AnyRTMP;
import org.anyrtc.core.RTMPHosterHelper;
import org.anyrtc.core.RTMPHosterKit;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.UVCCameraVideoCapturer;
import org.webrtc.VideoRenderer;

import java.lang.reflect.Method;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    private static final boolean DEBUG = AdSystemConfig.DEBUG;

    private ImageSwitcher mImageSwitcher;
    private VideoView mVideoView;
    private TextureView mTextureView;
    private SurfaceViewRenderer mRTCSurfaceView;
    private VideoRenderer mRTCRenderer = null;
    private RTMPHosterKit mHosterKit = null;

    private Button button;
    private TextView mElevatorTextView;
    private MediaPlayer mediaPlayer;
    private TextView textView;
    private String resPath;
    private CameraApp mCameraApp;
    private TelephonyManager mTelephonyManager;
    private Context mContext;
    private ServerRequest mServerRequest;
    private AdImageCtrl mAdImageCtrl;
    private AdVideoCtrl mAdVideoCtrl;

    public static final int Elevator_Info = 1;

    private Handler mMainHandler = new Handler(){
        @Override
        public void dispatchMessage(Message msg) {
            if (msg.what == Elevator_Info) {
                String elevatorInfo = (String) msg.obj;
                mElevatorTextView.setText(elevatorInfo);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE );
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                            WindowManager.LayoutParams.FLAG_FULLSCREEN
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        this.setContentView(R.layout.activity_main);
        mContext = this;
        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        mImageSwitcher = (ImageSwitcher) findViewById(R.id.image_switcher);
        button = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.textView);
        mTextureView = (TextureView) findViewById(R.id.textureView);
        mVideoView = (VideoView) findViewById(R.id.surface_view);
        mRTCSurfaceView = (SurfaceViewRenderer) findViewById(R.id.webrtc_surface_view);
        mElevatorTextView = (TextView) findViewById(R.id.Sensor_TextView);
        mTextureView.setVisibility(View.INVISIBLE);
        mVideoView.setVisibility(View.INVISIBLE);
//        mVideoView.setZOrderOnTop(true);
        mRTCSurfaceView.setVisibility(View.VISIBLE);


//        DownloadManager.instance(getApplicationContext());

        /***************** for push rtmp video stream test ******************/

//        final String mRtmpUrl = "rtmp://10.0.0.2:1935/live/test";
        final String mRtmpUrl = "rtmp://192.168.1.101:1935/live/test";
//        String mRtmpUrl = "rtmp://aokai.lymatrix.com/aokai/test";

        final UVCCameraEnumerator mUVCCamera = UVCCameraEnumerator.instance(getApplicationContext(), null);
        mRTCSurfaceView.init(AnyRTMP.Inst().Egl().getEglBaseContext(), null);
        mRTCRenderer = new VideoRenderer(mRTCSurfaceView);
        mHosterKit = new RTMPHosterKit(this, mRTMPHosterHelper);


//        MediaRecorder
//        MediaCodec
//        Surface
        /***************** for push rtmp video stream test ******************/

//        final CameraApp uvcCamera = new CameraApp(getApplicationContext(), mTextureView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//        mHosterKit.SetVideoCapturer(mRTCRenderer.GetRenderPointer(), true);
                mHosterKit.setUVCCameraCapturer(mRTCRenderer.GetRenderPointer(), mUVCCamera);
                mHosterKit.StartRtmpStream(mRtmpUrl);

//                Time time = new Time("GMT+8");
//                time.set(System.currentTimeMillis() + 10000);
//                String alarmTime = String.format("%02d:%02d:%02d", time.hour, time.minute, time.second);
//                Log.d(TAG, alarmTime);
//                AlarmUtil.setImageChangeTimeBroadcast(mContext, alarmTime, true);
//                AlarmUtil.setVideoChangeTimeBroadcast(mContext, alarmTime, true);

//                String url = "http://117.158.178.198:8010/esmp-ly-o-websvr/ws/esmp?wsdl";
//                JSONObject jsonObject = new JSONObject();
//                Handler handler = new Handler();
//                try {
//                    jsonObject.put("deviceId", "10000000000000000001");
//                    Log.d(TAG, "Request Json Content: \n" +
//                            jsonObject.toString());
//
////                    MiscUtil.postRequestTextFile(url, jsonObject.toString(), handler);
////                    MiscUtil.getRequestTextFile(url+"="+jsonObject.toString(), handler);
////                    MiscUtil.requestJsonFromWebservice(url, jsonObject.toString(), handler);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
////                String urlGet = MiscUtil.generateHttpGetUrl(0, 1, 80, 0, 0, 1, -89);
////                MiscUtil.getRequestTextFile(urlGet, handler);
        /*        Class<TelephonyManager> c = TelephonyManager.class;
                try
                {
                    Method getITelephonyMethod = c.getDeclaredMethod("getITelephony", (Class[]) null);
                    getITelephonyMethod.setAccessible(true);
                    ITelephony iTelephony = null;
                    iTelephony = (ITelephony) getITelephonyMethod.invoke(mTelephonyManager, (Object[]) null);
//                   iTelephony.endCall();
//                    iTelephony.answerRingingCall();

                    iTelephony.dial("+8618811610769");
                }
                catch (Exception e)
                {
                    Log.e(TAG, "Fail to answer ring call.", e);
                }*/

//                uvcCamera.startPreview();
                if (DEBUG) Log.d(TAG, "Button Pressed!");
            }
        });

     /*   NewImageMgr.instance(mContext, mImageSwitcher);
        NewVideoMgr.instance(mContext, mVideoView);
        ServerRequest request = new ServerRequest(mContext, mMainHandler);
        request.setFloorTextView(textView);*/

//        mAdVideoCtrl = AdVideoCtrl.instance(mContext, mVideoView);
//        mAdImageCtrl = AdImageCtrl.instance(mContext, mImageSwitcher);
//        mServerRequest = new ServerRequest(this);
//        mCameraApp = new CameraApp(this, mTextureView);
//        AdImageCtrl.instance(this, mImageSwitcher);
//        String url = "http://192.168.1.101:8080/download/purge_piece.mp4";
//        String url2 = "http://192.168.1.101:8080/download/coherence_piece.mp4";
//        String filename = URLUtil.guessFileName(url, null, null);
//        String filename2 = URLUtil.guessFileName(url2, null, null);
//        String filepath = FileDirMgr.instance().getCameraStoragePath();
//        DownloadManager.instance(this).startDownload(url, filepath, filename,
//                new OnDownload() {
//                    @Override
//                    public void onDownloading(String url, int finished) {
//                        if (DEBUG) Log.d(TAG, "downloaded1:" + finished);
//                    }
//
//                    @Override
//                    public void onDownloadFinished(File downloadFile) {
//                        if (DEBUG) Log.d(TAG, downloadFile.getAbsolutePath());
//                    }
//                });
//        DownloadManager.instance(this).startDownload(url2, filepath, filename2,
//                new OnDownload() {
//                    @Override
//                    public void onDownloading(String url, int finished) {
//                        if (DEBUG) Log.d(TAG, "downloaded2:" + finished);
//                    }
//
//                    @Override
//                    public void onDownloadFinished(File downloadFile) {
//                        if (DEBUG) Log.d(TAG, downloadFile.getAbsolutePath());
//                    }
//                });
//        int callState = mTelephonyManager.getCallState();
//        CellLocation cellLocation = mTelephonyManager.getCellLocation();
//        cellLocation.requestLocationUpdate();
//        mAdVideoCtrl = AdVideoCtrl.instance();
//        mTextureView.setVisibility(View.INVISIBLE);
//        mVideoView.setVisibility(View.VISIBLE);
//        mAdVideoCtrl.setVideoView(mVideoView);
//        mAdVideoCtrl.startPlayView();

//        resPath = mAdVideoCtrl.getVideoByOrder();
//        mVideoView.setVideoPath(resPath);
//        mVideoView.setZOrderOnTop(true);
//        mVideoView.start();
//        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                mVideoView.start();
//            }
//        });

//        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                mVideoView.setVideoPath(mAdVideoCtrl.getVideoByOrder());
//                mVideoView.start();
//            }
//        });
//        SurfaceHolder surfaceHolder = adVideoView.getHolder();
//        surfaceHolder.setFixedSize(720, 480);
//        surfaceHolder.addCallback(this);
//        mUSBMonitor = new USBMonitor(this, mOnDeviceConnectListener);
//        mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
//        PendingIntent mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(mUSBMonitor.ACTION_USB_PERMISSION), 0);
//        HashMap<String, UsbDevice> usbDevcieList = mUsbManager.getDeviceList();
//        if(usbDevcieList.size() == 1){
//            Toast.makeText(this, "find a USB device!", Toast.LENGTH_LONG).show();
//            Set<String> keySet = usbDevcieList.keySet();
//            for (String key : keySet)
//            mUsbManager.requestPermission(usbDevcieList.get(key), mPermissionIntent);
//        } else {
//            Toast.makeText(this, "USB device Num is:" + usbDevcieList.size(), Toast.LENGTH_LONG).show();
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LifeCycleMgr.onResume();
//        mCameraApp.registerUsbMonitor();
//        mCameraApp.startPreview();
    }

    @Override
    protected void onPause() {
//        mCameraApp.unregisterUsbMonitor();
//        mCameraApp.startPreview();
        LifeCycleMgr.onStop();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
//        mCameraApp.destroy();
//        mServerRequest.httpDisconnect();
        super.onDestroy();
    }

    final RTMPHosterHelper mRTMPHosterHelper = new RTMPHosterHelper() {
        @Override
        public void OnRtmpStreamOK() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (DEBUG) Log.d(TAG, "RTMP 连接成功！");
                }
            });
        }

        @Override
        public void OnRtmpStreamReconnecting(final int times) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (DEBUG) Log.d(TAG, String.format("RTMP 重连中(%1$d秒)...", times));
                }
            });
        }

        @Override
        public void OnRtmpStreamStatus(final int delayMs, final int netBand) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (DEBUG) Log.d(TAG, String.format(getString(org.anyrtc.anyrtmp.R.string.str_rtmp_status), delayMs, netBand));
                }
            });
        }

        @Override
        public void OnRtmpStreamFailed(int code) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (DEBUG) Log.d(TAG, "RTMP 连接失败");
                }
            });
        }

        @Override
        public void OnRtmpStreamClosed() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (DEBUG) Log.d(TAG, "RTMP 关闭!");
                }
            });
        }
    };

}
