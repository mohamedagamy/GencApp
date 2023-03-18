package com.example.genc.ui.view.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.genctask.data.Car
import com.google.gson.Gson
import com.example.genc.ui.theme.Shapes
import com.example.genc.ui.theme.Typography
import com.example.component.LoadingBar
import com.example.component.SearchTextField
import com.example.component.ShowToast
import com.example.genc.R


@Composable
fun CarSearchScreen(
    carsViewModel: CarsViewModel = hiltViewModel(),
    onClick: (String) -> Unit
) {

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column {
            Text(
                text = stringResource(R.string.cars_screen_title),
                style = Typography.h4,
                modifier = Modifier.padding(24.dp),
            )

            var search by rememberSaveable(stateSaver = TextFieldValue.Saver) {
                mutableStateOf(TextFieldValue())
            }

            var filter by remember { mutableStateOf(true) }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                SearchTextField(
                    value = search, onValueChange = {
                        search = it
                        carsViewModel.searchCars(it.text)
                    }, hint = stringResource(R.string.cars_screen_search_hint),
                    color = MaterialTheme.colors.background
                )
                IconButton(onClick = {
                    filter = !filter
                    carsViewModel.filterCars(filter)
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_filter),
                        contentDescription = null,
                    )
                }
            }
            CarsContent(vm = carsViewModel, onCarClick = onClick)
        }
    }
}

@Composable
fun CarsContent(vm: CarsViewModel, onCarClick: (String) -> Unit) {
    vm.state.value.let { state ->
        when (state) {
            is Loading -> LoadingBar()
            is CarsListUiStateReady -> state.cars?.let { BindList(it, onCarClick = onCarClick) }
            is CarsListUiStateError -> state.error?.let { ShowToast(it) }
        }
    }
}

@Composable
fun CarBrand(item: Car) {
    item.brand?.let {
        Text(
            text = it,
            modifier = Modifier
                .size(150.dp)
                .padding(end = 8.dp))

    }
}

@Composable
fun ListItem(item: Car, onClick: (String) -> Unit) {
    Card(
        shape = Shapes.large,
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp,
        modifier = Modifier
            .height(200.dp)
            .padding(8.dp)
            .clickable { onClick.invoke(Gson().toJson(item)) },
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

@Composable
fun BindList(list: List<Car>, onCarClick: (String) -> Unit) {
    if(list.isNotEmpty()) {
        LazyColumn(contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)) {
            items(
                items = list,
                itemContent = {
                    ListItem(it, onClick = {
                        it.let {
                            onCarClick.invoke(it)
                        }
                    })
                })
        }
    }else{
        EmptyView()
    }
}

@Composable
fun EmptyView() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.cars_no_match),
            modifier = Modifier.padding(16.dp)
        )
    }
    ShowToast(text = stringResource(id = R.string.cars_no_match))
}



