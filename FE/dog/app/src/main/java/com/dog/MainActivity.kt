package com.dog;

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SimpleAdapter
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.recyclerview.widget.RecyclerView
import com.dog.ui.navigation.BottomNavigationBar
import com.dog.ui.theme.DogTheme
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompHeader
import ua.naiksoftware.stomp.dto.StompMessage
import java.text.SimpleDateFormat
import java.util.Locale


class MainActivity : ComponentActivity() {

    private val TAG = "MainActivity"

    private var mAdapter: SimpleAdapter? = null
    private val mDataSet: List<String> = ArrayList()
    private var mStompClient: StompClient? = null
    private val mRestPingDisposable: Disposable? = null
    private val mTimeFormat: SimpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    private var mRecyclerView: RecyclerView? = null
    private val mGson: Gson = GsonBuilder().create()

    private var compositeDisposable: CompositeDisposable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DogTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    connectStomp()
                    BottomNavigationBar()
                }
            }
        }

        mStompClient = Stomp.over(
            Stomp.ConnectionProvider.OKHTTP, "ws://k9c205.p.ssafy.io:8000/ws-stomp"
        );


    }

    fun disconnectStomp(view: View?) {
        mStompClient!!.disconnect()
    }

    val LOGIN = "login"

    val PASSCODE = "passcode"

    @Composable
    fun connectStomp() {
        val headers: MutableList<StompHeader> = ArrayList()
        headers.add(StompHeader(LOGIN, "guest"))
        headers.add(StompHeader(PASSCODE, "guest"))
        val dispLifecycle: Disposable? = mStompClient?.lifecycle()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe { lifecycleEvent: LifecycleEvent ->
                when (lifecycleEvent.type) {
                    LifecycleEvent.Type.OPENED -> toast("Stomp connection opened")
                    LifecycleEvent.Type.ERROR -> {
                        Log.e(TAG, "Stomp connection error", lifecycleEvent.exception)
                        toast("Stomp connection error")
                    }

                    LifecycleEvent.Type.CLOSED -> {
                        toast("Stomp connection closed")
                        resetSubscriptions()
                    }

                    LifecycleEvent.Type.FAILED_SERVER_HEARTBEAT -> toast("Stomp failed server heartbeat")
                }
            }
        if (dispLifecycle != null) {
            compositeDisposable?.add(dispLifecycle)
        }

        // Receive greetings

        // Receive greetings
        val dispTopic = mStompClient!!.topic("/topic/greetings")
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

//    private fun addItem(echoModel: EchoModel) {
////        mDataSet.add(echoModel.getEcho() + " - " + mTimeFormat.format(Date()))
//        mAdapter!!.notifyDataSetChanged()
//        mRecyclerView!!.smoothScrollToPosition(mDataSet.size - 1)
//    }

    private fun toast(text: String) {
        Log.i(TAG, text)
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun resetSubscriptions() {
        compositeDisposable?.dispose()
        compositeDisposable = CompositeDisposable()
    }

    override fun onDestroy() {
        mStompClient!!.disconnect()
        mRestPingDisposable?.dispose()
        if (compositeDisposable != null) compositeDisposable!!.dispose()
        super.onDestroy()
    }
}

