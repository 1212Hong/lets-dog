package com.dog.util.common

import android.util.Log
import com.dog.data.model.chat.ChatState
import com.dog.data.viewmodel.chat.ChatViewModel
import com.dog.data.viewmodel.user.UserViewModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.reactivex.Completable
import io.reactivex.CompletableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompHeader
import ua.naiksoftware.stomp.dto.StompMessage
import java.text.SimpleDateFormat
import java.util.Locale

class StompManager(chatViewModel: ChatViewModel, userViewModel: UserViewModel) {

    private val TAG = "StompManager"

    private var mStompClient: StompClient? = null
    private val mRestPingDisposable: Disposable? = null
    private val mTimeFormat: SimpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    private val mGson: Gson = GsonBuilder().create()
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    private val chatViewModel = chatViewModel
    private val userViewModel = userViewModel

    fun initializeStompClient() {
        mStompClient = Stomp.over(
            Stomp.ConnectionProvider.OKHTTP, "ws://k9c205.p.ssafy.io:8000/ws-stomp"
        );
    }

    private fun disconnectStomp() {
        mStompClient!!.disconnect()
    }

    fun connectStomp(roomId: Long) {
        // Check if the StompClient is already connected
        if (mStompClient?.isConnected == true) {
            return
        }

        val headers: MutableList<StompHeader> = ArrayList()
        headers.add(StompHeader("Authorization", "Bearer ${userViewModel.jwtToken.value}"))
        headers.add(StompHeader("Chatroom-no", roomId.toString()))

        val disposableLifecycle: Disposable? = mStompClient?.lifecycle()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe { lifecycleEvent: LifecycleEvent ->
                when (lifecycleEvent.type) {
                    LifecycleEvent.Type.OPENED -> Log.d(TAG, "stomp open")
//                        toast("Stomp connection opened")
                    LifecycleEvent.Type.ERROR -> {
                        Log.e(TAG, "Stomp connection error", lifecycleEvent.exception)
//                        toast("Stomp connection error")
                    }

                    LifecycleEvent.Type.CLOSED -> {
//                        toast("Stomp connection closed")
                        resetSubscriptions()
                    }

                    LifecycleEvent.Type.FAILED_SERVER_HEARTBEAT -> Log.d(
                        TAG,
                        "Stomp failed server heartbeat"
                    )
//                        toast("Stomp failed server heartbeat")
                }
            }

        if (disposableLifecycle != null) {
            compositeDisposable?.add(disposableLifecycle)
        }

        // Receive greetings
        val disposableTopic = mStompClient!!.topic("/sub/chatroom/$roomId")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ topicMessage: StompMessage ->
                Log.d(TAG, "Received " + topicMessage.payload)
                val newChatLog = mGson.fromJson(
                    topicMessage.payload,
                    ChatState::class.java
                )
                chatViewModel.sendMessage(newChatLog)
            }
            ) { throwable: Throwable? ->
                Log.e(
                    TAG,
                    "Error on subscribe topic",
                    throwable
                )
            }

        val disposableNoticeTopic = mStompClient!!.topic("/sub/notice/$roomId")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ topicMessage: StompMessage ->
                Log.d(TAG, "Received " + topicMessage.payload)
                chatViewModel.updateReadList(
                    topicMessage.payload
                )
            }
            ) { throwable: Throwable? ->
                Log.e(
                    TAG,
                    "Error on subscribe topic",
                    throwable
                )
            }

        compositeDisposable?.add(disposableTopic)
        compositeDisposable?.add(disposableNoticeTopic)

        mStompClient?.connect(headers)
    }

    fun sendStomp(roomId: Long, nickName: String, message: String) {
        Log.d("compositeDisposable", compositeDisposable.toString())
        if (mStompClient?.isConnected == true && compositeDisposable != null) {
            val jsonObject = JSONObject()
            jsonObject.put("roomId", roomId);
            jsonObject.put("senderName", nickName);
            jsonObject.put("contentType", "글");
            jsonObject.put("content", message);
            Log.d("jsonTest", jsonObject.toString())


            compositeDisposable!!.add(
                mStompClient!!.send(
                    "/pub/message",
                    jsonObject.toString()
                )
                    .compose(applySchedulers())
                    .subscribe(
                        { Log.d(TAG, "STOMP echo send successfully") }
                    ) { throwable: Throwable ->
                        Log.e(TAG, "Error send STOMP echo", throwable)
//                        toast(throwable.message!!)
                    })
        }
    }

    private fun applySchedulers(): CompletableTransformer? {
        return CompletableTransformer { upstream: Completable ->
            upstream
                .unsubscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

//    private fun toast(text: String) {
//        Log.i(TAG, text)
//        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
//    }

    private fun resetSubscriptions() {
        compositeDisposable?.dispose()
        compositeDisposable = CompositeDisposable()
    }

    fun onDestroy() {
        disconnectStomp()
        mStompClient!!.disconnect()
        mRestPingDisposable?.dispose()
        if (compositeDisposable != null) compositeDisposable!!.dispose()
//        super.onDestroy()
    }

}
