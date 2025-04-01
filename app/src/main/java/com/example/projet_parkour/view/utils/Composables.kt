package com.example.projet_parkour.view.utils

import android.content.Context
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
import android.icu.util.Calendar
import android.widget.DatePicker
import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import java.util.Date

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

@Composable
fun CalendarComposable(context: Context, mDate: MutableState<String>){
    // Declaring integer values
    // for year, month and day
    val mYear: Int
    val mMonth: Int
    val mDay: Int

    // Initializing a Calendar
    val mCalendar = Calendar.getInstance()

    // Fetching current year, month and day
    mYear = mCalendar.get(Calendar.YEAR)
    mMonth = mCalendar.get(Calendar.MONTH)
    mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    mCalendar.time = Date()

    // Declaring a string value to
    // store date in string format

    // Declaring DatePickerDialog and setting
    // initial values as current values (present year, month and day)
    val mDatePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            mDate.value = "$mYear-${mMonth + 1}-$mDayOfMonth"
        }, mYear, mMonth, mDay
    )
    Button(onClick = {
        mDatePickerDialog.show()
    }, colors = ButtonDefaults.buttonColors(containerColor = Color(0XFF0F9D58)) ) {
        Text(text = "Sélectionner une date", color = Color.White)
    }
}

//credits : https://www.geeksforgeeks.org/drop-down-menu-in-android-using-jetpack-compose/
@Composable
fun DropdownMenuComposable(
    list: List<String>,
    menuName: String,
    selectedText: MutableState<String>
){
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