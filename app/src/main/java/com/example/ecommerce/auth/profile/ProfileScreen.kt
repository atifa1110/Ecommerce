package com.example.ecommerce.auth.profile

import android.content.Context
import android.content.res.Configuration
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PermIdentity
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.core.api.response.BaseResponse
import com.example.ecommerce.R
import com.example.ecommerce.auth.login.TextTermCondition
import com.example.ecommerce.component.ProgressDialog
import com.example.ecommerce.component.ToastMessage
import com.example.ecommerce.ui.theme.EcommerceTheme
import com.example.ecommerce.ui.theme.Purple
import java.io.File
import java.util.Objects

@Composable
fun ProfileScreen(
    onNavigateToHome: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var isDialog by remember { mutableStateOf(false) }

    if (isDialog) ProgressDialog().ProgressDialog()

    var openDialog by remember { mutableStateOf(false) }
    val username = remember { mutableStateOf("") }

    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val launcherImage = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        context.packageName + ".provider",
        file
    )

    var capturedImageUri by remember { mutableStateOf<Uri?>(Uri.EMPTY) }

    val launcherCamera = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) {
        capturedImageUri = uri
        Log.d("CapturedImage", capturedImageUri.toString())
    }

    val profileViewModel: ProfileViewModel = hiltViewModel()
    profileViewModel.profileResult.observe(lifecycleOwner) {
        when (it) {
            is BaseResponse.Loading -> {
                isDialog = true
            }

            is BaseResponse.Success -> {
                isDialog = false
                profileViewModel.saveProfileName(it.data?.data?.userName?:"")
                onNavigateToHome()
            }

            is BaseResponse.Error -> {
                isDialog = false
                ToastMessage().showMsg(context, it.msg.toString())
            }

            else -> {}
        }
    }

    ProfileContent(
        openDialog = openDialog,
        username = username,
        imageUri = imageUri,
        capturedImageUri = capturedImageUri,
        getProfileUser = {
            profileViewModel.getProfileUser(file, username.value)
        },
        buttonAnalytics = {
            profileViewModel.buttonAnalytics("Profile")
        },
        launchCamera = { launcherCamera.launch(uri) },
        launchGallery = { launcherImage.launch("image/*") }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
    openDialog :Boolean,
    username: MutableState<String>,
    imageUri : Uri?,
    capturedImageUri: Uri?,
    getProfileUser: () -> Unit,
    buttonAnalytics: () -> Unit,
    launchCamera: () -> Unit,
    launchGallery: () -> Unit
){
    var openDialog by remember { mutableStateOf(openDialog) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            stringResource(id = R.string.profile),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.surface
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.background)
                )
                Divider()
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Circle Image
            Column(modifier = Modifier
                .padding(24.dp)
                .clickable { openDialog = true }
                .size(128.dp)
                .clip(CircleShape)
                .background(Purple),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (imageUri != null) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.FillWidth,
                        model = imageUri,
                        contentDescription = "Image Uri"
                    )
                } else if (capturedImageUri?.path?.isNotEmpty() == true) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.FillWidth,
                        model = capturedImageUri,
                        contentDescription = "Captured Image Uri"
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.PermIdentity,
                        tint = Color.White,
                        contentDescription = stringResource(R.string.icon_profile),
                    )
                }
            }

            // text field name
            TextField(label = R.string.name, input = username)

            // button done
            Button(
                onClick = {
                    keyboardController?.hide()
                    getProfileUser()
                    buttonAnalytics()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Purple,
                    contentColor = Color.White
                ),
                enabled = username.value.isNotEmpty()
            ) {
                Text(
                    text = stringResource(id = R.string.done),
                    style = MaterialTheme.typography.labelLarge
                )
            }

            TextTermCondition(false)

            // if dialog is open than will open alert dialog
            if (openDialog) {
                BasicAlertDialog(onDismissRequest = { openDialog = false }) {
                    Column(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(8.dp)
                    ) {
                        Text(
                            modifier = Modifier.padding(
                                top = 16.dp,
                                bottom = 16.dp,
                                start = 12.dp
                            ),
                            fontSize = 24.sp,
                            text = stringResource(id = R.string.choose_picture),
                            fontWeight = FontWeight.W400
                        )

                        TextButton(
                            onClick = {
                                launchCamera()
                                openDialog = false
                            }
                        ) {
                            Text(
                                stringResource(id = R.string.kamera),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W400,
                                color = MaterialTheme.colorScheme.surface
                            )
                        }

                        TextButton(
                            onClick = {
                                launchGallery()
                                openDialog = false
                            }
                        ) {
                            Text(
                                stringResource(id = R.string.galeri),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W400,
                                color = MaterialTheme.colorScheme.surface
                            )
                        }
                    }
                }
            }
        }
    }
}

fun Context.createImageFile(): File {
    // create an image file name
    val imageFileName = "profile-${System.currentTimeMillis()}"
    return File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        externalCacheDir /* directory */
    )
}

@Composable
fun TextField(
    label: Int,
    input: MutableState<String>,
    onImeAction: () -> Unit = {}
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = input.value,
        onValueChange = {
            input.value = it
        },
        label = {
            Text(
                text = stringResource(id = label),
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        textStyle = MaterialTheme.typography.bodyMedium,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onImeAction()
            }
        ),
    )
}

@Preview("Light Mode", device = Devices.PIXEL_3)
@Preview("Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable 
fun ProfilePreview() {
    val username = rememberSaveable { mutableStateOf("Atifa") }

    EcommerceTheme {
        ProfileContent(
            openDialog = false,
            username = username,
            imageUri = null,
            capturedImageUri = null,
            getProfileUser = {},
            buttonAnalytics = {},
            launchCamera = {},
            launchGallery = {}
        )
    }
}
