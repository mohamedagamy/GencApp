package com.example.genc.ui.view.details

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.genctask.data.Car
import com.google.accompanist.pager.ExperimentalPagerApi
import com.example.genc.ui.theme.Shapes
import com.example.genc.ui.theme.Typography
import com.example.genc.ui.view.home.CarBrand
import com.example.component.LoadingBar
import com.example.genc.R

@Composable
fun CarDetailsScreen(detailsViewModel: DetailsViewModel = hiltViewModel()) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.details_screen_title)) },
                backgroundColor = MaterialTheme.colors.surface,
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        DetailsContent(vm = detailsViewModel)
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CarDetailsPager(car: Car) {
    Column(
        Modifier
            .fillMaxSize()
    ) {
        CardItemDetails(item = car)
    }
}

@Composable
fun DetailsContent(vm: DetailsViewModel) {
    vm.state.value.let { state ->
        when (state) {
            is Loading -> LoadingBar()
            is DetailsUiStateReady -> CarDetailsPager(state.car)
            else -> {}
        }
    }
}

@Composable
fun CardItemDetails(item: Car) {
    Card(
        shape = Shapes.large,
        backgroundColor = MaterialTheme.colors.background,
        elevation = 4.dp,
        modifier = Modifier
            .height(200.dp)
            .padding(8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
        ) {

            CarBrand(item)
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Text(
                    text = "€${item.unit_price}",
                    fontWeight = FontWeight.Bold,
                    style = Typography.subtitle2,
                )
                Text(
                    text = "${item.brand} ${item.model}",
                    fontWeight = FontWeight.Bold,
                    style = Typography.subtitle1,
                )

                Text(
                    text = item.currency,
                    style = Typography.caption,
                )
                Spacer(modifier = Modifier.height(8.dp))

                item.color?.let {
                    Text(
                        text = stringResource(R.string.cars_screen_color, it),
                        style = Typography.subtitle2,
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = item.plate_number,
                    style = Typography.caption,
                    maxLines = 2
                )

            }
        }
    }
}