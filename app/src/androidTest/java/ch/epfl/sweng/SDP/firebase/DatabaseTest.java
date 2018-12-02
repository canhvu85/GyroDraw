package ch.epfl.sweng.SDP.firebase;

import android.support.test.InstrumentationRegistry;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class DatabaseTest {

    @Test(expected = IllegalArgumentException.class)
    public void getReferenceWithNullStringShouldFail() {
        FirebaseApp.initializeApp(InstrumentationRegistry.getContext());
        Database.getReference(null);
    }

    @Test
    public void getReferenceWithSingleKeyReturnsValidReference() {
        FirebaseApp.initializeApp(InstrumentationRegistry.getContext());
        DatabaseReference ref = Database.getReference("test");
        assertThat(ref.getKey(), is("test"));
    }

    @Test
    public void getReferenceWithMultipleKeysReturnsValidReference() {
        FirebaseApp.initializeApp(InstrumentationRegistry.getContext());
        DatabaseReference ref = Database.getReference("test.tests");
        assertThat(ref.getKey(), is("tests"));
        assertThat(ref.getParent().getKey(), is("test"));
    }

    @Test
    public void constructBuilderReturnsValidBuilder() {
        DatabaseReference ref = Database.constructBuilder().addChild("test").build();
        assertThat(ref.getKey(), is("test"));
    }

    @Test
    public void constructBuilderWithInitialRefReturnsValidBuilder() {
        DatabaseReference initialRef = Database.getReference("test");
        DatabaseReference ref = Database.constructBuilder(initialRef).addChild("tests").build();
        assertThat(ref.getKey(), is("tests"));
        assertThat(ref.getParent().getKey(), is("test"));
    }
}
