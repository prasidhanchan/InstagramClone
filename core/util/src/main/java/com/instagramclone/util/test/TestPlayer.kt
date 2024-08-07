package com.instagramclone.util.test

import android.media.AudioDeviceInfo
import android.os.Looper
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.TextureView
import androidx.media3.common.AudioAttributes
import androidx.media3.common.AuxEffectInfo
import androidx.media3.common.DeviceInfo
import androidx.media3.common.Effect
import androidx.media3.common.Format
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.common.PriorityTaskManager
import androidx.media3.common.Timeline
import androidx.media3.common.TrackSelectionParameters
import androidx.media3.common.Tracks
import androidx.media3.common.VideoSize
import androidx.media3.common.text.CueGroup
import androidx.media3.common.util.Clock
import androidx.media3.common.util.Size
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DecoderCounters
import androidx.media3.exoplayer.ExoPlaybackException
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.PlayerMessage
import androidx.media3.exoplayer.Renderer
import androidx.media3.exoplayer.SeekParameters
import androidx.media3.exoplayer.analytics.AnalyticsCollector
import androidx.media3.exoplayer.analytics.AnalyticsListener
import androidx.media3.exoplayer.image.ImageOutput
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ShuffleOrder
import androidx.media3.exoplayer.source.TrackGroupArray
import androidx.media3.exoplayer.trackselection.TrackSelectionArray
import androidx.media3.exoplayer.trackselection.TrackSelector
import androidx.media3.exoplayer.video.VideoFrameMetadataListener
import androidx.media3.exoplayer.video.spherical.CameraMotionListener

/**
 * Fake Exoplayer Class for Testing Previews
 */
@Suppress("Todo", "DEPRECATION")
@UnstableApi
class TestPlayer: ExoPlayer {
    override fun getApplicationLooper(): Looper {
       return Looper.getMainLooper()
    }

    override fun addListener(listener: Player.Listener) {}

    override fun removeListener(listener: Player.Listener) {
        TODO("Not yet implemented")
    }

    override fun setMediaItems(mediaItems: MutableList<MediaItem>) {
        TODO("Not yet implemented")
    }

    override fun setMediaItems(mediaItems: MutableList<MediaItem>, resetPosition: Boolean) {
        TODO("Not yet implemented")
    }

    override fun setMediaItems(
        mediaItems: MutableList<MediaItem>,
        startIndex: Int,
        startPositionMs: Long
    ) {
        TODO("Not yet implemented")
    }

    override fun setMediaItem(mediaItem: MediaItem) {
        TODO("Not yet implemented")
    }

    override fun setMediaItem(mediaItem: MediaItem, startPositionMs: Long) {
        TODO("Not yet implemented")
    }

    override fun setMediaItem(mediaItem: MediaItem, resetPosition: Boolean) {
        TODO("Not yet implemented")
    }

    override fun addMediaItem(mediaItem: MediaItem) {
        TODO("Not yet implemented")
    }

    override fun addMediaItem(index: Int, mediaItem: MediaItem) {
        TODO("Not yet implemented")
    }

    override fun addMediaItems(mediaItems: MutableList<MediaItem>) {
        TODO("Not yet implemented")
    }

    override fun addMediaItems(index: Int, mediaItems: MutableList<MediaItem>) {
        TODO("Not yet implemented")
    }

    override fun moveMediaItem(currentIndex: Int, newIndex: Int) {
        TODO("Not yet implemented")
    }

    override fun moveMediaItems(fromIndex: Int, toIndex: Int, newIndex: Int) {
        TODO("Not yet implemented")
    }

    override fun replaceMediaItem(index: Int, mediaItem: MediaItem) {
        TODO("Not yet implemented")
    }

    override fun replaceMediaItems(
        fromIndex: Int,
        toIndex: Int,
        mediaItems: MutableList<MediaItem>
    ) {
        TODO("Not yet implemented")
    }

    override fun removeMediaItem(index: Int) {
        TODO("Not yet implemented")
    }

    override fun removeMediaItems(fromIndex: Int, toIndex: Int) {
        TODO("Not yet implemented")
    }

    override fun clearMediaItems() {
        TODO("Not yet implemented")
    }

    override fun isCommandAvailable(command: Int): Boolean {
        return true
    }

    override fun canAdvertiseSession(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getAvailableCommands(): Player.Commands {
        TODO("Not yet implemented")
    }

    @Deprecated("Deprecated in Java", ReplaceWith("TODO(\"Not yet implemented\")"))
    override fun prepare(mediaSource: MediaSource) {
        TODO("Not yet implemented")
    }

    @Deprecated("Deprecated in Java", ReplaceWith("TODO(\"Not yet implemented\")"))
    override fun prepare(mediaSource: MediaSource, resetPosition: Boolean, resetState: Boolean) {
        TODO("Not yet implemented")
    }

    override fun prepare() {
        TODO("Not yet implemented")
    }

    override fun getPlaybackState(): Int {
        TODO("Not yet implemented")
    }

    override fun getPlaybackSuppressionReason(): Int {
        TODO("Not yet implemented")
    }

    override fun isPlaying(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getPlayerError(): ExoPlaybackException? {
        TODO("Not yet implemented")
    }

    override fun play() {}

    override fun pause() {}

    override fun setPlayWhenReady(playWhenReady: Boolean) {}

    override fun getPlayWhenReady(): Boolean {
        return true
    }

    override fun setRepeatMode(repeatMode: Int) {
        TODO("Not yet implemented")
    }

    override fun getRepeatMode(): Int {
        TODO("Not yet implemented")
    }

    override fun setShuffleModeEnabled(shuffleModeEnabled: Boolean) {
        TODO("Not yet implemented")
    }

    override fun getShuffleModeEnabled(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isLoading(): Boolean {
        TODO("Not yet implemented")
    }

    override fun seekToDefaultPosition() {
        TODO("Not yet implemented")
    }

    override fun seekToDefaultPosition(mediaItemIndex: Int) {
        TODO("Not yet implemented")
    }

    override fun seekTo(positionMs: Long) {
        TODO("Not yet implemented")
    }

    override fun seekTo(mediaItemIndex: Int, positionMs: Long) {
        TODO("Not yet implemented")
    }

    override fun getSeekBackIncrement(): Long {
        TODO("Not yet implemented")
    }

    override fun seekBack() {
        TODO("Not yet implemented")
    }

    override fun getSeekForwardIncrement(): Long {
        TODO("Not yet implemented")
    }

    override fun seekForward() {
        TODO("Not yet implemented")
    }

    @Deprecated("Deprecated in Java")
    override fun hasPrevious(): Boolean {
        TODO("Not yet implemented")
    }

    @Deprecated("Deprecated in Java")
    override fun hasPreviousWindow(): Boolean {
        TODO("Not yet implemented")
    }

    override fun hasPreviousMediaItem(): Boolean {
        TODO("Not yet implemented")
    }

    @Deprecated("Deprecated in Java", ReplaceWith("TODO(\"Not yet implemented\")"))
    override fun previous() {
        TODO("Not yet implemented")
    }

    @Deprecated("Deprecated in Java", ReplaceWith("TODO(\"Not yet implemented\")"))
    override fun seekToPreviousWindow() {
        TODO("Not yet implemented")
    }

    override fun seekToPreviousMediaItem() {
        TODO("Not yet implemented")
    }

    override fun getMaxSeekToPreviousPosition(): Long {
        TODO("Not yet implemented")
    }

    override fun seekToPrevious() {
        TODO("Not yet implemented")
    }

    @Deprecated("Deprecated in Java")
    override fun hasNext(): Boolean {
        TODO("Not yet implemented")
    }

    @Deprecated("Deprecated in Java")
    override fun hasNextWindow(): Boolean {
        TODO("Not yet implemented")
    }

    override fun hasNextMediaItem(): Boolean {
        TODO("Not yet implemented")
    }

    @Deprecated("Deprecated in Java", ReplaceWith("TODO(\"Not yet implemented\")"))
    override fun next() {
        TODO("Not yet implemented")
    }

    @Deprecated("Deprecated in Java", ReplaceWith("TODO(\"Not yet implemented\")"))
    override fun seekToNextWindow() {
        TODO("Not yet implemented")
    }

    override fun seekToNextMediaItem() {
        TODO("Not yet implemented")
    }

    override fun seekToNext() {
        TODO("Not yet implemented")
    }

    override fun setPlaybackParameters(playbackParameters: PlaybackParameters) {
        TODO("Not yet implemented")
    }

    override fun setPlaybackSpeed(speed: Float) {
        TODO("Not yet implemented")
    }

    override fun getPlaybackParameters(): PlaybackParameters {
        TODO("Not yet implemented")
    }

    override fun stop() {
        TODO("Not yet implemented")
    }

    override fun release() {
        TODO("Not yet implemented")
    }

    override fun getCurrentTracks(): Tracks {
        return Tracks.EMPTY
    }

    override fun getTrackSelectionParameters(): TrackSelectionParameters {
        TODO("Not yet implemented")
    }

    override fun setTrackSelectionParameters(parameters: TrackSelectionParameters) {
        TODO("Not yet implemented")
    }

    override fun getMediaMetadata(): MediaMetadata {
        TODO("Not yet implemented")
    }

    override fun getPlaylistMetadata(): MediaMetadata {
        TODO("Not yet implemented")
    }

    override fun setPlaylistMetadata(mediaMetadata: MediaMetadata) {
        TODO("Not yet implemented")
    }

    override fun getCurrentManifest(): Any? {
        TODO("Not yet implemented")
    }

    override fun getCurrentTimeline(): Timeline {
        TODO("Not yet implemented")
    }

    override fun getCurrentPeriodIndex(): Int {
        TODO("Not yet implemented")
    }

    @Deprecated("Deprecated in Java")
    override fun getCurrentWindowIndex(): Int {
        TODO("Not yet implemented")
    }

    override fun getCurrentMediaItemIndex(): Int {
        TODO("Not yet implemented")
    }

    @Deprecated("Deprecated in Java")
    override fun getNextWindowIndex(): Int {
        TODO("Not yet implemented")
    }

    override fun getNextMediaItemIndex(): Int {
        TODO("Not yet implemented")
    }

    @Deprecated("Deprecated in Java")
    override fun getPreviousWindowIndex(): Int {
        TODO("Not yet implemented")
    }

    override fun getPreviousMediaItemIndex(): Int {
        TODO("Not yet implemented")
    }

    override fun getCurrentMediaItem(): MediaItem? {
        return MediaItem.fromUri("")
    }

    override fun getMediaItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun getMediaItemAt(index: Int): MediaItem {
        TODO("Not yet implemented")
    }

    override fun getDuration(): Long {
        return 1000L
    }

    override fun getCurrentPosition(): Long {
       return 1000L
    }

    override fun getBufferedPosition(): Long {
        TODO("Not yet implemented")
    }

    override fun getBufferedPercentage(): Int {
        TODO("Not yet implemented")
    }

    override fun getTotalBufferedDuration(): Long {
        TODO("Not yet implemented")
    }

    @Deprecated("Deprecated in Java")
    override fun isCurrentWindowDynamic(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isCurrentMediaItemDynamic(): Boolean {
        TODO("Not yet implemented")
    }

    @Deprecated("Deprecated in Java")
    override fun isCurrentWindowLive(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isCurrentMediaItemLive(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getCurrentLiveOffset(): Long {
        TODO("Not yet implemented")
    }

    @Deprecated("Deprecated in Java")
    override fun isCurrentWindowSeekable(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isCurrentMediaItemSeekable(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isPlayingAd(): Boolean {
        return true
    }

    override fun getCurrentAdGroupIndex(): Int {
        TODO("Not yet implemented")
    }

    override fun getCurrentAdIndexInAdGroup(): Int {
        TODO("Not yet implemented")
    }

    override fun getContentDuration(): Long {
        TODO("Not yet implemented")
    }

    override fun getContentPosition(): Long {
        TODO("Not yet implemented")
    }

    override fun getContentBufferedPosition(): Long {
        TODO("Not yet implemented")
    }

    override fun getAudioAttributes(): AudioAttributes {
        TODO("Not yet implemented")
    }

    override fun setVolume(volume: Float) {
        TODO("Not yet implemented")
    }

    override fun getVolume(): Float {
        TODO("Not yet implemented")
    }

    override fun clearVideoSurface() {
        TODO("Not yet implemented")
    }

    override fun clearVideoSurface(surface: Surface?) {
        TODO("Not yet implemented")
    }

    override fun setVideoSurface(surface: Surface?) {
        TODO("Not yet implemented")
    }

    override fun setVideoSurfaceHolder(surfaceHolder: SurfaceHolder?) {
        TODO("Not yet implemented")
    }

    override fun clearVideoSurfaceHolder(surfaceHolder: SurfaceHolder?) {
        TODO("Not yet implemented")
    }

    override fun setVideoSurfaceView(surfaceView: SurfaceView?) {
        TODO("Not yet implemented")
    }

    override fun clearVideoSurfaceView(surfaceView: SurfaceView?) {
        TODO("Not yet implemented")
    }

    override fun setVideoTextureView(textureView: TextureView?) {
        TODO("Not yet implemented")
    }

    override fun clearVideoTextureView(textureView: TextureView?) {
        TODO("Not yet implemented")
    }

    override fun getVideoSize(): VideoSize {
        TODO("Not yet implemented")
    }

    override fun getSurfaceSize(): Size {
        TODO("Not yet implemented")
    }

    override fun getCurrentCues(): CueGroup {
        TODO("Not yet implemented")
    }

    override fun getDeviceInfo(): DeviceInfo {
        TODO("Not yet implemented")
    }

    override fun getDeviceVolume(): Int {
        TODO("Not yet implemented")
    }

    override fun isDeviceMuted(): Boolean {
       return false
    }

    @Deprecated("Deprecated in Java", ReplaceWith("TODO(\"Not yet implemented\")"))
    override fun setDeviceVolume(volume: Int) {
        TODO("Not yet implemented")
    }

    override fun setDeviceVolume(volume: Int, flags: Int) {
        TODO("Not yet implemented")
    }

    @Deprecated("Deprecated in Java", ReplaceWith("TODO(\"Not yet implemented\")"))
    override fun increaseDeviceVolume() {
        TODO("Not yet implemented")
    }

    override fun increaseDeviceVolume(flags: Int) {
        TODO("Not yet implemented")
    }

    @Deprecated("Deprecated in Java", ReplaceWith("TODO(\"Not yet implemented\")"))
    override fun decreaseDeviceVolume() {
        TODO("Not yet implemented")
    }

    override fun decreaseDeviceVolume(flags: Int) {
        TODO("Not yet implemented")
    }

    @Deprecated("Deprecated in Java", ReplaceWith("TODO(\"Not yet implemented\")"))
    override fun setDeviceMuted(muted: Boolean) {
        TODO("Not yet implemented")
    }

    override fun setDeviceMuted(muted: Boolean, flags: Int) {
        TODO("Not yet implemented")
    }

    override fun setAudioAttributes(audioAttributes: AudioAttributes, handleAudioFocus: Boolean) {
        TODO("Not yet implemented")
    }

    @Deprecated("Deprecated in Java")
    override fun getAudioComponent(): ExoPlayer.AudioComponent? {
        TODO("Not yet implemented")
    }

    @Deprecated("Deprecated in Java")
    override fun getVideoComponent(): ExoPlayer.VideoComponent? {
        TODO("Not yet implemented")
    }

    @Deprecated("Deprecated in Java")
    override fun getTextComponent(): ExoPlayer.TextComponent? {
        TODO("Not yet implemented")
    }

    @Deprecated("Deprecated in Java")
    override fun getDeviceComponent(): ExoPlayer.DeviceComponent? {
        TODO("Not yet implemented")
    }

    override fun addAudioOffloadListener(listener: ExoPlayer.AudioOffloadListener) {
        TODO("Not yet implemented")
    }

    override fun removeAudioOffloadListener(listener: ExoPlayer.AudioOffloadListener) {
        TODO("Not yet implemented")
    }

    override fun getAnalyticsCollector(): AnalyticsCollector {
        TODO("Not yet implemented")
    }

    override fun addAnalyticsListener(listener: AnalyticsListener) {
        TODO("Not yet implemented")
    }

    override fun removeAnalyticsListener(listener: AnalyticsListener) {
        TODO("Not yet implemented")
    }

    override fun getRendererCount(): Int {
        TODO("Not yet implemented")
    }

    override fun getRendererType(index: Int): Int {
        TODO("Not yet implemented")
    }

    override fun getRenderer(index: Int): Renderer {
        TODO("Not yet implemented")
    }

    override fun getTrackSelector(): TrackSelector? {
        TODO("Not yet implemented")
    }

    @Deprecated("Deprecated in Java")
    override fun getCurrentTrackGroups(): TrackGroupArray {
        TODO("Not yet implemented")
    }

    @Deprecated("Deprecated in Java")
    override fun getCurrentTrackSelections(): TrackSelectionArray {
        TODO("Not yet implemented")
    }

    override fun getPlaybackLooper(): Looper {
        TODO("Not yet implemented")
    }

    override fun getClock(): Clock {
        TODO("Not yet implemented")
    }

    override fun setMediaSources(mediaSources: MutableList<MediaSource>) {
        TODO("Not yet implemented")
    }

    override fun setMediaSources(mediaSources: MutableList<MediaSource>, resetPosition: Boolean) {
        TODO("Not yet implemented")
    }

    override fun setMediaSources(
        mediaSources: MutableList<MediaSource>,
        startMediaItemIndex: Int,
        startPositionMs: Long
    ) {
        TODO("Not yet implemented")
    }

    override fun setMediaSource(mediaSource: MediaSource) {
        TODO("Not yet implemented")
    }

    override fun setMediaSource(mediaSource: MediaSource, startPositionMs: Long) {
        TODO("Not yet implemented")
    }

    override fun setMediaSource(mediaSource: MediaSource, resetPosition: Boolean) {
        TODO("Not yet implemented")
    }

    override fun addMediaSource(mediaSource: MediaSource) {
        TODO("Not yet implemented")
    }

    override fun addMediaSource(index: Int, mediaSource: MediaSource) {
        TODO("Not yet implemented")
    }

    override fun addMediaSources(mediaSources: MutableList<MediaSource>) {
        TODO("Not yet implemented")
    }

    override fun addMediaSources(index: Int, mediaSources: MutableList<MediaSource>) {
        TODO("Not yet implemented")
    }

    override fun setShuffleOrder(shuffleOrder: ShuffleOrder) {
        TODO("Not yet implemented")
    }

    override fun setAudioSessionId(audioSessionId: Int) {
        TODO("Not yet implemented")
    }

    override fun getAudioSessionId(): Int {
        TODO("Not yet implemented")
    }

    override fun setAuxEffectInfo(auxEffectInfo: AuxEffectInfo) {
        TODO("Not yet implemented")
    }

    override fun clearAuxEffectInfo() {
        TODO("Not yet implemented")
    }

    override fun setPreferredAudioDevice(audioDeviceInfo: AudioDeviceInfo?) {
        TODO("Not yet implemented")
    }

    override fun setSkipSilenceEnabled(skipSilenceEnabled: Boolean) {
        TODO("Not yet implemented")
    }

    override fun getSkipSilenceEnabled(): Boolean {
        TODO("Not yet implemented")
    }

    override fun setVideoEffects(videoEffects: MutableList<Effect>) {
        TODO("Not yet implemented")
    }

    override fun setVideoScalingMode(videoScalingMode: Int) {
        TODO("Not yet implemented")
    }

    override fun getVideoScalingMode(): Int {
        TODO("Not yet implemented")
    }

    override fun setVideoChangeFrameRateStrategy(videoChangeFrameRateStrategy: Int) {
        TODO("Not yet implemented")
    }

    override fun getVideoChangeFrameRateStrategy(): Int {
        TODO("Not yet implemented")
    }

    override fun setVideoFrameMetadataListener(listener: VideoFrameMetadataListener) {
        TODO("Not yet implemented")
    }

    override fun clearVideoFrameMetadataListener(listener: VideoFrameMetadataListener) {
        TODO("Not yet implemented")
    }

    override fun setCameraMotionListener(listener: CameraMotionListener) {
        TODO("Not yet implemented")
    }

    override fun clearCameraMotionListener(listener: CameraMotionListener) {
        TODO("Not yet implemented")
    }

    override fun createMessage(target: PlayerMessage.Target): PlayerMessage {
        TODO("Not yet implemented")
    }

    override fun setSeekParameters(seekParameters: SeekParameters?) {
        TODO("Not yet implemented")
    }

    override fun getSeekParameters(): SeekParameters {
        TODO("Not yet implemented")
    }

    override fun setForegroundMode(foregroundMode: Boolean) {
        TODO("Not yet implemented")
    }

    override fun setPauseAtEndOfMediaItems(pauseAtEndOfMediaItems: Boolean) {
        TODO("Not yet implemented")
    }

    override fun getPauseAtEndOfMediaItems(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getAudioFormat(): Format? {
        TODO("Not yet implemented")
    }

    override fun getVideoFormat(): Format? {
        TODO("Not yet implemented")
    }

    override fun getAudioDecoderCounters(): DecoderCounters? {
        TODO("Not yet implemented")
    }

    override fun getVideoDecoderCounters(): DecoderCounters? {
        TODO("Not yet implemented")
    }

    override fun setHandleAudioBecomingNoisy(handleAudioBecomingNoisy: Boolean) {
        TODO("Not yet implemented")
    }

    override fun setWakeMode(wakeMode: Int) {
        TODO("Not yet implemented")
    }

    override fun setPriorityTaskManager(priorityTaskManager: PriorityTaskManager?) {
        TODO("Not yet implemented")
    }

    override fun isSleepingForOffload(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isTunnelingEnabled(): Boolean {
        TODO("Not yet implemented")
    }

    override fun setImageOutput(imageOutput: ImageOutput) {
        TODO("Not yet implemented")
    }
}