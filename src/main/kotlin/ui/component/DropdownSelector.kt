import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import ui.component.CenterText
import ui.theme.groupBackGroundColor
import utils.DoubleClickUtils

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DropdownSelector(
    options: Map<String, String>,
    selectedType: String,
    modifier: Modifier,
    onSelected: (String) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .clickable {
                    if (!DoubleClickUtils.isFastDoubleClick())
                        expanded = expanded.not()
                }
                .padding(vertical = 5.dp, horizontal = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CenterText(
                text = "${options[selectedType]}",
                modifier = Modifier.weight(1f).padding(vertical = 10.dp, horizontal = 5.dp)
            )

            Image(
                modifier = Modifier.size(16.dp).rotate(if (expanded) 180f else 0f),
                painter = painterResource("image/ic_expand.png"),
                contentDescription = "Dropdown"
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                if (!DoubleClickUtils.isFastDoubleClick())
                    expanded = false
            },
            properties = PopupProperties(usePlatformDefaultWidth = false),
            modifier = modifier.background(groupBackGroundColor)
        ) {
            options.forEach {
                DropdownMenuItem(
                    onClick = {
                        onSelected(it.key)
                        expanded = false
                    },
                ) {
                    CenterText(it.value, modifier = Modifier.fillMaxWidth(1f))
                }
            }
        }
    }
}
