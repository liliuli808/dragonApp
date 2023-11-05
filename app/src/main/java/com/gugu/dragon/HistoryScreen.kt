import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gugu.dragon.model.ChatMessageModel
import com.gugu.dragon.model.OriginalMessage
import com.gugu.dragon.model.Result
import com.gugu.dragon.utils.extractImageUrls
import com.gugu.dragon.utils.removeHtmlTags
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HistoryViewModel : ViewModel() {
    private val historyMessages = mutableListOf<ChatMessageModel>()

    fun fetchHistoryData(date: String, onComplete: (List<ChatMessageModel>) -> Unit) {
        viewModelScope.launch {
            try {
                val chatData = withContext(Dispatchers.IO) {
                    fetchHistoryDataFromApi(date)
                }
                historyMessages.addAll(chatData)
                onComplete(chatData) // 调用回调函数，传递历史消息数据
            } catch (e: Exception) {
                // 处理网络请求失败或异常情况
                e.printStackTrace()
                onComplete(emptyList()) // 在出现异常时传递空列表
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen() {
    val calendar = Calendar.getInstance()

    // set the initial date
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = calendar.timeInMillis)

    var showDatePicker by remember {
        mutableStateOf(true)
    }

    var isLoading by remember { mutableStateOf(true) }



    var selectedDate by remember {
        mutableLongStateOf(calendar.timeInMillis) // or use mutableStateOf(calendar.timeInMillis)
    }

    val formatter = SimpleDateFormat("yyyyMMdd", Locale.CHINA)


    val viewModel: HistoryViewModel = viewModel()

    var messages by remember { mutableStateOf(emptyList<ChatMessageModel>()) }


    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = {
                showDatePicker = false
            },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker = false
                    selectedDate = datePickerState.selectedDateMillis!!

                    viewModel.fetchHistoryData(formatter.format(selectedDate)) { newMessages ->
                        messages = newMessages // 更新消息列表
                        isLoading = false
                    }
                }) {
                    Text(text = "确认")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDatePicker = false
                }) {
                    Text(text = "取消")
                }
            }
        ) {
            DatePicker(
                state = datePickerState
            )
        }
    }




    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 创建一个按钮来显示日期选择器对话框
        Button(onClick = { showDatePicker = true } ,  modifier = Modifier.fillMaxWidth()) {
            Text("Select Date")
        }
        // 数据加载完成后，显示消息列表
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) {
            items(messages.reversed()) { message ->
                ChatMessage(message)
                HorizontalDivider(
                    // 添加线间隔
                    modifier = Modifier.padding(vertical = 8.dp),
                    thickness = 1.dp,
                    color = Color.LightGray
                )
            }
        }
    }
}
 fun fetchHistoryDataFromApi(date: String): List<ChatMessageModel> {
        val url =
            String.format("https://gw.homeway.com.cn/zhibo/api/room/get_teacher_messages_sort?&roomId=11605&date=%s&sort=&_=1696843215944", date)

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
            return emptyList()
        }

        val jsonConfig = Json { ignoreUnknownKeys = true }
        val result = jsonConfig.decodeFromString<Result>(responseBody)
        if (result.data.messages.isEmpty()) {
            result.data.messages = emptyList()
        }

        return result.data.messages.map { message ->
            var originalBody = ""
            if (message.originalMessage != "") {
                val originalMess =
                    jsonConfig.decodeFromString<OriginalMessage>(message.originalMessage)
                originalBody = originalMess.body
            }
            ChatMessageModel(
                message.uuid,
                message.nickName,
                removeHtmlTags(message.body),
                removeHtmlTags(originalBody),
                message.messageTime,
                extractImageUrls(message.body),
                extractImageUrls(originalBody)
            )
        }
}
