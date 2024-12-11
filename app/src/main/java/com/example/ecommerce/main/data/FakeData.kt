package com.example.ecommerce.main.data

import com.example.core.api.model.Fulfillment
import com.example.core.api.model.Item
import com.example.core.api.model.Payment
import com.example.core.api.model.Product
import com.example.core.api.model.ProductDetail
import com.example.core.api.model.ProductVariant
import com.example.core.api.model.Transaction
import com.example.core.api.response.ReviewResponse
import com.example.core.room.cart.Cart
import com.example.core.room.cart.CartItem
import com.example.core.room.favorite.Favorite
import com.example.core.room.notification.Notification

val notifications = listOf(
    Notification(
        1,"Telkomsel Award 2023", "Nikmati Kemeriahan ulang tahun Telkomsel pada har jumat 21 Juli 2023 pukul 19.00 - 21.00 WIB langsung dari Beach City International Stadium dengan berbagai kemudahan untuk mendapatkan aksesnya.",
        "", "Promo","21 Jul 2023","12:34", true),
    Notification(
        2,"Telkomsel Award 2023", "Nikmati Kemeriahan ulang tahun Telkomsel pada har jumat 21 Juli 2023 pukul 19.00 - 21.00 WIB langsung dari Beach City International Stadium dengan berbagai kemudahan untuk mendapatkan aksesnya.",
        "", "Promo","21 Jul 2023","12:34", false),
    Notification(
        3, "Transaksi Berhasil","Selamat, transaksi dengan id 1234567 telah berhasil, barang yang anda beli akan segera di proses dan diantarkan ke tempat tujuan",
        "https://img.freepik.com/premium-vector/success-payment-icon-flat-style-approved-money-vector-illustration-isolated-background-successful-pay-sign-business-concept_157943-1354.jpg?w=512",
        "Info","22 Jan 2024", "13:40",true)
)

val paymentItem = listOf(
    Item("BCA Virtual Account","https://upload.wikimedia.org/wikipedia/commons/thumb/5/5c/Bank_Central_Asia.svg/2560px-Bank_Central_Asia.svg.png",
        true),
    Item("BNI Virtual Account","https://upload.wikimedia.org/wikipedia/id/thumb/5/55/BNI_logo.svg/1200px-BNI_logo.svg.png",true),
    Item("BRI Virtual Account","https://upload.wikimedia.org/wikipedia/commons/thumb/6/68/BANK_BRI_logo.svg/1200px-BANK_BRI_logo.svg.png",true),
    Item("Mandiri Virtual Account","https://upload.wikimedia.org/wikipedia/commons/thumb/a/ad/Bank_Mandiri_logo_2016.svg/2560px-Bank_Mandiri_logo_2016.svg.png",true)
)

val payments = Payment("Transfer Virtual Account", paymentItem)

val paymentItem2 = listOf(
    Item("Bank BCA","https://upload.wikimedia.org/wikipedia/commons/thumb/5/5c/Bank_Central_Asia.svg/2560px-Bank_Central_Asia.svg.png",false),
    Item("Bank BNI","https://upload.wikimedia.org/wikipedia/id/thumb/5/55/BNI_logo.svg/1200px-BNI_logo.svg.png",true),
    Item("Bank BRI","https://upload.wikimedia.org/wikipedia/commons/thumb/6/68/BANK_BRI_logo.svg/1200px-BANK_BRI_logo.svg.png",true),
    Item("Bank Mandiri","https://upload.wikimedia.org/wikipedia/commons/thumb/a/ad/Bank_Mandiri_logo_2016.svg/2560px-Bank_Mandiri_logo_2016.svg.png",true)
)

val payments2 = Payment("Transfer Bank", paymentItem2)

val paymentItem3 = listOf(
    Item("GoPay","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT2qhfoPfIl44pJzc9K_R-Q8oE0zltLmtCum53K-yZkf23NsMmf-VyQU1-qx3iu129rpfI&usqp=CAU",true),
    Item("OVO","https://theme.zdassets.com/theme_assets/1379487/2cb35fe96fa1191f49c2b769b50cf8b546fff65e.png",true),
)

val payments3 = Payment("Pembayaran Instan", paymentItem3)

val mockPayments = listOf(payments,payments2,payments3)

val itemPayment = Item("Bank BCA","https://upload.wikimedia.org/wikipedia/commons/thumb/5/5c/Bank_Central_Asia.svg/2560px-Bank_Central_Asia.svg.png",false)

val mockProducts = listOf(
    Product(
        productId = "1",
        productName = "Macbook Air M1 2020 Chip 13 inch 512GB Touch ID Grey Silver Gold - INTER, 256GB GREY",
        productPrice = 12285000,
        image = "https://images.tokopedia.net/img/cache/900/VqbcmM/2022/9/12/d1b5870e-f900-4387-bddf-20ddb3e16a6f.jpg",
        brand = "Apple",
        store = "AppleStore",
        sale = 2000,
        productRating = 5.0f
    ),
    Product(
        productId = "2",
        productName = "MacBook Air M2 2022 Chip 13\" Inch 512GB 256GB RAM 8GB Apple IBOX - INTER, 256GB GREY",
        productPrice = 17000000,
        image = "https://images.tokopedia.net/img/cache/900/VqbcmM/2022/9/12/fe20fea9-a892-45fe-b536-cd409f4b75c2.jpg",
        brand = "Apple",
        store = "AppleStore",
        sale = 1000,
        productRating = 4.9f
    ),
    Product(
        productId = "3",
        productName = "ASUS ROG Strix G17 G713RM-R736H6G-O - Eclipse Gray",
        productPrice = 24499000,
        image = "https://images.tokopedia.net/img/cache/900/VqbcmM/2022/4/6/0a49c399-cf6b-47f5-91c9-8cbd0b86462d.jpg",
        brand = "Asus",
        store = "AsusStore",
        sale = 12,
        productRating = 5.0f
    ),
    Product(
        productId = "4",
        productName = "ASUS TUF GAMING F15 FX506HC-I535B6T-O11 - Graphite Black",
        productPrice = 12499000,
        image = "https://images.tokopedia.net/img/cache/900/VqbcmM/2022/3/12/2d39f498-a3f2-4a75-935a-0ab20fb38361.jpg",
        brand = "Asus",
        store = "AsusStore",
        sale = 12,
        productRating = 5.0f
    ),
    Product(
        productId = "5",
        productName = "Dell Gaming G15 Ryzen 7 5800 16GB 512ssd RTX3050Ti 4GB W10 15.6FHD - PROMO",
        productPrice = 13849000,
        image = "https://images.tokopedia.net/img/cache/900/VqbcmM/2022/12/22/4f9c0f65-31dd-4fe4-9212-1e936bbe63f0.jpg",
        brand = "Dell",
        store = "DellStore",
        sale = 13,
        productRating = 5.0f
    ),
    Product(
        productId = "6",
        productName = "DELL XPS 13 PLUS 9320 OLED 4K TOUCH I7 1260P 16GB 1TBSSD W11 13.4 - PLATINUM STD",
        productPrice = 20999000,
        image = "https://images.tokopedia.net/img/cache/900/VqbcmM/2022/7/21/42244d54-0e70-4020-a286-a304996968f4.jpg",
        brand = "Dell",
        store = "DellStore",
        sale = 15,
        productRating = 5.0f
    ),
    Product(
        productId = "7",
        productName = "Lenovo Ideapad Slim 3i 14 i3 1115G4 8GB 256SSD W11+OHS 14.0 2YR F3ID - ABYSS BLUE, SSD 256GB",
        productPrice = 6049000,
        image = "https://images.tokopedia.net/img/cache/900/VqbcmM/2023/5/3/c5d545c5-c88d-4447-a6cb-8d6ee95ad975.png",
        brand = "Lenovo",
        store = "LenovoStore",
        sale = 15,
        productRating = 5.0f
    ),
    Product(
        productId = "8",
        productName = "Lenovo Ideapad Gaming 3i 2022 i5 12500 8GB 512SSD RTX3050 4GB 15.6FHD - 8GB/512SSD",
        productPrice = 14999000,
        image = "https://images.tokopedia.net/img/cache/900/VqbcmM/2023/5/17/dbd265f8-d610-4650-9357-47aa84c13be0.jpg",
        brand = "Lenovo",
        store = "LenovoStore",
        sale = 15,
        productRating = 5.0f
    )
)

val mockCarts = listOf(
    Cart("1",
        "Lenovo Ideapad Slim 3i 14 i3 1115G4 8GB 256SSD W11+OHS 14.0 2YR F3ID - ABYSS BLUE, SSD 256GB",
        6049000,
        "https://images.tokopedia.net/img/cache/900/VqbcmM/2023/5/3/c5d545c5-c88d-4447-a6cb-8d6ee95ad975.png",
        "Lenovo","",
        "LenovoStore",
        90,
        3,
        5,
        15,
        98,
        5.0f,
        "RAM 32GB",
        1000000,
        2,
        true),
    Cart("2",
        "Lenovo Ideapad Slim 5i i5 1135G7 8GB 512ssd MX450 2GB W10+OHS 14.0FHD - GRAPHITE GREY",
        11799000,
        "",
        "Lenovo","",
        "LenovoStore",
        90,
        3,
        5,
        15,
        98,
        5.0f,
        "RAM 16GB",
        0,
        2,
        true),
    Cart("3",
        "Macbook Pro M1 2020 8GB 256GB SSD MYD82 MYDA2 Gray Silver Resmi - CPO, SPACE GREY",
        14640000,
        "https://images.tokopedia.net/img/cache/900/VqbcmM/2022/9/13/16a686a4-bf70-4abe-9f3d-78efcd4eb6fe.jpg",
        "Apple","",
        "AppleStore",
        40,
        12,
        5,
        45,
        68,
        4.0f,
        "RAM 16GB",
        0,
        5,
        false),
)

val mockFavorites = listOf(
    Favorite("1",
        "Lenovo Ideapad Slim 3i 14 i3 1115G4 8GB 256SSD W11+OHS 14.0 2YR F3ID - ABYSS BLUE, SSD 256GB",
        6049000,
        "https://images.tokopedia.net/img/cache/900/VqbcmM/2023/5/3/c5d545c5-c88d-4447-a6cb-8d6ee95ad975.png",
        "Lenovo","",
        "LenovoStore",13,
        8,5,11,11,
        5.0f,"RAM 16GB",
        0,2,false),
    Favorite("2",
        "Macbook Pro M1 2020 8GB 256GB SSD MYD82 MYDA2 Gray Silver Resmi - CPO, SPACE GREY",
        14640000,
        "https://images.tokopedia.net/img/cache/900/VqbcmM/2022/9/13/16a686a4-bf70-4abe-9f3d-78efcd4eb6fe.jpg",
        "Apple","","AppleStore",13,
        8,5,14,15,
        3.0f,"RAM 32GB",
        1000000,2,false),
    Favorite("3",
        "Dell Gaming G15 Ryzen 7 5800 16GB 512ssd RTX3050Ti 4GB W10 15.6FHD - PROMO",
        13849000,
        "","Dell","","DellStore",13,
        8,5,14,15,
        3.0f,"RAM 32GB",
        1000000,2,false),
)

val fulfillment = Fulfillment("1",true,"23 November 2023","11:30","BCA",25000000)

val mockReviews = listOf(
    ReviewResponse.Review(
        userName = "John",
        userImage = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTQM4VpzpVw8mR2j9_gDajEthwY3KCOWJ1tOhcv47-H9o1a-s9GRPxdb_6G9YZdGfv0HIg&usqp=CAU",
        userRating = 4,
        userReview = "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
    ),
    ReviewResponse.Review(
        userName = "Doe",
        userImage = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTR3Z6PN8QNVhH0e7rEINu_XJS0qHIFpDT3nwF5WSkcYmr3znhY7LOTkc8puJ68Bts-TMc&usqp=CAU",
        userRating = 5,
        userReview = "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book."
    )
)

val cart = listOf(
    CartItem("1","Lenovo Ideapad Gaming 3i 2022 i5 12500 8GB 512SSD RTX3050 4GB 15.6FHD - 8GB/512SSD",3),
    CartItem("2","ASUS TUF GAMING F15 FX506HC-I535B6T-O11 - Graphite Black",3),
    CartItem("3","Dell Gaming G15 Ryzen 7 5800 16GB 512ssd RTX3050Ti 4GB W10 15.6FHD - PROMO",1)
)
val mockTransactions = listOf(
    Transaction("1",true,"12 November 2023",
        "11:30","BCA", 12000000 ,cart,
        "5","Really Good","","Lenovo 3"),
    Transaction("2",false,"13 November 2023",
        "15:30","BRI", 14000000 ,cart,
        "2","Really Good","","Lenovo 4"),
    Transaction("3",true,"14 November 2023",
        "15:30","BNI", 14000000 ,cart,
        "2","Really Good","","Lenovo 5")
)


val mockDetailProduct = ProductDetail(
    productId = "1",
    productName = "Lenovo Legion Pro 7i RTX4090 i9 13900 32GB 2TBSSD W11+OHS 240Hz 35ID",
    productPrice = 65999000,
    image = listOf(
        "https://images.tokopedia.net/img/cache/900/VqbcmM/2023/5/22/6cc80961-c431-40ed-b31e-c3b62b91f8c7.jpg",
        "https://images.tokopedia.net/img/cache/900/VqbcmM/2023/3/29/214e2a65-4576-42e3-b62c-5309044c0bc2.png",
        "https://images.tokopedia.net/img/cache/900/VqbcmM/2023/3/29/a0c45e17-cc73-4fe1-9162-43a29ecaf240.png"
    ),
    brand = "Lenovo",
    description = "Deskripsi Lenovo Legion Pro 7i 16IRX8H RTX4090 16GB/ i9 13900Hx 32GB 2TBSSD W11+OHS 16.0 WQXGA 240Hz 100SRGB 3YR+3ADP GRY\n\n" +
            "Spesifikasi unit:\n" +
            "Processor: Intel Core i9-13900HX, 24C (8P + 16E) / 32T, P-core up to 5.4GHz, E-core up to 3.9GHz, 36MB\n" +
            "RAM: 32GB (2x 16GB SO-DIMM DDR5-5600)\n" +
            "Storage: 2x 1TB SSD M.2 2280 PCIe 4.0x4 NVMe\n" +
            "VGA: NVIDIA GeForce RTX 4090 16GB GDDR6, Boost Clock 2040MHz, TGP 175W\n" +
            "Display: 16\" WQXGA (2560x1600) IPS 500nits Anti-glare, 100% sRGB, 240Hz, DisplayHDR 400, Dolby Vision, G-SYNC, Low Blue Light, High Gaming Performance\n" +
            "Sistem Operasi: Windows 11 Home 64bit + Microsoft Office Home & Student 2021\n" +
            "Warna: Onyx Grey\n" +
            "Kamera: FHD 1080p with E-shutter\n" +
            "Audio: Stereo speakers (super linear speaker), 2W x2, audio by HARMAN certification, optimized with Nahimic Audio, Smart Amplifier (AMP)",
    store = "LenovoStore",
    sale = 3,
    stock = 19,
    totalRating = 2,
    totalReview = 1,
    totalSatisfaction = 100,
    productRating = 5.0f,
    productVariant = listOf(
        ProductVariant("RAM 16GB", 0),
        ProductVariant("RAM 32GB", 1000000)
    )
)