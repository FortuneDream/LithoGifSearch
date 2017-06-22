package com.fenchtose.lithogifsearch.features.home;

import com.facebook.litho.Component;
import com.fenchtose.lithogifsearch.base.Presenter;
import com.fenchtose.lithogifsearch.base.router.Routes;
import com.fenchtose.lithogifsearch.components.GifItemView;
import com.fenchtose.lithogifsearch.models.GifItem;
import com.fenchtose.lithogifsearch.models.api.GifProvider;
import com.fenchtose.lithogifsearch.models.db.LikeStore;

import java.util.List;

public class HomePresenter extends Presenter<HomeView> implements HomeView.Callback {

	private final LikeStore likeStore;
	private final GifProvider gifProvider;

	public HomePresenter(LikeStore likeStore) {
		this.likeStore = likeStore;
		gifProvider = new GifProvider(new GifProvider.ResponseListener() {
			@Override
			public void onSuccess(List<GifItem> gifs) {
				HomeView view = getView();
				if (view != null) {
					view.updateContent(gifs);
				}
			}

			@Override
			public void onFailure(Throwable t) {
				t.printStackTrace();
			}
		}, likeStore);
	}

	@Override
	public void attach(HomeView view) {
		super.attach(view);
		view.setCallback(this);
	}

	@Override
	public void detach(HomeView view) {
		super.detach(view);
		view.setCallback(null);
	}

	@Override
	public void onSearchRequested(String query) {
		if (query.length() >= 6) {
			gifProvider.search(query);
		}
	}

	@Override
	public void onGifLiked(String id, boolean liked) {
		likeStore.setLiked(id, liked);
	}

	@Override
	public void onGifSelected(GifItem gif, Component gifComponent) {
		Routes.get().openGif(GifItemView.onFavChanged(gifComponent.getScopedContext()), gif, likeStore.isLiked(gif.getId()));
	}
}
