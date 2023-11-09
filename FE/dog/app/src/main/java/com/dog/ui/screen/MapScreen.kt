package com.dog.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
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
fun WalkingScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel = LocationTrackingViewModel(context = context)
    DogTheme {
        WalkingPage(viewModel = viewModel)
    }
}

@Composable
fun WalkingPage(viewModel: LocationTrackingViewModel) {
    val userLocation by viewModel.userLocation.collectAsState()
    val pathPoints by viewModel.pathPoints.collectAsState()
    val initialPosition = userLocation ?: LatLng(0.0, 0.0)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialPosition, 15f)
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
            cameraPositionState.animate(update, 2000)
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
    val uiSettings = MapUiSettings(myLocationButtonEnabled = true)
    val properties = MapProperties(isMyLocationEnabled = true)
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
        ControlButtons(viewModel = viewModel)
    }
}


@Composable
private fun ControlButtons(viewModel: LocationTrackingViewModel) {
    Row(Modifier.padding(bottom = 100.dp)) {
        Button(onClick = { viewModel.startTracking() }) {
            Text("시작")
        }
        Button(onClick = { viewModel.stopTracking() }) {
            Text("종료")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val context = LocalContext.current
    val viewModel = LocationTrackingViewModel(context)
    WalkingPage(viewModel = viewModel)
}
