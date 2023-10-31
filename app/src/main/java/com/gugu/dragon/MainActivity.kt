package com.gugu.dragon

import ChatScreen
import HistoryScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                BottomNavigationDemo()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationDemo() {
    // 创建一个 NavController
    val navController = rememberNavController()
    // 创建一个保存底部导航栏菜单项的列表
    val items = listOf(
        BottomNavItemEnum.Live, // 正在直播的菜单项
        BottomNavItemEnum.History // 历史消息的菜单项
    )
    // 使用 Scaffold 布局，包含一个 BottomNavigation 和一个 NavHost
    Scaffold(
        bottomBar = {
            // 底部导航栏，使用 items 中的菜单项
            BottomNavigation(
                backgroundColor = Color(0xffeaeaea)
            ) {
                // 遍历每个菜单项
                items.forEach { item ->
                    // 创建一个底部导航栏按钮，使用 item 中的路由、图标和标签
                    BottomNavigationItem(
                        icon = { Icon((item.icon), contentDescription = item.label) },
                        label = { Text(item.label) },
                        // 当前选中的菜单项与 NavController 的当前目的地匹配时，为 true
                        selected = navController.currentDestination?.route == item.route,
                        // 点击按钮时，导航到相应的目的地
                        onClick = {
                            navController.navigate(item.route) {
                                // 导航时，清空返回栈，避免重复的目的地
                                popUpTo(navController.graph.startDestinationId){
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        // 导航主机，使用 NavController 和起始目的地
        NavHost(navController, startDestination = BottomNavItemEnum.Live.route, Modifier.padding(innerPadding)) {
            // 为每个菜单项创建一个可组合的目的地，使用相应的路由和可组合函数
            composable(BottomNavItemEnum.Live.route) {
                ChatScreen()
            }
            composable(BottomNavItemEnum.History.route) {
                HistoryScreen()
            }
        }
    }
}

// 定义一个枚举类，表示底部导航栏的菜单项
enum class BottomNavItemEnum(
    val route: String,
     val icon: ImageVector,
    val label: String
) {
    Live("live", Icons.Default.Home, "直播"),
    History("history",  Icons.Default.Info, "历史")
}



// 定义一个可组合函数，表示历史消息的屏幕





data class BottomNavItem(val title: String, val route: String)

