package com.example.ecommerce.auth.profile


import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Camera
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PermIdentity
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import coil.compose.rememberImagePainter
import com.example.ecommerce.R
import com.example.ecommerce.auth.login.TextSyaratKetentuan
import com.example.ecommerce.ui.theme.LightGray
import com.example.ecommerce.ui.theme.Purple
import com.example.ecommerce.ui.theme.textColor
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects
import kotlin.contracts.contract

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(){

    var openDialog by remember { mutableStateOf(false) }
    var name = remember { mutableStateOf("") }

    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    val launcherImage = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }

    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        context.packageName + ".provider", file
    )

    var capturedImageUri by remember { mutableStateOf<Uri?>(Uri.EMPTY) }

    val launcherCamera = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()) {
        capturedImageUri = uri
        Log.d("CapturedImage", capturedImageUri.toString())
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(modifier = Modifier
                .drawBehind {
                    val borderSize = 1.dp.toPx()
                    drawLine(
                        color = LightGray,
                        start = Offset(0f,size.height),
                        end = Offset(size.width,size.height),
                        strokeWidth = borderSize
                    )
                },
                title = {
                    Text(
                        stringResource(id = R.string.profile),
                        fontSize = 22.sp, color = textColor,
                        fontWeight = FontWeight.Normal)
                }
            )
        }
    ) {
        Column(modifier = Modifier.padding(it).padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            //Circle Image
            Column(modifier = Modifier.padding(24.dp).clickable { openDialog = true }
                .size(128.dp)
                .clip(CircleShape)
                .background(Purple),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (imageUri != null) {
                    Image(modifier = Modifier
                        .clip(CircleShape),
                        contentScale = ContentScale.FillWidth,
                        painter = rememberImagePainter(imageUri),
                        contentDescription = null
                    )
                } else if (capturedImageUri?.path?.isNotEmpty() == true) {
                    Image(modifier = Modifier
                                    .clip(CircleShape),
                        contentScale = ContentScale.FillWidth,
                        painter = rememberImagePainter(capturedImageUri),
                        contentDescription = null
                    )
                }else{
                    Icon(
                        imageVector = Icons.Default.PermIdentity,
                        tint = Color.White,
                        contentDescription = stringResource(R.string.icon_profile),
                    )
                }
            }

            //text field name
            TextField(label = R.string.name, input = name)

            //button done
            Button(onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(Purple),
                enabled =  true
            ) {
                Text(
                    text = stringResource(id = R.string.done),
                    fontWeight = FontWeight.W500
                )
            }

            TextSyaratKetentuan(false)

            //if dialog is open than will open alert dialog
            if(openDialog) {
                AlertDialog(onDismissRequest = { openDialog = false }) {
                    Column(modifier= Modifier.background(Color.White).padding(8.dp)) {
                        Text(modifier = Modifier.padding(top = 16.dp, bottom = 16.dp, start= 12.dp), text = "Pilih Gambar",fontSize = 24.sp, fontWeight = FontWeight.W400)

                        TextButton(
                            onClick = {
                                launcherCamera.launch(uri)
                            openDialog = false }
                        ) {
                            Text(stringResource(id = R.string.kamera),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W400)
                        }

                        TextButton(onClick = {
                            launcherImage.launch("image/*")
                            openDialog = false }
                        ) {
                            Text(stringResource(id = R.string.galeri),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W400)
                        }
                    }
                }
            }
        }
    }
}

private fun generateFilename() = "profile-${System.currentTimeMillis()}.jpg"

fun Context.createImageFile(): File {
    // Create an image file name
    //val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "profile-${System.currentTimeMillis()}"
    val image = File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        externalCacheDir /* directory */
    )
    return image
}
@Composable
fun TextField(
    label : Int,
    input : MutableState<String>,
    imeAction: ImeAction = ImeAction.Done,
    onImeAction: () -> Unit = {}
) {
    OutlinedTextField(
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
        modifier = Modifier.fillMaxWidth(),
        textStyle = MaterialTheme.typography.bodyMedium,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = imeAction,
            keyboardType = KeyboardType.Text
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onImeAction()
            }
        ),
    )
}

@Composable
@Preview(showBackground = true)
fun ProfilePreview(){

}