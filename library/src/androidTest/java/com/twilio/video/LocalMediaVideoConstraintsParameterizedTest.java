package com.twilio.video;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;

import com.twilio.video.util.FakeVideoCapturer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
@LargeTest
public class LocalMediaVideoConstraintsParameterizedTest {
    private static final VideoFormat cif30FpsVideoFormat =
            new VideoFormat(VideoDimensions.CIF_VIDEO_DIMENSIONS, 30, VideoPixelFormat.RGBA_8888);
    private static final VideoFormat vga30FpsVideoFormat =
            new VideoFormat(VideoDimensions.VGA_VIDEO_DIMENSIONS, 30, VideoPixelFormat.RGBA_8888);
    private static final VideoFormat hd720p30fpsVideoFormat =
            new VideoFormat(VideoDimensions.HD_720P_VIDEO_DIMENSIONS, 30,
                    VideoPixelFormat.RGBA_8888);

    private static final VideoFormat cif24FpsVideoFormat =
            new VideoFormat(VideoDimensions.CIF_VIDEO_DIMENSIONS, 24, VideoPixelFormat.RGBA_8888);
    private static final VideoFormat vga24FpsVideoFormat =
            new VideoFormat(VideoDimensions.VGA_VIDEO_DIMENSIONS, 24, VideoPixelFormat.RGBA_8888);
    private static final VideoFormat hd720p24fpsVideoFormat =
            new VideoFormat(VideoDimensions.HD_720P_VIDEO_DIMENSIONS, 24,
                    VideoPixelFormat.RGBA_8888);


    private static final List<VideoFormat> formats30Fps = Arrays.asList(cif30FpsVideoFormat,
            vga30FpsVideoFormat,
            hd720p30fpsVideoFormat
    );
    private static final List<VideoFormat> formats4by3Ratio30Fps = Arrays.asList(
            new VideoFormat(new VideoDimensions(320, 240), 30, VideoPixelFormat.RGBA_8888),
            vga30FpsVideoFormat,
            new VideoFormat(new VideoDimensions(1280, 960), 30, VideoPixelFormat.RGBA_8888)
    );
    private static final List<VideoFormat> formats24Fps = Arrays.asList(cif24FpsVideoFormat,
            vga24FpsVideoFormat,
            hd720p24fpsVideoFormat
    );
    private static final List<VideoFormat> formats24And30Fps = Arrays.asList(cif24FpsVideoFormat,
            vga24FpsVideoFormat,
            hd720p24fpsVideoFormat,
            cif30FpsVideoFormat,
            vga30FpsVideoFormat,
            hd720p30fpsVideoFormat
    );
    private static final List<VideoFormat> galaxyS5BackCameraSupportedFormats = Arrays.asList(
            new VideoFormat(new VideoDimensions(1920, 1080), 30, VideoPixelFormat.NV21),
            new VideoFormat(new VideoDimensions(1440, 1080), 30, VideoPixelFormat.NV21),
            new VideoFormat(new VideoDimensions(1280, 720), 30, VideoPixelFormat.NV21),
            new VideoFormat(new VideoDimensions(1056, 864), 30, VideoPixelFormat.NV21),
            new VideoFormat(new VideoDimensions(960, 720), 30, VideoPixelFormat.NV21),
            new VideoFormat(new VideoDimensions(800, 480), 30, VideoPixelFormat.NV21),
            new VideoFormat(new VideoDimensions(720, 480), 30, VideoPixelFormat.NV21),
            new VideoFormat(new VideoDimensions(640, 480), 30, VideoPixelFormat.NV21),
            new VideoFormat(new VideoDimensions(352, 288), 30, VideoPixelFormat.NV21),
            new VideoFormat(new VideoDimensions(176, 144), 30, VideoPixelFormat.NV21)
    );
    private static final VideoCapturer videoCapturer30Fps = new FakeVideoCapturer(formats30Fps);
    private static final VideoCapturer videoCapturer4by3Ratio30Fps =
            new FakeVideoCapturer(formats4by3Ratio30Fps);
    private static final VideoCapturer videoCapturer24Fps = new FakeVideoCapturer(formats24Fps);
    private static final VideoCapturer videoCapturer30And24Fps =
            new FakeVideoCapturer(formats24And30Fps);
    private static final VideoCapturer galaxyS5BackCameraCapturer =
            new FakeVideoCapturer(galaxyS5BackCameraSupportedFormats);

    private static final VideoConstraints vgaMax24FpsMaxConstraints = new VideoConstraints.Builder()
            .maxFps(24)
            .maxVideoDimensions(VideoDimensions.VGA_VIDEO_DIMENSIONS)
            .build();
    private static final VideoConstraints vgaMin30FpsMaxConstraints = new VideoConstraints.Builder()
            .minFps(30)
            .maxFps(30)
            .maxVideoDimensions(VideoDimensions.VGA_VIDEO_DIMENSIONS)
            .build();
    private static final VideoConstraints minWidth1920Constraints = new VideoConstraints.Builder()
            .minVideoDimensions(new VideoDimensions(1920, 480))
            .maxVideoDimensions(new VideoDimensions(1920, 480))
            .build();
    private static final VideoConstraints minHeight1920Constraints = new VideoConstraints.Builder()
            .minVideoDimensions(new VideoDimensions(640, 1920))
            .maxVideoDimensions(new VideoDimensions(640, 1920))
            .build();
    private static final VideoConstraints maxWidth1920Constraints = new VideoConstraints.Builder()
            .minVideoDimensions(VideoDimensions.CIF_VIDEO_DIMENSIONS)
            .maxVideoDimensions(new VideoDimensions(1920, 480))
            .build();
    private static final VideoConstraints maxHeight1920Constraints = new VideoConstraints.Builder()
            .minVideoDimensions(VideoDimensions.CIF_VIDEO_DIMENSIONS)
            .maxVideoDimensions(new VideoDimensions(640, 1920))
            .build();
    private static final VideoConstraints aspectRatio16by9Constraints =
            new VideoConstraints.Builder()
            .aspectRatio(new AspectRatio(16, 9))
            .maxFps(30)
            .maxVideoDimensions(VideoDimensions.HD_1080P_VIDEO_DIMENSIONS)
            .build();

    private static class TestParameters {
        private final VideoConstraints videoConstraints;
        private final VideoCapturer videoCapturer;
        private final VideoConstraints expectedVideoConstraints;

        TestParameters(@Nullable VideoConstraints videoConstraints,
                       VideoCapturer videoCapturer,
                       VideoConstraints expectedVideoConstraints) {
            this.videoConstraints = videoConstraints;
            this.videoCapturer = videoCapturer;
            this.expectedVideoConstraints = expectedVideoConstraints;
        }
    }

    @Parameterized.Parameters(name = "{0}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"No constraints with capturer that supports default constraints",
                        new TestParameters(null, videoCapturer30Fps,
                                LocalMedia.defaultVideoConstraints)},
                {"No constraints with capturer format near default constraints",
                        new TestParameters(null, videoCapturer24Fps, vgaMax24FpsMaxConstraints)},
                {"Capturer does not support minimum width constraint",
                        new TestParameters(minWidth1920Constraints, videoCapturer24Fps,
                                vgaMax24FpsMaxConstraints)},
                {"Capturer does not support max width constraint",
                        new TestParameters(maxWidth1920Constraints, videoCapturer24Fps,
                                vgaMax24FpsMaxConstraints)},
                {"Capturer does not support minimum height constraint",
                        new TestParameters(minHeight1920Constraints, videoCapturer24Fps,
                                vgaMax24FpsMaxConstraints)},
                {"Capturer does not support max height constraint",
                        new TestParameters(maxHeight1920Constraints, videoCapturer24Fps,
                                vgaMax24FpsMaxConstraints)},
                {"Capturer does not support minimum FPS constraint",
                        new TestParameters(vgaMin30FpsMaxConstraints, videoCapturer24Fps,
                                vgaMax24FpsMaxConstraints)},
                {"Capturer does not support max FPS constraint",
                        new TestParameters(vgaMax24FpsMaxConstraints, videoCapturer30Fps,
                                LocalMedia.defaultVideoConstraints)},
                {"Capturer does not support aspect ratio constraint",
                        new TestParameters(aspectRatio16by9Constraints, videoCapturer4by3Ratio30Fps,
                                LocalMedia.defaultVideoConstraints)},
                {"Capturer supports aspect ratio constraint",
                        new TestParameters(aspectRatio16by9Constraints, galaxyS5BackCameraCapturer,
                                aspectRatio16by9Constraints)},
                {"Capturer fully supports constraints",
                        new TestParameters(vgaMax24FpsMaxConstraints, videoCapturer30And24Fps,
                                vgaMax24FpsMaxConstraints)}});
    }

    private final VideoConstraints videoConstraints;
    private final VideoCapturer videoCapturer;
    private final VideoConstraints expectedVideoConstraints;
    private Context context;
    private LocalMedia localMedia;

    public LocalMediaVideoConstraintsParameterizedTest(String testScenario,
                                                       TestParameters testParameters) {
        this.videoConstraints = testParameters.videoConstraints;
        this.videoCapturer = testParameters.videoCapturer;
        this.expectedVideoConstraints = testParameters.expectedVideoConstraints;
    }

    @Before
    public void setup() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        localMedia = LocalMedia.create(context);
    }

    @After
    public void teardown() {
        localMedia.release();
    }

    @Test
    public void addVideoTrack_shouldResolveCapturerSupportedVideoConstraints() {
        LocalVideoTrack localVideoTrack = localMedia.addVideoTrack(true, videoCapturer,
                videoConstraints);

        assertEquals(expectedVideoConstraints, localVideoTrack.getVideoConstraints());
    }
}
