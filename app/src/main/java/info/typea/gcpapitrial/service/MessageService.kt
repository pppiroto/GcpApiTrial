package info.typea.gcpapitrial.service

import android.app.NotificationManager
import android.content.Context
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * Firebase FCM (Firebase Cloud Messaging)
 *
 */
class MyFirebaseInstanceIdService : FirebaseInstanceIdService() {
    companion object {
        const val TAG = "InstanceIdService"
     }

    /**
     * アプリケーションの初期化時に、FCM SDKは、クライアントアプリケーションインスタンスの
     * 登録トークンを生成する
     * シングルデバイスもしくは、デバイスグループを対象にしたい場合、
     * このトークンにアクセスする必要がある
     *
     * onTokenRefresh コールバックは、新しいトークンが生成されたときにコールバックされる
     * getToken がコンテキストで呼ばれた時などに、登録されたトークンが有効か保証する
     *
     * トークンが生成されていない場合、FirebaseInstanceID.getToken()はnullを返す
     * トークン入手後、独自のapp サーバーに送信することができる。
     * https://firebase.google.com/docs/reference/android/com/google/firebase/iid/FirebaseInstanceId?utm_source=studio
     *
     */
    override fun onTokenRefresh() {
        // 更新されたインスタンスIDトークンの取得
        var refreshedToken = FirebaseInstanceId.getInstance().token
        Log.i(TAG, "Refreshed token: $refreshedToken")

        // サーバーから、アプリケーションへメッセージを送信したい
        // もしくはサブスクリプションを管理したい場合、
        // このインスタンスIDトークンを app サーバーへ送信する
        // sendRegistrationToServer(refreshedToken)
    }
}

/**
 * メッセージをバックグラウンドで受信し処理したい場合、FirebaseMessagingServiceを継承する
 * このサービスは、通知をフォアグラウンドアプリで受け取る必要がある
 * データ自体を受け取る、メッセージを送信するなど
 *
 */
class MyFirebaseMessagingService : FirebaseMessagingService(){
    companion object {
        const val TAG = "*** MessagingService"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        // TODO FCMメッセージをここで処理する
        Log.d(TAG, "From: ${remoteMessage?.from}")

        if (remoteMessage?.data?.size?:0 > 0) {
            Log.d(TAG, "Message data payload: ${remoteMessage?.data}")

            //if (時間がかかる処理) {
            //    scheduleJob()
            //} else {
            //    // 10秒以内の処理
            //    handleNow()
            //}
        }

        // メッセージに通知が含まれる場合
        if (remoteMessage?.notification != null) {
            Log.d(TAG, "Message Notification Body: ${remoteMessage?.notification?.body}")

            // https://developer.android.com/guide/topics/ui/notifiers/notifications?hl=ja
            // http://android.techblog.jp/archives/7976103.html
            var notificationManager = NotificationManagerCompat.from(this)
            var notification = NotificationCompat.Builder(this, "CancelID")
                    .setContentTitle("GcpApiTrial")
                    .setContentText(remoteMessage?.notification?.body)
                    .setSmallIcon(android.R.drawable.sym_def_app_icon)
                    .build()

            // 第1引数は通知を識別
            // 後から更新したり、削除が可能
            notificationManager.notify(99, notification)
        }

    }
}
