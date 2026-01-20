package com.example.project1

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    FuelCostScreen()
                }
            }
        }
    }
}

@Composable
fun FuelCostScreen() {
    val context = LocalContext.current

    var distanceKm by rememberSaveable { mutableStateOf("") }
    var consumption by rememberSaveable { mutableStateOf("") }
    var pricePerLiter by rememberSaveable { mutableStateOf("") }

    var litersResult by rememberSaveable { mutableStateOf<Double?>(null) }
    var costResult by rememberSaveable { mutableStateOf<Double?>(null) }

    fun parseDouble(text: String): Double? =
        text.trim().replace(',', '.').toDoubleOrNull()

    fun format2(value: Double): String = String.format("%.2f", value)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineSmall
        )

        OutlinedTextField(
            value = distanceKm,
            onValueChange = { distanceKm = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.distance_km)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(
            value = consumption,
            onValueChange = { consumption = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.consumption_l_100)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(
            value = pricePerLiter,
            onValueChange = { pricePerLiter = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.price_pln_l)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    val d = parseDouble(distanceKm)
                    val c = parseDouble(consumption)
                    val p = parseDouble(pricePerLiter)


                    if (d == null || c == null || p == null || d <= 0 || c <= 0 || p <= 0) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.toast_invalid),
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }

                    val liters = d * c / 100.0
                    val cost = liters * p

                    litersResult = liters
                    costResult = cost

                    Toast.makeText(
                        context,
                        context.getString(R.string.toast_calculated),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            ) {
                Text(stringResource(R.string.btn_calculate))
            }

            OutlinedButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    distanceKm = ""
                    consumption = ""
                    pricePerLiter = ""
                    litersResult = null
                    costResult = null

                    Toast.makeText(
                        context,
                        context.getString(R.string.toast_cleared),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            ) {
                Text(stringResource(R.string.btn_clear))
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.results),
                    style = MaterialTheme.typography.titleMedium
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(R.string.result_liters))
                    Text(
                        text = litersResult?.let { "${format2(it)} L" } ?: "—",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(R.string.result_cost))
                    Text(
                        text = costResult?.let { "${format2(it)} PLN" } ?: "—",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }

        Text(
            text = stringResource(R.string.hint_commas),
            style = MaterialTheme.typography.bodySmall
        )
    }
}
