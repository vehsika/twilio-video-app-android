package com.twilio.conversations;

import android.content.Context;

import com.twilio.conversations.CameraCapturer.CameraSource;

/**
 * A factory for creating an instance of {@link CameraCapturer}
 */
public class CameraCapturerFactory {
    /**
     * Creates an instance of CameraCapturer
     *
     * @param source the camera source
     * @return CameraCapturer
     */
    public static CameraCapturer createCameraCapturer (
            Context context,
            CameraSource source,
            CapturerErrorListener listener) {
        return CameraCapturer.create(context, source, listener);
    }
}
