package com.boostcamp.and03.ui.screen.bookdetail.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.boostcamp.and03.R
import com.boostcamp.and03.ui.component.LabelChip
import com.boostcamp.and03.ui.screen.bookdetail.model.MemoType
import com.boostcamp.and03.ui.theme.And03BorderWidth
import com.boostcamp.and03.ui.theme.And03Padding
import com.boostcamp.and03.ui.theme.And03Radius
import com.boostcamp.and03.ui.theme.And03Theme

@Composable
fun MemoCard(
    type: MemoType,
    title: String,
    contentPreview: String,
    pageLabel: String,
    date: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = And03BorderWidth.BORDER_1,
                color = And03Theme.colors.outline,
                shape = RoundedCornerShape(And03Radius.RADIUS_S)
            )
            .clip(RoundedCornerShape(And03Radius.RADIUS_S))
            .clickable(
                onClick = onClick
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Row(
                modifier = Modifier.padding(And03Padding.PADDING_2XL)
            ) {
                MemoTypeChip(type = type)

                Spacer(modifier = Modifier.width(And03Padding.PADDING_S))

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
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
                        }
                    )

                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.more_vert_delete)) },
                        onClick = {
                            closeMenu()
                        }
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(And03Padding.PADDING_S))

        /** 본문 영역 */
        when (type) {
            MemoType.CANVAS -> {}

            MemoType.TEXT -> {
                Text(
                    modifier = Modifier
                        .padding(horizontal = And03Padding.PADDING_2XL),
                    text = contentPreview,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Spacer(modifier = Modifier.height(And03Padding.PADDING_S))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = And03Padding.PADDING_2XL,
                    end = And03Padding.PADDING_2XL,
                    bottom = And03Padding.PADDING_2XL
                ),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LabelChip(
                content = {
                    Text(
                        text = pageLabel,
                        style = MaterialTheme.typography.labelSmall,
                        color = And03Theme.colors.onSecondaryContainer
                    )
                }
            )

            /** 날짜 */
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = date,
                    style = MaterialTheme.typography.labelSmall,
                    color = And03Theme.colors.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun MemoTypeChip(type: MemoType) {
    val backgroundColor: Color
    val icon: Int
    val label: String
    val tint: Color

    when (type) {
        MemoType.CANVAS -> {
            backgroundColor = Color(0xFFE3F2FD)
            icon = R.drawable.ic_outline_account_tree
            label = stringResource(R.string.memo_card_memo_type_canvas)
            tint = Color(0xFF1E88E5)
        }

        MemoType.TEXT -> {
            backgroundColor = Color(0xFFFFF7E0)
            icon = R.drawable.ic_outline_description
            label = stringResource(R.string.memo_card_memo_type_text)
            tint = Color(0xFFBE821B)
        }
    }


    LabelChip(backgroundColor = backgroundColor) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = tint,
            )

            Spacer(modifier = Modifier.width(And03Padding.PADDING_XS))

            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = tint
            )
        }
    }

}


@Preview(showBackground = true)
@Composable
fun MemoCardPreview() {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        MemoCard(
            type = MemoType.CANVAS,
            title = "메모 제목1",
            contentPreview = "ㅇㅇ",
            pageLabel = "p.1~26",
            date = "2025.12.24",
            onClick = {}
        )

        MemoCard(
            type = MemoType.TEXT,
            title = "메모 제목2",
            contentPreview = "내용내용내용내용내용내용내용내용내용내용내용내용내용...",
            pageLabel = "p.1~26",
            date = "2025.12.24",
            onClick = {}
        )
    }
}
