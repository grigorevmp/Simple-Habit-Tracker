package com.grigorevmp.habits.presentation.screen.habits.elements.stastistic

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer

@Composable
fun ShimmerCard() {
    Card(
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth()
            .shimmer(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        ),
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondary
            ),
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(8.dp)
            )
        }
//        Text(
//            text = "Loading...",
//            modifier = Modifier.padding(16.dp)
//        )
    }
}


@Preview(showBackground = true)
@Composable
fun ShimmerCardPreview() {
    ShimmerCard()
}

