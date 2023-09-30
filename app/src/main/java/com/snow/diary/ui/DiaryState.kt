package com.snow.diary.ui

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.snow.diary.core.domain.action.dream.AllDreams
import com.snow.diary.core.domain.action.location.AllLocations
import com.snow.diary.core.domain.action.person.AllPersons
import com.snow.diary.core.domain.action.preferences.GetPreferences
import com.snow.diary.feature.dreams.nav.goToDreamList
import com.snow.diary.feature.locations.nav.goToLocationList
import com.snow.diary.feature.persons.nav.goToPersonList
import com.snow.diary.feature.statistics.nav.goToStatistics
import com.snow.diary.nav.TopLevelDestinations
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.oneui.compose.dialog.FullscreenDialogLayout
import org.oneui.compose.layout.internal.SlidingDrawerState
import org.oneui.compose.layout.internal.rememberSlidingDrawerState

data class DiaryState(

    val navController: NavHostController,

    val drawerState: SlidingDrawerState,

    val scope: CoroutineScope,

    val screenSizeClass: WindowSizeClass,

    val getPreferences: GetPreferences,

    val allDreams: AllDreams,

    val allPersons: AllPersons,

    val allLocations: AllLocations

) {

    var currentNavDest by mutableStateOf(TopLevelDestinations.Dreams)

    val fullscreenDialogLayout = FullscreenDialogLayout.fromSizeClass(screenSizeClass)
    val fullscreenDialogFloating = fullscreenDialogLayout == FullscreenDialogLayout.Floating

    fun navigateTo(navDest: TopLevelDestinations) {
        when (navDest) {
            TopLevelDestinations.Dreams -> navController.goToDreamList()
            TopLevelDestinations.Persons -> navController.goToPersonList()
            TopLevelDestinations.Locations -> navController.goToLocationList()
            TopLevelDestinations.Statistics -> navController.goToStatistics()
        }
        currentNavDest = navDest
        closeDrawer()
    }

    val obfuscationEnabled = getPreferences(Unit)
        .map { it.obfuscationPreferences.obfuscationEnabled }
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val dreamsAmountState = allDreams(AllDreams.Input())
        .map { it.size }
        .stateIn(scope, SharingStarted.WhileSubscribed(5000), null)

    val personsAmountState = allPersons(AllPersons.Input())
        .map { it.size }
        .stateIn(scope, SharingStarted.WhileSubscribed(5000), null)

    val locationsAmountNum = allLocations(AllLocations.Input())
        .map { it.size }
        .stateIn(scope, SharingStarted.WhileSubscribed(5000), null)

    fun navigateBack() = navController.popBackStack()

    fun openDrawer() = scope.launch { drawerState.openAnimate() }

    fun closeDrawer() = scope.launch { drawerState.closeAnimate() }

}

@Composable
fun rememberDiaryState(
    navController: NavHostController = rememberNavController(),
    drawerState: SlidingDrawerState = rememberSlidingDrawerState(),
    scope: CoroutineScope = rememberCoroutineScope(),
    windowSizeClass: WindowSizeClass,
    getPreferences: GetPreferences,
    allDreams: AllDreams,
    allPersons: AllPersons,
    allLocations: AllLocations
) = DiaryState(
    navController,
    drawerState,
    scope,
    windowSizeClass,
    getPreferences,
    allDreams,
    allPersons,
    allLocations
)
