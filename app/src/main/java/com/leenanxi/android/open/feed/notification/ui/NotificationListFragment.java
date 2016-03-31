package com.leenanxi.android.open.feed.notification.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import com.android.volley.VolleyError;
import com.leenanxi.android.open.feed.R;
import com.leenanxi.android.open.feed.api.*;
import com.leenanxi.android.open.feed.api.model.Notification;
import com.leenanxi.android.open.feed.api.model.NotificationList;
import com.leenanxi.android.open.feed.network.RequestFragment;
import com.leenanxi.android.open.feed.settings.contract.Settings;
import com.leenanxi.android.open.feed.util.*;
import com.leenanxi.android.open.feed.widget.LoadMoreAdapter;
import com.leenanxi.android.open.feed.widget.NoChangeAnimationItemAnimator;
import com.leenanxi.android.open.feed.widget.OnVerticalScrollListener;
import com.leenanxi.android.open.feed.widget.RetainDataFragment;

import java.util.ArrayList;
import java.util.List;

public class NotificationListFragment extends Fragment implements RequestFragment.Listener {
    private static final int NOTIFICATION_COUNT_PER_LOAD = 20;
    private static final int REQUEST_CODE_LOAD_NOTIFICATION_LIST = 0;
    private static final String KEY_PREFIX = NotificationListFragment.class.getName() + '.';
    private static final String RETAIN_DATA_KEY_NOTIFICATION_LIST = KEY_PREFIX
            + "notification_list";
    private static final String RETAIN_DATA_KEY_CAN_LOAD_MORE = KEY_PREFIX + "can_load_more";
    private static final String RETAIN_DATA_KEY_LOADING_NOTIFICATION_LIST = KEY_PREFIX
            + "loading_notification_list";
    private static final String RETAIN_DATA_KEY_VIEW_STATE = KEY_PREFIX + "view_state";
    private final Handler mHandler = new Handler();
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mNotificationList;
    ProgressBar mProgress;
    View mButtonDoneAll;
    View mButtonHistory;
    View mNoNotifacaitonWrapper;
    private RetainDataFragment mRetainDataFragment;
    private NotificationAdapter mNotificationAdapter;
    private LoadMoreAdapter mAdapter;
    private boolean mCanLoadMore;
    private boolean mLoadingNotificationList;
    private UnreadNotificationCountListener mUnreadNotificationCountListener;


    private void initViews(View itemView) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) itemView.findViewById(R.id.swipe_refresh);
        mNotificationList = (RecyclerView) itemView.findViewById(R.id.notification_list);
        mProgress = (ProgressBar) itemView.findViewById(R.id.progress);
        mButtonDoneAll = itemView.findViewById(R.id.done_all);
        mButtonHistory = itemView.findViewById(R.id.notification_history);
        mNoNotifacaitonWrapper = itemView.findViewById(R.id.no_notification_wrapper);
        mButtonDoneAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Notification> notificationList = new ArrayList<Notification>();
                mNotificationAdapter.replace(notificationList);
            }
        });
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.notification_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        final Activity activity = getActivity();
        mRetainDataFragment = RetainDataFragment.attachTo(this);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadNotificationList(false);
            }
        });
        mNotificationList.setHasFixedSize(true);
        mNotificationList.setItemAnimator(new NoChangeAnimationItemAnimator());
        mNotificationList.setLayoutManager(new LinearLayoutManager(activity));
        List<Notification> notificationList = mRetainDataFragment.remove(
                RETAIN_DATA_KEY_NOTIFICATION_LIST);
        mNotificationAdapter = new NotificationAdapter(notificationList, activity);

        mNotificationAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                boolean hasNotification = mNotificationAdapter.getList().size() > 0;
                if(hasNotification) {
                    mButtonDoneAll.setVisibility(View.VISIBLE);
                    mNoNotifacaitonWrapper.setVisibility(View.GONE);
                } else {
                    mButtonDoneAll.setVisibility(View.GONE);
                    mNoNotifacaitonWrapper.setVisibility(View.VISIBLE);
                }
            }
        });

        mAdapter = new LoadMoreAdapter(R.layout.load_more_item, mNotificationAdapter);
        mNotificationList.setAdapter(mAdapter);
        mNotificationList.addOnScrollListener(new OnVerticalScrollListener() {
            @Override
            public void onScrolledToBottom() {
                loadNotificationList(true);
            }
        });
        mCanLoadMore = mRetainDataFragment.removeBoolean(RETAIN_DATA_KEY_CAN_LOAD_MORE, true);
        mLoadingNotificationList = mRetainDataFragment.removeBoolean(
                RETAIN_DATA_KEY_LOADING_NOTIFICATION_LIST, false);
        // View only saves state influenced by user action, so we have to do this ourselves.
        ViewState viewState = mRetainDataFragment.remove(RETAIN_DATA_KEY_VIEW_STATE);
        if (viewState != null) {
            onRestoreViewState(viewState);
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mRetainDataFragment.put(RETAIN_DATA_KEY_NOTIFICATION_LIST, mNotificationAdapter.getList());
        mRetainDataFragment.put(RETAIN_DATA_KEY_CAN_LOAD_MORE, mCanLoadMore);
        mRetainDataFragment.put(RETAIN_DATA_KEY_LOADING_NOTIFICATION_LIST,
                mLoadingNotificationList);
        mRetainDataFragment.put(RETAIN_DATA_KEY_VIEW_STATE, onSaveViewState());
    }

    @Override
    public void onStart() {
        super.onStart();
        // Only auto-load when initially empty, not loaded but empty.
        if (mNotificationAdapter.getItemCount() == 0 && mCanLoadMore) {
            loadNotificationList();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        saveNotificationListToCache(mNotificationAdapter.getList());
    }

    private ViewState onSaveViewState() {
        return new ViewState(mProgress.getVisibility(), mAdapter.isProgressVisible());
    }

    private void onRestoreViewState(ViewState state) {
        mProgress.setVisibility(state.progressVisibility);
        mAdapter.setProgressVisible(state.adapterProgressVisible);
    }

    @Override
    public void onVolleyResponse(int requestCode, boolean successful, Object result,
                                 VolleyError error, Object requestState) {
        switch (requestCode) {
            case REQUEST_CODE_LOAD_NOTIFICATION_LIST:
                //noinspection unchecked
                onLoadNotificationListResponse(successful, (NotificationList) result, error,
                        (LoadNotificationListState) requestState);
                break;
            default:
                LogUtils.w("Unknown request code " + requestCode + ", with successful=" + successful
                        + ", result=" + result + ", error=" + error);
        }
    }



    public void setUnreadNotificationCountListener(UnreadNotificationCountListener listener) {
        mUnreadNotificationCountListener = listener;
    }

    private void onNotificationListUpdated() {
        if (mUnreadNotificationCountListener != null) {
            mUnreadNotificationCountListener.onUnreadNotificationUpdate(
                    getUnreadNotificationCount());
        }
    }

    private int getUnreadNotificationCount() {
        int count = 0;
        for (Notification notification : mNotificationAdapter.getList()) {
            if (!notification.read) {
                ++count;
            }
        }
        return count;
    }

    private void loadNotificationList(boolean loadMore) {
        if (mLoadingNotificationList || (loadMore && !mCanLoadMore)) {
            return;
        }
        // Flawed Frodo API design: should use untilId instead of start.
        Integer start = loadMore ? mNotificationAdapter.getItemCount() : null;
        int count = NOTIFICATION_COUNT_PER_LOAD;
        ApiRequest<NotificationList> request = ApiRequests.newNotificationListRequest(start, count,
                getActivity());
        LoadNotificationListState state = new LoadNotificationListState(loadMore, count);
        RequestFragment.startRequest(REQUEST_CODE_LOAD_NOTIFICATION_LIST, request, state, this);
        mLoadingNotificationList = true;
        setRefreshing(true, loadMore);
    }

    @Frodo("status/notificaitons is buggy in notifications.length")
    private void onLoadNotificationListResponse(boolean successful, NotificationList result,
                                                VolleyError error, LoadNotificationListState state) {
        Activity activity = getActivity();
        if (successful) {
            List<Notification> notificationList = result.notifications;
            // Workaround Frodo API bug.
            //mCanLoadMore = notificationList.size() == state.count;

            mCanLoadMore = notificationList.size() > 0;
            if (state.loadMore) {
                mNotificationAdapter.addAll(notificationList);
            } else {
                mNotificationAdapter.replace(notificationList);
            }
            onNotificationListUpdated();
            setRefreshing(false, state.loadMore);
            mLoadingNotificationList = false;
        } else {
            LogUtils.e(error.toString());
            ToastUtils.show(ApiError.getErrorString(error, activity), activity);
            setRefreshing(false, state.loadMore);
            mLoadingNotificationList = false;
        }
    }

    private void setRefreshing(boolean refreshing, boolean loadMore) {
        mSwipeRefreshLayout.setEnabled(!refreshing);
        if (!refreshing) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        ViewUtils.setVisibleOrGone(mProgress, refreshing
                && mNotificationAdapter.getItemCount() == 0);
        mAdapter.setProgressVisible(refreshing && mNotificationAdapter.getItemCount() > 0
                && loadMore);
    }

    private void loadNotificationList() {
        CacheHelper.getData(ApiContract.CacheKeys.NOTIFICATION_LIST, mHandler, new Callback<List<Notification>>() {
            @Override
            public void onValue(List<Notification> notificationList) {
                // FIXME: If after onStop() then RequestFragment should not be added. Use
                // commitAllowingStateLoss()?
                if (isRemoving()) {
                    return;
                }
                boolean hasCache = notificationList != null && notificationList.size() > 0;
                if (hasCache) {
                    mNotificationAdapter.replace(notificationList);
                    onNotificationListUpdated();
                }
                if (!hasCache || Settings.AUTO_REFRESH_HOME.getValue(getActivity())) {
                    // FIXME: Revert to calling refresh() later.
                    if (!mNotificationAdapter.getList().isEmpty()) {
                        mSwipeRefreshLayout.setRefreshing(true);
                    }
                    loadNotificationList(false);
                }
            }
        }, getActivity());
    }

    private void saveNotificationListToCache(List<Notification> notificationList) {
        final int MAX_LIST_SIZE = 20;
        if (notificationList.size() > MAX_LIST_SIZE) {
            notificationList = notificationList.subList(0, MAX_LIST_SIZE);
        }
        CacheHelper.putData(ApiContract.CacheKeys.NOTIFICATION_LIST, notificationList,
                getActivity());
    }

    public void refresh() {
        //TODO: refresh
    }

    public interface UnreadNotificationCountListener {
        void onUnreadNotificationUpdate(int count);
    }

    private static class LoadNotificationListState {
        public boolean loadMore;
        public int count;

        public LoadNotificationListState(boolean loadMore, int count) {
            this.loadMore = loadMore;
            this.count = count;
        }
    }

    private static class ViewState {
        public int progressVisibility;
        public boolean adapterProgressVisible;

        public ViewState(int progressVisibility, boolean adapterProgressVisible) {
            this.progressVisibility = progressVisibility;
            this.adapterProgressVisible = adapterProgressVisible;
        }
    }
}
