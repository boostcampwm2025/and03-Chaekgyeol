package com.boostcamp.and03.ui.screen.bookdetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.boostcamp.and03.R
import com.boostcamp.and03.ui.component.IconBadge
import com.boostcamp.and03.ui.component.LabelChip
import com.boostcamp.and03.ui.theme.And03IconSize
import com.boostcamp.and03.ui.theme.And03Padding
import com.boostcamp.and03.ui.theme.And03Radius
import com.boostcamp.and03.ui.theme.And03Spacing
import com.boostcamp.and03.ui.theme.And03Theme

@Composable
fun CharacterCard(
    name: String,
    role: String,
    iconColor: Color,
    description: String,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    onClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
) {
    val borderColor = if (selected) {
        And03Theme.colors.primary
    } else {
        And03Theme.colors.outline
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = And03Theme.colors.surface,
                shape = RoundedCornerShape(And03Radius.RADIUS_S)
            )
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(And03Radius.RADIUS_S)
            )
            .clickable(onClick = onClick)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(And03Padding.PADDING_M)
        ) {
            Row(
                modifier = Modifier.padding(
                    start = And03Padding.PADDING_XL,
                ),
                verticalAlignment = Alignment.Top
            ) {
                // 프로필 아이콘
                IconBadge(
                    modifier = Modifier.padding(top = And03Padding.PADDING_XL),
                    iconResId = R.drawable.ic_person_filled,
                    iconColor = iconColor,
                    contentDescription = stringResource(
                        id = R.string.character_card_profile_icon_cd,
                        name
                    ),
                    size = And03IconSize.ICON_SIZE_L
                )

                Spacer(modifier = Modifier.width(And03Spacing.SPACE_S))

                // 이름 + 역할
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = And03Padding.PADDING_XL)
                ) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(And03Spacing.SPACE_XS))

                    LabelChip(
                        content = {
                            Text(
                                text = role,
                                style = MaterialTheme.typography.labelSmall,
                                color = And03Theme.colors.onSecondaryContainer
                            )
                        }
                    )
                }

                DropdownMenuContainer(
                    trigger = { onClick ->
                        IconButton(onClick = onClick) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_more_vert_filled),
                                contentDescription = stringResource(R.string.cd_more_options)
                            )
                        }
                    },
                    menuContent = { closeMenu ->
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.more_vert_edit)) },
                            onClick = {
                                closeMenu()
                                onEditClick()
                            }
                        )

                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.more_vert_delete)) },
                            onClick = {
                                closeMenu()
                                onDeleteClick()
                            }
                        )
                    }
                )
            }

            // 설명
            Text(
                modifier = Modifier.padding(
                    start = And03Padding.PADDING_XL,
                    bottom = And03Padding.PADDING_XL
                ),
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF616161)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CharacterCardPreview() {
    CharacterCard(
        name = "Character Name",
        role = "Character Role",
        iconColor = Color(0xFF1E88E5),
        description = "Character Description",
        onClick = {},
        onEditClick = {},
        onDeleteClick = {}
    )
}