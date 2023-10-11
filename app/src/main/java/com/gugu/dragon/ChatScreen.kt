import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import com.gugu.dragon.model.ChatMessageModel
import com.gugu.dragon.model.OriginalMessage
import com.gugu.dragon.model.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ChatScreen() {

    val messages = remember { mutableStateListOf<ChatMessageModel>() }


    // 声明一个可变状态，用于跟踪加载状态
    var isLoading by remember { mutableStateOf(true) }

    // 使用 LaunchedEffect 启动一个协程来执行网络操作
    LaunchedEffect(Unit) {
        try {
            val chatData = withContext(Dispatchers.IO) {
                fetchDataFromApi()
            }
            // 更新消息列表
            messages.addAll(chatData)

            // 标记加载完成
            isLoading = false
        } catch (e: Exception) {
            // 处理网络请求失败或异常情况
            e.printStackTrace()
            isLoading = false
        }
    }




    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        if (isLoading) {
            // 如果数据还在加载中，显示加载指示器或其他 UI
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            )
        } else {
            // 数据加载完成后，显示消息列表
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            ) {
                items(messages) { message ->
                    ChatMessage(message)
                    Divider( // 添加线间隔
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = Color.LightGray,
                        thickness = 1.dp
                    )
                }
            }
        }
    }

}

fun fetchDataFromApi(): Collection<ChatMessageModel> {
    val url =
        "https://gw.homeway.com.cn/zhibo/api/room/get_teacher_messages_sort?&roomId=11605&date=&sort=&_=1696843215944"

    val client = OkHttpClient()
    val request = Request.Builder()
        .url(url)
        .addHeader("Accept", "*/*")
        .addHeader("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8")
        .addHeader("Connection", "keep-alive")
        .addHeader(
            "Cookie",
            "hx_bigdata_uid=20235261759431685095183803adcc0125decf83cab986760c833f4c5c; UM_distinctid=188577ff5ce928-096fc6ed0d5bc7-14462c6c-1fa400-188577ff5cfc50; hxck_cd_sourceteacher=OayAMtIi6OlL1NW9iprsnvhxxTcON95P3IdFdbSd%2BTM8xNwFrseaRMku053IoovdEYFA8ZT1PRTUQwwYCv4rVUTPkuvUeY%2FvQvJX51kYC6mEOw75xLnn0wkUz8tmo2edvKvy2ZlNF8yiz6TiDicscgZIbWRw%2BJgMnf8Sfjbn3qUAzp1gzXW8MzgVIDDvIT4r23MVJoSgbJI%3D; hxck_cd_channel=7%2FBhpcAVxCCLixmtPLSnFm%2BqGxlhvgA733PTPd9fLqwGd%2FBxmjFNlecsFgA5SVuZsIhWza%2FLxD38DlzkMWhKWq1OT1Mw9jXPqo76zR7A3baLgFdpNTiCu1g%2B8%2BuOzGqN6acIVV%2BPnpVV8V1CK0Y1ZBRaRFHJHjIB; kickId=f5153aa50d864521a6e8a4cf68c3b640; HexunTrack=SID=2023052617594414699b80f8132334caf9ba158043e1d8817&CITY=0&TOWN=0; hxck_sqkf_user_guid=3b4c64823d4a4942afc15c828971c108; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%2230831016%22%2C%22first_id%22%3A%22188577ff5b3c0c-0ca09f91dfd54c8-14462c6c-2073600-188577ff5b4ec5%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%2C%22%24latest_landing_page%22%3A%22https%3A%2F%2Fzhibo.homeway.com.cn%2F11605%2Fvips.html%22%7D%2C%22identities%22%3A%22eyIkaWRlbnRpdHlfbG9naW5faWQiOiIzMDgzMTAxNiIsIiRpZGVudGl0eV9jb29raWVfaWQiOiIxODg1NzdmZjViM2MwYy0wY2EwOWY5MWRmZDU0YzgtMTQ0NjJjNmMtMjA3MzYwMC0xODg1NzdmZjViNGVjNSJ9%22%2C%22history_login_id%22%3A%7B%22name%22%3A%22%24identity_login_id%22%2C%22value%22%3A%2230831016%22%7D%2C%22%24device_id%22%3A%22188577ff5b3c0c-0ca09f91dfd54c8-14462c6c-2073600-188577ff5b4ec5%22%7D; LoginStateCookie=; cn_1261777628_dplus=%7B%22distinct_id%22%3A%20%22188577ff5ce928-096fc6ed0d5bc7-14462c6c-1fa400-188577ff5cfc50%22%2C%22userFirstDate%22%3A%20%2220230526%22%2C%22userID%22%3A%20%2230831016%22%2C%22userName%22%3A%20%22wxp9pyrmby%22%2C%22userType%22%3A%20%22loginuser%22%2C%22%24_sessionid%22%3A%200%2C%22%24_sessionTime%22%3A%201690442462%2C%22%24dp%22%3A%200%2C%22%24_sessionPVTime%22%3A%201690442462%7D; hxck_cd_channel_order_mark1=4002000001_auto; bigdata_userid=30831016; userToken=30831016%7C0000%7C0%2CzYY7NWE84tQR74xElxPmotXt6esiFomKCWL%2Bdj0Ma9wFKeXPCjpUBaOHnmO7%2Fb6BZ0RTOKfkCgNQd053a7RfUs%2Bf0w1FWaaMxlBEVXMIzdgTVw9uaLzvCxD5hEHQEoYV1ue2cj%2FxwHyg32Xe5CfzeMndOfJ31XdCILy8Za2YUljossxUGAp59myBbhBeuEr%2BnnSzvU76BUwg3JIx1w537faFIBq1X4GJ; SnapCookie=o6D6d2VoJf2ioZcbN7DylaSIldXM%2F44nMLmQ228kEHzx37czBpe%2Fkc5mlFUTvXCBvhA2PZctpnIOhenUq0oiDtBLAdi82oYm%2B3NZ%2BXr5IoCy0NDFXNuN4A%3D%3D; hxck_sq_common=LoginStateCookie=&SnapCookie=o6D6d2VoJf2ioZcbN7DylaSIldXM%2F44nMLmQ228kEHzx37czBpe%2Fkc5mlFUTvXCBvhA2PZctpnIOhenUq0oiDtBLAdi82oYm%2B3NZ%2BXr5IoCy0NDFXNuN4A%3D%3D; JSESSIONID=260651704AE00FA20B539B9BDD450AEA"
        )
        .addHeader("Referer", "https://zhibo.homeway.com.cn/11605/vips.html")
        .addHeader("Sec-Fetch-Dest", "script")
        .addHeader("Sec-Fetch-Mode", "no-cors")
        .addHeader("Sec-Fetch-Site", "same-site")
        .addHeader(
            "User-Agent",
            "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Safari/537.36"
        )
        .addHeader(
            "sec-ch-ua",
            "\"Chromium\";v=\"116\", \"Not)A;Brand\";v=\"24\", \"Google Chrome\";v=\"116\""
        )
        .addHeader("sec-ch-ua-mobile", "?0")
        .addHeader("sec-ch-ua-platform", "\"Linux\"")
        .build()


    val response: Response = client.newCall(request).execute()

    val responseBody = response.body?.string()
    if (responseBody.isNullOrBlank()) {
        return emptyList();
    }

    val jsonConfig = Json { ignoreUnknownKeys = true }
    val result = jsonConfig.decodeFromString<Result>(responseBody)
    if (result.data.messages.isEmpty()) {
        result.data.messages = emptyList()
    }

    return result.data.messages.reversed().map { message ->
        var originalBody = ""
        if (message.originalMessage != "") {
            val originalMess = jsonConfig.decodeFromString<OriginalMessage>(message.originalMessage)
            originalBody = originalMess.body
        }
        ChatMessageModel(
            message.nickName,
            removeHtmlTags(message.body),
            removeHtmlTags(originalBody),
            message.messageTime,
            extractImageUrls(message.body),
            extractImageUrls(originalBody)
        )
    }

}

fun extractImageUrls(text: String): List<String> {
    var imageUrlRegex = Regex("\\[img](.*?)\\[/img]")
    var matchResults = imageUrlRegex.findAll(text)
    val imageUrls = mutableListOf<String>()

    for (matchResult in matchResults) {
        val imageUrl = matchResult.groupValues[1]
        imageUrls.add(imageUrl)
    }

    imageUrlRegex = Regex("\\[face](.*?)\\[/face]")
    matchResults = imageUrlRegex.findAll(text)
    for (matchResult in matchResults) {
        val imageUrl = matchResult.groupValues[1]
        imageUrls.add(imageUrl)
    }

    println(imageUrls)
    return imageUrls
}

fun removeHtmlTags(input: String): String {
    // 使用正则表达式匹配 HTML 标签
    var regex = Regex("<.*?>")
    var res = regex.replace(input, "")

    regex = """\[face\].*?\[/face\]""".toRegex()
    res = regex.replace(res, "")

    regex = """\[img\].*?\[/img\]""".toRegex()

    res.replace("&nbsp;", "")
    return regex.replace(res, "")
}


fun convertTimestampToString(timestamp: String): String {
    val inputFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA)
    val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA)

    try {
        val date = inputFormat.parse(timestamp) ?: return ""
        return outputFormat.format(date)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return ""
}

@Composable
fun ChatMessage(message: ChatMessageModel) {
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
            Text(text = convertTimestampToString(message.time), color = Color.Gray)
        }
        Spacer(modifier = Modifier.height(8.dp))


        if (message.originalText.isNotBlank()) {
            Text(text = "问：" + removeHtmlTags(message.originalText))
        }

        if (message.originalResource.isNotEmpty()) {
            for (imageUrl in message.originalResource) {
                Image(
                    painter = rememberAsyncImagePainter(model = imageUrl),
                    modifier = Modifier
                        .fillMaxSize()
                        .height(200.dp),
                    contentDescription = null,
                    contentScale = ContentScale.Fit
                )
            }
        }

        if (message.originalText != "") {
            Text(text = "答:" + removeHtmlTags(message.text))
        } else {
            Text(text = removeHtmlTags(message.text))
        }


        if (message.imageResource.isNotEmpty()) {
            for (imageUrl in message.imageResource) {
                Image(
                    painter = rememberAsyncImagePainter(model = imageUrl),
                    modifier = Modifier
                        .fillMaxSize()
                        .height(200.dp)
                        .clickable {
                            ShowImageDialog(imageUrl) {
                                // 点击对话框时关闭对话框
                            }
                        },
                    contentDescription = null,
                    contentScale = ContentScale.Fit
                )
            }
        }
    }

}

@Composable
fun ShowImageDialog(imageUrl: String, onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = imageUrl),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
    }
}