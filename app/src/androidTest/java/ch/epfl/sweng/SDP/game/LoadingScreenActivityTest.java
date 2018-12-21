package ch.epfl.sweng.SDP.game;

import android.support.annotation.NonNull;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import ch.epfl.sweng.SDP.firebase.FbDatabase;
import ch.epfl.sweng.SDP.firebase.OnSuccessValueEventListener;

import static ch.epfl.sweng.SDP.firebase.AccountAttributes.FRIENDS;
import static ch.epfl.sweng.SDP.game.LoadingScreenActivity.disableLoadingAnimations;
import static org.mockito.ArgumentMatchers.isA;

@RunWith(AndroidJUnit4.class)
public class LoadingScreenActivityTest {

    private static final String USER_ID = "123456789";

    @Rule
    public final ActivityTestRule<LoadingScreenActivity> activityRule =
            new ActivityTestRule<LoadingScreenActivity>(LoadingScreenActivity.class) {

                @Override
                protected void beforeActivityLaunched() {
                    disableLoadingAnimations();
                }

            };

    @Test
    public void testWordsReady() {
        activityRule.getActivity().wordsVotesRef = Mockito.mock(DatabaseReference.class);
        Mockito.doNothing().when(activityRule.getActivity().wordsVotesRef)
                .removeEventListener(isA(ValueEventListener.class));
        activityRule.getActivity().isRoomReady.setBool(true);
        activityRule.getActivity().areWordsReady.setBool(true);
        activityRule.getActivity().listenerRoomReady.onChange();

        FbDatabase.getAccountAttribute(USER_ID, FRIENDS,
                new OnSuccessValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        activityRule.getActivity().listenerWords.onDataChange(dataSnapshot);
                    }
                });
    }
}
