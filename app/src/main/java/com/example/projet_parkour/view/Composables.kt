package com.example.projet_parkour.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController
import com.example.projet_parkour.ui.theme.Pink40

@Composable
fun FloatingButtonAdd(modifier: Modifier, route : String, navController: NavController){
    FloatingActionButton(
        modifier = modifier,
        onClick = {
            navController.navigate(route)
        },
        containerColor = Pink40,
        contentColor = Color.White,
        elevation = FloatingActionButtonDefaults.elevation()
    ) {
        // adding icon for button.
        Icon(Icons.Filled.Add, "Ajouter")
    }
}

//credits : https://www.geeksforgeeks.org/drop-down-menu-in-android-using-jetpack-compose/
@Composable
fun DropMenu(list: List<String>, menuName: String, selectedText: MutableState<String>){
    // Declaring a boolean value to store
    // the expanded state of the Text Field
    var expanded = remember { mutableStateOf(false) }
    var textFieldSize = remember { mutableStateOf(Size.Zero)}

    val itemHeights = remember { mutableStateMapOf<Int, Int>() }
    val baseHeight = 330.dp
    val density = LocalDensity.current
    val maxHeight = remember(itemHeights.toMap()) {
        if (itemHeights.keys.toSet() != list.indices.toSet()) {
            // if we don't have all heights calculated yet, return default value
            return@remember baseHeight
        }
        val baseHeightInt = with(density) { baseHeight.toPx().toInt() }

        // top+bottom system padding
        var sum = with(density) { 8.dp.toPx().toInt() } * 2
        for ((i, itemSize) in itemHeights.toSortedMap()) {
            sum += itemSize
            if (sum >= baseHeightInt) {
                return@remember with(density) { (sum - itemSize / 2).toDp() }
            }
        }
        // all items fit into base height
        baseHeight
    }

    // Up Icon when expanded and down icon when collapsed
    val icon = if (expanded.value)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column(Modifier.padding(20.dp)) {
        // Create an Outlined Text Field
        // with icon and not expanded
        OutlinedTextField(
            readOnly = true,
            value = selectedText.value,
            onValueChange = { selectedText.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    // This value is used to assign to
                    // the DropDown the same width
                    textFieldSize.value = coordinates.size.toSize()
                },
            label = {Text(menuName)},
            trailingIcon = {
                Icon(icon,"contentDescription",
                    Modifier.clickable { expanded.value = !expanded.value })
            }
        )

        // Create a drop-down menu with list of cities,
        // when clicked, set the Text Field text as the city selected
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
            modifier = Modifier
                .width(with(LocalDensity.current){textFieldSize.value.width.toDp()})
                .requiredSizeIn(maxHeight = maxHeight)
        ) {
            list.forEachIndexed { index, label ->
                DropdownMenuItem(modifier = Modifier.onSizeChanged { itemHeights[index] = it.height },
                    onClick = {
                        selectedText.value = label
                        expanded.value = false
                    },
                    text = { Text(text = label) })
            }
        }
    }
}