
package com.openclassrooms.entrevoisins.neighbour_list;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;
import com.openclassrooms.entrevoisins.ui.neighbour_list.ListNeighbourActivity;
import com.openclassrooms.entrevoisins.utils.DeleteViewAction;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.openclassrooms.entrevoisins.utils.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.core.IsNull.notNullValue;



/**
 * Test class for list of neighbours
 */
@RunWith(AndroidJUnit4.class)
public class NeighboursListTest {

    // This is fixed
    private static int ITEMS_COUNT;
    private static int ITEMS_COUNT_MINUS_ONE;
    private int POSITION_ITEM = 0;

    private ActivityScenario<ListNeighbourActivity> mActivity;
    private NeighbourApiService mService;
    private List<Neighbour> neighbourList;

    @Rule
    public ActivityScenarioRule<ListNeighbourActivity> mActivityRule =
            new ActivityScenarioRule<>(ListNeighbourActivity.class);

    @Before
    public void setUp() {
        mActivity = mActivityRule.getScenario();
        assertThat(mActivity, notNullValue());
        mService = DI.getNewInstanceApiService();
        neighbourList = mService.getNeighbours();
        ITEMS_COUNT = neighbourList.size();
        ITEMS_COUNT_MINUS_ONE = ITEMS_COUNT - 1;

    }

    /**
     * We ensure that our recyclerview is displaying at least on item
     */
    @Test
    public void myNeighboursList_shouldNotBeEmpty() {
        // First scroll to the position that needs to be matched and click on it.
        onView(withId(R.id.list_neighbours))
                .check(matches(hasMinimumChildCount(1)));
    }

    /**
     * When we delete an item, the item is no more shown
     */
    @Test
    public void myNeighboursList_deleteAction_shouldRemoveItem() {
        // Given : We remove the element at position 2
        onView(withId(R.id.list_neighbours)).check(withItemCount(ITEMS_COUNT));
        // When perform a click on a delete icon
        onView(withId(R.id.list_neighbours))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, new DeleteViewAction()));
        // Then : the number of element is 11
        onView(withId(R.id.list_neighbours)).check(withItemCount(ITEMS_COUNT_MINUS_ONE));
    }

    /**
     * Open Activity detail, when click on list element.
     */
    @Test
    public void myNeighboursList_onClickItem_shouldOpenDetailActivity() {
        //Given : Start Detail Activity
        //when perform a click on item position
        onView(withId(R.id.list_neighbours))
                .perform(RecyclerViewActions.actionOnItemAtPosition(POSITION_ITEM, click()));
        //Then : we check if textView neighbourNameTitle in DetailActivity is displayed.
        onView(withId(R.id.tv_dn_hname)).check(matches(isDisplayed()));
    }

    /**
     * Check if the name in DetailActivity is the same as the item selected.
     */
    @Test
    public void detailNeighbourName_onDetailActivity_isCorrect() {
        Neighbour neighbour = neighbourList.get(POSITION_ITEM);

        //Given : Proper name Textview in detailActivity
        //when : open detailActivity
        onView(withId(R.id.list_neighbours))
                .perform(RecyclerViewActions.actionOnItemAtPosition(POSITION_ITEM, click()));
        //Then : we check if text Name displayed in DetailActivity match with neigbour name.
        onView(withId(R.id.tv_dn_hname)).check(matches(withText(neighbour.getName())));
    }

    /**
     * Check if favorite list contain items marked as favorite.
     */
    @Test
    public void favoritesList_onFavoriteTab_showFavoriteItems() {
        //Given : Favorites list in favorite Tab.

        //when : add 2 items in favorite onClick on floating action button.
        onView(withId(R.id.list_neighbours))
                .perform(RecyclerViewActions.actionOnItemAtPosition(POSITION_ITEM, click()));
        onView(withId(R.id.iv_dn_isfav))
                .perform(click());
        pressBack();

        onView(withId(R.id.list_neighbours))
                .perform(RecyclerViewActions.actionOnItemAtPosition(POSITION_ITEM+1, click()));
        onView(withId(R.id.iv_dn_isfav))
                .perform(click());
        pressBack();

        //swipe to favorite Tab.
        onView(withId(R.id.container)).perform(swipeLeft());

        //Then : Check if the number of items in Favorite list is same as the number neigbours we added.
        onView(ViewMatchers.withId(R.id.list_neighboursFavorite)).check(withItemCount(2));
    }

    /**
     * When we delete an item in favorite, the item is no more shown
     */
    @Test
    public void myNeighboursListFavorite_deleteAction_shouldRemoveItemFromFavorite() {
        // Given : We remove the item in favorite List.

        //add item in favorite.
        onView(withId(R.id.list_neighbours))
                .perform(RecyclerViewActions.actionOnItemAtPosition(POSITION_ITEM, click()));
        onView(withId(R.id.iv_dn_isfav))
                .perform(click());
        pressBack();
        onView(withId(R.id.container)).perform(swipeLeft());

        //check if list is not empty.
        onView(ViewMatchers.withId(R.id.list_neighboursFavorite)).check(withItemCount(1));

        // When perform a click on a delete icon
        onView(ViewMatchers.withId(R.id.list_neighboursFavorite))
                .perform(RecyclerViewActions.actionOnItemAtPosition( 0, new DeleteViewAction()));
        // Then : the number of element is 11
        onView(ViewMatchers.withId(R.id.list_neighboursFavorite)).check(withItemCount(0));
    }
}