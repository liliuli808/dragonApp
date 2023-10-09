import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.gugu.dragon.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import coil.compose.rememberImagePainter
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

@Composable
fun ChatScreen() {

    val messages = remember { mutableStateListOf<ChatMessage>() }

    val chatData =  fetchDataFromApi()

    messages.addAll(chatData)


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(messages) { message ->
                ChatMessage(message)
            }
        }

    }
}

fun fetchDataFromApi(): Collection<ChatMessage> {
    val url = "https://gw.homeway.com.cn/zhibo/api/room/get_teacher_messages_sort?&roomId=11605&date=&sort=&_=1696843215944"

    val client = OkHttpClient()
    val request = Request.Builder()
        .url(url)
        .addHeader("Accept", "*/*")
        .addHeader("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8")
        .addHeader("Connection", "keep-alive")
        .addHeader("Cookie", "hx_bigdata_uid=20235261759431685095183803adcc0125decf83cab986760c833f4c5c; UM_distinctid=188577ff5ce928-096fc6ed0d5bc7-14462c6c-1fa400-188577ff5cfc50; hxck_cd_sourceteacher=OayAMtIi6OlL1NW9iprsnvhxxTcON95P3IdFdbSd%2BTM8xNwFrseaRMku053IoovdEYFA8ZT1PRTUQwwYCv4rVUTPkuvUeY%2FvQvJX51kYC6mEOw75xLnn0wkUz8tmo2edvKvy2ZlNF8yiz6TiDicscgZIbWRw%2BJgMnf8Sfjbn3qUAzp1gzXW8MzgVIDDvIT4r23MVJoSgbJI%3D; hxck_cd_channel=7%2FBhpcAVxCCLixmtPLSnFm%2BqGxlhvgA733PTPd9fLqwGd%2FBxmjFNlecsFgA5SVuZsIhWza%2FLxD38DlzkMWhKWq1OT1Mw9jXPqo76zR7A3baLgFdpNTiCu1g%2B8%2BuOzGqN6acIVV%2BPnpVV8V1CK0Y1ZBRaRFHJHjIB; kickId=f5153aa50d864521a6e8a4cf68c3b640; HexunTrack=SID=2023052617594414699b80f8132334caf9ba158043e1d8817&CITY=0&TOWN=0; hxck_sqkf_user_guid=3b4c64823d4a4942afc15c828971c108; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%2230831016%22%2C%22first_id%22%3A%22188577ff5b3c0c-0ca09f91dfd54c8-14462c6c-2073600-188577ff5b4ec5%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%2C%22%24latest_landing_page%22%3A%22https%3A%2F%2Fzhibo.homeway.com.cn%2F11605%2Fvips.html%22%7D%2C%22identities%22%3A%22eyIkaWRlbnRpdHlfbG9naW5faWQiOiIzMDgzMTAxNiIsIiRpZGVudGl0eV9jb29raWVfaWQiOiIxODg1NzdmZjViM2MwYy0wY2EwOWY5MWRmZDU0YzgtMTQ0NjJjNmMtMjA3MzYwMC0xODg1NzdmZjViNGVjNSJ9%22%2C%22history_login_id%22%3A%7B%22name%22%3A%22%24identity_login_id%22%2C%22value%22%3A%2230831016%22%7D%2C%22%24device_id%22%3A%22188577ff5b3c0c-0ca09f91dfd54c8-14462c6c-2073600-188577ff5b4ec5%22%7D; LoginStateCookie=; cn_1261777628_dplus=%7B%22distinct_id%22%3A%20%22188577ff5ce928-096fc6ed0d5bc7-14462c6c-1fa400-188577ff5cfc50%22%2C%22userFirstDate%22%3A%20%2220230526%22%2C%22userID%22%3A%20%2230831016%22%2C%22userName%22%3A%20%22wxp9pyrmby%22%2C%22userType%22%3A%20%22loginuser%22%2C%22%24_sessionid%22%3A%200%2C%22%24_sessionTime%22%3A%201690442462%2C%22%24dp%22%3A%200%2C%22%24_sessionPVTime%22%3A%201690442462%7D; hxck_cd_channel_order_mark1=4002000001_auto; bigdata_userid=30831016; userToken=30831016%7C0000%7C0%2CzYY7NWE84tQR74xElxPmotXt6esiFomKCWL%2Bdj0Ma9wFKeXPCjpUBaOHnmO7%2Fb6BZ0RTOKfkCgNQd053a7RfUs%2Bf0w1FWaaMxlBEVXMIzdgTVw9uaLzvCxD5hEHQEoYV1ue2cj%2FxwHyg32Xe5CfzeMndOfJ31XdCILy8Za2YUljossxUGAp59myBbhBeuEr%2BnnSzvU76BUwg3JIx1w537faFIBq1X4GJ; SnapCookie=o6D6d2VoJf2ioZcbN7DylaSIldXM%2F44nMLmQ228kEHzx37czBpe%2Fkc5mlFUTvXCBvhA2PZctpnIOhenUq0oiDtBLAdi82oYm%2B3NZ%2BXr5IoCy0NDFXNuN4A%3D%3D; hxck_sq_common=LoginStateCookie=&SnapCookie=o6D6d2VoJf2ioZcbN7DylaSIldXM%2F44nMLmQ228kEHzx37czBpe%2Fkc5mlFUTvXCBvhA2PZctpnIOhenUq0oiDtBLAdi82oYm%2B3NZ%2BXr5IoCy0NDFXNuN4A%3D%3D; JSESSIONID=260651704AE00FA20B539B9BDD450AEA")
        .addHeader("Referer", "https://zhibo.homeway.com.cn/11605/vips.html")
        .addHeader("Sec-Fetch-Dest", "script")
        .addHeader("Sec-Fetch-Mode", "no-cors")
        .addHeader("Sec-Fetch-Site", "same-site")
        .addHeader(
            "User-Agent",
            "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Safari/537.36"
        )
        .addHeader("sec-ch-ua", "\"Chromium\";v=\"116\", \"Not)A;Brand\";v=\"24\", \"Google Chrome\";v=\"116\"")
        .addHeader("sec-ch-ua-mobile", "?0")
        .addHeader("sec-ch-ua-platform", "\"Linux\"")
        .build()

    val
    try {
        val response: Response = client.newCall(request).execute()
        if (response.isSuccessful) {
            val responseBody = response.body?.string()
            // 处理响应数据 responseBody
        } else {
            // 处理请求失败
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

@Composable
fun ChatMessage(message: ChatMessage) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = "https://game.mhcdkey.com/image/ask/200222/wu1obehppmu.jpg"), // 替换为头像资源
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = message.sender, fontWeight = FontWeight.Bold)
            }
            Text(text = message.time, color = Color.Gray)
        }
        Spacer(modifier = Modifier.height(4.dp))

        // 根据消息类型渲染内容
        when (message.messageType) {
            MessageType.Text -> {
                Text(text = message.text)
            }
            MessageType.Image -> {
                // 显示图片
                Image(
                    painter = rememberAsyncImagePainter(message.imageResource),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
                Text(text = message.text)
            }
        }
    }
}

data class ChatMessage(
    val sender: String,
    val text: String,
    val originalText : String,
    val time: String,
    val messageType: MessageType = MessageType.Text,
    val imageResource: String = "",
    val imageResourceId: Int = 0
)

enum class MessageType {
    Text,
    Image
}