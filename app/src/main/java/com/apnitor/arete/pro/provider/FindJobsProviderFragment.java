package com.apnitor.arete.pro.provider;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.api.request.CancelBidReq;
import com.apnitor.arete.pro.api.request.GetJobReq;
import com.apnitor.arete.pro.api.request.MakeBidReq;
import com.apnitor.arete.pro.api.response.GetJobRes;
import com.apnitor.arete.pro.databinding.FragmentViewJobsBinding;
import com.apnitor.arete.pro.interfaces.FindJobClickCallback;
import com.apnitor.arete.pro.profile.BaseFragment;
import com.apnitor.arete.pro.util.RequestCodes;
import com.apnitor.arete.pro.util.ResultCodes;
import com.apnitor.arete.pro.viewjob.AdapterViewJobs;
import com.apnitor.arete.pro.viewjob.JobDetailActivity;
import com.apnitor.arete.pro.viewjob.PlaceBidActivity;
import com.apnitor.arete.pro.viewmodel.JobSpecViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * Class to view jobs
 */

public class FindJobsProviderFragment extends BaseFragment implements FindJobClickCallback, Observer {
    public static final String LOG_TAG = "FindJobsFragment";
    public static final String BROADCAST_ACTION = "com.apnitor.arete.pro.newjobalert";
    public static boolean isFragmentActive;
    HomeProviderActivity homeActivity;
    String type = "";
    GetJobRes job;
    List<GetJobRes> bidsResFilter = new ArrayList<>();
    List<GetJobRes> mJobList = new ArrayList<>();
    ArrayList<String> mm = new ArrayList<>();
    ArrayList<GetJobRes> mJobListSorting = new ArrayList<>();
    String myLatitude, myLongitude;
    NewJobAlertBroadcastReceiver myBroadCastReceiver;
    private FragmentViewJobsBinding binding;
    private JobSpecViewModel jobSpecViewModel;
    private boolean loadmore;
    private AdapterViewJobs adapter;
    private String message = "";

    private static int compared(long a, long b) {
        return a < b ? -1
                : a > b ? 1
                : 0;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.homeActivity = (HomeProviderActivity) context;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser && getView()!=null) {
//            if (homeActivity.mJobList!=null && homeActivity.mJobList.size() < 1) {
//                showProviderList(true);
//            } else
//                showProviderList(false);
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentViewJobsBinding.inflate(inflater, container, false);
        jobSpecViewModel = getViewModel(JobSpecViewModel.class);
        LinearLayoutManager mManager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(mManager);
        ((HomeProviderActivity) getActivity()).isShowFilter(true);
        getLatLongFromZipCode();
        // API Calling
        showProviderList(false);
        binding.refresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorPrimary);
        binding.refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                jobSpecViewModel.getJobs(new GetJobReq("0", getAuthToken(), "after"), false);
            }
        });
        binding.cardViewFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilterDialog();
            }
        });
        homeActivity.mIvFilterJobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilterDialog();
            }
        });
        binding.cvViewNewJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.cvViewNewJob.setVisibility(View.GONE);
                //
                showProviderList(false);
                //
                binding.refresh.setRefreshing(true);
            }
        });
        myBroadCastReceiver = new NewJobAlertBroadcastReceiver();
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        isFragmentActive = true;
        registerMyReceiver();
        //
        if (homeActivity.mJobList != null && homeActivity.mJobList.size() < 1) {
        } else
            showPreviousData();

    }

    private void showProviderList(boolean isShowLoader) {

        Log.v(LOG_TAG, "showProviderList");
        if (!isShowLoader) {
            ((HomeProviderActivity) getActivity()).isShowProgress(true);
        }
        jobSpecViewModel.getJobs(new GetJobReq("0", getAuthToken(), "after"), isShowLoader);
        jobSpecViewModel.getJobsResLiveData()
                .observe(this, res -> {
                    if (binding.refresh.isRefreshing()) {
                        binding.refresh.setRefreshing(false);
                    }
                    //
                    ((HomeProviderActivity) getActivity()).isShowProgress(false);
                    binding.cvViewNewJob.setVisibility(View.GONE);
                    loadmore = true;
                    homeActivity.mJobList.clear();
                    homeActivity.mJobList.addAll(res.jobs);
                    Log.i("FindJobs--", "---" + homeActivity.mJobList.size());

                    Iterator<GetJobRes> iterator = homeActivity.mJobList.iterator();
                    while (iterator.hasNext()) {
                        GetJobRes job = iterator.next();
                        if (job.statusOfJob != 1) {
                            iterator.remove();
                        }
                    }
                    if (homeActivity.mJobList.size() > 0) {
                        adapter = new AdapterViewJobs(getActivity(), homeActivity.mJobList, FindJobsProviderFragment.this);
                        binding.recyclerView.setAdapter(adapter);
                    }
                });
    }

    @Override
    public void onListItemClick(Object object) {
    }

    @Override
    public void onListItemClickWithPositionClick(Object object, int position) {
        GetJobRes res = (GetJobRes) object;
        if (res.jobType.equals("Bid") && res.bidPlaced) {
            // Go to Modify Bid
            Intent mIntent = new Intent(getActivity(), PlaceBidActivity.class);
            mIntent.putExtra("jobRes", res);
            mIntent.putExtra("type", "modifyBid");
            mIntent.putExtra("bidId", res.bid.bidId);
            startActivityForResult(mIntent, RequestCodes.MODIFY_BID_REQUEST);
            getActivity().overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
        } else {
            // Job Detail
            Intent mIntent = new Intent(getActivity(), JobDetailActivity.class);
            mIntent.putExtra("jobRes", res);
            mIntent.putExtra("type", "bidPlace");
            mIntent.putExtra("providerNotes", "");
            String adapPosition = String.valueOf(position);
            mIntent.putExtra("position", adapPosition);
            startActivityForResult(mIntent, RequestCodes.JOB_DETAIL_REQUEST);
            //  startActivityForResult(mIntent, RequestCodes.PLACE_BID_REQUEST);
            getActivity().overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
        }
    }

    @Override
    public void onMakeBidClick(Object object) {


    }

    @Override
    public void onMakeBidWithPositionClick(Object object, int position) {
        job = (GetJobRes) object;

        if (job.jobType != null) {
            if (job.jobType.equals("Bid")) {
                Bundle mBundle = new Bundle();
                mBundle.putParcelable("jobRes", job);

                Log.e(LOG_TAG, " position in clk " + position);

                String adapPosition = String.valueOf(position);
                //startActivity(PlaceBidActivity.class,mBundle);
                Intent intent = new Intent(getActivity(), PlaceBidActivity.class);
                intent.putExtras(mBundle);
                intent.putExtra("type", "bidPlace"); // M 18

                intent.putExtra("position", adapPosition); //

                startActivityForResult(intent, RequestCodes.PLACE_BID_REQUEST);

            } else if (job.jobType.equals("Fixed Price")) {
                MakeBidReq mProviderReq = new MakeBidReq(getAuthToken(), job.jobId);
                jobSpecViewModel.makeBid(mProviderReq);
                message = "Bid Placed Successfully..";
                observeBidResponse(message, job);
            } else {
                //MakeBidReq mProviderReq = new MakeBidReq(getAuthToken(),res.jobId,"Accept");
                MakeBidReq mProviderReq = new MakeBidReq(getAuthToken(), job.jobId, "Accepted");
                jobSpecViewModel.makeBid(mProviderReq);
                showToast("Job accepted..");
                message = "Job accepted..";
                observeBidResponse(message, job);
            }
        } else {
            Log.d(LOG_TAG, "BId id is null");
        }
    }

    private void observeBidResponse(String message, GetJobRes job) {
        Log.e("observeBidResponse", "observeBidResponse");
        jobSpecViewModel.makeBidResLiveData()
                .observe(this, res -> {

                    showToast(message);
                    job.bidPlaced = true;
                    // Added to change bid Status m15
                    try {
                        if (job.bid != null) {
                            job.bid.bidStatus = "Active";
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // Added to refresh adapter
                    job.statusOfJob = 2;
                    adapter.notifyDataSetChanged();
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 5001 || resultCode == ResultCodes.MODIFY_BID_RESULT || resultCode == ResultCodes.PLACE_BID_RESULT) {

            Log.d(LOG_TAG, " onActivityResult");
            GetJobRes job = data.getParcelableExtra("jobRes");
            String price = data.getStringExtra("price");
            String description = data.getStringExtra("description");
            String position = data.getStringExtra("position");

            job.bidPlaced = true;
            // Update Bid Price & description
            job.bidPrice = price;
            job.description = description;

            if (homeActivity.mJobList.size() > 0) {
                homeActivity.mJobList.remove(Integer.parseInt(position));
                adapter.notifyItemChanged(Integer.parseInt(position));
                adapter.notifyDataSetChanged();
            }

        }
    }

    @Override
    public void onCancelBidClick(Object object) {
        GetJobRes res = (GetJobRes) object;

        // MakeBidReq mProviderReq = new MakeBidReq(getAuthToken(),res.jobId,"Reject");
        MakeBidReq mProviderReq = new MakeBidReq(getAuthToken(), res.jobId, "Rejected");
        jobSpecViewModel.makeBid(mProviderReq);

        /**
         * TODO
         *
         * It should be job rejected successfully
         *
         */

        message = "Bid rejected successfully";


    }

    @Override
    public void onRatingClick(Object object) {

    }

    @Override
    public void onStartJobWithPosition(Object object, int Position) {

    }

    /**
     * Cancel Bid
     *
     * @param object
     */

    @Override
    public void onCancelBidOnlyClick(Object object) {
        GetJobRes job = (GetJobRes) object;
        cancelBid(job);
        //  job.bid.bidStatus="Cancelled";
    }

    private void cancelBid(GetJobRes mJob) {
        CancelBidReq cancelBidReq = new CancelBidReq(getPrefHelper().getAuthToken(), mJob.bid.bidId);
        jobSpecViewModel.cancelBid(cancelBidReq);


        cancelPlacedBid(mJob, "Bid Cancelled successfully.");

        //showToast("Bid Cancelled successfully.");
        // job.bid.bidStatus = "Cancelled";
        // adapter.notifyDataSetChanged();

        /**
         * TODO
         *
         * Remove below code
         */

        /*
        // Reload Fragment: So that fresh data can be load
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
        */
    }

    /**
     * @param mJob
     * @param messagec
     */


    private void cancelPlacedBid(GetJobRes mJob, String messagec) {
        jobSpecViewModel.cancelBidResLiveData()
                .observe(this, res -> {
//                    showToast("Bid Cancelled successfully.");
                    /**
                     * TODO
                     *
                     * Update Bid Data
                     *
                     *
                     */
                    int indexOfCancelledBidJob = homeActivity.mJobList.indexOf(mJob);
                    if (indexOfCancelledBidJob >= 0) {
                        GetJobRes cancelledBidJob = homeActivity.mJobList.get(indexOfCancelledBidJob);
                        /**
                         * TODO
                         * Add it to Constants
                         *
                         * TODO
                         *
                         * Don't set it directly
                         * add setter method to set
                         */
                        cancelledBidJob.bid.bidStatus = "Cancelled";
                    }

                    //adapter.notifyDataSetChanged();

                    //
                    homeActivity.mJobList.remove(mJob);
                    adapter.notifyDataSetChanged();

                });
    }

    /**
     * TODO
     * <p>
     * Below Code doesn't seem correct
     * <p>
     * Cancel Job response is handled here..
     **/

    private void cancelBidResponse(GetJobRes mJob, String messagec) {
        jobSpecViewModel.cancelJobReqLiveData()
                .observe(this, res -> {
//                    showToast("Bid Cancelled successfully.");

                    /**
                     * TODO
                     *
                     * Update Bid Data
                     *
                     *
                     */

                    int indexOfCancelledBidJob = homeActivity.mJobList.indexOf(mJob);

                    if (indexOfCancelledBidJob >= 0) {
                        GetJobRes cancelledBidJob = homeActivity.mJobList.get(indexOfCancelledBidJob);
                        /**
                         * TODO
                         * Add it to Contants
                         *
                         * TODO
                         *
                         * Don't set it directly
                         * add setter method to set
                         */
                        cancelledBidJob.bid.bidStatus = "Cancelled";
                    }

                    adapter.notifyDataSetChanged();
                    // Reload Fragment: SO that fresh data can be load
                   /* FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach(this).attach(this).commit();*/
                });
    }

    private void onLoadMore(RecyclerView mRecyclerView) {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();
                if (lastVisiblePosition == recyclerView.getChildCount()) {

                    if (loadmore) {
                        loadmore = false;
                        showProviderList(false);
                    }
                }
            }
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void openFilterDialog() {

        String[] langArray = {"Price (Low to High)", "Price (High to Low)", "Time & Date", "Distance"};
        MaterialDialog.Builder materialDialog = new MaterialDialog.Builder(getActivity())
                .title("Filter By")
                .theme(Theme.LIGHT)
                .negativeColor(getResources().getColor(R.color.colorPrimary))
                .items(langArray)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        if (text.equals("Price (Low to High)")) {
                            sortScheduleByPriceLowToHigh(homeActivity.mJobList, "LtoH");
                        } else if (text.equals("Price (High to Low)")) {
                            sortScheduleByPriceLowToHigh(homeActivity.mJobList, "HtoL");
                        } else if (text.equals("Time & Date")) {
                            // TODO uncomment n test it when jobs get filtered from API
                           // sortListByTimeAndDate();
                            showProviderList(false);

                        } else if (text.equals("Distance")) {
                            showToast(getActivity().getResources().getString(R.string.coming_soon));
                            // sortScheduleByPriceLowToHigh(homeActivity.mJobList, "Distance");
                        } else {
                            Log.e(LOG_TAG, "In Else");
                        }
                        dialog.dismiss();
                    }
                })
                .negativeText("Cancel");

        materialDialog.show();
    }

    private void sortListByTimeAndDate() {

        String date = "";
        String time = "";
        try {
            for (int i = 0; i < homeActivity.mJobList.size(); i++) {
                if (homeActivity.mJobList.get(i).when.equals("Future")) {
                    String strtEnd[] = homeActivity.mJobList.get(i).date.split("to");
                    date = strtEnd[0].trim();
                    String timeArray[] = homeActivity.mJobList.get(i).time.split("-");
                    time = timeArray[0].trim();
                } else {
                    time = homeActivity.mJobList.get(i).time.trim();
                    date = homeActivity.mJobList.get(i).date.trim();
                }


                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdfDate = new SimpleDateFormat("MM-dd-yyyy HH:mm");
                try {
                    cal.setTime(sdfDate.parse(date + " " + time));


                    homeActivity.mJobList.get(i).timeInMillisSec = cal.getTimeInMillis();
                    Date datea = cal.getTime();
                    //datea.set
                    Log.e("Check date >>>>", "" + cal.getTimeInMillis());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            Collections.sort(homeActivity.mJobList, new Comparator<GetJobRes>() {
                @Override
                public int compare(GetJobRes x, GetJobRes y) {
                    int startComparison = compared(y.timeInMillisSec, x.timeInMillisSec);
                    return startComparison != 0 ? startComparison
                            : compared(y.timeInMillisSec, x.timeInMillisSec);
                }

                // I don't know why this isn't in Long...
                //  TODO uncomment n test it when jobs get filtered from API
            });
            /*Notify Data here*/
            // TODO uncomment
            adapter.notifyDataSetChanged();

        } catch (Exception e) {

        }
    }

    // Method for filter
    ArrayList<GetJobRes> sortScheduleByPriceLowToHigh(List<GetJobRes> mGetTeamScheduleRes, String sortType) {
        int size = mGetTeamScheduleRes.size();
        if (size > 0) {
            if (sortType.equalsIgnoreCase("HtoL")) {
                Collections.sort(mGetTeamScheduleRes, new SortByPriceHighToLow());
            } else if (sortType.equalsIgnoreCase("LtoH")) {
                Collections.sort(mGetTeamScheduleRes, new SortByPriceLowToHigh());
            } else if (sortType.equalsIgnoreCase("Time")) {
                Collections.sort(mGetTeamScheduleRes, new SortByTime());
            } else if (sortType.equalsIgnoreCase("Date")) {
                //  Collections.sort(mGetTeamScheduleRes, new SortByDate());
                sortScheduleByDateOnly(homeActivity.mJobList, "Date");
            } else if (sortType.equalsIgnoreCase("Distance")) {
                showToast("Coming Soon");
//                if (!PreferenceHandler.readBoolean(homeActivity, PreferenceHandler.PREF_IS_VALID_ZIPCODE, false)) {
//                    openZipcodeDialog();
//                } else
//                    Collections.sort(mGetTeamScheduleRes, new SortByDistance());

            }

            // Time
            adapter = new AdapterViewJobs(getActivity(), mGetTeamScheduleRes, FindJobsProviderFragment.this);
            binding.recyclerView.setAdapter(adapter);
        }
        return mJobListSorting;
    }

    private void openZipcodeDialog() {

        MaterialDialog.Builder materialDialog = new MaterialDialog.Builder(homeActivity)
                .title("Update Zipcode")
                .theme(Theme.LIGHT)
                .content("Please update correct Zipcode in your Profile to filter jobs by distance.")
                .negativeColor(getResources().getColor(R.color.colorPrimary))
                .positiveText(R.string.mdtp_ok)
                .negativeText("Cancel");
        materialDialog.show();
    }


    // Custom Dialog
    public void dialog() {
        final Dialog customView = new Dialog(getActivity());
        customView.setContentView(R.layout.custom_dialog_search);
        customView.setTitle("Enter a Value");
        customView.show();
    }

    void showPreviousData() {
        if (homeActivity.mJobList.size() > 0) {
            //adapter = new AdapterViewJobs(getActivity(), jobs, FindJobsProviderFragment.this);
            adapter = new AdapterViewJobs(getActivity(), homeActivity.mJobList, FindJobsProviderFragment.this);
            binding.recyclerView.setAdapter(adapter);
        }
    }

    // New Method for Date Only
    ArrayList<GetJobRes> sortScheduleByDateOnly(List<GetJobRes> mGetTeamScheduleRes, String sortType) {
        int size = mGetTeamScheduleRes.size();
        if (size > 0) {
            Collections.sort(mGetTeamScheduleRes, new Comparator<GetJobRes>() {
                public int compare(final GetJobRes object1, final GetJobRes object2) {
                    Log.d(LOG_TAG, " Comparing 1st " + object1.date + " with " + object2.date);
                    //  return object1.estPrice.compareobject2.estPrice));
                    return object1.date.compareTo(object2.date);
//                    return ((Integer)object1.estPrice).compare((Integer)object2.estPrice);
                }
            });
            // Time

            adapter = new AdapterViewJobs(getActivity(), mGetTeamScheduleRes, FindJobsProviderFragment.this);
            binding.recyclerView.setAdapter(adapter);


        }

        return mJobListSorting;
    }

    private void getLatLongFromZipCode() {
        SharedPreferences mPreferences = getActivity().getSharedPreferences("MaidPickerPrefs", Context.MODE_PRIVATE);
        final Geocoder geocoder = new Geocoder(getActivity());
        final String zipCode = mPreferences.getString("zipcode", "");
        try {
            List<Address> addresses = geocoder.getFromLocationName(zipCode, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
//                String message = String.format("Latitude: %f, Longitude: %f",
//                        address.getLatitude(), address.getLongitude());
//                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                Log.d(LOG_TAG, " Latitude " + address.getLatitude() + " Longitude " + address.getLongitude());
//                String latitude = "30.7333";
//                String longitude = "76.7794";
                myLatitude = String.valueOf(address.getLatitude());
                myLongitude = String.valueOf(address.getLongitude());
                // calculateDistance(myLatitude,myLongitude,latitude,longitude);
            } else {
                myLatitude = "0.0";
                myLongitude = "0.0";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void calculateDistance(String myLatitude, String myLongitude, String jobLatitude, String jobLongitude) {
        Log.e(LOG_TAG, "calculateDistance");

        Location selected_location = new Location("locationA");
        selected_location.setLatitude(Double.parseDouble(myLatitude));
        selected_location.setLongitude(Double.parseDouble(myLongitude));

        Location near_locations = new Location("locationB");
        near_locations.setLatitude(Double.parseDouble(jobLatitude));
        near_locations.setLongitude(Double.parseDouble(jobLongitude));

        float distance = selected_location.distanceTo(near_locations);
        distance = distance / 1000;
        Log.e(LOG_TAG, "distance = " + distance);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            isFragmentActive = false;
            homeActivity.unregisterReceiver(myBroadCastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        isFragmentActive = false;
    }

    private void registerMyReceiver() {
        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(BROADCAST_ACTION);
            homeActivity.registerReceiver(myBroadCastReceiver, intentFilter);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    class SortByPriceLowToHigh implements Comparator<GetJobRes> {
        // Used for sorting in ascending order of
        public int compare(GetJobRes a, GetJobRes b) {
            float one = Float.parseFloat(a.estPrice);
            float two = Float.parseFloat(b.estPrice);
            return ((int) one) - ((int) two); // L to H

        }
    }

    // Filter Price Hight to Low
    class SortByPriceHighToLow implements Comparator<GetJobRes> {
        public int compare(GetJobRes a, GetJobRes b) {
            float one = Float.parseFloat(a.estPrice);
            float two = Float.parseFloat(b.estPrice);
            return ((int) two) - ((int) one); // H to L

        }
    }

    /* Filter Data By Time*/
    class SortByTime implements Comparator<GetJobRes> {
        public int compare(GetJobRes a, GetJobRes b) {
            float one = (float) a.estTime;
            float two = (float) b.estTime;
            int retval = Float.compare(one, two);
            return retval;

        }
    }

    class SortByDistance implements Comparator<GetJobRes> {

        public int compare(GetJobRes a, GetJobRes b) {
            float one = (float) a.estTime;
            float two = (float) b.estTime;

           /* Double latitude = b.address.get(0).latitude;
            Double longitude = b.address.get(0).longitude;*/

            Double latitude = b.address.latitude;
            Double longitude = b.address.longitude;

            String lat = String.valueOf(latitude);
            String longi = String.valueOf(longitude);
            calculateDistance(myLatitude, myLatitude, lat, longi);
            return ((int) one) - ((int) two);
        }
    }

    class SortByDate implements Comparator<GetJobRes> {
        public int compare(GetJobRes a, GetJobRes b) {

            /*int one= Integer.parseInt(a.date);
            int two= Integer.parseInt(b.date);
            return one-two;*/

            float one = Float.parseFloat(a.date);
            float two = Float.parseFloat(b.date);
            return ((int) two) - ((int) one);
        }
    }

    class NewJobAlertBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
//                binding.cvViewNewJob.setVisibility(View.VISIBLE);
                binding.cvViewNewJob.performClick();
                //
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


}