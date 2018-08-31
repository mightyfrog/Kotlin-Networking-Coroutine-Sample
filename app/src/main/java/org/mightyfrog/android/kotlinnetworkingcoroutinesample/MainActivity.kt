package org.mightyfrog.android.kotlinnetworkingcoroutinesample

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.experimental.DefaultDispatcher
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import org.json.JSONObject
import java.net.URL
import java.util.*

/**
 * @author Shigehiro Soejima
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
//            launch(UI) {
//                activityCircle.visibility = View.VISIBLE
//                textView.text = parse(load().await()).await()
//                activityCircle.visibility = View.GONE
//            }

            launch(UI) {
                activityCircle.visibility = View.VISIBLE
                textView.text = withContext(DefaultDispatcher) { parse2(load2()) }
                activityCircle.visibility = View.GONE
            }
        }
    }

    private fun load() = async {
        val url = URL("https://www.reddit.com/.json")
        url.openStream().use { stream ->
            return@async Scanner(stream).useDelimiter("\\A").next()
        }
    }

    private fun parse(json: String) = async {
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
