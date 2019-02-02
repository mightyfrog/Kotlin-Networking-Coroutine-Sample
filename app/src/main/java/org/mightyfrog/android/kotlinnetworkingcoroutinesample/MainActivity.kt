package org.mightyfrog.android.kotlinnetworkingcoroutinesample

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.*
import org.json.JSONObject
import java.net.URL
import java.util.*
import kotlin.coroutines.CoroutineContext

/**
 * @author Shigehiro Soejima
 */
class MainActivity : AppCompatActivity(), CoroutineScope {
    private val job = Job()

    override val coroutineContext: CoroutineContext get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            getData()

//            getData2()
        }
    }

    override fun onDestroy() {
        job.cancel()

        super.onDestroy()
    }

    private fun getData() = launch {
        activityCircle.visibility = View.VISIBLE
        textView.text = parseAsync(loadAsync().await()).await()
        activityCircle.visibility = View.GONE
    }

    private fun loadAsync() = async(Dispatchers.IO) {
        val url = URL("https://www.reddit.com/.json")
        url.openStream().use { stream ->
            return@async Scanner(stream).useDelimiter("\\A").next()
        }
    }

    private fun parseAsync(json: String) = async {
        val jo = JSONObject(json).getJSONObject("data").getJSONArray("children")
        val len = jo.length()
        val sb = StringBuilder()
        for (i in 0 until len) {
            sb.append(jo.getJSONObject(i).getJSONObject("data").getString("title"))
                    .append(("\n"))
                    .append(("\n"))
        }
        return@async sb.toString()
    }

    //

    private fun getData2() = launch {
        activityCircle.visibility = View.VISIBLE
        textView.text = withContext(Dispatchers.IO) { parse2(load2()) }
        activityCircle.visibility = View.GONE
    }

    private fun load2(): String {
        val url = URL("https://www.reddit.com/.json")
        url.openStream().use { stream ->
            return Scanner(stream).useDelimiter("\\A").next()
        }
    }

    private fun parse2(json: String): String {
        val jo = JSONObject(json).getJSONObject("data").getJSONArray("children")
        val len = jo.length()
        val sb = StringBuilder()
        for (i in 0 until len) {
            sb.append(jo.getJSONObject(i).getJSONObject("data").getString("title"))
                    .append(("\n"))
                    .append(("\n"))
        }
        return sb.toString()
    }
}
