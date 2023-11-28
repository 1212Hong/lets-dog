package com.dog.ui.screen.walking

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.dog.data.Screens
import com.dog.data.model.gps.TrackingHistory
import com.dog.data.viewmodel.map.LocationTrackingHistoryViewModel
import com.dog.data.viewmodel.map.LocationTrackingViewModel
import com.dog.ui.theme.DogTheme
import com.dog.util.common.formatTrackingDate
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun WalkingHistoryScreen(
    navController: NavController,
    trackingViewModel: LocationTrackingViewModel,
    locationTrackingHistoryViewModel: LocationTrackingHistoryViewModel
) {

    var isMapDialogOpen by remember { mutableStateOf(false) }
    var polylinePoints by remember { mutableStateOf(emptyList<LatLng>()) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val lifecycleOwner = LocalLifecycleOwner.current


    DogTheme {
        Column {
            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp)
            ) {
                if (currentRoute == Screens.WalkingHistory.route) {
                    Button(onClick = { navController.navigate(Screens.Walking.route) }) {
                        Text("트래킹")
                    }
                } else {
                    Button(onClick = { navController.navigate(Screens.WalkingHistory.route) }) {
                        Text("기록")
                    }
                }
            }
            TrackingHistoryPage(viewModel = locationTrackingHistoryViewModel) { gpsPoints ->
                polylinePoints = gpsPoints
                isMapDialogOpen = true
            }
        }
        if (isMapDialogOpen) {
            ShowMapDialog(
                isDialogOpen = isMapDialogOpen,
                polylinePoints = polylinePoints,
                onDismiss = { isMapDialogOpen = false }
            )
        }
    }
}

@Composable
fun TrackingHistoryPage(
    viewModel: LocationTrackingHistoryViewModel,
    onItemClicked: (List<LatLng>) -> Unit
) {
    val trackingHistoryState = viewModel.trackingHistory.collectAsState()
    var currentPage by remember { mutableStateOf(1) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            trackingHistoryState.value?.contents?.forEach { trackingHistory ->
                TrackingHistoryItem(trackingHistory = trackingHistory, onItemClicked = onItemClicked)
            }

            Spacer(modifier = Modifier.weight(1f, true))
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 86.dp)
                .fillMaxWidth()
        ) {
            val pages = trackingHistoryState.value?.totalPages
            val len = if (pages != null && pages > 0) pages else 1
            (1..len).forEach { page ->
                Text(
                    text = "$page",
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                        .clickable {
                            currentPage = page
                            viewModel.getTrackingHistory(page)
                        },
                    fontSize = 19.sp,
                    color = if (page == currentPage) Color.Blue else Color.Gray
                )
            }
        }
    }
}


@Composable
fun TrackingHistoryItem(trackingHistory: TrackingHistory, onItemClicked: (List<LatLng>) -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onItemClicked(convertGpsPointsToLatLngList(trackingHistory.gpsPoints.gps_points)) },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "날짜: ${formatTrackingDate(trackingHistory.trackingDate)}")
            Text(text = "산책 시간: ${trackingHistory.runningTime}")
        }
    }
}

fun convertGpsPointsToLatLngList(gpsPoints: List<List<Double>>): List<LatLng> {
    return gpsPoints.map { LatLng(it[0], it[1]) }
}

@Composable
fun ShowMapDialog(isDialogOpen: Boolean, polylinePoints: List<LatLng>, onDismiss: () -> Unit) {
    if (isDialogOpen) {
        Dialog(onDismissRequest = onDismiss) {
            Box(
                modifier = Modifier
                    .wrapContentSize()
            ) {
                val cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(
                        polylinePoints.firstOrNull() ?: polylinePoints[polylinePoints.size / 2], 18f
                    )
                }

                GoogleMap(
                    modifier = Modifier
                        .size(300.dp, 300.dp),
                    cameraPositionState = cameraPositionState
                ) {
                    Polyline(
                        points = polylinePoints,
                        color = Color.Blue,
                        width = 10f
                    )
                }
            }
        }
    }
}
