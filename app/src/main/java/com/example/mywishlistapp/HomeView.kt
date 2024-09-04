package com.example.mywishlistapp

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.*
import androidx.compose.material.icons.filled.Edit
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mywishlistapp.data.Wish

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeView(
    navController: NavController,
    viewModel: WishViewModel
){
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { AppBarView(title= "WishList") },
        contentColor = Color.White,
        backgroundColor = colorResource(id = R.color.gray),
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(all = 20.dp),
                contentColor = Color.White,
                backgroundColor = colorResource(id = R.color.app_bar_color),
                onClick = {
                    navController.navigate(Screen.AddScreen.route + "/0L")
                }
            )
            {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
        }
    )
    {
        val wishlist = viewModel.getAllWishes.collectAsState(initial = listOf())

        LazyColumn(modifier = Modifier.fillMaxSize().padding(it))
        {
            items(wishlist.value, key={wish-> wish.id} ){
                wish ->
                val dismissState = rememberDismissState(
                    confirmStateChange = {
                        if(it == DismissValue.DismissedToEnd || it== DismissValue.DismissedToStart){
                            viewModel.deleteWish(wish)
                        }
                        true
                    }
                )

                SwipeToDismiss(
                    state = dismissState,
                    background = {
                        val color by animateColorAsState(
                            if(dismissState.dismissDirection == DismissDirection.EndToStart)
                                Color.Red
                            else
                                Color.Transparent
                            ,label = ""
                        )
                        val alignment = Alignment.CenterEnd
                        Box(
                            Modifier.fillMaxSize().background(color).padding(horizontal = 20.dp),
                            contentAlignment = alignment
                        ){
                            Icon(Icons.Default.Delete,
                                contentDescription = "Delete Icon",
                                tint = Color.White)
                        }

                    },
                    directions = setOf(DismissDirection.EndToStart),
                    dismissThresholds = {FractionalThreshold(1f)},
                    dismissContent = {
                        WishItem(wish = wish) {
                            val id = wish.id
                            navController.navigate(Screen.AddScreen.route + "/$id")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun WishItem(wish: Wish, onClick: () -> Unit){
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 8.dp, start = 8.dp, end = 8.dp)
        .clickable { onClick() },
        elevation = 10.dp,
        backgroundColor = colorResource(id = R.color.card_background)
    ) {
        Column(modifier = Modifier.padding(16.dp)){
            Text(text = wish.title, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = wish.description, fontSize = 16.sp)
        }
    }
}