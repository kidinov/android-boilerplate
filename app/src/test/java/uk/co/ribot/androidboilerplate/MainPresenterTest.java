package uk.co.ribot.androidboilerplate;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import rx.Observable;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.data.model.Ribot;
import uk.co.ribot.androidboilerplate.test.common.TestDataFactory;
import uk.co.ribot.androidboilerplate.ui.main.MainMvpView;
import uk.co.ribot.androidboilerplate.ui.main.MainPresenter;
import uk.co.ribot.androidboilerplate.util.RxSchedulersOverrideRule;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MainPresenterTest {

    @Mock
    MainMvpView mockMainMvpView;
    @Mock
    DataManager mockDataManager;
    private MainPresenter mainPresenter;

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Before
    public void setUp() {
        mainPresenter = new MainPresenter(mockDataManager);
        mainPresenter.attachView(mockMainMvpView);
    }

    @After
    public void tearDown() {
        mainPresenter.detachView();
    }

    @Test
    public void loadRibotsReturnsRibots() {
        List<Ribot> ribots = TestDataFactory.makeListRibots(10);
        when(mockDataManager.getRibots())
                .thenReturn(Observable.just(ribots));

        mainPresenter.loadRibots();
        verify(mockMainMvpView).showRibots(ribots);
        verify(mockMainMvpView, never()).showRibotsEmpty();
        verify(mockMainMvpView, never()).showError();
    }

    @Test
    public void loadRibotsReturnsEmptyList() {
        when(mockDataManager.getRibots())
                .thenReturn(Observable.just(Collections.emptyList()));

        mainPresenter.loadRibots();
        verify(mockMainMvpView).showRibotsEmpty();
        verify(mockMainMvpView, never()).showRibots(anyListOf(Ribot.class));
        verify(mockMainMvpView, never()).showError();
    }

    @Test
    public void loadRibotsFails() {
        when(mockDataManager.getRibots())
                .thenReturn(Observable.error(new RuntimeException()));

        mainPresenter.loadRibots();
        verify(mockMainMvpView).showError();
        verify(mockMainMvpView, never()).showRibotsEmpty();
        verify(mockMainMvpView, never()).showRibots(anyListOf(Ribot.class));
    }

}