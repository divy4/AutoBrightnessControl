package com.danivyit.auto_brightnesscontrol.system;

import android.app.Activity;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;

import java.util.Observable;

public class NFCHandler extends Observable implements NfcAdapter.CreateNdefMessageCallback, NfcAdapter.OnNdefPushCompleteCallback {

    NfcAdapter adapter;
    String toSend;
    String received;

    public NFCHandler(Activity app) {
        adapter = NfcAdapter.getDefaultAdapter(app);
        if (adapter != null) {
            adapter.setNdefPushMessageCallback(this, app);
            adapter.setOnNdefPushCompleteCallback(this, app);
        }
    }

    /**
     * Sets the string that will be sent when another device is nearby.
     * @param message
     */
    public void setSendMessage(String message) {
        toSend = message;
    }

    /**
     * Returns the last message received from another device.
     * @return
     */
    public String getReceivedMessage() {
        return received;
    }

    /**
     * Called when creating a message.
     * @param nfcEvent
     * @return
     */
    @Override
    public NdefMessage createNdefMessage(NfcEvent nfcEvent) {
        byte[] mode = "text/plain".getBytes();
        byte[] empty = new byte[0];
        byte[] str = toSend.getBytes();
        NdefRecord record = new NdefRecord(NdefRecord.TNF_MIME_MEDIA, mode, empty, str);
        return new NdefMessage(record);
    }

    /**
     * Called when receiving a message.
     * @param nfcEvent
     */
    @Override
    public void onNdefPushComplete(NfcEvent nfcEvent) {
        received = nfcEvent.toString();
    }
}
