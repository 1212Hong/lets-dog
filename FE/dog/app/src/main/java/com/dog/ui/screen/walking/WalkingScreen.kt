package com.dog.ui.screen.walking

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.datastore.dataStore
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dog.data.Screens
import com.dog.data.viewmodel.map.LocationTrackingViewModel
import com.dog.ui.theme.DogTheme
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun WalkingScreen(navController: NavController, viewModel: LocationTrackingViewModel) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    DogTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            WalkingPage(viewModel = viewModel)
            Row(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp)
                    .zIndex(1f)
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
        }
    }
}

@Composable
fun WalkingPage(viewModel: LocationTrackingViewModel) {
    val userLocation by viewModel.userLocation.collectAsState()
    val pathPoints by viewModel.pathPoints.collectAsState()
    val initialPosition = userLocation ?: LatLng(0.0, 0.0)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialPosition, 18f)
    }

    LaunchedEffect(key1 = true) {
        viewModel.updateLocationOnly()
    }

    UpdateCameraPosition(userLocation, cameraPositionState)
    RenderWalkingScreen(userLocation, pathPoints, cameraPositionState, viewModel)
}

@Composable
private fun UpdateCameraPosition(
    userLocation: LatLng?,
    cameraPositionState: CameraPositionState
) {
    LaunchedEffect(userLocation) {
        userLocation?.let { location ->
            val update = CameraUpdateFactory.newCameraPosition(
                CameraPosition.Builder()
                    .target(location)
                    .zoom(cameraPositionState.position.zoom)
                    .build()
            )
            cameraPositionState.animate(update, 1000)
        }
    }
}


@Composable
private fun RenderWalkingScreen(
    userLocation: LatLng?,
    pathPoints: List<LatLng>,
    cameraPositionState: CameraPositionState,
    viewModel: LocationTrackingViewModel
) {
    val uiSettings = MapUiSettings(myLocationButtonEnabled = true, zoomControlsEnabled = false)
    val properties = MapProperties(isMyLocationEnabled = true)
    val isRunning by viewModel.isRunning.collectAsState()
    Log.i("LocationTracking", "pathPoints[RenderWalkingScreen] : $pathPoints")

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = uiSettings,
            properties = properties
        ) {
            userLocation?.let { location ->
                Marker(state = MarkerState(position = location))
            }
            if (pathPoints.isNotEmpty()) {
                Polyline(
                    points = pathPoints,
                    color = Color.Blue,
                    width = 12.dp.value
                )
            }
        }
        RunningCard(
            isRunning = isRunning, viewModel = viewModel,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp, vertical = 90.dp)
        )
    }
}

@Composable
private fun RunningCard(
    modifier: Modifier = Modifier,
    isRunning: Boolean,
    viewModel: LocationTrackingViewModel
) {
    ElevatedCard(
        modifier = modifier
            .padding(vertical = 10.dp, horizontal = 15.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        RunningCardTime(
            modifier = Modifier
                .padding(
                    top = 24.dp,
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 16.dp
                )
                .fillMaxWidth(),
            viewModel = viewModel,
            isRunning = isRunning,
        )
    }
}

@Composable
private fun RunningCardTime(
    modifier: Modifier = Modifier,
    isRunning: Boolean,
    viewModel: LocationTrackingViewModel
) {
    val formattedTime by viewModel.formattedTime.collectAsState()
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 10.dp)
        ) {
            Text(
                text = "산책 시간",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Normal),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = formattedTime,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold),
            )
        }
        ControlButtons(viewModel = viewModel, isRunning = isRunning)
    }
}

@Composable
private fun ControlButtons(viewModel: LocationTrackingViewModel, isRunning: Boolean) {
    Button(onClick = {
        if (isRunning) viewModel.stopTracking() else viewModel.startTracking()
    }) {
        Text(if (isRunning) "종료" else "시작")
    }
}

