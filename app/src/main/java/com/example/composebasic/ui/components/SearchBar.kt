package com.example.composebasic.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composebasic.ui.theme.LightOnSurface
import com.example.composebasic.ui.theme.LightOnSurfaceVariant
import com.example.composebasic.ui.theme.LightSurfaceVariant

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(LightSurfaceVariant)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            tint = LightOnSurfaceVariant
        )

        Spacer(modifier = Modifier.width(8.dp))

        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.weight(1f),
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = LightOnSurface
            ),
            decorationBox = { innerTextField ->
                if (query.isEmpty()) {
                    Text(
                        text = "Search replies",
                        style = MaterialTheme.typography.bodyLarge,
                        color = LightOnSurfaceVariant
                    )
                }
                innerTextField()
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SearchBarPreview(
    query: String = "Search",
    onQueryChange: (String) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(LightSurfaceVariant)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            tint = LightOnSurfaceVariant
        )

        Spacer(modifier = Modifier.width(16.dp))

        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.weight(1f),
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = LightOnSurface
            ),
            decorationBox = { innerTextField ->
                if (query.isEmpty()) {
                    Text(
                        text = "Search replies",
                        style = MaterialTheme.typography.bodyLarge,
                        color = LightOnSurfaceVariant
                    )
                }
                innerTextField()
            }
        )
    }
}