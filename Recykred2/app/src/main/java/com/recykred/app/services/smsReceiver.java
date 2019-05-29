package com.recykred.app.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class smsReceiver  extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("aca","intent = " +intent.getExtras());
        Bundle bundle = intent.getExtras();
        SmsMessage[] msg = null;
        String str = "";
        if(bundle!=null){
            Object[] pdus = (Object[]) bundle.get("pdus");
            msg = new SmsMessage[pdus.length];
            for (int i=0; i<msg.length; i++){
                msg[i] = SmsMessage.createFromPdu((byte[])pdus[i]);

                str += msg[i].getMessageBody().toString();
            }
            //---display the new SMS message---
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
            if(str.contains("RC")){
                String userId = str.substring(4,str.length());
                String pedidoId = str.substring(0,2);
                Log.d("aca","userID" + userId);
                Log.d("aca","pedidoID = " + pedidoId);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateandTime = sdf.format(new Date());

                AndroidNetworking.post("http://conectemonos.com/pedido/maracarasignado/{id_pedido}")
                        .addPathParameter("id_pedido",pedidoId)
                        .addBodyParameter("id_reciclador",userId)
                        .addBodyParameter("estado_id","3")
                        .addBodyParameter("fecha_actualizacion",currentDateandTime)
                        .build()
                        .getAsString(new StringRequestListener() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("aca","response = " + response);
                            }

                            @Override
                            public void onError(ANError anError) {
                                Log.d("aca","error = " + anError.getErrorBody());
                            }
                        });
            }

        }
    }
}
