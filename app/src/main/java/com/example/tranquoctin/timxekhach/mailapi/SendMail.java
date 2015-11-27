package com.example.tranquoctin.timxekhach.mailapi;

import android.util.Log;

/**
 * Created by sapui on 11/20/2015.
 */
public class SendMail {

    public static void sendMail(final String subject,
                                    final String body) {
        try {
            final String email = "timxekhachapp@gmail.com";
            final String pass = "kutesosNO!";

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        GMailSender sender = new GMailSender(email, pass);
                        sender.sendMail(subject, body, email, email);

                    } catch (Exception e) {
                        Log.e("SendMail", e.getMessage(), e);
                    }
                }
            }).start();

        } catch (Exception e) {
            Log.e("sendResponse", e.getMessage());
        }
    }
}
