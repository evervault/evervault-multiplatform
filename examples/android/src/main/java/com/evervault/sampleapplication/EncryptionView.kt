package com.evervault.sampleapplication

import EncryptionViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EncryptionView() {

    var encryptedValue: String? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        encryptedValue = EncryptionViewModel().encryptedValue()
    }

    Column(modifier = Modifier.padding(20.dp)) {
        Text("Encrypted string:")
        Text(encryptedValue ?: "Loading..")
    }

}
