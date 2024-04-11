package com.stylianosgakis.nav.typed.list.repro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.CollectionNavType
import androidx.navigation.ExperimentalSafeArgsApi
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.stylianosgakis.nav.typed.list.repro.ui.theme.NavtypedlistreproTheme
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    enableEdgeToEdge()
    super.onCreate(savedInstanceState)
    setContent {
      NavtypedlistreproTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
          App(modifier = Modifier.padding(innerPadding))
        }
      }
    }
  }
}

@OptIn(ExperimentalSafeArgsApi::class)
@Composable
fun App(modifier: Modifier = Modifier) {
  val navController = rememberNavController()
  NavHost(
    navController = navController,
    startDestination = StartDestination::class,
    route = Root::class,
    modifier = modifier,
  ) {
    composable<StartDestination> {
      TextButton(
        onClick = { navController.navigate(AddFilesDestination.WithSomeDefaultValues) },
      ) {
        Text(text = it.toRoute<StartDestination>().toString())
      }
    }
    composable<AddFilesDestination>(
      typeMap = mapOf(typeOf<List<String>>() to StringListType)
    ) {
      Text(text = it.toRoute<AddFilesDestination>().toString())
    }
  }
}

@Serializable
data object Root

@Serializable
data object StartDestination

@Serializable
data class AddFilesDestination(
  val targetUploadUrl: String,
  val initialFilesUri: List<String>,
) {
  companion object {
    val WithSomeDefaultValues = AddFilesDestination(
      targetUploadUrl = "targetUploadUrl",
      initialFilesUri = List(5) { it.toString().repeat(it) }
    )
  }
}


private val StringListType: NavType<List<String>> = object : CollectionNavType<List<String>>(false) {
  override fun put(bundle: Bundle, key: String, value: List<String>) {
    bundle.putStringArrayList(key, ArrayList(value))
  }

  override fun get(bundle: Bundle, key: String): List<String>? {
    return bundle.getStringArrayList(key)
  }

  override fun parseValue(value: String): List<String> {
    return listOf(value)
  }

  override fun parseValue(value: String, previousValue: List<String>): List<String> {
    return previousValue + value
  }

  override fun valueEquals(value: List<String>, other: List<String>): Boolean {
    return value == other
  }

  override fun serializeAsValues(value: List<String>): List<String> {
    return value
  }
}