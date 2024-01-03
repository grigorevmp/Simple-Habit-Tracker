package com.grigorevmp.habits.presentation.screen.habits.elements.common

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun ActionIcon(
    iconId: Int,
    iconDescription: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { },
) {
    Card(
        modifier = modifier.height(36.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        ),
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = iconDescription,
            modifier = Modifier
                .padding(9.dp)
                .size(18.dp)
        )
    }
}