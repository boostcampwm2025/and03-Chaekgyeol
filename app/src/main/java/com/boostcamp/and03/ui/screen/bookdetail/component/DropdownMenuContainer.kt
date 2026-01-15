package com.boostcamp.and03.ui.screen.bookdetail.component

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.boostcamp.and03.ui.theme.And03Theme

@Composable
fun DropdownMenuContainer(
    trigger: @Composable (onClick: () -> Unit) -> Unit,
    menuContent: @Composable (closeMenu: () -> Unit) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        trigger { expanded = true }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = And03Theme.colors.surface
        ) {
            menuContent {
                expanded = false
            }
        }
    }
}