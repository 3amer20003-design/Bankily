package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.border
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CallReceived
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.PermContactCalendar
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Money
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.Smartphone
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material.icons.filled.SouthWest
import androidx.compose.material.icons.outlined.CheckBoxOutlineBlank
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.CheckCircle
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Sync
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (androidx.core.content.ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                androidx.core.app.ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }
        
        FavoritesManager.init(this)
        NotificationsManager.init(this)
        TransactionsManager.init(this)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    BankilyApp()
                }
            }
        }
    }
}

data class FavoriteItem(val id: String, val type: String, val phone: String, val amount: String)

object FavoritesManager {
    private lateinit var prefs: android.content.SharedPreferences
    val favorites = androidx.compose.runtime.mutableStateListOf<FavoriteItem>()
    
    fun init(context: android.content.Context) {
        prefs = context.getSharedPreferences("BankilyFavorites", android.content.Context.MODE_PRIVATE)
        load()
    }
    
    fun add(item: FavoriteItem) {
        if (favorites.none { it.id == item.id }) {
            favorites.add(item)
            save()
        }
    }
    
    fun remove(item: FavoriteItem) {
        favorites.removeAll { it.id == item.id }
        save()
    }
    
    private fun load() {
        favorites.clear()
        val set = prefs.getStringSet("favorites", null) ?: emptySet()
        for (str in set) {
            val parts = str.split("|||")
            if (parts.size == 4) {
                favorites.add(FavoriteItem(parts[0], parts[1], parts[2], parts[3]))
            }
        }
    }
    
    private fun save() {
        val set = favorites.map { "${it.id}|||${it.type}|||${it.phone}|||${it.amount}" }.toSet()
        prefs.edit().putStringSet("favorites", set).apply()
    }
}

data class NotificationItem(val id: String, val type: String, val phone: String, val amount: String, val date: String)

object NotificationsManager {
    private lateinit var prefs: android.content.SharedPreferences
    val notifications = androidx.compose.runtime.mutableStateListOf<NotificationItem>()
    
    fun init(context: android.content.Context) {
        prefs = context.getSharedPreferences("BankilyNotifications", android.content.Context.MODE_PRIVATE)
        load()
    }
    
    fun add(item: NotificationItem) {
        notifications.add(0, item) // Add to top
        save()
    }
    
    private fun load() {
        notifications.clear()
        val set = prefs.getStringSet("notifications", null) ?: emptySet()
        val loadedList = mutableListOf<NotificationItem>()
        for (str in set) {
            val parts = str.split("|||")
            if (parts.size == 5) {
                loadedList.add(NotificationItem(parts[0], parts[1], parts[2], parts[3], parts[4]))
            }
        }
        notifications.addAll(loadedList)
    }
    
    private fun save() {
        val set = notifications.map { "${it.id}|||${it.type}|||${it.phone}|||${it.amount}|||${it.date}" }.toSet()
        prefs.edit().putStringSet("notifications", set).apply()
    }
}

data class TransactionItem(val id: String, val type: String, val description: String, val date: String, val details: String, val amount: String)

object TransactionsManager {
    private lateinit var prefs: android.content.SharedPreferences
    val transactions = androidx.compose.runtime.mutableStateListOf<TransactionItem>()
    
    fun init(context: android.content.Context) {
        prefs = context.getSharedPreferences("BankilyTransactionsV2", android.content.Context.MODE_PRIVATE)
        load()
    }
    
    fun add(item: TransactionItem) {
        transactions.add(0, item) // Add to top
        save()
    }
    
    private fun load() {
        transactions.clear()
        val set = prefs.getStringSet("transactions", null) ?: emptySet()
        val loadedList = mutableListOf<TransactionItem>()
        for (str in set) {
            val parts = str.split("|||")
            if (parts.size == 6) {
                loadedList.add(TransactionItem(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]))
            }
        }
        // Basic sort by descending (to keep newest first roughly, though string splitting ordering is lost from set)
        transactions.addAll(loadedList)
    }
    
    private fun save() {
        val set = transactions.map { "${it.id}|||${it.type}|||${it.description}|||${it.date}|||${it.details}|||${it.amount}" }.toSet()
        prefs.edit().putStringSet("transactions", set).apply()
    }
}


@Composable
fun BankilyApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                BankilyLoginScreen(
                    modifier = Modifier.padding(innerPadding),
                    onLoginClick = {
                        navController.navigate("dashboard")
                    }
                )
            }
        }
        composable("dashboard") {
            DashboardScreen(
                onTransferMoneyClick = {
                    navController.navigate("transfer_money")
                },
                onHelpClick = {
                    navController.navigate("help")
                },
                onMyAccountClick = {
                    navController.navigate("my_account")
                }
            )
        }
        composable("my_account") {
            MyAccountScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        composable("help") {
            HelpScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        composable("transfer_money") {
             TransferMoneyScreen(
                 onBackClick = { navController.popBackStack() },
                 onSendMoneyClick = { navController.navigate("send_money") }
             )
        }
        composable("send_money") {
             SendMoneyScreen(
                 onBackClick = { navController.popBackStack() },
                 onSendClick = { phone, amount ->
                     navController.navigate("confirm_transfer/$phone/$amount")
                 }
             )
        }
        composable(
            route = "confirm_transfer/{phone}/{amount}",
            arguments = listOf(
                navArgument("phone") { type = NavType.StringType },
                navArgument("amount") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val phone = backStackEntry.arguments?.getString("phone") ?: ""
            val amount = backStackEntry.arguments?.getString("amount") ?: ""
            ConfirmTransferScreen(
                phoneNumber = phone,
                amount = amount,
                onBackClick = { navController.popBackStack() },
                onCompleteClick = {
                    // Navigate back to dashboard or show success
                    navController.popBackStack("dashboard", inclusive = false)
                }
            )
        }
    }
}

object AuthSession {
    var savedPin by androidx.compose.runtime.mutableStateOf("")
    var userBalance by androidx.compose.runtime.mutableStateOf(1550.52)
}

@Composable
fun BankilyLoginScreen(modifier: Modifier = Modifier, onLoginClick: () -> Unit = {}) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val sharedPreferences = remember { context.getSharedPreferences("BankilyPrefs", android.content.Context.MODE_PRIVATE) }
    var username by remember { mutableStateOf(sharedPreferences.getString("saved_phone", "") ?: "") }
    var password by remember { mutableStateOf(AuthSession.savedPin) }
    var showError by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    if (isLoading) {
        androidx.compose.runtime.LaunchedEffect(Unit) {
            kotlinx.coroutines.delay(3000)
            isLoading = false
            onLoginClick()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            
            coil.compose.AsyncImage(
                model = "https://i.ibb.co/whTsFP8w/logo.png",
                contentDescription = "Bankily Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = androidx.compose.ui.layout.ContentScale.Fit
            )
            
            Spacer(modifier = Modifier.height(24.dp))

            // Username field
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "اسم المستخدم أو رقم الهاتف",
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AccountCircle,
                        contentDescription = "User Icon",
                        modifier = Modifier.size(32.dp),
                        tint = Color.Black
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        BasicTextField(
                            value = username,
                            onValueChange = { 
                                username = it 
                                sharedPreferences.edit().putString("saved_phone", it).apply()
                            },
                            textStyle = TextStyle(fontSize = 18.sp, color = Color.Black),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            singleLine = true
                        )
                        HorizontalDivider(color = Color.Black, thickness = 1.dp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Password field
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "الرقم السري",
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = "Lock Icon",
                        modifier = Modifier.size(32.dp),
                        tint = Color.Black
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    BasicTextField(
                        value = password,
                        onValueChange = { 
                            if (it.length <= 4 && it.all { char -> char.isDigit() }) {
                                password = it 
                                AuthSession.savedPin = it
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        decorationBox = { innerTextField ->
                            Box {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(top = 16.dp)
                                ) {
                                    repeat(4) { index ->
                                        val isFilled = index < password.length
                                        Box(
                                            modifier = Modifier
                                                .width(36.dp)
                                                .height(36.dp),
                                            contentAlignment = Alignment.BottomCenter
                                        ) {
                                            if (isFilled) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(12.dp)
                                                        .background(Color.Black, shape = androidx.compose.foundation.shape.CircleShape)
                                                        .align(Alignment.Center)
                                                        .padding(bottom = 8.dp)
                                                )
                                            }
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(2.dp)
                                                    .background(Color.Black)
                                            )
                                        }
                                    }
                                }
                                Box(modifier = Modifier.matchParentSize().alpha(0.01f)) {
                                    innerTextField()
                                }
                            }
                        },
                        textStyle = TextStyle(color = Color.Transparent)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Forgot Password
            Text(
                text = "نسيت الرقم السري ؟",
                fontSize = 14.sp,
                color = Color.Black,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable { }
            )

            Spacer(modifier = Modifier.weight(1f))

            // Register link
            Text(
                text = "مستخدم جديد ؟ سجل الآن!",
                fontSize = 16.sp,
                color = Color.Black,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .padding(bottom = 80.dp)
                    .clickable { }
            )
        }

        // Bottom Button
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (showError) {
                Text(
                    text = "الرجاء إدخال اسم المستخدم والرقم السري",
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            Button(
                onClick = {
                    if (username.isNotBlank() && password.length == 4) {
                        showError = false
                        isLoading = true
                    } else {
                        showError = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(0.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF7C02D))
            ) {
                Text(
                    text = "تسجيل الدخول",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x80000000)),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.CircularProgressIndicator(
                    color = Color(0xFF00AEEF),
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Composable
fun FavoritesContent() {
    val favorites = FavoritesManager.favorites
    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) {
        items(
            count = favorites.size
        ) { index ->
            val favorite = favorites[index]
            FavoritesItemRow(
                item = favorite,
                onDelete = { FavoritesManager.remove(favorite) }
            )
            androidx.compose.material3.HorizontalDivider(color = Color.LightGray, thickness = 1.dp)
        }
    }
}

@Composable
fun FavoritesItemRow(item: FavoriteItem, onDelete: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // First child in RTL goes to the Right
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = item.type, fontSize = 18.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "رقم الهاتف : ${item.phone}", fontSize = 16.sp, color = Color.DarkGray)
        }

        // Second child in RTL goes to the Left
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(end = 16.dp)
            ) {
                Text(text = "MRU ${item.amount}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(4.dp))
                Icon(
                    imageVector = Icons.Filled.Sync,
                    contentDescription = "Sync",
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "More", tint = Color.Gray, modifier = Modifier.size(32.dp))
                }
                androidx.compose.material3.DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    androidx.compose.material3.DropdownMenuItem(
                        text = { Text("حذف", color = Color.Red, fontSize = 16.sp) },
                        onClick = {
                            expanded = false
                            onDelete()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun NotificationsContent() {
    val context = androidx.compose.ui.platform.LocalContext.current
    var selectedNotificationTab by remember { mutableStateOf(0) } // 0: عامة, 1: الواردة, 2: المرسلة

    Column(
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) {
        // Cyan Top Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF00BCD4))
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { selectedNotificationTab = 0 }.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Outlined.CheckBoxOutlineBlank,
                    contentDescription = "General",
                    tint = if (selectedNotificationTab == 0) Color(0xFFF7C02D) else Color.White,
                    modifier = Modifier.size(36.dp)
                )
                Text("عامة", color = if (selectedNotificationTab == 0) Color(0xFFF7C02D) else Color.White, fontSize = 12.sp)
                if (selectedNotificationTab == 0) {
                    androidx.compose.material3.HorizontalDivider(modifier = Modifier.padding(top = 8.dp).width(40.dp), color = Color(0xFFF7C02D), thickness = 3.dp)
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { selectedNotificationTab = 1 }.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Filled.SouthWest,
                    contentDescription = "Received",
                    tint = if (selectedNotificationTab == 1) Color(0xFFF7C02D) else Color.White,
                    modifier = Modifier.size(36.dp)
                )
                Text("الطلبات الواردة", color = if (selectedNotificationTab == 1) Color(0xFFF7C02D) else Color.White, fontSize = 12.sp)
                if (selectedNotificationTab == 1) {
                    androidx.compose.material3.HorizontalDivider(modifier = Modifier.padding(top = 8.dp).width(40.dp), color = Color(0xFFF7C02D), thickness = 3.dp)
                }
            }
            
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { selectedNotificationTab = 2 }.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowOutward,
                    contentDescription = "Sent",
                    tint = if (selectedNotificationTab == 2) Color(0xFFF7C02D) else Color.White,
                    modifier = Modifier.size(36.dp)
                )
                Text("الطلبات المرسلة", color = if (selectedNotificationTab == 2) Color(0xFFF7C02D) else Color.White, fontSize = 12.sp)
                if (selectedNotificationTab == 2) {
                    androidx.compose.material3.HorizontalDivider(modifier = Modifier.padding(top = 8.dp).width(40.dp), color = Color(0xFFF7C02D), thickness = 3.dp)
                }
            }
        }
        
        if (selectedNotificationTab != 0) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "لا توجد اشعارات", fontSize = 18.sp, color = Color.Gray)
            }
        } else {
            val notifications = NotificationsManager.notifications
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    count = notifications.size
                ) { index ->
                    val notif = notifications[index]
                    NotificationItemRow(item = notif)
                    androidx.compose.material3.HorizontalDivider(color = Color.LightGray, thickness = 1.dp)
                }
            }
        }
    }
}

@Composable
fun NotificationItemRow(item: NotificationItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Icon(
                imageVector = Icons.Outlined.Email,
                contentDescription = "Envelope",
                tint = Color.Gray,
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = item.type, fontSize = 18.sp, color = Color.Black)
                Text(text = "المبلغ : ${item.amount} أوقية", fontSize = 16.sp, color = Color.Black)
                Text(text = "المستفيد : ${item.phone}", fontSize = 16.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = item.id, fontSize = 14.sp, color = Color.Black)
            }
        }
        
        Text(text = item.date, fontSize = 16.sp, color = Color.Black)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(onTransferMoneyClick: () -> Unit = {}, onHelpClick: () -> Unit = {}, onMyAccountClick: () -> Unit = {}) {
    var currentTab by remember { mutableStateOf(0) }
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        when (currentTab) {
                            1 -> "المفضلة"
                            2 -> "الإشعارات"
                            else -> "لوحة القيادة"
                        },
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    if (currentTab == 0) {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Menu",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF00BCD4)
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                contentColor = Color.Gray,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Home", modifier = Modifier.size(28.dp)) },
                    label = { Text("الرئيسية") },
                    selected = currentTab == 0,
                    onClick = { currentTab = 0 },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF00BCD4),
                        selectedTextColor = Color(0xFF00BCD4),
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color.White
                    )
                )
                NavigationBarItem(
                    icon = { Icon(if (currentTab == 1) Icons.Filled.Star else Icons.Filled.StarBorder, contentDescription = "Favorites", modifier = Modifier.size(28.dp)) },
                    label = { Text("المفضلة") },
                    selected = currentTab == 1,
                    onClick = { currentTab = 1 },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF00BCD4),
                        selectedTextColor = Color(0xFF00BCD4),
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color.White
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.NotificationsNone, contentDescription = "Notifications", modifier = Modifier.size(28.dp)) },
                    label = { Text("الإشعارات") },
                    selected = currentTab == 2,
                    onClick = { currentTab = 2 },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF00BCD4),
                        selectedTextColor = Color(0xFF00BCD4),
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color.White
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.HelpOutline, contentDescription = "Help", modifier = Modifier.size(28.dp)) },
                    label = { Text("المساعدة") },
                    selected = currentTab == 3,
                    onClick = { onHelpClick() },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF00BCD4),
                        selectedTextColor = Color(0xFF00BCD4),
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color.White
                    )
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentTab) {
                0 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                    ) {
                        // Top Section with cyan background
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF00BCD4))
                                .padding(vertical = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                DashboardTopAction(
                    imageUrl = "https://i.ibb.co/vvDqQc7s/Screenshot-Bankily.jpg",
                    title = "تسديد مشتريات"
                )
                DashboardTopAction(
                    imageUrl = "https://i.ibb.co/4LRc8tr/Screenshot-Bankily.jpg",
                    title = "تحويل الأموال",
                    onClick = onTransferMoneyClick
                )
                DashboardTopAction(
                    imageUrl = "https://i.ibb.co/5hsqc3Gk/Screenshot-Bankily.jpg",
                    title = "حسابي",
                    onClick = onMyAccountClick
                )
            }

            // Grid Section
            val gridItems = listOf(
                GridItemData(null, "تعبئة رصيد الهاتف", Color(0xFF00BCD4), imageUrl = "https://i.ibb.co/HLJWcg0g/Screenshot-Bankily.jpg"),
                GridItemData(null, "تسديد الفواتير", Color(0xFFF7C02D), imageUrl = "https://i.ibb.co/jkjcx1w5/Screenshot-Bankily.jpg"),
                GridItemData(null, "طلب دفتر شيكات", Color(0xFF00BCD4), imageUrl = "https://i.ibb.co/xT3z3m5/Screenshot-Bankily.jpg"),
                GridItemData(null, "البطاقات البنكية", Color(0xFFF7C02D), imageUrl = "https://i.ibb.co/zW7W7mpP/Screenshot-Bankily.jpg"),
                GridItemData(null, "سحب النقود", Color(0xFF00BCD4), imageUrl = "https://i.ibb.co/TMH71s3G/Screenshot-Bankily.jpg"),
                GridItemData(null, "ب-باي", Color(0xFFF7C02D), imageUrl = "https://i.ibb.co/rG0kjrzS/Screenshot-Bankily.jpg"),
                GridItemData(null, "جيمتل", Color(0xFF00BCD4), imageUrl = "https://i.ibb.co/0jGfDR2x/Screenshot-Bankily.jpg")
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(gridItems) { item ->
                    DashboardGridItem(item)
                }
            }
                    }
                }
                1 -> {
                    FavoritesContent()
                }
                2 -> {
                    NotificationsContent()
                }
            }
        }
    }
}

data class GridItemData(val icon: ImageVector?, val title: String, val iconColor: Color, val isGimtel: Boolean = false, val imageUrl: String? = null)

@Composable
fun DashboardTopAction(icon: ImageVector? = null, imageUrl: String? = null, title: String, onClick: () -> Unit = {}) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .border(2.dp, Color(0xFFF7C02D), RoundedCornerShape(32.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (imageUrl != null) {
                coil.compose.AsyncImage(
                    model = imageUrl,
                    contentDescription = title,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(32.dp)),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
            } else if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            color = Color.White,
            fontSize = 14.sp
        )
    }
}

@Composable
fun DashboardGridItem(item: GridItemData) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
            .clickable { },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(8.dp)
        ) {
            if (item.isGimtel) {
                // Gimtel 'G' Icon Custom
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color(0xFF00BCD4), RoundedCornerShape(24.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "G",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else if (item.imageUrl != null) {
                coil.compose.AsyncImage(
                    model = item.imageUrl,
                    contentDescription = item.title,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(24.dp)),
                    contentScale = androidx.compose.ui.layout.ContentScale.Fit
                )
            } else if (item.icon != null) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    tint = item.iconColor,
                    modifier = Modifier.size(48.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = item.title,
                fontSize = 12.sp,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferMoneyScreen(onBackClick: () -> Unit = {}, onSendMoneyClick: () -> Unit = {}) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "تحويل الأموال",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF00BCD4)
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
        ) {
            // Top Send/Request Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF00BCD4))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TransferActionCard(
                    modifier = Modifier.weight(1f),
                    backgroundColor = Color(0xFF00838F),
                    imageUrl = "https://i.ibb.co/s9g2CgGJ/Screenshot-Bankily.jpg",
                    title = "إرسال الأموال",
                    onClick = onSendMoneyClick
                )
                TransferActionCard(
                    modifier = Modifier.weight(1f),
                    backgroundColor = Color(0xFFFFC107),
                    imageUrl = "https://i.ibb.co/Q3FQgQSz/Screenshot-Bankily.jpg",
                    title = "طلب المال"
                )
            }

            // Quick Transfer Section
            SectionHeader("التحويل السريع")
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { }
                ) {
                    Icon(
                        imageVector = Icons.Filled.QrCode2,
                        contentDescription = "QR Code",
                        tint = Color(0xFF00BCD4),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("رمز QR", fontSize = 16.sp, color = Color.Black)
                }
            }

            // Board Section
            SectionHeader("اللوحة")
            Text(
                text = "أنشئ مجموعة جديدة أو حدد مجموعة حالية لتقسيم المبلغ",
                fontSize = 12.sp,
                color = Color.DarkGray,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            )

            // Dashed Add Button
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                val stroke = Stroke(
                    width = 4f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                )
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .drawBehind {
                            drawRoundRect(
                                color = Color(0xFF00BCD4),
                                style = stroke,
                                cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f)
                            )
                        }
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add",
                        tint = Color(0xFF00BCD4),
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFEEEEEE))
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            color = Color.Black
        )
    }
}

@Composable
fun TransferActionCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    icon: ImageVector? = null,
    imageUrl: String? = null,
    title: String,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .height(100.dp)
            .background(if (imageUrl != null) Color.Transparent else backgroundColor, RoundedCornerShape(4.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (imageUrl != null) {
            coil.compose.AsyncImage(
                model = imageUrl,
                contentDescription = title,
                modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(4.dp)),
                contentScale = androidx.compose.ui.layout.ContentScale.Fit
            )
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendMoneyScreen(onBackClick: () -> Unit = {}, onSendClick: (String, String) -> Unit = { _, _ -> }) {
    var selectedTab by remember { mutableStateOf(0) }
    var phoneNumber by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    if (isLoading) {
        androidx.compose.runtime.LaunchedEffect(Unit) {
            kotlinx.coroutines.delay(3000)
            isLoading = false
            onSendClick(phoneNumber, amount)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "إرسال الأموال",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color(0xFF00BCD4)
                    )
                )
            },
            bottomBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (showError) {
                        Text(
                            text = "الرجاء إدخال رقم الهاتف والمبلغ",
                            color = Color.Red,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    Button(
                        onClick = { 
                            if (phoneNumber.isNotBlank() && amount.isNotBlank()) {
                                showError = false
                                isLoading = true
                            } else {
                                showError = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = RoundedCornerShape(0.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF7C02D))
                    ) {
                        Text(
                            text = "إرسال",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color.White)
            ) {
                // Tabs Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF00BCD4)),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    val tabs = listOf(
                        "رقم الهاتف" to Icons.Outlined.Smartphone,
                        "اسم المستخدم" to Icons.Outlined.AccountCircle,
                        "بنك" to Icons.Filled.AccountBalance,
                        "فيسبوك" to null
                    )
                    
                    tabs.forEachIndexed { index, (title, icon) ->
                        val isSelected = selectedTab == index
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedTab = index }
                                .padding(vertical = 12.dp)
                        ) {
                            if (index == 3) {
                                // Custom 'f' icon for Facebook
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(Color.White, shape = androidx.compose.foundation.shape.CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("f", color = Color(0xFF00BCD4), fontWeight = FontWeight.Bold, fontSize = 24.sp)
                                }
                            } else if (icon != null) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = title,
                                    tint = if (isSelected) Color.White else Color(0x99FFFFFF),
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = title,
                                color = if (isSelected) Color.White else Color(0x80FFFFFF),
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .width(40.dp)
                                        .height(3.dp)
                                        .background(Color.White)
                                )
                            } else {
                                Spacer(modifier = Modifier.height(3.dp))
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                    // Phone Number Field
                    Text(
                        text = "رقم الهاتف",
                        fontSize = 16.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BasicTextField(
                            value = phoneNumber,
                            onValueChange = { phoneNumber = it },
                            textStyle = TextStyle(fontSize = 18.sp, color = Color.Black),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 8.dp),
                            singleLine = true,
                            decorationBox = { innerTextField ->
                                if (phoneNumber.isEmpty()) {
                                    Text(
                                        text = "أدخل رقم الهاتف",
                                        color = Color.LightGray,
                                        fontSize = 18.sp
                                    )
                                }
                                innerTextField()
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Outlined.PermContactCalendar,
                            contentDescription = "Contacts",
                            tint = Color(0xFFF7C02D),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    HorizontalDivider(color = Color.Black, thickness = 1.dp)
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Amount Field
                    Text(
                        text = "المبلغ",
                        fontSize = 16.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    BasicTextField(
                        value = amount,
                        onValueChange = { amount = it },
                        textStyle = TextStyle(fontSize = 18.sp, color = Color.Black),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            if (amount.isEmpty()) {
                                Text(
                                    text = "أدخل المبلغ",
                                    color = Color.LightGray,
                                    fontSize = 18.sp
                                )
                            }

                        innerTextField()
                    }
                )
                HorizontalDivider(color = Color.Black, thickness = 1.dp)
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Note Field
                Text(
                    text = "ملاحظة (اختياري)",
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                BasicTextField(
                    value = note,
                    onValueChange = { note = it },
                    textStyle = TextStyle(fontSize = 18.sp, color = Color.Black),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        if (note.isEmpty()) {
                            Text(
                                text = "أدخل ملاحظة",
                                color = Color.LightGray,
                                fontSize = 18.sp
                            )
                        }
                        innerTextField()
                    }
                )
                HorizontalDivider(color = Color.Black, thickness = 1.dp)
            }
        }
        } // Close Scaffold
        
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x80000000)),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.CircularProgressIndicator(
                    color = Color(0xFF00AEEF),
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmTransferScreen(
    phoneNumber: String,
    amount: String,
    onBackClick: () -> Unit = {},
    onCompleteClick: () -> Unit = {}
) {
    var showPinDialog by remember { mutableStateOf(false) }
    var pin by remember { mutableStateOf("") }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var transactionId by remember { mutableStateOf("") }
    var transactionDateTime by remember { mutableStateOf("") }
    var isConfirmLoading by remember { mutableStateOf(false) }
    val context = androidx.compose.ui.platform.LocalContext.current

    if (isConfirmLoading) {
        androidx.compose.runtime.LaunchedEffect(Unit) {
            kotlinx.coroutines.delay(3000)
            isConfirmLoading = false
            showSuccessDialog = true
            showTransferNotification(context, amount, phoneNumber)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "تأكيد تحويل الأموال",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF00BCD4)
                )
            )
        },
        bottomBar = {
            Button(
                onClick = { showPinDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(0.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF7C02D))
            ) {
                Text(
                    text = "أتمم",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF5F5F5))
        ) {
            Text(
                text = "مراجعة تفاصيل التحويل",
                fontSize = 16.sp,
                color = Color.DarkGray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textAlign = TextAlign.Start
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(Color(0xFF5A6B7A), shape = androidx.compose.foundation.shape.CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Smartphone,
                        contentDescription = "Phone",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = phoneNumber,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .background(Color(0xFFEEEEEE), RoundedCornerShape(8.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "المبلغ",
                        fontSize = 14.sp,
                        color = Color.DarkGray,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "MRU $amount",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = Color.White, thickness = 2.dp)
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "تفاصيل تكلفة الخدمة",
                        fontSize = 14.sp,
                        color = Color.DarkGray,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    CostDetailRow(label = "تكلفة الخدمة", value = "MRU 0")
                    Spacer(modifier = Modifier.height(4.dp))
                    CostDetailRow(label = "العمولة", value = "MRU 0")
                    Spacer(modifier = Modifier.height(4.dp))
                    CostDetailRow(label = "الضريبة", value = "MRU 0")
                }
            }
        }
    }

    if (showPinDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showPinDialog = false },
            properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
        ) {
            androidx.compose.material3.Card(
                modifier = Modifier.width(300.dp),
                shape = RoundedCornerShape(8.dp),
                colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
                        IconButton(onClick = { showPinDialog = false }) {
                            Icon(imageVector = Icons.Filled.Close, contentDescription = "Close", tint = Color.Gray)
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "الرجاء إدخال الرمز السري",
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BasicTextField(
                            value = pin,
                        onValueChange = { if(it.length <= 4 && it.all { char -> char.isDigit() }) pin = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        decorationBox = { innerTextField ->
                            Box {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                ) {
                                    repeat(4) { index ->
                                        Box(
                                            modifier = Modifier
                                                .width(40.dp)
                                                .height(36.dp),
                                            contentAlignment = Alignment.BottomCenter
                                        ) {
                                            if (index < pin.length) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(12.dp)
                                                        .background(Color.Black, shape = androidx.compose.foundation.shape.CircleShape)
                                                        .align(Alignment.Center)
                                                        .padding(bottom = 4.dp)
                                                )
                                            }
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(2.dp)
                                                    .background(Color.Black)
                                            )
                                        }
                                    }
                                }
                                Box(modifier = Modifier.matchParentSize().alpha(0.01f)) {
                                    innerTextField()
                                }
                            }
                        },
                        textStyle = TextStyle(color = Color.Transparent)
                    )
                    }
                    
                    Spacer(modifier = Modifier.height(48.dp))
                    
                    Button(
                        onClick = {
                            if (pin.length == 4) {
                                if (pin == AuthSession.savedPin || AuthSession.savedPin.isEmpty()) {
                                    showPinDialog = false
                                    transactionId = (1..19).map { (0..9).random() }.joinToString("")
                                    val sdf = java.text.SimpleDateFormat("HH:mm:ss yy-MM-dd", java.util.Locale.US)
                                    sdf.timeZone = java.util.TimeZone.getTimeZone("Africa/Nouakchott")
                                    transactionDateTime = sdf.format(java.util.Date())
                                    
                                    val dateSdf = java.text.SimpleDateFormat("dd-MM-yy", java.util.Locale.US)
                                    dateSdf.timeZone = java.util.TimeZone.getTimeZone("Africa/Nouakchott")
                                    val notifDate = dateSdf.format(java.util.Date())
                                    NotificationsManager.add(NotificationItem(
                                        id = transactionId,
                                        type = "تحويل أموال",
                                        phone = phoneNumber,
                                        amount = amount,
                                        date = notifDate
                                    ))
                                    
                                    val transDateSdf = java.text.SimpleDateFormat("HH:mm:ss dd-MM-yy", java.util.Locale.US)
                                    transDateSdf.timeZone = java.util.TimeZone.getTimeZone("Africa/Nouakchott")
                                    val transDate = transDateSdf.format(java.util.Date())
                                    TransactionsManager.add(TransactionItem(
                                        id = transactionId,
                                        type = "Dr",
                                        description = "BKL-Tranfsert Argent Client",
                                        date = transDate,
                                        details = phoneNumber,
                                        amount = "$amount MRU"
                                    ))
                                    
                                    val transferAmount = amount.toDoubleOrNull() ?: 0.0
                                    AuthSession.userBalance -= transferAmount
                                    
                                    isConfirmLoading = true
                                } else {
                                    android.widget.Toast.makeText(context, "الرمز السري غير صحيح", android.widget.Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp, topStart = 0.dp, topEnd = 0.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BCD4))
                    ) {
                        Text(
                            text = "ارسل الطلب",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }

    if (showSuccessDialog) {
        androidx.compose.ui.window.Dialog(
            onDismissRequest = { },
            properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.Card(
                    modifier = Modifier.fillMaxWidth(0.9f),
                    shape = RoundedCornerShape(8.dp),
                    colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "النقل ناجح!",
                            fontSize = 18.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Icon(
                            imageVector = Icons.Outlined.CheckCircle,
                            contentDescription = "Success",
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(100.dp)
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = "المستفيد: $phoneNumber المبلغ المرسل: $amount MRU",
                            fontSize = 16.sp,
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = "معرف المعاملة : $transactionId",
                            fontSize = 14.sp,
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        Text(
                            text = "التاريخ والوقت : \u200E$transactionDateTime\u200E",
                            fontSize = 14.sp,
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                            style = androidx.compose.ui.text.TextStyle(
                                localeList = androidx.compose.ui.text.intl.LocaleList("en-US")
                            ),
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        var isFavorite by remember { mutableStateOf(false) }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {
                                isFavorite = true
                                FavoritesManager.add(FavoriteItem(
                                    id = transactionId,
                                    type = "إرسال الأموال",
                                    phone = phoneNumber,
                                    amount = amount
                                ))
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "Favorite",
                                tint = if (isFavorite) Color(0xFFF7C02D) else Color.Gray,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "إضافة إلى المفضلة",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Button(
                            onClick = {
                                showSuccessDialog = false
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp, topStart = 0.dp, topEnd = 0.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BCD4))
                        ) {
                            Text(
                                text = "تم",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
    
    if (isConfirmLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x80000000)),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.material3.CircularProgressIndicator(
                color = Color(0xFF00AEEF),
                modifier = Modifier.size(32.dp)
            )
        }
    }
    }
}

@Composable
fun CostDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Black
        )
        Text(
            text = "-",
            fontSize = 14.sp,
            color = Color.Black
        )
        Text(
            text = value,
            fontSize = 14.sp,
            color = Color.Black
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpScreen(onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "المساعدة",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFF00BCD4))
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF5F5F5))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "كيف يمكننا مساعدتك؟",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            val context = androidx.compose.ui.platform.LocalContext.current
            Button(
                onClick = {
                    val intent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        setPackage("org.telegram.messenger")
                        putExtra(android.content.Intent.EXTRA_TEXT, "بادر بتنزيل تطبيق Bankily لتتمتع بتجربة مصرفية سلسة ومريحة!")
                    }
                    try {
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        val fallbackIntent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(android.content.Intent.EXTRA_TEXT, "بادر بتنزيل تطبيق Bankily لتتمتع بتجربة مصرفية سلسة ومريحة!")
                        }
                        context.startActivity(android.content.Intent.createChooser(fallbackIntent, "مشاركة عبر"))
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0088CC))
            ) {
                Icon(
                    imageVector = Icons.Filled.Share,
                    contentDescription = "Share to Telegram",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "شارك التطبيق على تلجرام",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAccountScreen(onBackClick: () -> Unit = {}) {
    var showTransactions by remember { mutableStateOf(false) }
    var showFromDatePicker by remember { mutableStateOf(false) }
    var showToDatePicker by remember { mutableStateOf(false) }
    
    val dateFormatter = remember { java.text.SimpleDateFormat("MMM yyyy dd", java.util.Locale.US).apply { timeZone = java.util.TimeZone.getTimeZone("Africa/Nouakchott") } }
    
    var fromDate by remember { mutableStateOf("May 2026 07") }
    var toDate by remember { mutableStateOf(dateFormatter.format(java.util.Date())) }

    val fromDatePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis() - 86400000L * 30
    )
    val toDatePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis()
    )

    if (showFromDatePicker) {
        androidx.compose.material3.DatePickerDialog(
            onDismissRequest = { showFromDatePicker = false },
            colors = androidx.compose.material3.DatePickerDefaults.colors(containerColor = Color.White),
            confirmButton = {
                TextButton(onClick = { 
                    fromDatePickerState.selectedDateMillis?.let { 
                        fromDate = dateFormatter.format(java.util.Date(it)) 
                    }
                    showFromDatePicker = false 
                }) {
                    Text("موافق")
                }
            },
            dismissButton = {
                TextButton(onClick = { showFromDatePicker = false }) {
                    Text("إلغاء")
                }
            }
        ) {
            val datePickerColors = androidx.compose.material3.DatePickerDefaults.colors(
                containerColor = Color.White,
                titleContentColor = Color.Black,
                headlineContentColor = Color.Black,
                weekdayContentColor = Color.Black,
                dayContentColor = Color.Black,
                selectedDayContentColor = Color.White,
                selectedDayContainerColor = Color(0xFFFF5722),
                todayContentColor = Color(0xFFFF5722),
                todayDateBorderColor = Color(0xFFFF5722)
            )
            DatePicker(state = fromDatePickerState, colors = datePickerColors)
        }
    }

    if (showToDatePicker) {
        androidx.compose.material3.DatePickerDialog(
            onDismissRequest = { showToDatePicker = false },
            colors = androidx.compose.material3.DatePickerDefaults.colors(containerColor = Color.White),
            confirmButton = {
                TextButton(onClick = { 
                    toDatePickerState.selectedDateMillis?.let { 
                        toDate = dateFormatter.format(java.util.Date(it)) 
                    }
                    showToDatePicker = false 
                }) {
                    Text("موافق")
                }
            },
            dismissButton = {
                TextButton(onClick = { showToDatePicker = false }) {
                    Text("إلغاء")
                }
            }
        ) {
            val datePickerColors = androidx.compose.material3.DatePickerDefaults.colors(
                containerColor = Color.White,
                titleContentColor = Color.Black,
                headlineContentColor = Color.Black,
                weekdayContentColor = Color.Black,
                dayContentColor = Color.Black,
                selectedDayContentColor = Color.White,
                selectedDayContainerColor = Color(0xFFFF5722),
                todayContentColor = Color(0xFFFF5722),
                todayDateBorderColor = Color(0xFFFF5722)
            )
            DatePicker(state = toDatePickerState, colors = datePickerColors)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("حسابي", color = Color.White, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFF00BCD4))
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
        ) {
            // "البنك الشعبي الموريتاني" and account info
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color(0xFF00BCD4), shape = RoundedCornerShape(24.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("||", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.Start) {
                    Text("البنك الشعبي الموريتاني", fontSize = 16.sp, color = Color.Gray)
                    Text("00018001002100191000172", fontSize = 18.sp, color = Color.Black)
                }
            }

            // Balance section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
                    Text("الرصيد", fontSize = 16.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        String.format(java.util.Locale.US, "MRU %.2f", AuthSession.userBalance),
                        fontSize = 24.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Filtering row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Outlined.CalendarToday, contentDescription = "Calendar", tint = Color.Black, modifier = Modifier.size(28.dp))
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column(modifier = Modifier.weight(1f).clickable { showFromDatePicker = true }, horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("من عند", fontSize = 14.sp, color = Color.Black)
                    Text(fromDate, fontSize = 14.sp, color = Color.Black)
                    HorizontalDivider(modifier = Modifier.padding(top = 4.dp), color = Color.Black, thickness = 1.dp)
                }

                Spacer(modifier = Modifier.width(16.dp))
                Text("—", fontSize = 16.sp, color = Color.Black)
                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f).clickable { showToDatePicker = true }, horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("إلى", fontSize = 14.sp, color = Color.Black)
                    Text(toDate, fontSize = 14.sp, color = Color.Black)
                    HorizontalDivider(modifier = Modifier.padding(top = 4.dp), color = Color.Black, thickness = 1.dp)
                }

                Spacer(modifier = Modifier.width(24.dp))

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color(0xFFFF5722), shape = RoundedCornerShape(4.dp))
                        .clickable { showTransactions = true },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Filled.ArrowDownward, contentDescription = "Download", tint = Color.White)
                }
            }
            
            HorizontalDivider(modifier = Modifier.fillMaxWidth().padding(top = 16.dp), color = Color.LightGray)
            
            if (showTransactions) {
                val transactions = TransactionsManager.transactions
                androidx.compose.foundation.lazy.LazyColumn(
                    modifier = Modifier.fillMaxWidth().weight(1f)
                ) {
                    items(
                        count = transactions.size
                    ) { index ->
                        val t = transactions[index]
                        TransactionRow(
                            amount = t.amount,
                            type = t.type,
                            description = t.description,
                            date = t.date,
                            details = t.details,
                            transactionId = t.id
                        )
                        HorizontalDivider(color = Color.LightGray, thickness = 1.dp)
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionRow(amount: String, type: String, description: String, date: String, details: String, transactionId: String) {
    val isDebit = type == "Dr"
    val mainColor = if (isDebit) Color(0xFFE91E63) else Color(0xFF4CAF50)
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.Start) {
            Text(text = description, color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = details, color = Color.Black, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "معرف المعاملة: $transactionId", color = Color.Black, fontSize = 12.sp)
        }
        
        Column(horizontalAlignment = Alignment.End) {
            Row {
                Text(text = amount, color = mainColor, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = type, color = Color.Black, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = date, color = Color.Black, fontSize = 14.sp)
        }
    }
}

fun showTransferNotification(context: android.content.Context, amount: String, phoneNumber: String) {
    val channelId = "bankily_transfer_channel"
    val notificationManager = context.getSystemService(android.content.Context.NOTIFICATION_SERVICE) as android.app.NotificationManager

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        val channel = android.app.NotificationChannel(
            channelId,
            "Transfers",
            android.app.NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Notifications for money transfers"
        }
        notificationManager.createNotificationChannel(channel)
    }

    val bitmap = android.graphics.BitmapFactory.decodeResource(context.resources, R.drawable.bankily_logo_1781005126036)

    val notification = androidx.core.app.NotificationCompat.Builder(context, channelId)
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setLargeIcon(bitmap)
        .setContentTitle("تحويل أموال")
        .setContentText("تم إرسال مبلغ $amount MRU إلى الرقم $phoneNumber")
        .setPriority(androidx.core.app.NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)
        .build()

    if (androidx.core.content.ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) == android.content.pm.PackageManager.PERMISSION_GRANTED || android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.TIRAMISU) {
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}