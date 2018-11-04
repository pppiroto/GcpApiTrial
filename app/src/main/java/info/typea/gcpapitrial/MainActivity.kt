package info.typea.gcpapitrial

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    companion object {
        const val TAG: String = "*** MainActivity ***";
    }

    // FirebaseAuth 1.FirebaseAuthインスタンスの宣言
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // FirebaseAuth 2.初期化
        mAuth = FirebaseAuth.getInstance()

        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        nav_view.getHeaderView(0).textCurrentUser.setOnClickListener { view ->
            startActivity(Intent(this, LoginActivity::class.java))
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onStart() {
        super.onStart()

        // FirebaseAuth 3.Activity初期化時にサインインチェック
        var currentUser = mAuth?.currentUser

        if (currentUser == null) {
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        updateUI(mAuth?.currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        Log.i(TAG,"uid=${currentUser?.uid},name=${currentUser?.displayName},email=${currentUser?.email}");

        // https://stackoverflow.com/questions/33540090/textview-from-navigationview-header-returning-null
        nav_view.getHeaderView(0).textCurrentUser.text = currentUser?.email?:getString(R.string.nav_header_subtitle)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {
                var intent = Intent(this, FunctionCheckActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
            R.id.nav_logout -> {
//                mAuth?.signOut()
//                updateUI(mAuth?.currentUser)

                // カテゴリー名（通知設定画面に表示される情報）
                val notificationCategoryName = "GCP Api Trial App"
                val notificationTitle = "TITLE"
                val notificationContent = "CONTENT"
                // システムに登録するChannelのID
                val channelId = "gcp_api_trial_channel_id"
                var builder: NotificationCompat.Builder = NotificationCompat.Builder(this, channelId)
                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    // https://qiita.com/naoi/items/367fc23e55292c50d459

                    // 通知の詳細情報（通知設定画面に表示される情報）
                    val notifyDescription = "この通知の詳細情報を設定します"
                    // Channelの取得と生成
                    if (notificationManager.getNotificationChannel(channelId) == null) {
                        val channel = NotificationChannel(channelId, notificationCategoryName, NotificationManager.IMPORTANCE_HIGH)
                        channel.apply {
                            description = notifyDescription
                        }
                        notificationManager.createNotificationChannel(channel)
                    }
                }

                var notification = builder.apply {
                    setSmallIcon(android.R.drawable.sym_def_app_icon)
                    setContentTitle(notificationTitle)
                    setContentText(notificationContent)
                }.build()

                // 第1引数は通知を識別
                // 後から更新したり、削除が可能
                notificationManager.notify(1, notification)

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
