/*
 * Copyright (C) 2017 Twilio, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#include <memory>

#include "android_video_capturer.h"
#include "webrtc/rtc_base/timeutils.h"
#include "logging.h"

AndroidVideoCapturer::AndroidVideoCapturer(
        const rtc::scoped_refptr<AndroidVideoCapturerDelegate>& delegate)
        : running_(false),
          delegate_(delegate) {
    thread_checker_.DetachFromThread();
    SetSupportedFormats(delegate_->GetSupportedFormats());
}

AndroidVideoCapturer::~AndroidVideoCapturer() {
    RTC_CHECK(!running_);
}

cricket::CaptureState AndroidVideoCapturer::Start(const cricket::VideoFormat& capture_format) {
    RTC_CHECK(thread_checker_.CalledOnValidThread());
    RTC_CHECK(!running_);
    const int fps = cricket::VideoFormat::IntervalToFps(capture_format.interval);
    VIDEO_ANDROID_LOG(twilio::video::LogModule::kPlatform,
                      twilio::video::LogLevel::kInfo,
                      "AndroidVideoCapturer::Start %dx%d@%d", capture_format.width,
                      capture_format.height, fps);

    running_ = true;
    delegate_->Start(capture_format, this);
    SetCaptureFormat(&capture_format);
    return cricket::CS_STARTING;
}

void AndroidVideoCapturer::Stop() {
    VIDEO_ANDROID_LOG(twilio::video::LogModule::kPlatform,
                      twilio::video::LogLevel::kInfo,
                      "AndroidVideoCapturer::Stop");
    RTC_CHECK(thread_checker_.CalledOnValidThread());
    RTC_CHECK(running_);
    running_ = false;
    SetCaptureFormat(NULL);

    delegate_->Stop();
    SetCaptureState(cricket::CS_STOPPED);
}

bool AndroidVideoCapturer::IsRunning() {
    RTC_CHECK(thread_checker_.CalledOnValidThread());
    return running_;
}

bool AndroidVideoCapturer::GetPreferredFourccs(std::vector<uint32_t>* fourccs) {
    RTC_CHECK(thread_checker_.CalledOnValidThread());
    fourccs->push_back(cricket::FOURCC_YV12);
    return true;
}

void AndroidVideoCapturer::OnCapturerStarted(bool success) {
    RTC_CHECK(thread_checker_.CalledOnValidThread());
    const cricket::CaptureState new_state =
            success ? cricket::CS_RUNNING : cricket::CS_FAILED;
    SetCaptureState(new_state);
}

void AndroidVideoCapturer::OnOutputFormatRequest(
        int width, int height, int fps) {
    RTC_CHECK(thread_checker_.CalledOnValidThread());
    cricket::VideoFormat format(width, height,
                                cricket::VideoFormat::FpsToInterval(fps), 0);
    video_adapter()->OnOutputFormatRequest(format);
}

bool AndroidVideoCapturer::GetBestCaptureFormat(
        const cricket::VideoFormat& desired,
        cricket::VideoFormat* best_format) {
    // Delegate this choice to VideoCapturer.startCapture().
    *best_format = desired;
    return true;
}
