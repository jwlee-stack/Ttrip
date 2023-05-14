package org.sfy.ttrip.presentation.live

import android.media.AsyncPlayer
import android.media.AudioManager
import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
//import com.dds.App
//import com.dds.core.socket.SocketManager
import com.dds.skywebrtc.inter.ISkyEvent
//import com.dds.webrtc.R


/**
 * Created by dds on 2019/8/25.
 * android_shuai@163.com
 */
class VoipEvent() : ISkyEvent {
    private val ringPlayer: AsyncPlayer
//    private val callViewModel by fragment.viewModels<CallViewModel>()
    init {
        ringPlayer = AsyncPlayer(null)
    }

    override fun createRoom(room: String?, roomSize: Int) {
Log.d("CallTest", "createRoom")
//        SocketManager.getInstance().createRoom(room)
    }

    //    public void sendInvite(String room, List<String> userIds, boolean audioOnly) {
    override fun sendInvite(room: String, userIds: List<String>) {
//        SocketManager.getInstance().sendInvite(room, userIds, audioOnly);
//        SocketManager.getInstance().sendInvite(room, userIds)
    }

    override fun sendRefuse(room: String, inviteId: String, refuseType: Int) {
//        SocketManager.getInstance().sendRefuse(room, inviteId, refuseType)
    }

    //    @Override
    //    public void sendTransAudio(String toId) {
    //        SocketManager.getInstance().sendTransAudio(toId);
    //    }
    override fun sendDisConnect(room: String, toId: String, isCrashed: Boolean) {
//        SocketManager.getInstance().sendDisconnect(room, toId)
    }

    override fun sendCancel(mRoomId: String, toIds: List<String>) {
//        SocketManager.getInstance().sendCancel(mRoomId, toIds)
    }

    override fun sendJoin(room: String) {
//        SocketManager.getInstance().sendJoin(room)
    }

    override fun sendRingBack(targetId: String, room: String) {
//        SocketManager.getInstance().sendRingBack(targetId, room)
    }

    override fun sendLeave(room: String, userId: String) {
//        SocketManager.getInstance().sendLeave(room, userId)
    }

    override fun sendOffer(userId: String, sdp: String) {
//        SocketManager.getInstance().sendOffer(userId, sdp)
    }

    override fun sendAnswer(userId: String, sdp: String) {
//        SocketManager.getInstance().sendAnswer(userId, sdp)
    }

    override fun sendIceCandidate(userId: String, id: String, label: Int, candidate: String) {
//        SocketManager.getInstance().sendIceCandidate(userId, id, label, candidate)
    }

    override fun onRemoteRing() {}

    //==============================================================================
    override fun shouldStartRing(isComing: Boolean) {
//        if (isComing) {
//            val uri = Uri.parse(
//                "android.resource://" + App.getInstance()
//                    .getPackageName() + "/" + R.raw.incoming_call_ring
//            )
//            ringPlayer.play(App.getInstance(), uri, true, AudioManager.STREAM_RING)
//        } else {
//            val uri = Uri.parse(
//                "android.resource://" + App.getInstance().getPackageName() + "/" + R.raw.wr_ringback
//            )
//            ringPlayer.play(App.getInstance(), uri, true, AudioManager.STREAM_RING)
//        }
    }

    override fun shouldStopRing() {
        Log.d(TAG, "shouldStopRing begin")
        ringPlayer.stop()
    }

    companion object {
        private const val TAG = "VoipEvent"
    }
}