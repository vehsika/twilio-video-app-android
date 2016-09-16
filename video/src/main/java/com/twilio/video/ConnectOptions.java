package com.twilio.video;

/**
 * Represents options when connecting to a {@link Room}.
 */
public class ConnectOptions {
    final String roomName;
    final LocalMedia localMedia;
    final IceOptions iceOptions;

    private ConnectOptions(Builder builder) {
        this.roomName = builder.roomName;
        this.localMedia = builder.localMedia;
        this.iceOptions = builder.iceOptions;
    }

    private long createNativeObject() {
        return nativeCreate(roomName, localMedia, iceOptions);
    }

    private native long nativeCreate(String name,
                                     LocalMedia localMedia,
                                     IceOptions iceOptions);
    /**
     * Build new {@link ConnectOptions}.
     *
     * <p>All methods are optional</p>
     */
    public static class Builder {
        private String roomName = "";
        private LocalMedia localMedia;
        private IceOptions iceOptions;

        public Builder() { }

        /**
         * The name of the room being connected to
         */
        public Builder roomName(String roomName) {
            this.roomName = roomName;
            return this;
        }

        /**
         * Media that will be published upon connection
         */
        public Builder localMedia(LocalMedia localMedia) {
            this.localMedia = localMedia;
            return this;
        }

        /**
         * Ice options used when connecting
         */
        public Builder iceOptions(IceOptions iceOptions) {
            this.iceOptions = iceOptions;
            return this;
        }

        public ConnectOptions build() {
            return new ConnectOptions(this);
        }
    }
}