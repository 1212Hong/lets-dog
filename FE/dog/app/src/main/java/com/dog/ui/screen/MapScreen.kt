package com.dog.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dog.data.viewmodel.map.LocationTrackingViewModel
import com.dog.ui.theme.DogTheme
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
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

    var uiSettings by remember { mutableStateOf(MapUiSettings()) }
    var properties by remember {
        mutableStateOf(MapProperties(mapType = MapType.SATELLITE))
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 15f)
    }

    LaunchedEffect(userLocation) {
        userLocation?.let {
            cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(it, 15f))
        }
    }
    Box(Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            properties = properties,
            uiSettings = uiSettings
        ) {

            userLocation?.let { userLocation ->
                Marker(state = MarkerState(position = userLocation))
            }

            Polyline(
                points = pathPoints,
                color = Color.Blue,
                width = 50f
            )
            Log.i("LocationTracking", "스크린 : ${pathPoints}")

            Row {
                Button(onClick = { viewModel.getCurrentLocation() }) {
                    Text("현재 위치")
                }
                Button(onClick = { viewModel.startTracking() }) {
                    Text("시작")
                }
                Button(onClick = { viewModel.stopTracking() }) {
                    Text("종료")
                }
            }

        }
    }

//    Column {
//        GoogleMap(
//            modifier = Modifier
//                .weight(1f)
//                .fillMaxWidth(),
//            cameraPositionState = cameraPositionState,
//            properties = properties,
//            uiSettings = uiSettings
//        ) {
//            userLocation?.let { userLocation ->
//                Marker(state = MarkerState(position = userLocation))
//            }
//
//            Polyline(
//                points = pathPoints,
//                color = Color.Blue,
//                width = 50f
//            )
//            Log.i("LocationTracking", "스크린 : ${pathPoints}")
//
//        }
//        Row {
//            Button(onClick = { viewModel.getCurrentLocation() }) {
//                Text("현재 위치")
//            }
//            Button(onClick = { viewModel.startTracking() }) {
//                Text("시작")
//            }
//            Button(onClick = { viewModel.stopTracking() }) {
//                Text("종료")
//            }
//        }
//    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val context = LocalContext.current
    val viewModel = LocationTrackingViewModel(context)
    WalkingPage(viewModel = viewModel)
}
