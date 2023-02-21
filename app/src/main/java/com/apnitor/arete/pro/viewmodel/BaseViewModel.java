package com.apnitor.arete.pro.viewmodel;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.api.response.BaseRes;
import com.apnitor.arete.pro.pojo.ApiError;
import com.apnitor.arete.pro.pojo.ApiProgress;
import com.apnitor.arete.pro.util.ConnectivityHelper;

import java.util.concurrent.atomic.AtomicReference;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import retrofit2.HttpException;

public class BaseViewModel extends AndroidViewModel {

    String LOG_TAG = "BaseViewModel";
    private MutableLiveData<ApiProgress> apiProgressLiveData = new MediatorLiveData<>();
    private MutableLiveData<ApiError> apiErrorLiveData = new MediatorLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Application application;

    public BaseViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
    }

    public LiveData<ApiProgress> getApiProgressLiveData() {
        return apiProgressLiveData;
    }

    public LiveData<ApiError> getApiErrorLiveData() {
        return apiErrorLiveData;
    }

    <T> void consumeApi(Single<BaseRes<T>> apiCall, Consumer<T> consumer) {
        AtomicReference<String> errMsg = new AtomicReference<>("");
        compositeDisposable.add(apiCall
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> apiProgressLiveData.setValue(ApiProgress.start()))
                .doFinally(() -> apiProgressLiveData.setValue(ApiProgress.stop()))
                .subscribe(data -> {
                    if (data.isSuccess()) {
                        consumer.accept(data.getData());
                    } else {
                        Log.d(LOG_TAG, "consumeApi error message " + data.errorMessage + " Error code " + data.errorCode);
                        apiErrorLiveData.setValue(ApiError.create(data.errorMessage, data.errorCode));
                       // Toast.makeText(application, data.errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }, err -> {
                    if (ConnectivityHelper.isConnectedToNetwork(application)) {
                        try {
                            Log.d(LOG_TAG, "err loader " + err.getMessage());
                            apiErrorLiveData.setValue(ApiError.create(err, err.getMessage(), ((HttpException) err).code()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(application, application.getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
                    }
                })
        );
    }


    <T> void consumeApiNoLoader(Single<BaseRes<T>> apiCall, Consumer<T> consumer) {
        AtomicReference<String> errMsg = new AtomicReference<>("");
        compositeDisposable.add(apiCall
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    if (data.isSuccess()) {
                        Log.i(LOG_TAG, "consumeApi error Success " + data.errorMessage + " Error code " + data.errorCode);
                        consumer.accept(data.getData());
                    } else {
                        Log.d(LOG_TAG, "consumeApi error message " + data.errorMessage + " Error code " + data.errorCode);
                        apiErrorLiveData.setValue(ApiError.create(data.errorMessage, data.errorCode));
                      //  Toast.makeText(application, data.errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }, err -> {
                    if (ConnectivityHelper.isConnectedToNetwork(application)) {
                        try {
                            Log.d(LOG_TAG, "err Noloader " + err.getMessage());
                            apiErrorLiveData.setValue(ApiError.create(err, err.getMessage(), ((HttpException) err).code()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(application, application.getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
                    }
                    //  apiErrorLiveData.setValue(ApiError.create(err, "Something is went wrong!Try again ", -2));
                })
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        // Dispose subscriptions..
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
        }
    }
}
