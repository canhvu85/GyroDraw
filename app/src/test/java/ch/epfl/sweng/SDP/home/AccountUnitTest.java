package ch.epfl.sweng.SDP.home;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import ch.epfl.sweng.SDP.Account;
import ch.epfl.sweng.SDP.Constants;
import ch.epfl.sweng.SDP.R;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AccountUnitTest {

    Constants mockConstants;
    DatabaseReference mockReference;
    Query mockQuery;
    Account account;

    @Before
    public void init(){
        mockConstants = Mockito.mock(Constants.class);
        account = new Account(mockConstants, "123456789");
        mockReference = Mockito.mock(DatabaseReference.class);
        mockQuery = Mockito.mock(Query.class);
        when(mockConstants.getUsersRef()).thenReturn(mockReference);
        when(mockReference.child(isA(String.class))).thenReturn(mockReference);
        when(mockReference.orderByChild(isA(String.class))).thenReturn(mockQuery);
        when(mockQuery.equalTo(isA(String.class))).thenReturn(mockQuery);
        doNothing().when(mockReference).setValue(isA(Integer.class), isA(DatabaseReference.CompletionListener.class));
        doNothing().when(mockReference).setValue(isA(Boolean.class), isA(DatabaseReference.CompletionListener.class));
        doNothing().when(mockReference).removeValue(isA(DatabaseReference.CompletionListener.class));
        doNothing().when(mockQuery).addListenerForSingleValueEvent(isA(ValueEventListener.class));
    }

    @Test
    public void testGetStars(){
        assertThat(account.getStars(), is(0));
    }

    @Test
    public void testGetUsername(){
        assertThat(account.getUsername(), is("standardName"));
    }

    @Test
    public void testGetTrophies(){
        assertThat(account.getTrophies(), is(0));
    }

    @Test
    public void testChangeTrophies(){
        account.changeTrophies(20);
        assertEquals(account.getTrophies(), 20);
    }

    @Test
    public void testAddStars(){
        account.addStars(20);
        assertEquals(account.getStars(), 20);
    }

    @Test
    public void testAddFriend(){
        account.addFriend("EPFLien");
    }

    @Test
    public void testRemoveFriend(){
        account.removeFriend("EPFLien");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullUsername(){
        account.updateUsername(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNegativeBalanceStars(){
        account.addStars(-1000);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullFriend(){
        account.addFriend(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeNullFriend(){
        account.removeFriend(null);
    }
}
