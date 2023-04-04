package com.dhana.githubmate.ui.activity

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import com.dhana.githubmate.R
import com.dhana.githubmate.ui.adapter.UserListAdapter
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {

    @Before
    fun setup() {
        Intents.init()
        ActivityScenario.launch(MainActivity::class.java)
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun searchUserTest() {
        Thread.sleep(3000)
        onView(withId(R.id.searchView)).check(matches(isDisplayed()))
        onView(withId(R.id.searchView)).perform(click())
        onView(withId(androidx.appcompat.R.id.search_src_text)).perform(typeText("dhana"), pressImeActionButton())
        Thread.sleep(2000)
        onView(withId(R.id.rvUser)).check(matches(isDisplayed()))
    }



    @Test
    fun clickUserTest() {
        Thread.sleep(3000)
        onView(withId(R.id.rvUser)).perform(
            actionOnItemAtPosition<UserListAdapter.ViewHolder>(
                0,
                click()
            )
        )
        onView(withId(R.id.tvUsername)).check(matches(isDisplayed()))
    }

    @Test
    fun clickFavoriteOptionsMenuTest() {
        Thread.sleep(3000)
        onView(withId(R.id.options)).check(matches(isDisplayed()))
        onView(withId(R.id.options)).perform(click())
        onView(withText(R.string.favorite_activity)).perform(click())
        intended(hasComponent(FavoriteActivity::class.java.name))
    }

    @Test
    fun clickSettingOptionsMenuTest() {
        Thread.sleep(3000)
        onView(withId(R.id.options)).check(matches(isDisplayed()))
        onView(withId(R.id.options)).perform(click())
        onView(withText(R.string.setting_activity)).perform(click())
        intended(hasComponent(SettingActivity::class.java.name))
    }
}