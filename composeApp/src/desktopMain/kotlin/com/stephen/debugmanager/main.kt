import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import com.stephen.composeapp.generated.resources.*
import com.stephen.debugmanager.MainStateHolder
import com.stephen.debugmanager.data.ThemeState
import com.stephen.debugmanager.di.koinModules
import com.stephen.debugmanager.ui.ContentView
import com.stephen.debugmanager.ui.component.CenterText
import com.stephen.debugmanager.ui.component.CommonDialog
import com.stephen.debugmanager.ui.component.CustomTitleBar
import com.stephen.debugmanager.ui.pages.SplashScreen
import com.stephen.debugmanager.ui.theme.DarkColorScheme
import com.stephen.debugmanager.ui.theme.LightColorScheme
import com.stephen.debugmanager.ui.theme.backGroundColor
import com.stephen.debugmanager.ui.theme.groupBackGroundColor
import org.jetbrains.compose.resources.painterResource
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin

/**
 * 应用入口
 */
fun main() = application {

    startKoin {
        modules(koinModules)
    }

    val mainStateHolder by remember { mutableStateOf(GlobalContext.get().get<MainStateHolder>()) }

    val windowState = rememberWindowState(width = 1000.dp, height = 650.dp)

    val dialogState = remember { mutableStateOf(false) }

    /**
     * 先设置默认黑色，后期改为本地datastore记忆存储
     */
    val themeState = mainStateHolder.themeStateStateFlow.collectAsState()

    Window(
        onCloseRequest = {
            if (windowState.isMinimized)
                windowState.isMinimized = false
            dialogState.value = true
        },
        title = "DebugManager",
        undecorated = true,
        state = windowState,
        icon = painterResource(Res.drawable.app_logo),
    ) {
        MaterialTheme(
            colors = when (themeState.value) {
                ThemeState.DARK -> DarkColorScheme
                ThemeState.LIGHT -> LightColorScheme
                ThemeState.SYSTEM -> if (isSystemInDarkTheme()) DarkColorScheme else LightColorScheme
            }
        ) {
            SplashScreen {
                Column(modifier = Modifier.background(backGroundColor)) {
                    WindowDraggableArea {
                        CustomTitleBar(
                            title = "DebugManager by Stephen",
                            windowState = windowState,
                            onClose = {
                                dialogState.value = true
                            },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }

                    ContentView {
                        mainStateHolder.uninstallToolsApp()
                        exitApplication()
                    }

                    if (dialogState.value) {
                        CommonDialog(
                            title = "确认退出应用程序？",
                            onConfirm = {
                                mainStateHolder.uninstallToolsApp()
                                exitApplication()
                            },
                            onCancel = { dialogState.value = false },
                            onDismiss = { dialogState.value = false }
                        )
                    }
                }
            }
        }
    }
}