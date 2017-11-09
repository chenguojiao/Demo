package com.example.administrator.myapplication;

import android.content.*;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import com.example.administrator.myapplication.model.*;
import com.example.administrator.myapplication.socket.BackService;

import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static com.example.administrator.myapplication.socket.BackService.HOST;
import static com.example.administrator.myapplication.socket.BackService.PORT;


@ContentView(R.layout.activity_setting)
public class SettingActivity extends AppCompatActivity {
    @ViewInject(R.id.lv_show)
    ListView listView;
    @ViewInject(R.id.tv_room_name)
    TextView roomNameTv;
    @ViewInject(R.id.tv_total_current_show)
    TextView totalCurrentShowTv;
    @ViewInject(R.id.btn_modify)
    Button modifyBtn;
    View vPopupWindow;
    PopupWindow pw;
    private String sendstr = "";
    BufferedWriter writer = null;
    MyReceiver receiver = null;

//    SettingSpinerAdapter mAdapter;
//    SpinerPopWindow mSpinerPopWindow;
    List<String> mListType = new ArrayList<String>();  //类型列表
    String roomAddress;
    String totalCurrentShow;

    SettingTotalCurrentRequestEntity request1 = new SettingTotalCurrentRequestEntity();
    SettingTotalCurrentRequestEntity.DataEntity data1 = new SettingTotalCurrentRequestEntity.DataEntity();
    SettingTotalCurrentResponseEntity response1 = new SettingTotalCurrentResponseEntity();




    IntentFilter filter;
    Intent startIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);


          startIntent = new Intent(this, BackService.class);

            startService(startIntent);
            receiver = new MyReceiver();
           filter = new IntentFilter();
            filter.addAction("com.example.administrator.androidfloorhot.socket");
            registerReceiver(receiver, filter);



    }

    @Event(value = {R.id.btn_goto_home, R.id.btn_out, R.id.tv_total_current_show,  R.id.btn_many_setting,
           R.id.rb_high_power, R.id.rb_low_power,
            R.id.rb_room_priority, R.id.rb_two_way_priority})
    private void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_total_current_show:
                showPopupWindow3(getBaseContext(),
                        LayoutInflater.from(SettingActivity.this).inflate(R.layout.activity_setting, null));
                break;


        }
    }


    public void showPopupWindow3(Context context, View parent) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        vPopupWindow = inflater.inflate(R.layout.setting_total_current_popupwindow, null, false);
        pw = new PopupWindow(vPopupWindow, 216, 230, true);
        pw.setOutsideTouchable(true);
        pw.setTouchable(true);
        pw.setFocusable(true);
        pw.setBackgroundDrawable(new BitmapDrawable());
        final EditText totalCurrent = vPopupWindow.findViewById(R.id.et_total_current);

        //按钮及其处理事件
        Button btnConfirm = (Button) vPopupWindow.findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalCurrentShow = totalCurrent.getText().toString();
//                totalCurrentShowTv.setText(totalCurrent.getText().toString());
                data1.setA_2(totalCurrent.getText().toString());

                data1.setCmd("a_2");
                request1.setData(data1);

                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            Socket socket = null;
                            try {
                                socket = new Socket(HOST, PORT);

                                sendstr = new Gson().toJson(request1);
                                writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                                writer.write(sendstr);
                                writer.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }).start();

                pw.dismiss();//关闭
            }
        });
        //显示popupWindow对话框
        pw.showAtLocation(parent, Gravity.CENTER, -100, -40);
    }

    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String contant = bundle.getString("contant");
            String cmd = null;
            try {
                JSONObject jsonObject = new JSONObject(contant)
                        .getJSONObject("data");
                cmd = jsonObject.getString("cmd");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            switch (cmd) {

                case "a_2":
                    response1 = new Gson().fromJson(contant, SettingTotalCurrentResponseEntity.class);
                    if (response1.getData().getA_2().equals("1")) {
                        totalCurrentShowTv.setText(totalCurrentShow);
                        Toast.makeText(getBaseContext(), "入户总功率设置成功",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "入户总功率设置失败",
                                Toast.LENGTH_SHORT).show();

                    }
                    break;

            }


        }


    }
}
