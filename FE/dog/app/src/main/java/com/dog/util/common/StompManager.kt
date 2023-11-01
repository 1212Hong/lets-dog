package com.dog.util.common

import android.util.Log
import android.widget.SimpleAdapter
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

class StompManager() {

    val TAG = "MainActivity"

    var mAdapter: SimpleAdapter? = null
    val mDataSet: List<String> = ArrayList()
    var mStompClient: StompClient? = null
    val mRestPingDisposable: Disposable? = null
    val mTimeFormat: SimpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    val mGson: Gson = GsonBuilder().create()
    var compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun initializeStompClient() {
        mStompClient = Stomp.over(
            Stomp.ConnectionProvider.OKHTTP, "ws://k9c205.p.ssafy.io:8000/ws-stomp"
        );
    }

    private fun disconnectStomp() {
        mStompClient!!.disconnect()
    }

    val LOGIN = "login"

    val PASSCODE = "passcode"

    fun connectStomp() {
        // Check if the StompClient is already connected
        if (mStompClient?.isConnected == true) {
            return
        }

        val headers: MutableList<StompHeader> = ArrayList()
        headers.add(StompHeader("Authorization", "test"))

        val dispLifecycle: Disposable? = mStompClient?.lifecycle()
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

        if (dispLifecycle != null) {
            compositeDisposable?.add(dispLifecycle)
        }

        // Receive greetings
        val dispTopic = mStompClient!!.topic("/sub/chatroom/1")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ topicMessage: StompMessage ->
                Log.d(TAG, "Received " + topicMessage.payload)
//                addItem(mGson.fromJson(topicMessage.payload, EchoModel::class.java))
            }
            ) { throwable: Throwable? ->
                Log.e(
                    TAG,
                    "Error on subscribe topic",
                    throwable
                )
            }

        compositeDisposable?.add(dispTopic)

        mStompClient?.connect(headers)
    }

    fun sendStomp(message: String) {
        Log.d("compositeDisposable", compositeDisposable.toString())
        if (mStompClient?.isConnected == true && compositeDisposable != null) {
            val jsonObject = JSONObject()
            jsonObject.put("room_id", "1");
            jsonObject.put("sender_id", "2");
            jsonObject.put("sender_name", "머홍");
            jsonObject.put("content_type", "글");
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

    fun applySchedulers(): CompletableTransformer? {
        return CompletableTransformer { upstream: Completable ->
            upstream
                .unsubscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

//    private fun addItem(echoModel: EchoModel) {
////        mDataSet.add(echoModel.getEcho() + " - " + mTimeFormat.format(Date()))
//        mAdapter!!.notifyDataSetChanged()
//        mRecyclerView!!.smoothScrollToPosition(mDataSet.size - 1)
//    }

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
