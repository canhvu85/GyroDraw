package ch.epfl.sweng.SDP;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AccountTestHelperActivityTest {

    @Rule
    public final ActivityTestRule<AccountTestHelperActivity> accountFunctionsActivityRule =
            new ActivityTestRule<>(AccountTestHelperActivity.class);

    @Test
    public void testAccountFunctions() {
        onView(withId(R.id.button1)).perform(click());
    }
}